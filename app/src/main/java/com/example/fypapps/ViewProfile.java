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

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ViewProfile extends AppCompatActivity {

        Account ac;

    TextView account;
    TextView name;
    TextView email;
    TextView birth;
    TextView gpsright;

    com.example.fypapps.UrlClass.url ul = new url();
    String url = ul.geturl();
    HashMap<String,String> hm = new HashMap<String,String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewprofile);

        ac = (Account)getIntent().getSerializableExtra("acdata");

         account = (TextView)findViewById(R.id.textView5);
         name = (TextView)findViewById(R.id.textView9);
         email = (TextView)findViewById(R.id.textView15);
         birth = (TextView)findViewById(R.id.textView23);
         gpsright = (TextView)findViewById(R.id.textView45);


        int uid = ac.getid();
        String acname = ac.getac();

        String newurl = url + "getuserdata.php?userid=" +uid + "&ac="+acname;
        JsonDataGetter jdg = new JsonDataGetter(ViewProfile.this,newurl);
        try {
            String a =  jdg.execute().get();
            StringToData std = new StringToData(a);
            hm = std.getUserData();
            if (hm.get("successful").equals('F')) {

            } else {
                account.setText(hm.get("account").toString());
                name.setText(hm.get("name").toString());
                email.setText(hm.get("email").toString());
                birth.setText(hm.get("birth").toString());
                gpsright.setText(hm.get("gpsright").toString());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




    }

    public void UpdateProfile(View v) {
        Intent i = new Intent(this, UpdateProfile.class);
        i.putExtra("acdata",ac);
        startActivity(i);
        finish();
    }

    public void ManageFriend(View v) {
        Intent i = new Intent(this, ManageFriend.class);
        i.putExtra("acdata",ac);
        startActivity(i);
        finish();
    }

}
