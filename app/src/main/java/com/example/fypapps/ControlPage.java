package com.example.fypapps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.example.fypapps.Account.Account;

public class ControlPage extends AppCompatActivity {

    //sharepreference
    SharedPreferences sharedpreferences;
    String mypreference = "mypref";
    //end of share
    Account ac;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control);
        ac = (Account)getIntent().getSerializableExtra("acdata");
        TextView text6 = (TextView)findViewById(R.id.textView6);

        ac = (Account)getIntent().getSerializableExtra("acdata");
        text6.setText(ac.getac());
    }

    public void Logout(View v) {
        getSharedPreferences(mypreference, 0).edit().clear().commit();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void ViewF(View v) {

        Intent i = new Intent(this, ViewFacility.class);
        i.putExtra("acdata",ac);
        startActivity(i);


    }

    public void ViewFd(View v) {

        Intent i = new Intent(this, ViewFriend.class);
        i.putExtra("acdata",ac);
        startActivity(i);


    }

    public void ViewP(View v) {
        Intent i = new Intent(this,ViewProfile.class);
        i.putExtra("acdata",ac);
        startActivity(i);
    }

    public void place(View v) {

    }


        public void add(View v) {

        Intent i = new Intent(this,InsertFacility.class);
            i.putExtra("acdata",ac);
            startActivity(i);


        }





}
