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
import org.centum.techconnect.views.SelfHelpSlidingView;

import butterknife.Bind;

/**
 * Created by Phani on 1/26/2016.
 */
public class SelfHelpFragment extends Fragment {

    @Bind(R.id.sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;
    @Bind(R.id.self_help_main_container)
    FrameLayout mainContainer;
    @Bind(R.id.self_help_sliding)
    SelfHelpSlidingView slidingView;

    private SelfHelpState currentState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_self_help, container, true);

    }

    private enum SelfHelpState {
        INTRODUCTION, FLOW
    }
}
