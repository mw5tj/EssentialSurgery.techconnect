package org.centum.techconnect.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents 1 "state" from the flowchart.
 */
public class FlowchartElement {

    private String id;
    private String question;
    private String details;
    private String attachment;
    private String[] options;
    private String[] nextElemIDs;

    private FlowchartElement() {

    }

    public static FlowchartElement fromJSON(JSONObject obj) throws JSONException {
        FlowchartElement el = new FlowchartElement();
        el.id = obj.getString("id");
        el.question = obj.getString("question");
        el.details = obj.getString("detauls");
        el.attachment = obj.getString("attachment");
        JSONArray opts = obj.getJSONArray("options");
        JSONArray next = obj.getJSONArray("next_question");
        el.options = new String[opts.length()];
        el.nextElemIDs = new String[next.length()];
        if (el.options.length != el.nextElemIDs.length) {
            throw new JSONException("'options' and 'next_questions' should be the same length!");
        }

        for (int i = 0; i < opts.length(); i++) {
            el.options[i] = opts.getString(i);
            el.nextElemIDs[i] = next.getString(i);
        }
        return el;
    }

    public String getId() {
        return id;
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

    public int getNumOptions() {
        return options.length;
    }

    public boolean hasAttachment() {
        return attachment != null;
    }

    public String[] getOptions() {
        return options;
    }

    public String[] getNextElemIDs() {
        return nextElemIDs;
    }
}
