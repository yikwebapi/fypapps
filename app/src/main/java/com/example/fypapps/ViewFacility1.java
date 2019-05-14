package com.example.fypapps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ViewFacility1 extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, TextToSpeech.OnInitListener, RecognitionListener {
    com.example.fypapps.UrlClass.url ul = new url();
    String url = ul.geturl();
    String url1 = ul.geturl1();
    Account ac;

    double X = 114.1899843;
    double Y = 22.304086;
    float zoomLevel = 16.0f;
    //New
    private GoogleMap mMap;
    private MapView mapView;

    private LocationManager locationMangaer = null;
    private String best;

    String[] typefacility = {"All","Toilet","wheelchair ramp","lift"};

    String newtype = "All";
    ArrayList<HashMap<String,String>> al = new ArrayList();
    private LocationManager locationManager;
    Spinner spinner;



    //new sound
    static final int REQUEST_PERMISSION_KEY1 = 2;

    static final int REQUEST_PERMISSION_KEY = 1;
    SharedPreferences sharedpreferences;
    String mypreference = "mypref";
    final Handler handler = new Handler();
    private Intent recognizerIntent;
    private SpeechRecognizer speech = null;
    String a = "";
    TextToSpeech tts;
    String[] commandlist = {"command list", "facility type list", "system control list", "special command list"};
    String[] typelist = {"toilet", "wheelchair wamp", "elevator"};
    String[] controllist = {"magnification", "zoom out", "nearest targert"};
    String[] speciallist = {"setting", "turn on alert", "turn off alert"};

    AudioManager audioManager;
    boolean checker = true;
    //end of sound tts




    @SuppressLint("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewfacility1);

        String[] PERMISSIONS1 = {Manifest.permission.ACCESS_COARSE_LOCATION};
        if (!Function1.hasPermissions(this, PERMISSIONS1)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS1, REQUEST_PERMISSION_KEY1);
        }

        //sound
        tts = new TextToSpeech(this, this);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO};
        if (!Function.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_KEY);
        }
        //end


        mapView = (MapView) findViewById(R.id.mapView3);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync( this);

        ac = (Account) getIntent().getSerializableExtra("acdata");


        int userid = ac.getid();





        //listview ini item
         spinner = (Spinner)findViewById(R.id.spinner13);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item);


        for (int i = 0; i < typefacility.length; i++) {
            adapter.add(typefacility[i]);
        }
        spinner.setAdapter(adapter);



        // end of listview


        //GPS
        if (canGetLocation() == false) {
            startGPS();
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                    newtype = typefacility[(int)arg3];

                    String newurl = url + "facilityjson.php?x=" + X + "&y="+Y+"&dtype="+newtype;
                    JsonDataGetter jdg = new JsonDataGetter(ViewFacility1.this,newurl);
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

                    String newurl = url + "facilityjson.php?x=" + X + "&y="+Y+"&dtype="+newtype;
                    JsonDataGetter jdg = new JsonDataGetter(ViewFacility1.this,newurl);
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

        if (sharedpreferences.contains("mic")) {
            String micsetting = sharedpreferences.getString("mic", "");

            if (micsetting.equals("T")) {
                speech = SpeechRecognizer.createSpeechRecognizer(this);
                speech.setRecognitionListener(this);
                recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                        "en");
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                if (checker == true) {
                    speech.startListening(recognizerIntent);
                }
                checker = false;
            }
        }



    }

    @SuppressLint("MissingPermission")
    public void startGPS() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30*1000, 10, locationListener);

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
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
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
            JsonDataGetter jdg = new JsonDataGetter(ViewFacility1.this,newurl);
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
            jdg = new JsonDataGetter(ViewFacility1.this,newurl);
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

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Setting speech language
            int result = tts.setLanguage(Locale.US);
            tts.setSpeechRate(0.75f);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Cook simple toast message with message
                Toast.makeText(getApplicationContext(), "Language not supported",
                        Toast.LENGTH_LONG).show();
                Log.e("TTS", "Language is not supported");
            }
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speech.cancel();
                // Do something after 5s = 5000ms
                Log.d("RECOGNIZER", "done");
                speech.startListening(recognizerIntent);
            }
        }, 2000);
    }

    @Override
    public void onError(int error) {

        String errorMessage = getErrorText(error);
        Log.d("Log", "FAILED " + errorMessage);
        if (errorMessage.equals("a") || errorMessage.equals("b")) {
            speech.cancel();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    speech.startListening(recognizerIntent);
                }
            }, 2000);
        } else {
        }
    }

    @Override
    public void onResults(Bundle results) {

    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches) {
            text += result + " ";
        }

        String atest = clist(text);

        if (atest.equals("")) {

        } else {
            speech.stopListening();
            speakOut(atest);
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }



    private void speakOut(String s) {
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
        while (tts.isSpeaking()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);

    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "a";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "b";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "a";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;

    }




    //speed rec testing
    public String clist(String matches) {
        String alert = "";
        if (matches.toLowerCase().matches("(.*)command(.*)") || matches.toLowerCase().matches("(.*)application(.*)")) {
            alert = "You can speak the following keywords to search the command,";
            for (int j = 0; j < commandlist.length; j++) {
                alert = alert + commandlist[j] + ",";
            }

        } else if (matches.toLowerCase().matches("(.*)facility(.*)") || matches.toLowerCase().matches("(.*)type(.*)"))  {

            alert = "You can speak the following keywords to search the type of facility,";
            for (int j = 0; j < typelist.length; j++) {
                alert = alert + typelist[j] + ",";
            }


        } else if (matches.toLowerCase().matches("(.*)system(.*)") || matches.toLowerCase().matches("(.*)control(.*)")) {
            alert = "You can speak the following keywords to search the control the map action,";
            for (int j = 0; j < controllist.length; j++) {
                alert = alert + controllist[j] + ",";
            }

        } else if (matches.toLowerCase().matches("(.*)special(.*)")) {
            alert = "You can speak the following keywords to search the special command or setting of application,";
            for (int j = 0; j < speciallist.length; j++) {
                alert = alert + speciallist[j] + ",";
            }
            //facility type
        } else if (matches.toLowerCase().matches("(.*)toilet(.*)")) {
            alert = "You have selected toilet type";

            spinner.setSelection(1);
            changespinner(1);
        }  else if (matches.toLowerCase().matches("(.*)wheelchair(.*)") ||matches.toLowerCase().matches("(.*)wamp(.*)")  ) {
            alert = "You have selected wheelchair wamp type";
            spinner.setSelection(2);
            changespinner(2);
        } else if (matches.toLowerCase().matches("(.*)elevator(.*)")) {
            alert = "You have selected elevator type";
            spinner.setSelection(3);
            changespinner(3);
            // end of facility type
            //start of system control list
        } else if  (matches.toLowerCase().matches("(.*)magnification(.*)")) {
            alert = "Map will be larger";
            zoomLevel = (float)zoomLevel + 1;
            setMap();

        } else if (matches.toLowerCase().matches("(.*)zoom(.*)") ||matches.toLowerCase().matches("(.*)out(.*)")  ) {
            alert = "Map will be smaller";
            zoomLevel = (float)zoomLevel - 1;
            setMap();
        } else if (matches.toLowerCase().matches("(.*)nearest(.*)") ||matches.toLowerCase().matches("(.*)targert(.*)")  ) {
            alert = "The nearest target have been located";

            //end of system control list
        } else if (matches.toLowerCase().matches("(.*)setting(.*)")  ) {
            alert = "You current alert setting is " + getalertsetting();

        } else if(matches.toLowerCase().matches("(.*)on(.*)")){
            alert = "Alert setting On";
            alertOn();
        } else if(matches.toLowerCase().matches("(.*)off(.*)") ){
            alert = "Alert setting Off";
            alertOff();
        }
        return alert;

    }
    // end


    public void alertOn() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("alert", "On");
        editor.commit();
    }
    public void alertOff() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("alert", "Off");
        editor.commit();
    }
    public String getalertsetting() {
        String alert = "On";
        if (sharedpreferences.contains("alert")) {
            alert = sharedpreferences.getString("alert", "");
        }
        return alert;
    }
    public void changespinner(int i) {
        newtype = typefacility[i];

        String newurl = url + "facilityjson.php?x=" + X + "&y="+Y+"&dtype="+newtype;
        JsonDataGetter jdg = new JsonDataGetter(ViewFacility1.this,newurl);
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
    }
}


class Function1 {

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
class Function {

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}





