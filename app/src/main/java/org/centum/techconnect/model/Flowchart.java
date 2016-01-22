package org.centum.techconnect.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A flowchart for a problem.
 */
public class Flowchart {

    private String name;
    private String description;
    private Map<String, FlowchartElement> elements = new HashMap<>();
    private Map<String, FlowchartElement> nextOpts;
    private FlowchartElement currentElement;

    public static Flowchart fromJSON(JSONObject json, JSONArray elements) throws JSONException {
        Flowchart flowchart = new Flowchart();
        flowchart.name = json.getString("name");
        flowchart.description = json.getString("description");
        for (int i = 0; i < elements.length(); i++) {
            FlowchartElement el = FlowchartElement.fromJSON(elements.getJSONObject(i));
            flowchart.elements.put(el.getId(), el);
        }
        flowchart.gotoElement(flowchart.elements.get(json.getString("entry_id")));
        return flowchart;
    }

    public FlowchartElement getCurrentElement() {
        return currentElement;
    }

    public void gotoElement(FlowchartElement element) {
        this.currentElement = element;
        Map<String, FlowchartElement> opts = new HashMap<>();
        String nextElemIDs[] = element.getNextElemIDs();
        String nextOpts[] = element.getOptions();
        for (int i = 0; i < nextOpts.length; i++) {
            opts.put(nextOpts[i], elements.get(nextElemIDs[i]));
        }
    }

    public Map<String, FlowchartElement> getNextOptions() {
        return nextOpts;
    }

}
