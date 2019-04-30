package com.example.fypapps;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fypapps.Account.Account;
import com.example.fypapps.UrlClass.url;
import com.example.fypapps.jsonData.JsonDataGetter;
import com.example.fypapps.toJsonData.StringToData;



import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    com.example.fypapps.UrlClass.url ul = new url();
    String url = ul.geturl();
    String jsonResponse = "";
    JsonDataGetter jdg = new JsonDataGetter(MainActivity.this,url);
    HashMap<String,String> hm = new HashMap<>();

    //sharepreference
    SharedPreferences sharedpreferences;
     String mypreference = "mypref";
    //end of share


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        if (sharedpreferences.contains("idkey")) {
           int userid= sharedpreferences.getInt("idkey",0);
           String account= sharedpreferences.getString("ackey","");
          String type= sharedpreferences.getString("typekey","");
           Boolean gpsRight= sharedpreferences.getBoolean("gpskey",false);

            Account ac = new Account(userid,account,type,gpsRight);

            Intent i = new Intent(this, ControlPage.class);
            i.putExtra("acdata",ac);
             startActivity(i);
        }
    }
    public void onButtonClick(View view) throws ExecutionException, InterruptedException {

        EditText eac = (EditText) findViewById(R.id.editText1);
        EditText epw = (EditText) findViewById(R.id.editText2);

        String ac = eac.getText().toString();
        String pw = epw.getText().toString();

        if (!ac.equals("") && !pw.equals("")) {

            String newurl = url + "loginjson?ac=" + ac.toString() + "&pw="+pw.toString();
            JsonDataGetter jdg = new JsonDataGetter(MainActivity.this,newurl);

            String a = jdg.execute().get();
            StringToData std = new StringToData(a);
            hm = std.checkAccount();



            String checker =hm.get("checker");
            if ( checker.equals("F")) {
                Toast.makeText(getApplicationContext(), "Wrong Input", Toast.LENGTH_SHORT).show();
            } else {
                int id = Integer.parseInt(hm.get("id"));
                String account = hm.get("account");
                String type = hm.get("type");
                boolean right = Boolean.parseBoolean(hm.get("gpsright"));

               Account acc = new Account(id,account,type,right);
                Save(id,account,type,right);

                Intent i = new Intent(this, ControlPage.class);
                i.putExtra("acdata",acc);
                startActivity(i);
            }




        } else {
            Toast.makeText(getApplicationContext(), "Can not be null", Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void Save(int id,String acc,String type,boolean gpsright) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("idkey", id);
        editor.putString("ackey", acc);
        editor.putString("typekey", type);
        editor.putBoolean("gpskey", gpsright);
        editor.commit();
    }

    public void Register(View v) {
        Intent i = new Intent(this, Register.class);
        startActivity(i);

    }





}
