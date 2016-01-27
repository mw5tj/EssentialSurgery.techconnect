package org.centum.techconnect.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.centum.techconnect.R;
import org.centum.techconnect.model.DeviceManager;
import org.centum.techconnect.model.Session;
import org.centum.techconnect.model.SessionCompleteListener;
import org.centum.techconnect.views.SelfHelpFlowView;
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
    @Bind(R.id.intercept_button)
    Button interceptButton;

    private SelfHelpIntroView introView;
    private SelfHelpFlowView flowView;
    private Session currentSession = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self_help, container, false);
        ButterKnife.bind(this, view);
        updateViews();
        slidingView.setOnEndSessionListener(this);
        interceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelCollapsed(View panel) {
                interceptButton.setVisibility(View.GONE);
            }

            @Override
            public void onPanelExpanded(View panel) {
                interceptButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPanelAnchored(View panel) {
                interceptButton.setVisibility(View.GONE);
            }

            @Override
            public void onPanelHidden(View panel) {
                interceptButton.setVisibility(View.GONE);
            }
        });
        return view;
    }

    private void updateViews() {
        if (currentSession == null) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            if (introView == null) {
                introView = (SelfHelpIntroView) getLayoutInflater(null).inflate(R.layout.self_help_intro_view, mainContainer, false);
                introView.setDevices(DeviceManager.get().getDevices());
                introView.setSessionCreatedListener(this);
            }
            mainContainer.removeAllViews();
            mainContainer.addView(introView);
        } else {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            slidingView.setSession(currentSession);
            mainContainer.removeAllViews();
            flowView = (SelfHelpFlowView) getLayoutInflater(null).inflate(R.layout.self_help_flow_view, mainContainer, false);
            flowView.setSession(currentSession, new SessionCompleteListener() {
                @Override
                public void onSessionComplete() {
                    currentSession = null;
                    updateViews();
                }
            });
            mainContainer.addView(flowView);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == introView) {
            currentSession = introView.getSession();
            updateViews();
        } else if (view == slidingView) {
            new AlertDialog.Builder(getContext())
                    .setTitle("End Session")
                    .setMessage("Are you sure you want to end the session?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            currentSession = null;
                            updateViews();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        }
    }
}
