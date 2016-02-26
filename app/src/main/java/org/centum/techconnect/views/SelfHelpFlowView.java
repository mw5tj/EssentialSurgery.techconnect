package org.centum.techconnect.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.centum.techconnect.R;
import org.centum.techconnect.model.Flowchart;
import org.centum.techconnect.model.Session;
import org.centum.techconnect.model.SessionCompleteListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Phani on 1/27/2016.
 */
public class SelfHelpFlowView extends ScrollView implements View.OnClickListener {

    @Bind(R.id.question_textView)
    TextView questionTextView;
    @Bind(R.id.details_textView)
    TextView detailsTextView;
    @Bind(R.id.options_linearLayout)
    LinearLayout optionsLinearLayout;
    @Bind(R.id.back_button)
    Button backButton;

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
        for (int i = 0; i < optionsLinearLayout.getChildCount(); i++) {
            optionsLinearLayout.getChildAt(i).setOnClickListener(null);
        }
        optionsLinearLayout.removeAllViews();
        String options[] = flow.getOptions();
        for (int i = 0; i < flow.getNumChildren(); i++) {
            final String option = options[i];
            Button button = new Button(getContext());
            button.setTransformationMethod(null);
            button.setText(option);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    advanceFlow(option);
                }
            });
            optionsLinearLayout.addView(button);
        }
        final String[] attachments = flow.getAttachments();
        if (attachments.length > 0) {
            TextView tv = new TextView(getContext());
            tv.setText("Help Documents");
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv.setGravity(Gravity.CENTER);
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
            tv.setPadding(px, px, px, px);
            optionsLinearLayout.addView(tv);
        }
        for (int i = 0; i < attachments.length; i++) {
            final String att = attachments[i];
            String name = att.substring(att.lastIndexOf("/") + 1);
            if (name.contains("?")) {
                name = name.substring(0, name.indexOf('?'));
            }
            Button button = new Button(getContext());
            button.setTransformationMethod(null);
            button.setText(name);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    openAttachment(att);
                }
            });
            optionsLinearLayout.addView(button);
        }
        backButton.setEnabled(session.hasPrevious());
    }

    private void openAttachment(String att) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(att));
        getContext().startActivity(browserIntent);
    }

    private void advanceFlow(String option) {
        if (session.getCurrentFlowchart().getChild(option) == null) {
            if (listener != null) {
                listener.onSessionComplete();
            }
        } else {
            session.selectOption(option);
            updateViews();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_button) {
            session.goBack();
            updateViews();
        }
    }
}
