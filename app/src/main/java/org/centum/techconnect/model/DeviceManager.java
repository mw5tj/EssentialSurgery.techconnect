package org.centum.techconnect.model;

import android.content.Context;

import org.centum.techconnect.network.NetworkHelper;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Phani on 1/26/2016.
 */
public class DeviceManager {

    private static DeviceManager instance = null;

    private Device[] devices = new Device[0];
    private Context context;

    private DeviceManager(Context context) {
        this.context = context;
    }

    public static DeviceManager get() {
        return instance;
    }

    public static DeviceManager get(Context context) {
        if (instance == null) {
            instance = new DeviceManager(context);
        }
        return instance;
    }

    public void loadDevices() throws IOException, JSONException {
        devices = new NetworkHelper().loadDevices();
    }

    public Device[] getDevices() {
        return devices;
    }
}
