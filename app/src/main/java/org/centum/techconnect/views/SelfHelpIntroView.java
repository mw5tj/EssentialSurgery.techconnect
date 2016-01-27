package org.centum.techconnect.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.centum.techconnect.R;
import org.centum.techconnect.model.Device;
import org.centum.techconnect.model.DeviceProblem;
import org.centum.techconnect.model.Session;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Phani on 1/26/2016.
 */
public class SelfHelpIntroView extends ScrollView implements View.OnClickListener {

    @Bind(R.id.department_editText)
    EditText departmentEditText;
    @Bind(R.id.device_spinner)
    Spinner deviceSpinner;
    @Bind(R.id.problem_spinner)
    Spinner problemSpinner;
    @Bind(R.id.urgency_spinner)
    Spinner urgencySpinner;
    @Bind(R.id.description_textView)
    TextView descriptionTextView;
    @Bind(R.id.notes_editText)
    EditText notesEditText;
    @Bind(R.id.start_session_button)
    Button startButton;

    private Device[] devices;
    private OnClickListener clickListener;
    private Device selectedDevice;
    private DeviceProblem selectedDeviceProblem;
    private Session.Urgency selectedUrgency;
    private Session session;


    public SelfHelpIntroView(Context context) {
        super(context);
    }

    public SelfHelpIntroView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelfHelpIntroView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSessionCreatedListener(OnClickListener listener) {
        this.clickListener = listener;
    }

    public void setDevices(Device[] devices) {
        this.devices = devices;
        updateDeviceSpinner();
    }

    public Session getSession() {
        return session;
    }

    private void updateDeviceSpinner() {
        String deviceNames[] = new String[devices.length];
        for (int i = 0; i < devices.length; i++) {
            deviceNames[i] = devices[i].getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, deviceNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deviceSpinner.setAdapter(adapter);
        deviceSpinner.setOnItemClickListener(null);
        deviceSpinner.setSelection(0);
        deviceSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDevice = devices[i];
                updateProblemSpinner();
            }
        });
    }

    private void updateProblemSpinner() {
        final DeviceProblem problems[] = selectedDevice.getProblems();
        problemSpinner.setOnItemClickListener(null);
        String problemNames[] = new String[problems.length];
        for (int i = 0; i < problems.length; i++) {
            problemNames[i] = problems[i].getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, problemNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        problemSpinner.setAdapter(adapter);
        problemSpinner.setOnItemClickListener(null);
        problemSpinner.setSelection(0);
        problemSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDeviceProblem = problems[i];
                descriptionTextView.setText(selectedDeviceProblem.getDescription());
            }
        });
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        startButton.setOnClickListener(this);
        final String levels[] = new String[Session.Urgency.values().length];
        for (int i = 0; i < levels.length; i++) {
            levels[i] = Session.Urgency.values()[i].name();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, levels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        urgencySpinner.setAdapter(adapter);
        urgencySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUrgency = Session.Urgency.valueOf(levels[i]);
            }
        });
    }

    @Override
    public void onClick(View view) {
        //Attempt to start
        if (validate() && clickListener != null) {
            session = new Session();
            session.setCreatedDate(System.currentTimeMillis());
            session.setDepartment(departmentEditText.getText().toString());
            session.setDevice(selectedDevice);
            session.setDeviceProblem(selectedDeviceProblem);
            session.setUrgency(selectedUrgency);
            session.setNotes(notesEditText.getText().toString());
            clickListener.onClick(this);
        }
    }

    private boolean validate() {
        boolean valid = true;
        if (departmentEditText.getText() == null
                || departmentEditText.getText().toString().trim().equals("")) {
            departmentEditText.setError("Department cannot be empty");
            valid = false;
        } else {
            departmentEditText.setError(null);
        }
        return valid;
    }
}
