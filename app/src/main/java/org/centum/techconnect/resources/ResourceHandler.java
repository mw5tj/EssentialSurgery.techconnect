package org.centum.techconnect.resources;

import android.content.Context;
import android.content.SharedPreferences;

import org.centum.techconnect.model.Device;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Phani on 4/16/2016.
 */
public class ResourceHandler {

    private static ResourceHandler instance = null;
    private Context context;
    private SharedPreferences prefs;
    private SharedPreferences resourcePrefs;

    private Device[] devices = null;

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

    public void loadResources() {
        try {
            devices = new NetworkHelper(context).loadDevices(true);
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
