package org.centum.techconnect.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Phani on 1/22/2016.
 */
public class Device {

    private String name;
    private DeviceProblem problems[];

    public static Device fromJSON(JSONObject obj) throws JSONException {
        Device device = new Device();
        device.name = obj.getString("name");
        JSONArray problemids = obj.getJSONArray("problemids");
        device.problems = new DeviceProblem[problemids.length()];
        for (int i = 0; i < problemids.length(); i++) {
            JSONObject problem = obj.getJSONObject(problemids.getString(i));
            device.problems[i] = DeviceProblem.fromJSON(problem);
        }
        return device;
    }

    public DeviceProblem[] getProblems() {
        return problems;
    }
}
