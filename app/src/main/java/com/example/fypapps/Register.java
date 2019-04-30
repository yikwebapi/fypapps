package com.example.fypapps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fypapps.UrlClass.url;
import com.example.fypapps.jsonData.JsonDataGetter;
import com.example.fypapps.toJsonData.StringToData;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText ed;
    EditText ed8;
    EditText ed9;
    EditText ed11;
    Spinner sp2;
    Spinner sp3;
    Spinner sp4;
    Spinner sp5;
    Spinner sp6;
    com.example.fypapps.UrlClass.url ul = new url();
    String url = ul.geturl();
    HashMap<String,String> hm = new HashMap<>();


    String[] valuetype = {"user","pd","hd","bd","user"};
    String[] valuegps = {"T","F"};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        ed = (EditText)findViewById(R.id.editText);
        ed8 = (EditText)findViewById(R.id.editText8);
        ed9 = (EditText)findViewById(R.id.editText9);
        ed11 = (EditText)findViewById(R.id.editText11);

        sp2 = (Spinner) findViewById(R.id.spinner2);
        sp3 = (Spinner) findViewById(R.id.spinner3);
        sp4 = (Spinner) findViewById(R.id.spinner4);
        sp5 = (Spinner) findViewById(R.id.spinner5);
        sp6 = (Spinner) findViewById(R.id.spinner6);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

        String[] type = {"user","physical disability","hard of hearing","vision Impairment","Other"};

        for (int i =0; i <type.length; i++) {
            adapter.add(type[i]);
        }
        sp2.setAdapter(adapter);

        ArrayAdapter a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

        String[] gpsright = {"True","False"};
        for (int i =0; i <gpsright.length; i++) {
            a1.add(gpsright[i]);
        }
        sp3.setAdapter(a1);


        a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

        for (int i =1; i <=31; i++) {
            a1.add(add0today(i));
        }
        sp4.setAdapter(a1);


        sp5.setOnItemSelectedListener(this);

        a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

        for (int i =1; i <=12; i++) {
            a1.add(add0today(i));
        }
        sp5.setAdapter(a1);

        a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

        for (int i =99; i >=1; i--) {
            a1.add(1919 + i);
        }
        sp6.setAdapter(a1);
    }

    public void Register1(View v) {

        String ac = ed.getText().toString();
        String email = ed8.getText().toString();
        String password = ed9.getText().toString();
        String name = ed11.getText().toString();
        String type = valuetype[sp2.getSelectedItemPosition()];
        String gps = valuegps[sp3.getSelectedItemPosition()];
        String birth = sp6.getSelectedItem().toString() + "-" + sp5.getSelectedItem() + "-" + sp4.getSelectedItem();
        Date now = new Date();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(now);

        if (ac.equals("") || email.equals("") || password.equals("")  || name.equals("") ) {
            Toast.makeText(v.getContext(), "Can not be null",
                    Toast.LENGTH_LONG).show();
        } else {

                System.out.println(ac);
                System.out.println(email);
                System.out.println(password);
                System.out.println(name);
                System.out.println(type);
                System.out.println(gps);
                System.out.println(birth);
                System.out.println(currentDate);

            String newurl = url + "insertRegister?ac=" + ac.toString() + "&em="+email.toString() + "&ps="+password + "&ty="+type +"&cd="+currentDate + "&na="+name+"&bi="+birth+"&gp="+gps;
            JsonDataGetter jdg = new JsonDataGetter(Register.this,newurl);
            try {
                String a = jdg.execute().get();
                StringToData std = new StringToData(a);
                hm = std.createregister();
                if (hm.get("successful").equals("T")){
                    Toast.makeText(v.getContext(), "Register Successful",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(v.getContext(), "Failed, Account is Existed",
                            Toast.LENGTH_LONG).show();
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position ==0 || position == 2 || position == 4 || position == 6 || position == 7 || position == 9 || position == 11) {
            sp4.setAdapter(null);
            ArrayAdapter  a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

            for (int i =1; i <=31; i++) {
                a1.add(add0today(i));
            }
            sp4.setAdapter(a1);
        } else if (position == 1) {
            sp4.setAdapter(null);
            ArrayAdapter  a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

            for (int i =1; i <=28; i++) {
                a1.add(add0today(i));
            }
            sp4.setAdapter(a1);
        } else {
            sp4.setAdapter(null);
            ArrayAdapter  a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item);

            for (int i =1; i <=30; i++) {
                a1.add(add0today(i));
            }
            sp4.setAdapter(a1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String add0today(int i) {
        if (i >=10) {
            return i + "";
        } else {
            return "0"+i;
        }
    }
}
