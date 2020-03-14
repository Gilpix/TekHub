package com.kulartist.tekhub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DatabaseObjects {


    static JSONObject studentProfile = new JSONObject();

    static JSONArray itemList = new JSONArray();

    static JSONArray orderList = new JSONArray();

    static JSONArray waitingList = new JSONArray();


    static JSONArray sortedJsonArrayByPopularity = new JSONArray();
    static JSONArray sortedJsonArrayByAvailability = new JSONArray();
    static JSONArray sortedJsonArrayByDate = new JSONArray();
    static JSONArray sortedJsonArrayBySearch = new JSONArray();
    List<JSONObject> jsonList = new ArrayList<JSONObject>();


    public void parseArrayFromJson() throws JSONException {

    for(int i = 0; i<itemList.length();i++)
    {
        jsonList.add(itemList.getJSONObject(i));
    }
}


    public void sortArrayListByPopularity() {

        Collections.sort(jsonList, new Comparator<JSONObject>() {

            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();
                try {
                    valA = (String) a.get("borrowNum");
                    valB = (String) b.get("borrowNum");
                } catch (JSONException e) {
                }

                return valA.compareTo(valB);
            }
        });
    }


    public void sortArrayListByDate() {

        Collections.sort(jsonList, new Comparator<JSONObject>() {

            public int compare(JSONObject a, JSONObject b) {
                Date date1 = null;
                Date date2=null;
                try {
                    date1= java.sql.Date.valueOf((String) a.get("availableDate"));
                    date2= java.sql.Date.valueOf((String) b.get("availableDate"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return date1.compareTo(date2);
            }
        });
    }


    public void storeSortedArrayInList(JSONArray array) {
        for (int i = itemList.length()-1; i >= 0; i--) {
            array.put(jsonList.get(i));
        }
    }


    public void sortArrayListByAvailability() throws JSONException {
        JSONObject temp;
        for (int i =0;i< itemList.length(); i++)
        {
            temp=itemList.getJSONObject(i);
            if(temp.getString("isAvailable").equals("1"))
                sortedJsonArrayByAvailability.put(temp);
        }
    }


    public JSONArray sortArrayListBySearch(String searchKey) throws JSONException {
        JSONObject temp;
        for (int i =0;i< itemList.length(); i++)
        {
            temp=itemList.getJSONObject(i);
            if(temp.getString("itemname").matches("(?i)("+searchKey+").*")){
                sortedJsonArrayBySearch.put(temp);
            }

        }
        return sortedJsonArrayBySearch;
    }









}


