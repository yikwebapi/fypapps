package com.example.fypapps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS;


public class TestMic extends AppCompatActivity implements RecognitionListener, TextToSpeech.OnInitListener {


    static final int REQUEST_PERMISSION_KEY = 1;
    SharedPreferences sharedpreferences;
    String mypreference = "mypref";
    final Handler handler = new Handler();
    private Intent recognizerIntent;
    private SpeechRecognizer speech = null;
    Button btstart;
    TextView tv65;
    String a = "";
    TextToSpeech tts;
    TextView tv59;


    String[] commandlist = {"command list", "facility type list", "system control list", "special command list"};
    String[] typelist = {"toilet", "wheelchair wamp", "elevator"};
    String[] controllist = {"magnification", "zoom out", "nearest targert"};
    String[] speciallist = {"setting", "turn on alert", "turn off alert"};

    AudioManager audioManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testmic);

        tts = new TextToSpeech(this, this);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO};
        if (!Function.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_KEY);
        }

        btstart = (Button) findViewById(R.id.button13);
        tv65 = (TextView) findViewById(R.id.textView65);
        tv59 = (TextView) findViewById(R.id.textView59);
        a = "start";

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
            }
        }
    }

    public void Start(View v) {
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);

        speech.startListening(recognizerIntent);
        btstart.setEnabled(false);

    }


    @Override
    public void onReadyForSpeech(Bundle params) {
        a = a + ",1";
    }

    @Override
    public void onBeginningOfSpeech() {
        a = a + ",2";
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        a = a + ",3";
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        a = a + ",4";
    }

    @Override
    public void onEndOfSpeech() {
        a = a + ",5";
        tv65.setText(a);
        a = "start";
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Log.d("RECOGNIZER", "done");
                speech.startListening(recognizerIntent);
            }
        }, 2000);
    }

    @Override
    public void onError(int error) {
        a = a + ",6";
        tv65.setText(a);
        a = "start";

        String errorMessage = getErrorText(error);
        Log.d("Log", "FAILED " + errorMessage);
        if (errorMessage.equals("a") || errorMessage.equals("b")) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Log.d("RECOGNIZER", "done");
                    speech.startListening(recognizerIntent);
                }
            }, 2000);
        } else {
            btstart.setEnabled(true);
            tv65.setText(errorMessage);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Log.d("RECOGNIZER", "done");
                    speech.startListening(recognizerIntent);
                }
            }, 2000);
        }
    }

    @Override
    public void onResults(Bundle results) {
        a = a + ",7";
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        a = a + ",8";
        ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches) {
            text += result + " ";
        }

        String atest = clist(text);

        if (atest.equals("")) {

        } else {

            speakOut(atest);
        }


        //       text = matches.get(0); //  Remove this line while uncommenting above    codes

        boolean checker = false;
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        a = a + ",9";
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Setting speech language
            int result = tts.setLanguage(Locale.US);
            tts.setSpeechRate(0.75f);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Cook simple toast message with message
                Toast.makeText(getApplicationContext(), "Language not supported",
                        Toast.LENGTH_LONG).show();
                Log.e("TTS", "Language is not supported");
            }
        }
    }

    private void speakOut(String s) {
        speech.cancel();
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
        while (tts.isSpeaking()){
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
            }  else if (matches.toLowerCase().matches("(.*)wheelchair(.*)") ||matches.toLowerCase().matches("(.*)wamp(.*)")  ) {
                alert = "You have selected wheelchair wamp type";
            } else if (matches.toLowerCase().matches("(.*)elevator(.*)")) {
                alert = "You have selected elevator type";
                // end of facility type
                //start of system control list
            } else if  (matches.toLowerCase().matches("(.*)magnification(.*)")) {
                alert = "Map will be larger";

            } else if (matches.toLowerCase().matches("(.*)zoom(.*)") ||matches.toLowerCase().matches("(.*)out(.*)")  ) {
                alert = "Map will be smaller";
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
    public void cc(ArrayList<String> matches) {
        String alert = "";
        for (int i = 0; i < matches.size(); i++) {
            alert = alert + matches.get(i) + "  , ";
        }
        tv65.setText(alert);

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
}

