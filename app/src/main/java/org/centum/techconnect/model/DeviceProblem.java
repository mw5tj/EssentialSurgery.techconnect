package org.centum.techconnect.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Phani on 1/23/2016.
 */
public class DeviceProblem {

    private String name;
    private String description;
    private String jsonFile;
    private Flowchart flowchart;

    public static DeviceProblem fromJSON(JSONObject obj) throws JSONException {
        DeviceProblem problem = new DeviceProblem();
        problem.name = obj.getString("name");
        problem.description = obj.getString("description");
        problem.jsonFile = obj.getString("chart");
        return problem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
