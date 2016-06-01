package org.centum.techconnect.resources;

import android.content.Context;
import android.content.SharedPreferences;

import org.centum.techconnect.model.Contact;
import org.centum.techconnect.model.Device;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Phani on 4/16/2016.
 *
 * Manages downloaded resources for offline capability.
 */
public class ResourceHandler {

    private static ResourceHandler instance = null;
    private Context context;
    private SharedPreferences prefs;
    private SharedPreferences resourcePrefs;

    private Device[] devices = new Device[0];
    private Contact[] contacts = new Contact[0];

    public ResourceHandler(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("ResourceHandler", Context.MODE_PRIVATE);
        resourcePrefs = context.getSharedPreferences("ResourceHandlerCached", Context.MODE_PRIVATE);
    }

    public static ResourceHandler get(Context context) {
        if (instance == null) {
            instance = new ResourceHandler(context);
        }
        return instance;
    }

    public static ResourceHandler get() {
        return instance;
    }

    public Device[] getDevices() {
        return devices;
    }

    public Contact[] getContacts() {
        return contacts;
    }

    public void loadResources() {
        try {
            NetworkHelper helper = new NetworkHelper(context);
            devices = helper.loadDevices(true);
            contacts = helper.loadCallDirectoryContacts(true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean hasStringResource(String tag) {
        return resourcePrefs.getString(tag, null) != null;
    }

    public String getStringResource(String tag) {
        return resourcePrefs.getString(tag, null);
    }

    public void addStringResource(String tag, String s) {
        resourcePrefs.edit().putString(tag, s).apply();
    }

    public void clear() {
        resourcePrefs.edit().clear().apply();
    }
}
