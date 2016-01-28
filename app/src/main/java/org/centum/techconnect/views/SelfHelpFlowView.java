package org.centum.techconnect.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.centum.techconnect.R;
import org.centum.techconnect.model.Flowchart;
import org.centum.techconnect.model.Session;
import org.centum.techconnect.model.SessionCompleteListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Phani on 1/27/2016.
 */
public class SelfHelpFlowView extends RelativeLayout {

    @Bind(R.id.question_textView)
    TextView questionTextView;
    @Bind(R.id.details_textView)
    TextView detailsTextView;
    @Bind(R.id.attachment_imageView)
    ImageView attachmentImageView;
    @Bind(R.id.options_linearLayout)
    LinearLayout optionsLinearLayout;

    private Session session;
    private SessionCompleteListener listener;

    public SelfHelpFlowView(Context context) {
        super(context);
    }

    public SelfHelpFlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelfHelpFlowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSession(Session session, SessionCompleteListener listener) {
        this.listener = listener;
        this.session = session;
        updateViews();
    }

    private void updateViews() {
        Flowchart flow = session.getCurrentFlowchart();
        questionTextView.setText(flow.getQuestion());
        detailsTextView.setText(flow.getDetails());
        if (flow.getAttachment() == null || "".equals(flow.getAttachment())) {
            attachmentImageView.setVisibility(GONE);
        } else {
            attachmentImageView.setVisibility(VISIBLE);
            Picasso.with(getContext())
                    .load(flow.getAttachment())
                    .into(attachmentImageView);
        }
        for (int i = 0; i < optionsLinearLayout.getChildCount(); i++) {
            optionsLinearLayout.getChildAt(i).setOnClickListener(null);
        }
        optionsLinearLayout.removeAllViews();
        String options[] = flow.getOptions();
        for (int i = 0; i < flow.getNumChildren(); i++) {
            final String option = options[i];
            Button button = new Button(getContext());
            button.setText(option);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    advanceFlow(option);
                }
            });
            optionsLinearLayout.addView(button);
        }
    }

    private void advanceFlow(String option) {
        if (session.getCurrentFlowchart().getChild(option) == null) {
            if (listener != null) {
                listener.onSessionComplete();
            }
        } else {
            session.setCurrentFlowchart(session.getCurrentFlowchart().getChild(option));
            updateViews();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }
}
