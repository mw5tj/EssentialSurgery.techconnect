package org.centum.techconnect.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Phani on 1/23/2016.
 */
public class Flowchart {

    private String question;
    private String details;
    private String attachment;
    private Map<String, Flowchart> children;

    public Flowchart(String question, String details, String attachment) {
        this.question = question;
        this.details = details;
        this.attachment = attachment;
    }

    public static Flowchart fromJSON(JSONObject object) throws JSONException {
        Flowchart flowchart = new Flowchart(object.getString("question"),
                object.getString("details"),
                object.getString("attachment"));
        return flowchart;
    }

    public void addChild(String option, Flowchart child) {
        children.put(option, child);
    }

    public int getNumChildren() {
        return children.size();
    }

    public String[] getOptions() {
        return children.keySet().toArray(new String[getNumChildren()]);
    }

    public Flowchart getChild(String option) {
        return children.get(option);
    }

    public String getQuestion() {
        return question;
    }

    public String getDetails() {
        return details;
    }

    public String getAttachment() {
        return attachment;
    }
}
