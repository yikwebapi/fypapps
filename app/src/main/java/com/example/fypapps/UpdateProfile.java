package com.example.fypapps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fypapps.Account.Account;
import com.example.fypapps.UrlClass.url;
import com.example.fypapps.jsonData.JsonDataGetter;
import com.example.fypapps.toJsonData.StringToData;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class UpdateProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    url ul = new url();
    String url = ul.geturl();
    HashMap<String,String> hm = new HashMap<String,String>();
    HashMap<String,String> hm1 = new HashMap<String,String>();
    Account ac;
    EditText et4ac;
    EditText et7email;
    Spinner sp7Day;
    Spinner sp9month;
    Spinner sp11Year;
    Spinner sp12GPS;
    String[] valuegps = {"T","F"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateprofile);
        ac = (Account)getIntent().getSerializableExtra("acdata");


        et4ac = (EditText)findViewById(R.id.editText4);
        et7email = (EditText)findViewById(R.id.editText7);
        sp7Day = (Spinner)findViewById(R.id.spinner7);
        sp9month = (Spinner)findViewById(R.id.spinner9);
        sp11Year = (Spinner)findViewById(R.id.spinner11);
        sp12GPS = (Spinner)findViewById(R.id.spinner12);
        ArrayAdapter a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

        String[] gpsright = {"True","False"};
        for (int i =0; i <gpsright.length; i++) {
            a1.add(gpsright[i]);
        }
        sp12GPS.setAdapter(a1);


        a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

        for (int i =1; i <=31; i++) {
            a1.add(add0today(i));
        }
        sp7Day.setAdapter(a1);


        sp7Day.setOnItemSelectedListener(this);

        a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

        for (int i =1; i <=12; i++) {
            a1.add(add0today(i));
        }
        sp9month.setAdapter(a1);

        a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

        for (int i =99; i >=1; i--) {
            a1.add(1919 + i);
        }
        sp11Year.setAdapter(a1);


        int uid = ac.getid();
        String acname = ac.getac();

        String newurl = url + "getuserdata.php?userid=" +uid + "&ac="+acname;
        JsonDataGetter jdg = new JsonDataGetter(UpdateProfile.this,newurl);
        try {
            String a =  jdg.execute().get();
            StringToData std = new StringToData(a);
            hm = std.getUserData();
            if (hm.get("successful").equals('F')) {
            } else {
                et4ac.setText(hm.get("account").toString());
                et7email.setText(hm.get("email").toString());

                String birth = hm.get("birth").toString();

                int Day = Integer.parseInt(birth.split("-")[2]);
                int month = Integer.parseInt(birth.split("-")[1]);
                int year = 2018- Integer.parseInt(birth.split("-")[0]);

               sp7Day.setSelection(Day-1);
                sp9month.setSelection(month -1);
                sp11Year.setSelection(year -1);
                String gps = hm.get("gpsright").toString();

                if (gps.equals("T")) {
                   sp12GPS.setSelection(0);
                } else {
                    sp12GPS.setSelection(1);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(View v) {
            String iac = et4ac.getText().toString();
            String iemail = et7email.getText().toString();
             String gps = valuegps[sp12GPS.getSelectedItemPosition()];
        String birth = sp11Year.getSelectedItem().toString() + "-" + sp9month.getSelectedItem() + "-" + sp7Day.getSelectedItem();

        String newurl = url + "updateregister?ac=" + iac.toString() + "&em="+iemail.toString() +  "&bi="+birth+"&gps="+gps+"&uid="+ac.getid();
        JsonDataGetter jdg = new JsonDataGetter(UpdateProfile.this,newurl);
        try {
            String a = jdg.execute().get();
            StringToData std = new StringToData(a);
            hm = std.createregister();
            if (hm.get("successful").equals("T")){
                Toast.makeText(v.getContext(), "Update Successful",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(v.getContext(), "Failed, Account is not Existed",
                        Toast.LENGTH_LONG).show();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    public String add0today(int i) {
        if (i >=10) {
            return i + "";
        } else {
            return "0"+i;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position ==0 || position == 2 || position == 4 || position == 6 || position == 7 || position == 9 || position == 11) {
            sp7Day.setAdapter(null);
            ArrayAdapter  a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

            for (int i =1; i <=31; i++) {
                a1.add(add0today(i));
            }
            sp7Day.setAdapter(a1);
        } else if (position == 1) {
            sp9month.setAdapter(null);
            ArrayAdapter  a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

            for (int i =1; i <=28; i++) {
                a1.add(add0today(i));
            }
            sp9month.setAdapter(a1);
        } else {
            sp11Year.setAdapter(null);
            ArrayAdapter  a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

            for (int i =1; i <=30; i++) {
                a1.add(add0today(i));
            }
            sp11Year.setAdapter(a1);
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
