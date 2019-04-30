package com.example.fypapps;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class ViewFacility extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    url ul = new url();
    String url = ul.geturl();
    String url1 = ul.geturl1();
    Account ac;

    double X = 114.1911163;
    double Y = 22.3048102;

    //New
    private GoogleMap mMap;
    private MapView mapView;

    private LocationManager locationMangaer = null;
    private String best;

        //hardcode
        String[] typefacility = {"All","Toilet","wheelchair ramp","lift","Other"};
        //hardcode

    //New
    TextView dtype;
    String newtype = "All";
    ArrayList<HashMap<String,String>> al = new ArrayList();



    private LocationManager locationManager;



    @SuppressLint("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewfacility);

        mapView = (MapView) findViewById(R.id.mapView2);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync( this);

        ac = (Account) getIntent().getSerializableExtra("acdata");
        dtype = (TextView)findViewById(R.id.textView12);
        dtype.setText("All");


        int userid = ac.getid();



        //listview ini item
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item);


        for (int i = 0; i < typefacility.length; i++) {
            adapter.add(typefacility[i]);
        }
        spinner.setAdapter(adapter);



        // end of listview


        //GPS
        if (canGetLocation() == false) {
            dtype.setText("GPS is not open. Please open the location permission and GPS");
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                    newtype = typefacility[(int)arg3];
                    dtype.setText(newtype);

                    String newurl = url + "facilityjson.php?x=" + X + "&y="+Y+"&dtype="+newtype;
                    JsonDataGetter jdg = new JsonDataGetter(ViewFacility.this,newurl);
                    try {
                        String a =  jdg.execute().get();
                        StringToData std = new StringToData(a);
                        al = std.getFacility();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    setMap();

                    endGPS();

                    startGPS();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } else {

            startGPS();

            //GPS
            //GPS
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                        newtype = typefacility[(int)arg3];
                        dtype.setText(newtype);

                    String newurl = url + "facilityjson.php?x=" + X + "&y="+Y+"&dtype="+newtype;
                    JsonDataGetter jdg = new JsonDataGetter(ViewFacility.this,newurl);
                    try {
                        String a =  jdg.execute().get();
                        StringToData std = new StringToData(a);
                        al = std.getFacility();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    setMap();

                        endGPS();

                        startGPS();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }
    }

    @SuppressLint("MissingPermission")
    public void startGPS() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30*1000, 10, locationListener);

    }

    public void endGPS() {
        try
        {
            locationManager.removeUpdates(locationListener);
            locationManager=null;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }



    public boolean canGetLocation() {
        boolean result = false;
        LocationManager lm = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        if (lm == null)

            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // exceptions will be thrown if provider is not permitted.
        try {
            network_enabled = lm
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        if (network_enabled == false) {
            result = false;
        } else {
            result = true;
        }

        return result;
    }

    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
             Y = location.getLatitude();
             X = location.getLongitude();

            String newurl = url + "facilityjson.php?x=" + X + "&y="+Y+"&dtype="+newtype;
            JsonDataGetter jdg = new JsonDataGetter(ViewFacility.this,newurl);
            try {
                String a =  jdg.execute().get();
                StringToData std = new StringToData(a);
                al = std.getFacility();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            newurl = url + "insertGPS.php?X=" + X + "&Y="+Y+"&userid="+ac.getid();
            jdg = new JsonDataGetter(ViewFacility.this,newurl);
            try {
                jdg.execute();
            }catch (Exception e) {

            }
            setMap();

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };


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

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void setMap() {
        if (Y != 0 && X != 0) {
            mMap.clear();
            mMap.setMinZoomPreference(12);
            mMap.setIndoorEnabled(true);
            UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setIndoorLevelPickerEnabled(true);
            uiSettings.setMyLocationButtonEnabled(true);
            uiSettings.setMapToolbarEnabled(true);
            uiSettings.setCompassEnabled(true);
            uiSettings.setZoomControlsEnabled(true);
            LatLng XY = new LatLng(Y, X);

            mMap.addMarker(new MarkerOptions().position(XY).title("here"));
            float zoomLevel = 16.0f; //This goes up to 21

            for (int i =0; i < al.size(); i++)  {
                HashMap<String,String> hm = (HashMap<String,String>)al.get(i);
                LatLng fXY = new LatLng(Double.parseDouble(hm.get("Y")),Double.parseDouble(hm.get("X")));
                String tn = hm.get("icon").split("/")[1].replace(".ico","");
                mMap.setOnMarkerClickListener(this);
                if (tn.equals("other")) {
                    mMap.addMarker(new MarkerOptions().position(fXY).title(hm.get("fid")).icon(BitmapDescriptorFactory.fromResource(R.drawable.other)));
                } else if (tn.equals("toilet")) {
                    mMap.addMarker(new MarkerOptions().position(fXY).title(hm.get("fid")).icon(BitmapDescriptorFactory.fromResource(R.drawable.toilet)));
                } else if (tn.equals("lift")) {
                    mMap.addMarker(new MarkerOptions().position(fXY).title(hm.get("fid")).icon(BitmapDescriptorFactory.fromResource(R.drawable.lift)));
                } else if (tn.equals("slope")) {
                    mMap.addMarker(new MarkerOptions().position(fXY).title(hm.get("fid")).icon(BitmapDescriptorFactory.fromResource(R.drawable.slope)));
                }
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(XY, zoomLevel));
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!marker.getTitle().equals("here")) {
            Intent i = new Intent(this, DetailFacility.class);
            i.putExtra("acdata", ac);
            i.putExtra("fid", marker.getTitle());
            startActivity(i);
            return false;
        }
        return false;
    }
}




