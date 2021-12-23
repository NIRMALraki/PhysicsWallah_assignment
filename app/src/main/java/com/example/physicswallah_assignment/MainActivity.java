package com.example.physicswallah_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    String data="";
    ArrayList<String> mainnode;
    ListView l;
    Button fetch;
    String result="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l=findViewById(R.id.list);
        fetch=findViewById(R.id.button);
        Intializer();
        Toast.makeText(getApplicationContext(), "initialized", Toast.LENGTH_SHORT).show();
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Fetchingjson().start();
                Toast.makeText(getApplicationContext(), "fetching", Toast.LENGTH_SHORT).show();

            }
        });





    }

    private void Intializer() {
        mainnode = new ArrayList<>();
        adapter=new ArrayAdapter<String>(this, R.layout.activity_main,R.id.list,mainnode);
        l.setAdapter(adapter);


    }

    class Fetchingjson extends Thread
    {

        @Override
        public void run() {
            super.run();
            try {
                URL url = new URL("https://my-json-server.typicode.com/ricky1550/pariksha/db");
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String newline="";



                while ((newline= bufferedReader.readLine())!=null)
                {
                    data = data + newline;
                }
                System.out.println(data);
                if (!data.isEmpty())
                {
                    JSONArray jsonArray = new JSONObject(data).getJSONArray("facilities");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject JO = (JSONObject) jsonArray.get(i);
                         result +=  "facility_id :" + JO.get("facility_id") + "\n"+
                                "name :" + JO.get("name") + "\n";
                         JSONArray options= JO.getJSONArray("options");
                        for (int j = 0; j <options.length(); j++) {
                            JSONObject opt=options.getJSONObject(j);
                            result += " name="+opt.get("name") +"\n"+ "icon ="+opt.get("icon")+"\n" +
                                    "id="+opt.get("id")+ "\n";


                        }



                    }
                    System.out.println(result);

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}