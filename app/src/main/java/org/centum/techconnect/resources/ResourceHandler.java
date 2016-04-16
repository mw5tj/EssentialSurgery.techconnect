package org.centum.techconnect.resources;

import android.content.Context;

import java.io.File;

/**
 * Created by Phani on 4/16/2016.
 */
public class ResourceHandler {

    private static ResourceHandler instance = null;
    private Context context;

    public ResourceHandler(Context context) {
        this.context = context;
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

    public File getImageFile(String url) {
        return null;
    }

}
