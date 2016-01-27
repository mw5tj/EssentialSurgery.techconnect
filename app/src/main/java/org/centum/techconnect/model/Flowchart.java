package org.centum.techconnect.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phani on 1/23/2016.
 */
public class Flowchart {

    private String question;
    private String details;
    private String attachment;
    private Flowchart parent = null;
    private Map<String, Flowchart> children = new HashMap<>();

    public Flowchart(String question, String details, String attachment) {
        this.question = question;
        this.details = details;
        this.attachment = attachment;
    }

    public static Flowchart fromJSON(JSONObject object) throws JSONException {
        Flowchart flowchart = new Flowchart(object.getString("question"),
                object.getString("details"),
                object.isNull("attachment") ? null : object.getString("attachment"));
        return flowchart;
    }

    public void addChild(String option, Flowchart child) {
        if (child != null)
            child.setParent(this);
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

    public Flowchart getParent() {
        return parent;
    }

    private void setParent(Flowchart flowchart) throws IllegalStateException {
        if (hasParent() && getParent() != flowchart) {
            throw new IllegalStateException("This object already has a parent!");
        }
        parent = flowchart;
    }

    public boolean hasParent() {
        return parent != null;
    }
}
