package com.example.fypapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypapps.Account.Account;
import com.example.fypapps.UrlClass.url;
import com.example.fypapps.jsonData.DownloadImageTask;
import com.example.fypapps.jsonData.JsonDataGetter;
import com.example.fypapps.toJsonData.StringToData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class DetailFacility extends AppCompatActivity {



    com.example.fypapps.UrlClass.url ul = new url();
    String url = ul.geturl();
    String url1 = ul.geturl1();
    HashMap<String,Object> hm = new HashMap<String,Object>();
    Account ac;
    int fid = 0;

    String denplace = "";
    String type ="";
    String mark = "";
    ArrayList comment = new ArrayList();
    String checker = "";
    String imageurl = "";
    Spinner sp8;

    EditText edtext;

    LinearLayout ly;


    //invisible
    TextView t43;
    TextView t44;
    Button bt2;
    //end of invisible
    HashMap<String,String> hm1 = new HashMap<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailfacility);
        ac = (Account) getIntent().getSerializableExtra("acdata");
        fid = Integer.parseInt(getIntent().getStringExtra("fid"));
        String newurl = url + "facilitydetailjson.php?userid=" + ac.getid()+"&fid="+fid;
        JsonDataGetter jdg = new JsonDataGetter(DetailFacility.this,newurl);
        try {
            String a =  jdg.execute().get();
            StringToData std = new StringToData(a);
            hm = std.getFacilityDetail();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        edtext = (EditText)findViewById(R.id.editText3);
        ly = (LinearLayout)findViewById(R.id.linearLayout13);
        t43 = (TextView)findViewById(R.id.textView43);
        t44 = (TextView)findViewById(R.id.textView44);
        bt2 = (Button)findViewById(R.id.button2);


         denplace = hm.get("denplace").toString();
         type = hm.get("dename").toString();
         mark = hm.get("mark").toString();
         comment = (ArrayList)hm.get("desc");
         checker = hm.get("ok").toString();
        imageurl = url1 + "/process/" + hm.get("image").toString();

        if (checker.equals("F")) {
            ly.setVisibility(View.INVISIBLE);
            t43.setVisibility(View.INVISIBLE);
            t44.setVisibility(View.INVISIBLE);
            bt2.setVisibility(View.INVISIBLE);
            edtext.setVisibility(View.INVISIBLE);
            sp8.setVisibility(View.INVISIBLE);

        }
        TextView t13 = (TextView)findViewById(R.id.textView32);
        TextView t14 = (TextView)findViewById(R.id.textView40);
        TextView t18 = (TextView)findViewById(R.id.textView42);

        t13.setText(denplace);
        t14.setText(type);
        t18.setText(mark);
        if (hm.get("image").toString() != null) {
            new DownloadImageTask((ImageView) findViewById(R.id.imageView))
                    .execute(imageurl);
        }
        sp8 = (Spinner)findViewById(R.id.spinner8);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item);

        for (int i = 0; i <= 10; i++) {
            adapter.add(i);
        }
        sp8.setAdapter(adapter);

    }


    public void InsertComment(View v) {
            int uid = ac.getid();
            int mark = Integer.parseInt(sp8.getSelectedItem().toString());
            String desc = edtext.getText().toString();

        String newurl = url + "insertDScore.php?userid="+uid+"&fid="+fid+"&score="+mark+"&comment="+desc;
        JsonDataGetter jdg = new JsonDataGetter(DetailFacility.this,newurl);
        try {
            String a = jdg.execute().get();
            StringToData std = new StringToData(a);
            hm1 = std.createregister();
            if (hm1.get("successful").equals("T")){
                Toast.makeText(v.getContext(), "Insert Successful",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(v.getContext(), "Failed, You have already added",
                        Toast.LENGTH_LONG).show();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
}