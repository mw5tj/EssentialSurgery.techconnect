package org.centum.techconnect.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Phani on 1/22/2016.
 */
public class Device {

    private String name;
    private Flowchart flowcharts[];

    public static Device fromJSON(JSONObject obj) throws JSONException {
        Device device = new Device();
        device.name = obj.getString("name");
        JSONArray problemids = obj.getJSONArray("problemids");
        device.flowcharts = new Flowchart[problemids.length()];
        for (int i = 0; i < problemids.length(); i++) {
            JSONObject problem = obj.getJSONObject(problemids.getString(i));
            JSONArray elements = new JSONArray(); //TODO read the file at problem.chart
            device.flowcharts[i] = Flowchart.fromJSON(problem, elements);
        }
        return device;
    }

}
