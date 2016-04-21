package org.centum.techconnect.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.centum.techconnect.R;

import java.util.logging.Level;
import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CallActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PHONE_PERMISSION = 1;

    @Bind(R.id.callBtn)
    Button callButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        ButterKnife.bind(this);

        callButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        callNumber();
    }

    private void callNumber() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:4349962243"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Logger.getLogger(getLocalClassName()).log(Level.INFO, "Requesting call permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_PERMISSION);
            return;
        }
        Logger.getLogger(getLocalClassName()).log(Level.INFO, "Calling...");
        startActivity(callIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callNumber();
                } else {
                    callButton.setEnabled(false);
                }
                return;
            }
        }
    }

}
