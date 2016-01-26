package org.centum.techconnect.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.centum.techconnect.R;
import org.centum.techconnect.model.Session;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Phani on 1/26/2016.
 */
public class SelfHelpSlidingView extends RelativeLayout {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");

    @Bind(R.id.device_imageView)
    ImageView deviceImageView;
    @Bind(R.id.device_textView)
    TextView deviceTextView;
    @Bind(R.id.problem_textView)
    TextView problemTextView;
    @Bind(R.id.date_textView)
    TextView dateTextView;
    @Bind(R.id.department_textView)
    TextView departmentTextView;
    @Bind(R.id.urgency_textView)
    TextView urgencyTextView;
    @Bind(R.id.notes_textView)
    TextView notesTextView;

    private Session session;

    public SelfHelpSlidingView(Context context) {
        super(context);
    }

    public SelfHelpSlidingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelfHelpSlidingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setSession(Session session) {
        this.session = session;
        update();
    }

    private void update() {
        if (session == null) {
            Picasso.with(getContext())
                    .load(R.drawable.ic_devices_black)
                    .into(deviceImageView);
            deviceTextView.setText("");
            problemTextView.setText("");
        } else {
            if (session.getDevice().getImageURL() == null) {
                Picasso.with(getContext())
                        .load(R.drawable.ic_devices_black)
                        .into(deviceImageView);
            } else {
                Picasso.with(getContext())
                        .load(session.getDevice().getImageURL())
                        .error(R.drawable.ic_devices_black)
                        .into(deviceImageView);
            }
            deviceTextView.setText(session.getDevice().getName());
            problemTextView.setText(session.getDeviceProblem().getName());
            dateTextView.setText(DATE_FORMAT.format(new Date(session.getCreatedDate())));
            departmentTextView.setText(session.getDepartment());
            notesTextView.setText(session.getNotes());
            urgencyTextView.setText(session.getUrgency().name());
        }
    }
}
