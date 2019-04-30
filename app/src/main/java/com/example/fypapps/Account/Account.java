package com.example.fypapps.Account;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import java.io.Serializable;

import static android.content.Context.*;

public class Account implements Serializable {


    private int userid;
    private String account;
    private String type;
    private boolean gpsRight;

    private SharedPreferences sharedpreferences;
    private String mypreference = "mypref";


    public Account () {
    }
    public Account(int id, String ac, String t, boolean right) {
        userid = id;
        account = ac;
        type = t;
        gpsRight = right;
    }


    public int getid() {
        return userid;
    }

    public String getac() {
        return account;
    }

        public String gettype() {
        return type;
        }

        public boolean getright() {
        return gpsRight;
        }
}
