package com.example.fypapps.toJsonData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class StringToData {

    public StringToData() {

    }

    private String result = "";

    public StringToData(String s) {
        result = s;
    }

    //login part
    public HashMap<String, String> checkAccount() {
        HashMap<String, String> hm = new HashMap<>();

        hm.put("checker", "F");

        if (result != "") {

            try {
                JSONArray jarray = new JSONArray(result);
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jb = jarray.getJSONObject(i);
                    String checker = jb.getString("checker");
                    if (checker.equals("T")) {
                        hm.put("checker", checker);
                        hm.put("id", jb.getInt("id") + "");
                        hm.put("account", jb.getString("account"));
                        hm.put("type", jb.getString("type"));
                        hm.put("gpsright", jb.getString("gpsright"));
                    }
                }
            } catch (JSONException e) {
            }

            //return login hashmap data
        }
        return hm;
    }




    public ArrayList<HashMap<String, String>> getFriendList() {
        ArrayList<HashMap<String,String>> al = new ArrayList();

        if (result != "") {

            try {
                JSONArray jarray = new JSONArray(result);
                for (int i = 0; i < jarray.length(); i++) {
                    HashMap<String, String> hm = new HashMap<>();
                    JSONObject jb = jarray.getJSONObject(i);
                    hm.put("gpsright", jb.getString("gpsright") + "");
                    hm.put("acc", jb.getString("acc"));
                    hm.put("checkercounter",jb.getInt("checkercounter")+"");
                    al.add(hm);
                }
            } catch (JSONException e) {
            }

            //return login hashmap data
        }
        return al;
    }


    public HashMap<String, String> getFriendGPS() {
        HashMap<String,String> hm = new HashMap<>();

        if (result != "") {

            try {
                JSONArray jarray = new JSONArray(result);
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jb = jarray.getJSONObject(i);
                    hm.put("acc", jb.getString("acc") + "");
                    hm.put("X", jb.getString("X"));
                    hm.put("Y",jb.getString("Y"));
                    hm.put("date",jb.getString("date"));
                }
            } catch (JSONException e) {
            }

            //return login hashmap data
        }
        return hm;
    }

    public ArrayList<HashMap<String, String>> getFacility() {
        ArrayList<HashMap<String,String>> al = new ArrayList();

        if (result != "") {

            try {
                JSONArray jarray = new JSONArray(result);
                for (int i = 0; i < jarray.length(); i++) {
                    HashMap<String, String> hm = new HashMap<>();
                    JSONObject jb = jarray.getJSONObject(i);
                    hm.put("X", jb.getString("X") );
                    hm.put("Y", jb.getString("Y"));
                    hm.put("dcnplace", jb.getString("dcnplace"));
                    hm.put("denplace", jb.getString("denplace"));
                    hm.put("typename", jb.getString("typename"));
                    hm.put("fid", jb.getString("fid"));
                    hm.put("icon", jb.getString("icon"));
                    al.add(hm);
                }
            } catch (JSONException e) {
            }

            //return login hashmap data
        }
        return al;
    }


    public HashMap<String,Object> getFacilityDetail() {
        HashMap<String,Object> hm = new HashMap<String,Object>();

        if (result != "") {

            try {
                JSONArray jarray = new JSONArray(result);
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jb = jarray.getJSONObject(i);
                    hm.put("dcnplace", jb.getString("dcnplace") );
                    hm.put("denplace", jb.getString("denplace"));
                    hm.put("image", jb.getString("image"));
                    hm.put("dcname", jb.getString("dcname"));
                    hm.put("dename", jb.getString("dename"));
                    hm.put("ok", jb.getString("ok"));
                    hm.put("mark", jb.getInt("mark") + "");


                    JSONArray newarray = jb.getJSONArray("desc");
                    ArrayList alist = new ArrayList();
                    for (int j = 0; j < newarray.length(); j++) {
                        JSONObject newjb = newarray.getJSONObject(j);
                       alist.add("acc:" + newjb.getString("acc") + " comment:"+ newjb.getString("comment"));
                    }
                    hm.put("desc",alist);

                }
            } catch (JSONException e) {

            }

            //return login hashmap data
        }
        return hm;
    }


    //register

    public HashMap<String, String> createregister() {
        HashMap<String, String> hm = new HashMap<>();

        hm.put("successful", "F");

        if (result != "") {

            try {
                JSONArray jarray = new JSONArray(result);
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jb = jarray.getJSONObject(i);
                    String successful = jb.getString("successful");
                    hm.put("successful",successful);
                }
            } catch (JSONException e) {
            }
            //return login hashmap data
        }
        return hm;
    }
    //register

    //profile
    public HashMap<String, String> getUserData() {
        HashMap<String, String> hm = new HashMap<>();

        hm.put("successful", "F");

        if (result != "") {

            try {
                JSONArray jarray = new JSONArray(result);
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jb = jarray.getJSONObject(i);
                    String successful = jb.getString("successful");
                    String account = jb.getString("account");
                    String email = jb.getString("email");
                    String birth = jb.getString("birth");
                    String gpsright = jb.getString("gpsright");
                    String name = jb.getString("name");
                    hm.put("successful",successful);
                    hm.put("account",account);
                    hm.put("email",email);
                    hm.put("birth",birth);
                    hm.put("gpsright",gpsright);
                    hm.put("name",name);
                }
            } catch (JSONException e) {
            }
            //return login hashmap data
        }
        return hm;
    }

    //end of profile


    //Get Address by google map api (X,Y)
    public String getAddress() {
        String address = "";
        if (result != "") {

            try {

                JSONObject jo = new JSONObject(result);
                JSONArray jarray = jo.getJSONArray("results");
                JSONObject jo1 = jarray.getJSONObject(0);
                address = jo1.getString("formatted_address");

            } catch (JSONException e) {

            }
        }
        return address;
    }
    //

}