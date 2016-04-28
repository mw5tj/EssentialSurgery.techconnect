package org.centum.techconnect.model;

/**
 * Created by Phani on 1/23/2016.
 *
 * A particular role references a particular flowchart.
 * Really just a wrapper, but can be extended.
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
