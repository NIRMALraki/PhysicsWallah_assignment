package com.example.physicswallah_assignment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.physicswallah_assignment.Jsonvalues.ExclusionsBean;
import com.example.physicswallah_assignment.Jsonvalues.FacilitiesBean;
import com.example.physicswallah_assignment.Jsonvalues.OptionsBeans;

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
import java.util.Set;
import java.util.TreeMap;

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
                TreeMap<FacilitiesBean,TreeMap<Integer,OptionsBeans>> facilitiesBeanTreeMap= new TreeMap<>();
                TreeMap<Integer, ExclusionsBean> exclusionsBeanTreeMap= new TreeMap<>();
                if (!data.isEmpty())
                {
                    JSONArray jsonArray = new JSONObject(data).getJSONArray("facilities");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject JO = (JSONObject) jsonArray.get(i);
                        FacilitiesBean facilitiesBean= new FacilitiesBean();

                        facilitiesBean.setFacility_id(JO.getInt("facility_id"));
                        facilitiesBean.setName(JO.getString("name"));
                        JSONArray options= JO.getJSONArray("options");
                        TreeMap<Integer,OptionsBeans> optionsBeansTreeMap= new TreeMap<>();
                        Log.d(TAG, "run:facites done ");
                        for (int j = 0; j <options.length(); j++) {
                            JSONObject opt=options.getJSONObject(j);

                            OptionsBeans optionsBeans= new OptionsBeans();
                            optionsBeans.setId(opt.getInt("id"));
                            optionsBeans.setIcon(opt.getString("icon"));
                            optionsBeans.setName(opt.getString("name"));
                            facilitiesBean.setOptions(optionsBeans);
                            optionsBeansTreeMap.put(optionsBeans.getId(), optionsBeans);


                        }
                        facilitiesBeanTreeMap.put(facilitiesBean,optionsBeansTreeMap);



                    }

                    JSONArray exclutions = new JSONObject(data).getJSONArray("exclusions");
                    for (int i = 0; i < exclutions.length(); i++) {
                        JSONArray nested= exclutions.getJSONArray(i);
                        ExclusionsBean exclusionsBean =new ExclusionsBean();
                        for (int j = 0; j < nested.length(); j++) {
                            JSONObject values= nested.getJSONObject(j);
                            if (j%2==0)
                            {
                                exclusionsBean.setFacility_id1(values.getInt("facility_id"));
                                exclusionsBean.setOptions_id1(values.getInt("options_id"));
                            }
                            else 
                            {
                                exclusionsBean.setFacility_id2(values.getInt("facility_id"));
                                exclusionsBean.setOptions_id2(values.getInt("options_id"));
                            }




                        }
                        exclusionsBeanTreeMap.put(i, exclusionsBean);

                    }
                    Set<FacilitiesBean> keys=facilitiesBeanTreeMap.keySet();
                    Set<Integer> keys2=exclusionsBeanTreeMap.keySet();
                    for (FacilitiesBean key:keys)
                    {
                        System.out.println(key.toString()+" : "+facilitiesBeanTreeMap.get(key).toString());
                        
                    }

                    for (int k:keys2)
                    {
                        System.out.println(exclusionsBeanTreeMap.get(k).toString());
                    }





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