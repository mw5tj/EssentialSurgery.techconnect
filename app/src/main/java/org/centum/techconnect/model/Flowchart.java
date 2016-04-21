package org.centum.techconnect.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Phani on 1/23/2016.
 */
public class Flowchart {

    private String key;
    private String question;
    private String details;
    private String[] attachments;
    private String imageURL;
    private List<String> options = new LinkedList<>();
    private List<Flowchart> children = new LinkedList<>();

    public Flowchart(String question, String details, String[] attachments, String imageURL, String key) {
        this.question = question;
        this.details = details;
        this.attachments = attachments;
        this.key = key;
        this.imageURL = imageURL;
    }

    public static Flowchart fromJSON(JSONObject object, String key) throws JSONException {
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
        String imageURL = null;
        if (object.has("image")) {
            imageURL = object.getString("image");
        } else {
            imageURL = "https://i.vimeocdn.com/portrait/58832_300x300.jpg";
        }
        Flowchart flowchart = new Flowchart(object.getString("question"),
                object.getString("details"), attachmentsStr, imageURL, key);
        return flowchart;
    }

    public void addChild(String option, Flowchart child) {
        options.add(option);
        children.add(child);
    }

    public int getNumChildren() {
        return children.size();
    }

    public String[] getOptions() {
        return options.toArray(new String[options.size()]);
    }

    public Flowchart getChild(String option) {
        return children.get(options.indexOf(option));
    }

    public Flowchart getChildByIndex(int i) {
        return children.get(i);
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

    public String getImageURL() {
        return imageURL;
    }

    public boolean hasImage() {
        return imageURL != null;
    }

    public String getKey() {
        return key;
    }
}
