package org.centum.techconnect.network;

import org.centum.techconnect.model.Device;
import org.centum.techconnect.model.DeviceProblem;
import org.centum.techconnect.model.Flowchart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Phani on 1/14/2016.
 */
public class NetworkHelper {

    public static final String ENTRY_ID = "q1";
    private static final String URL = "https://dl.dropboxusercontent.com/u/2767282/TechConnect/";
    private static final String INDEX_FILE = "index.json";
    private boolean useCached = false;

    public Device[] loadDevices() throws IOException, JSONException {
        //Load the devices first
        List<Device> deviceList = new LinkedList<>();
        JSONObject index = new JSONObject(downloadFile(URL + INDEX_FILE));
        JSONArray devicesids = index.getJSONArray("deviceids");
        for (int i = 0; i < devicesids.length(); i++) {
            deviceList.add(Device.fromJSON(index.getJSONObject(devicesids.getString(i))));
        }
        //Now load all of the flowcharts for the device/deviceproblems
        for (Device device : deviceList) {
            for (DeviceProblem deviceProblem : device.getProblems()) {
                Map<String, JSONObject> elementMap = loadElements(URL, deviceProblem.getJsonFile());
                deviceProblem.setFlowchart(loadFlowchart(elementMap, URL, deviceProblem.getJsonFile()));
            }
        }
        return deviceList.toArray(new Device[deviceList.size()]);
    }

    private Flowchart loadFlowchart(Map<String, JSONObject> elementMap, String path, String id) throws JSONException, IOException {
        if (id.endsWith(".json")) {
            //Go into a new JSON file
            elementMap = loadElements(path, id);
            id = ENTRY_ID;
        }
        JSONObject element = elementMap.get(id);
        JSONArray children = element.getJSONArray("next_question");
        JSONArray opts = element.getJSONArray("options");
        Flowchart flowchart = Flowchart.fromJSON(element);

        for (int i = 0; i < children.length(); i++) {
            if (children.get(i) != null) {
                String nextID = children.getString(i);
                flowchart.addChild(opts.getString(i), loadFlowchart(elementMap, path, nextID));
            } else {
                flowchart.addChild(opts.getString(i), null);
            }
        }
        return flowchart;
    }

    /**
     * Loads all of the elements from a JSON file and child JSON files
     *
     * @param path
     * @param name
     * @return
     * @throws JSONException
     */
    private Map<String, JSONObject> loadElements(String path, String name) throws JSONException, IOException {
        Queue<String> toLoad = new LinkedList<>();
        Map<String, JSONObject> elements = new HashMap<>();

        toLoad.add(name);

        while (toLoad.size() > 0) {
            String jsonName = toLoad.poll();
            String abspath = path + File.separator + jsonName;
            String json = downloadFile(abspath);
            JSONArray obj = new JSONArray(json);
            //Load the elements
            for (int i = 0; i < obj.length(); i++) {
                JSONObject el = obj.getJSONObject(i);
                elements.put(el.getString("id"), el);
            }
        }

        return elements;
    }

    private String downloadFile(String urlS) throws IOException {
        if (useCached) {
            return getCachedFile(urlS);
        } else {
            java.net.URL url = new URL(urlS);
            URLConnection conection = url.openConnection();
            conection.connect();
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            input.close();
            return builder.toString();
        }
    }

    private String getCachedFile(String urlS) {
        return null;
    }

}
