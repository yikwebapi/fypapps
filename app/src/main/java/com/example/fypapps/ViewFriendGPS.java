package com.example.fypapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.fypapps.Account.Account;
import com.example.fypapps.UrlClass.url;
import com.example.fypapps.jsonData.JsonDataGetter;
import com.example.fypapps.toJsonData.StringToData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ViewFriendGPS  extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private Account ac;
    url ul = new url();
    String url = ul.geturl();
    HashMap<String,String> hm = new HashMap<>();
    String X = "40.714";
    String Y = "-74.005";
    String username = "";




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewfriendgps);
        ac = (Account)getIntent().getSerializableExtra("acdata");
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        String acname = getIntent().getStringExtra("fdname");

        String newurl = url + "getGPS.php?acc=" + acname;
        JsonDataGetter jdg = new JsonDataGetter(ViewFriendGPS.this,newurl);
        try {
            String a =  jdg.execute().get();
            StringToData std = new StringToData(a);
            hm = std.getFriendGPS();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView name = (TextView)findViewById(R.id.textView27);
        TextView date = (TextView)findViewById(R.id.textView29);
        TextView location = (TextView)findViewById(R.id.textView57);
        name.setText(hm.get("acc"));
        date.setText(hm.get("date"));

        location.setText("NULL");

        username = hm.get("acc");
        X = hm.get("X");
        Y = hm.get("Y");
        String enjson = getEngJson(Y+","+X);
        String enjsonall = "";


        // X,Y to address
         jdg = new JsonDataGetter(ViewFriendGPS.this,enjson);
        //Get json string full
        try{
            enjsonall = jdg.execute().get();
            StringToData jdd = new StringToData(enjsonall);
            location.setText(jdd.getAddress());
        } catch (ExecutionException e) {
        } catch (InterruptedException e) {
        }
        //
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMap();
    }

    public void setMap() {
        mMap.setMinZoomPreference(12);
        mMap.setIndoorEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        LatLng XY = new LatLng(Double.parseDouble(Y), Double.parseDouble(X));
        mMap.addMarker(new MarkerOptions().position(XY).title(username));
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(XY,zoomLevel));

    }


    public void Back(View v) {
        Intent i = new Intent(this, ControlPage.class);
        i.putExtra("acdata",ac);
        startActivity(i);

    }


    public String getEngJson(String latlon) {
        return "https://maps.googleapis.com/maps/api/geocode/json?latlng="+latlon+"&zoom=14&size=400x300&sensor=false&language=en&key=AIzaSyAM9WQCE5FM9vJpjUbXAEPq0iVafQyI0XM";
    }
    public String getcnJson(String latlon) {
        return "https://maps.googleapis.com/maps/api/geocode/json?latlng="+latlon+"&zoom=14&size=400x300&sensor=false&language=zh_tw&key=AIzaSyAM9WQCE5FM9vJpjUbXAEPq0iVafQyI0XM";
    }



}
