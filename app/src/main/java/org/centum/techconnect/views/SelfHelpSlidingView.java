package org.centum.techconnect.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.centum.techconnect.R;
import org.centum.techconnect.activities.CallActivity;
import org.centum.techconnect.model.Session;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Phani on 1/26/2016.
 *
 * The pull-up menu always visisble.
 */
public class SelfHelpSlidingView extends RelativeLayout implements View.OnClickListener {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");

    @Bind(R.id.device_imageView)
    ImageView deviceImageView;
    @Bind(R.id.device_textView)
    TextView deviceTextView;
    @Bind(R.id.date_textView)
    TextView dateTextView;
    @Bind(R.id.department_textView)
    TextView departmentTextView;
    @Bind(R.id.notes_textView)
    TextView notesTextView;
    @Bind(R.id.end_session_button)
    Button endSessionButton;
    @Bind(R.id.resourcesButton)
    Button resourcesButton;
    @Bind(R.id.callDirBtn)
    Button callDirBtn;

    private Session session;
    private OnClickListener endListener;

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

    public void setOnEndSessionListener(OnClickListener listener) {
        endListener = listener;
    }

    private void update() {
        if (session == null) {
            Picasso.with(getContext())
                    .load(R.drawable.ic_devices_black)
                    .into(deviceImageView);
            deviceTextView.setText("");
            endSessionButton.setOnClickListener(null);
            resourcesButton.setOnClickListener(null);
            callDirBtn.setOnClickListener(null);
        } else {
            resourcesButton.setOnClickListener(this);
            callDirBtn.setOnClickListener(this);
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
            dateTextView.setText(DATE_FORMAT.format(new Date(session.getCreatedDate())));
            departmentTextView.setText(session.getDepartment());
            notesTextView.setText(session.getNotes());
            endSessionButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (endListener != null) {
                        endListener.onClick(SelfHelpSlidingView.this);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.resourcesButton) {
            showResources();
        } else if (view.getId() == R.id.callDirBtn) {
            getContext().startActivity(new Intent(getContext(), CallActivity.class));
        }
    }

    private void showResources() {
        final String[] resources = session.getDevice().getResources();
        final String[] formattedResources = new String[resources.length];
        for (int i = 0; i < resources.length; i++) {
            formattedResources[i] = resources[i].substring(resources[i].lastIndexOf("/") + 1).replace('_', ' ');
            if (formattedResources[i].contains("?")) {
                formattedResources[i] = formattedResources[i].substring(0, formattedResources[i].indexOf('?'));
            }
        }

        new AlertDialog.Builder(getContext()).setTitle("Resources")
                .setItems(formattedResources, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openAttachment(resources[i]);
                    }
                }).show();
    }

    private void openAttachment(String att) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(att));
        getContext().startActivity(browserIntent);
    }
}
