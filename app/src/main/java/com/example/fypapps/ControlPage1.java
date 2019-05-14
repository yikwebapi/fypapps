package com.example.fypapps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.fypapps.Account.Account;

public class ControlPage1 extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    //sharepreference
    SharedPreferences sharedpreferences;
    String mypreference = "mypref";
    //end of share
    Account ac;

    TextView tv58;
    Switch sw3;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control1);

        ac = (Account)getIntent().getSerializableExtra("acdata");
        tv58 = (TextView)findViewById(R.id.textView58);
        tv58.setText(ac.getac());

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        sw3 = (Switch)findViewById(R.id.switch3);
        sw3.setOnCheckedChangeListener(this);

        if (sharedpreferences.contains("mic")) {
                if ( sharedpreferences.getString("mic","").equals("T")) {
                    sw3.setChecked(true);
                    sw3.setText("Mic On");
                } else if ( sharedpreferences.getString("mic","").equals("F")) {
                    sw3.setChecked(false);
                    sw3.setText("Mic Off");
                }
        }



    }







    public void ViewF(View v) {
        Intent i = new Intent(this, ViewFacility1.class);
        i.putExtra("acdata",ac);
        startActivity(i);
    }
    public void ViewP(View v) {

    }
    public void ViewL(View v) {
        getSharedPreferences(mypreference, 0).edit().clear().commit();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }


    public void SaveMic(String checker) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("mic", checker);
        editor.commit();
    }





    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            SaveMic("T");
            sw3.setText("Mic On");
        } else {
            SaveMic("F");
            sw3.setText("Mic Off");
        }
    }
}
