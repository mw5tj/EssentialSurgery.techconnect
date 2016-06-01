package org.centum.techconnect.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.centum.techconnect.R;
import org.centum.techconnect.adapters.ContactListAdapter;
import org.centum.techconnect.model.Contact;
import org.centum.techconnect.resources.ResourceHandler;
import org.centum.techconnect.views.ContactListItemView;

import java.util.logging.Level;
import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CallActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final int REQUEST_PHONE_PERMISSION = 1;

    @Bind(R.id.contactListView)
    ListView listView;

    private Contact currentlyCalling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        ButterKnife.bind(this);

        listView.setAdapter(new ContactListAdapter(this, ResourceHandler.get().getContacts()));
        listView.setOnItemClickListener(this);
    }

    private void call(Contact c) {
        if (c != null) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + c.getPhone()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Logger.getLogger(getLocalClassName()).log(Level.INFO, "Requesting call permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_PERMISSION);
                return;
            }
            Logger.getLogger(getLocalClassName()).log(Level.INFO, "Calling...");
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call(currentlyCalling);
                }
                return;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (view instanceof ContactListItemView) {
            final Contact contact = ((ContactListItemView) view).getContact();
            if (contact != null) {
                new AlertDialog.Builder(this).setMessage("Call " + contact.getName() + "?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                currentlyCalling = contact;
                                call(contact);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        }
    }
}
