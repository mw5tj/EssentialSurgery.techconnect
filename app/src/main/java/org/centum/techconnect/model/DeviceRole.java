package org.centum.techconnect.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Phani on 1/23/2016.
 */
public class DeviceRole {

    private String jsonFile;
    private Flowchart flowchart;


    public DeviceRole(String jsonFile) {
        this.jsonFile = jsonFile;
    }

    public Flowchart getFlowchart() {
        return flowchart;
    }

    public void setFlowchart(Flowchart flowchart) {
        this.flowchart = flowchart;
    }

    public String getJsonFile() {
        return jsonFile;
    }

    public void setJsonFile(String jsonFile) {
        this.jsonFile = jsonFile;
    }
}
