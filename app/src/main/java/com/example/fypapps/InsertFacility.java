package com.example.fypapps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fypapps.Account.Account;
import com.example.fypapps.UrlClass.url;
import com.example.fypapps.jsonData.JsonDataGetter;
import com.example.fypapps.toJsonData.StringToData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class InsertFacility extends AppCompatActivity {


    Account ac;
    com.example.fypapps.UrlClass.url url = new url();
    String path1 = url.geturl();
    String path2 = url.geturl1() + "insertfacilitypicture.php";
    LocationManager lm = null;

    double X = 22.3101;

    double Y = 114.19;

    EditText et5;
    EditText et6;
    EditText et10;
    EditText et12;
    Spinner sp10;

    String dfid = "0";

    public static final int camera_cap = 1000;
    Bitmap bm;
    String[] typefacility = {"Toilet","wheelchair ramp","lift","Other"};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insertfacility);
        ac = (Account)getIntent().getSerializableExtra("acdata");

        et5 = (EditText)findViewById(R.id.editText5);
        et6 = (EditText)findViewById(R.id.editText6);
        et10 = (EditText)findViewById(R.id.editText10);
        et12 = (EditText)findViewById(R.id.editText12);
        sp10 = (Spinner)findViewById(R.id.spinner10);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item);
        for (int i = 0; i < typefacility.length; i++) {
            adapter.add(typefacility[i]);
        }
        sp10.setAdapter(adapter);
        startGPS();

    }
    @SuppressLint("MissingPermission")
    public void startGPS() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        // Register the listener with the Location Manager to receive location updates
        //mobile = network provider
        //android studio virtual devices = gps provider
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30*1000, 10, locationListener);
    }
          LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            Y = location.getLongitude();
            X = location.getLatitude();

            et5.setText(X + "");
            et6.setText(Y+ "");

            String engjson = getEngJson(X+","+Y);
            String cnjson = getcnJson(X+","+Y);
            String engjsonall = "";
            String cnjsonall = "";
            JsonDataGetter jdg = new JsonDataGetter(InsertFacility.this,engjson);
            //Get json string full
            try{
                engjsonall = jdg.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            StringToData jdd = new StringToData(engjsonall);
            et12.setText(jdd.getAddress());
            // end of get json string full (ENGLISH)

            //start get json address string full(Chinese)
            jdg = new JsonDataGetter(InsertFacility.this,cnjson);
            try{
                cnjsonall = jdg.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            StringToData jdd2 = new StringToData(cnjsonall);
            et10.setText(jdd2.getAddress());

            //start get json address string full(Chinese)
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };



    public String getEngJson(String latlon) {
        return "https://maps.googleapis.com/maps/api/geocode/json?latlng="+latlon+"&zoom=14&size=400x300&sensor=false&language=en&key=AIzaSyAM9WQCE5FM9vJpjUbXAEPq0iVafQyI0XM";
    }
    public String getcnJson(String latlon) {
        return "https://maps.googleapis.com/maps/api/geocode/json?latlng="+latlon+"&zoom=14&size=400x300&sensor=false&language=zh_tw&key=AIzaSyAM9WQCE5FM9vJpjUbXAEPq0iVafQyI0XM";
    }


    public void SubmitData(View v) {
        HashMap<String,String> hm = new HashMap<>();
        String type = sp10.getSelectedItem().toString();
        String Yresult = et5.getText().toString();
        String Xresult = et6.getText().toString();

        String cnaddress = et10.getText().toString();
        String enaddress = et12.getText().toString();

        int userid = ac.getid();

        if (Yresult.equals("") || Xresult.equals("") || cnaddress.equals("") || enaddress.equals("") ) {
            Toast.makeText(v.getContext(), "Can not be null",
                    Toast.LENGTH_LONG).show();
        } else {

            String newurl = path1 + "insertFacilityad?type=" +type + "&Y="+Yresult + "&X="+Xresult + "&cn="+cnaddress +"&en="+enaddress + "&userid="+userid;
            JsonDataGetter jdg = new JsonDataGetter(InsertFacility.this,newurl);
            try {
                String a = jdg.execute().get();
                StringToData std = new StringToData(a);
                hm = std.insertFChecker();
                if (hm.get("successful").equals("T")){

                    dfid = hm.get("dfid");

                    //alertdialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InsertFacility.this);
                    alertDialogBuilder.setTitle("Insert Successful");
                    alertDialogBuilder.setMessage("Do You Want to Add Image?").setCancelable(false)
                            .setPositiveButton("Add",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(cameraIntent, camera_cap);
                                }
                            })
                            .setNeutralButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });
                    // create alert dialog
                    // show it
                    alertDialogBuilder.show();


                } else if (hm.get("successful").equals("AE")) {
                    Toast.makeText(v.getContext(), "Already Exist in database",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(v.getContext(), "Failed, Account is Existed",
                            Toast.LENGTH_LONG).show();
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    JSONObject jsonObject;
    RequestQueue rQueue;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == camera_cap && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            bm = photo;
            uploadImage(bm);
        }
    }

    private void uploadImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("dfid", dfid);
            jsonObject.put("image", encodedImage);
            System.out.println("ggggggggggggggggggggggggggggggggg");
            System.out.println(dfid);
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, path2, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("aaaaaaa", jsonObject.toString());
                        rQueue.getCache().clear();
                        Toast.makeText(getApplication(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("aaaaaaa", volleyError.toString());

            }
        });

        rQueue = Volley.newRequestQueue(InsertFacility.this);
        rQueue.add(jsonObjectRequest);

    }





}
