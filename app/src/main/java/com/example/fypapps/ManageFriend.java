package com.example.fypapps;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.fypapps.Account.Account;
import com.example.fypapps.UrlClass.url;
import com.example.fypapps.jsonData.JsonDataGetter;
import com.example.fypapps.toJsonData.StringToData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ManageFriend  extends AppCompatActivity {

    Account ac;
    url ul = new url();
    String url = ul.geturl();
    ArrayList<HashMap<String,String>> al = new ArrayList();
    ListView lv;
    String acname = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managefriend);

        ac = (Account)getIntent().getSerializableExtra("acdata");
        lv = (ListView)findViewById(R.id.lv1);

    }


    public void invited(View v) {
        String newurl = url + "getinvited.php?userid=" + ac.getid();
        JsonDataGetter jdg = new JsonDataGetter(ManageFriend.this,newurl);
        try {
            String a =  jdg.execute().get();
            StringToData std = new StringToData(a);
            al = std.getFriendList();
            ArrayAdapter adapter = new ArrayAdapter(this,R.layout.lvtvdesign);
            for (int i = 0; i < al.size(); i++) {
                HashMap<String,String> frienddata = (HashMap<String,String>)al.get(i);
                adapter.add(" FriendName : " + frienddata.get("acc") +   " \n GPS:" + frienddata.get("gpsright"));
            }
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {

                    HashMap<String, String> hdata = (HashMap<String, String>) al.get((int) arg3);
                    acname = hdata.get("acc");

                    //alertdialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ManageFriend.this);
                    alertDialogBuilder.setTitle(acname);
                    alertDialogBuilder.setMessage("").setCancelable(false)
                            .setPositiveButton("Add",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    String newurl = url + "updateinvitedlist.php?userid=" + ac.getid() + "&targetname=" + acname + "&action=add";
                                    JsonDataGetter jdg = new JsonDataGetter(ManageFriend.this, newurl);
                                    jdg.execute();
                                    finish();
                                    startActivity(getIntent());
                                }
                            })
                            .setNegativeButton("delete",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    String newurl = url + "updateinvitedlist.php?userid=" + ac.getid() + "&targetname=" + acname + "&action=delete";
                                    JsonDataGetter jdg = new JsonDataGetter(ManageFriend.this, newurl);
                                    jdg.execute();
                                    finish();
                                    startActivity(getIntent());
                                }
                            })
                            .setNeutralButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });
                    // create alert dialog
                    // show it
                    alertDialogBuilder.show();
                    //end of alertdialog
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




    }

    public void Friend(View v) {
        String newurl = url + "getFriend.php?userid=" + ac.getid();
        JsonDataGetter jdg = new JsonDataGetter(ManageFriend.this,newurl);
        try {
            String a =  jdg.execute().get();
            StringToData std = new StringToData(a);
            al = std.getFriendList();
            ArrayAdapter adapter = new ArrayAdapter(this,R.layout.lvtvdesign);
            for (int i = 0; i < al.size(); i++) {
                HashMap<String,String> frienddata = (HashMap<String,String>)al.get(i);
                adapter.add(" FriendName : " + frienddata.get("acc") +   " \n GPS:" + frienddata.get("gpsright"));
            }
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {

                    HashMap<String, String> hdata = (HashMap<String, String>) al.get((int) arg3);
                    acname = hdata.get("acc");

                    //alertdialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ManageFriend.this);
                    alertDialogBuilder.setTitle(acname);
                    alertDialogBuilder.setMessage("").setCancelable(false)
                            .setPositiveButton("Delete",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    String newurl = url + "updatefriendlist.php?userid=" + ac.getid() + "&targetname=" + acname + "&action=delete";
                                    JsonDataGetter jdg = new JsonDataGetter(ManageFriend.this, newurl);
                                    jdg.execute();
                                    finish();
                                    startActivity(getIntent());
                                }
                            })
                            .setNegativeButton("BlackList",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    String newurl = url + "updatefriendlist.php?userid=" + ac.getid() + "&targetname=" + acname + "&action=blacklist";
                                    JsonDataGetter jdg = new JsonDataGetter(ManageFriend.this, newurl);
                                    jdg.execute();
                                    finish();
                                    startActivity(getIntent());
                                }
                            })
                            .setNeutralButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });
                    // create alert dialog
                    // show it
                    alertDialogBuilder.show();
                    //end of alertdialog
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void BlackList(View v) {
        String newurl = url + "getblacklist.php?userid=" + ac.getid();
        JsonDataGetter jdg = new JsonDataGetter(ManageFriend.this, newurl);
        try {
            String a = jdg.execute().get();
            StringToData std = new StringToData(a);
            al = std.getFriendList();
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.lvtvdesign);
            for (int i = 0; i < al.size(); i++) {
                HashMap<String, String> frienddata = (HashMap<String, String>) al.get(i);
                adapter.add(" BlackListName : " + frienddata.get("acc") + " \n GPS:" + frienddata.get("gpsright"));
            }
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {

                    HashMap<String, String> hdata = (HashMap<String, String>) al.get((int) arg3);
                     acname = hdata.get("acc");

                    //alertdialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ManageFriend.this);
                    alertDialogBuilder.setTitle(acname);
                    alertDialogBuilder.setMessage("Click yes to unblacklist").setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    String newurl = url + "updateblacklist.php?userid=" + ac.getid() + "&targetname=" + acname;
                                    JsonDataGetter jdg = new JsonDataGetter(ManageFriend.this, newurl);
                                    jdg.execute();
                                    finish();
                                    startActivity(getIntent());

                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });
                    // create alert dialog
                    // show it
                    alertDialogBuilder.show();
                    //end of alertdialog
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
