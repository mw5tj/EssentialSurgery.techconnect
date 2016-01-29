package org.centum.techconnect.model;

import org.json.JSONArray;
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
    private String[] attachments;
    private Map<String, Flowchart> children = new HashMap<>();

    public Flowchart(String question, String details, String[] attachments) {
        this.question = question;
        this.details = details;
        this.attachments = attachments;
    }

    public static Flowchart fromJSON(JSONObject object) throws JSONException {
        String[] attachmentsStr;
        if (object.isNull("attachment")) {
            attachmentsStr = new String[0];
        } else {
            JSONArray attachments = object.getJSONArray("attachment");
            attachmentsStr = new String[attachments.length()];
            for (int i = 0; i < attachments.length(); i++) {
                attachmentsStr[i] = attachments.getString(i);
            }
        }
        Flowchart flowchart = new Flowchart(object.getString("question"),
                object.getString("details"), attachmentsStr);
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

    public String[] getAttachments() {
        return attachments;
    }
}
