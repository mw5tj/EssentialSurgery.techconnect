package org.centum.techconnect.network;

import android.support.annotation.NonNull;

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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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
                deviceProblem.setFlowchart(loadFlowchart(URL, deviceProblem.getJsonFile(), ENTRY_ID));
            }
        }
        return deviceList.toArray(new Device[deviceList.size()]);
    }

    private Flowchart loadFlowchart(String path, String filename, String entry) throws JSONException {
        Map<String, JSONObject> elements = deepLoadElements(path, filename);
        Map<JSONObject, Flowchart> flowchartsByJSON = new HashMap<>();
        Map<String, Flowchart> flowchartsByID = new HashMap<>();
        //Create maps
        for (String key : elements.keySet()) {
            JSONObject obj = elements.get(key);
            Flowchart chart = Flowchart.fromJSON(obj, key);
            flowchartsByJSON.put(obj, chart);
            flowchartsByID.put(key, chart);
        }

        //Create links
        for (String key : flowchartsByID.keySet()) {
            Flowchart chart = flowchartsByID.get(key);
            JSONObject obj = elements.get(key);
            JSONArray opts = obj.getJSONArray("options");
            JSONArray next = obj.getJSONArray("next_question");
            for (int i = 0; i < next.length(); i++) {
                Flowchart child = flowchartsByID.get(next.getString(i));
                chart.addChild(opts.getString(i), child);
            }
        }

        return flowchartsByID.get(filename + "/" + entry);
    }

    @NonNull
    private Map<String, JSONObject> deepLoadElements(String path, String filename) throws JSONException {
        //Map of jsonname/id to JSONObject
        Map<String, JSONObject> loadedElements = new HashMap<>();
        Set<String> loadedJSONs = new HashSet<>();
        Queue<String> toLoadJSON = new LinkedList<>();
        toLoadJSON.add(filename);

        while (toLoadJSON.size() > 0) {
            String jsonFile = toLoadJSON.poll();
            Map<String, JSONObject> elements = loadElements(path, jsonFile);
            extendIDs(jsonFile, elements);
            Set<String> referencedJSONs = getReferencedJSONs(elements);
            loadedJSONs.add(jsonFile);
            loadedElements.putAll(elements);

            for (String json : referencedJSONs) {
                if (!loadedJSONs.contains(json)) {
                    toLoadJSON.add(json);
                }
            }
        }
        return loadedElements;
    }

    private Set<String> getReferencedJSONs(Map<String, JSONObject> elements) throws JSONException {
        Set<String> jsons = new HashSet<>();
        for (String id : elements.keySet()) {
            JSONObject obj = elements.get(id);
            JSONArray nextQuestions = obj.getJSONArray("next_question");
            for (int i = 0; i < nextQuestions.length(); i++) {
                String nextID = nextQuestions.getString(i);
                jsons.add(nextID.substring(0, nextID.indexOf("/")));
            }
        }
        return jsons;
    }

    private void extendIDs(String jsonFile, Map<String, JSONObject> elements) throws JSONException {
        //convert next_question into extended id
        for (String id : elements.keySet()) {
            JSONObject obj = elements.get(id);
            JSONArray nextQuestions = obj.getJSONArray("next_question");
            JSONArray newNextQuestions = new JSONArray();
            for (int i = 0; i < nextQuestions.length(); i++) {
                String oldNextID = nextQuestions.getString(i);
                String newNextID = oldNextID;
                if (oldNextID.endsWith(".json")) {
                    //implied entry id
                    newNextID = oldNextID + "/" + ENTRY_ID;
                } else if (!oldNextID.contains(".json")) {
                    //implied json file
                    newNextID = jsonFile + "/" + nextQuestions.getString(i);
                }
                newNextQuestions.put(newNextID);
            }
            obj.put("next_question", newNextQuestions);
        }
    }

    private Map<String, JSONObject> loadElements(String path, String jsonName) throws JSONException {
        Map<String, JSONObject> elements = new HashMap<>();
        String abspath = path + File.separator + jsonName;
        String json = null;
        try {
            json = downloadFile(abspath);
        } catch (IOException e) {
            e.printStackTrace();
            return elements;
        }
        JSONArray obj = new JSONArray(json);
        for (int i = 0; i < obj.length(); i++) {
            JSONObject el = obj.getJSONObject(i);
            elements.put(jsonName + "/" + el.getString("id"), el);
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
