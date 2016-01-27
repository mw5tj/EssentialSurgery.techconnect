package org.centum.techconnect.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.centum.techconnect.R;
import org.centum.techconnect.model.DeviceManager;
import org.centum.techconnect.model.Session;
import org.centum.techconnect.views.SelfHelpIntroView;
import org.centum.techconnect.views.SelfHelpSlidingView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Phani on 1/26/2016.
 */
public class SelfHelpFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;
    @Bind(R.id.self_help_main_container)
    FrameLayout mainContainer;
    @Bind(R.id.self_help_sliding)
    SelfHelpSlidingView slidingView;

    private SelfHelpIntroView introView;
    private Session currentSession = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self_help, container, true);
        ButterKnife.bind(this, view);
        introView = (SelfHelpIntroView) inflater.inflate(R.layout.self_help_intro_view, mainContainer, false);
        introView.setDevices(DeviceManager.get().getDevices());
        introView.setSessionCreatedListener(this);
        updateViews();
        return view;
    }

    private void updateViews() {
        if (currentSession == null) {
            slidingView.setVisibility(View.GONE);
            mainContainer.removeAllViews();
            mainContainer.addView(introView);
        } else {
            slidingView.setVisibility(View.VISIBLE);
            slidingView.setSession(currentSession);
            mainContainer.removeAllViews();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == introView) {
            currentSession = introView.getSession();
            updateViews();
        }
    }
}
