package com.developucth.montirku;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParsers {
    private HashMap<String, String> parseJsonObject(JSONObject object) {
        //Initalizate hash map
        HashMap<String, String> datalist = new HashMap<>();
        //Get name from object
        try {
            //get name from object
            String name = object.getString("name");
            //Get lotatitude object
            String latitude = object.getJSONObject("geometry").getJSONObject("location").getString("lat");
            //getlongatitude
            String longatitude = object.getJSONObject("geometry").getJSONObject("location").getString("lng");
            //put all value in hash map
            datalist.put("name", name);
            datalist.put("lat", latitude);
            datalist.put("lng", longatitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Retrurn
        return datalist;
    }

    private List<HashMap<String, String>> parseJsonArray(JSONArray jsonArray) {
        //Initali
        List<HashMap<String, String>> dataList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            //intializate hash map
            try {
                HashMap<String, String> data = parseJsonObject((JSONObject) jsonArray.get(i));
                dataList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //return datalist
        return dataList;
    }

    public List<HashMap<String, String>> parseResule(JSONObject object){
        JSONArray jsonArray = null;

        try {
            jsonArray = object.getJSONArray("restult");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Return
        return parseJsonArray(jsonArray);
    }
}
