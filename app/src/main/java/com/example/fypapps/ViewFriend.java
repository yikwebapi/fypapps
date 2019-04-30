package com.example.fypapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypapps.Account.Account;
import com.example.fypapps.UrlClass.url;
import com.example.fypapps.jsonData.JsonDataGetter;
import com.example.fypapps.toJsonData.StringToData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ViewFriend extends AppCompatActivity {

        Account ac;
    com.example.fypapps.UrlClass.url ul = new url();
    String url = ul.geturl();

    ArrayList<HashMap<String,String>> al = new ArrayList();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewfriend);
        ac = (Account)getIntent().getSerializableExtra("acdata");
        String newurl = url + "getFriend.php?userid=" + ac.getid();
        JsonDataGetter jdg = new JsonDataGetter(ViewFriend.this,newurl);
        try {
            String a =  jdg.execute().get();
            StringToData std = new StringToData(a);
            al = std.getFriendList();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ListView listView = (ListView)findViewById(R.id.listview);

        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.lvtvdesign);

        for (int i = 0; i < al.size(); i++) {
            HashMap<String,String> frienddata = (HashMap<String,String>)al.get(i);

            adapter.add(" FriendName : " + frienddata.get("acc") +   " \n GPS:" + frienddata.get("gpsright"));

        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {

                HashMap<String,String> hdata = (HashMap<String,String>)al.get((int)arg3);

                int checkercounter = Integer.parseInt(hdata.get("checkercounter"));
                String acname = hdata.get("acc");
                String gpsright = hdata.get("gpsright");

                if (gpsright.equals("T") && checkercounter == 1) {
                    Intent i = new Intent(arg0.getContext(), ViewFriendGPS.class);
                    i.putExtra("acdata",ac);
                    i.putExtra("fdname",acname);
                    startActivity(i);

                }

            }
        });
    }





}
