package com.example.physicswallah_assignment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter,adapter1,adapter2;
    String data="",room_choice,property_choice,facility_choice;
    Button refresh;
    Spinner property_type_spinner,number_of_rooms_spinner,other_facilities_spinner;
    TreeMap<FacilitiesBean,TreeMap<Integer,OptionsBeans>> facilitiesBeanTreeMap= new TreeMap<>();
    TreeMap<Integer, ExclusionsBean> exclusionsBeanTreeMap= new TreeMap<>();
    Handler handler= new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         refresh=findViewById(R.id.refresh);
        property_type_spinner=findViewById(R.id.property_type);
        number_of_rooms_spinner=findViewById(R.id.number_of_rooms);
        other_facilities_spinner=findViewById(R.id.other_facilities);
        new Fetchingjson().start();

            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i= getIntent();
                    finish();
                    startActivity(i);

                }
            });
    }
    //subclass to fetch and display the values
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
                        for (int j = 0; j <options.length(); j++) {
                            JSONObject opt=options.getJSONObject(j);

                            OptionsBeans optionsBeans= new OptionsBeans();
                            optionsBeans.setId(opt.getInt("id"));
                            optionsBeans.setIcon(opt.getString("icon"));
                            optionsBeans.setName(opt.getString("name"));
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


                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    display (facilitiesBeanTreeMap,exclusionsBeanTreeMap);

                }
            });

        }
    }
        //method to display
    private void display(TreeMap<FacilitiesBean, TreeMap<Integer, OptionsBeans>> facilitiesBeanTreeMap, TreeMap<Integer, ExclusionsBean> exclusionsBeanTreeMap) {
            /// array intialization
        ArrayList<String> property_type = new ArrayList<>();
        property_type.add("select property type");
        ArrayList<String> number_of_rooms = new ArrayList<>();
        number_of_rooms.add("select number of rooms");
        ArrayList<String> other_facilities = new ArrayList<>();
        other_facilities.add("select other facilities");

        Set<FacilitiesBean> keys=facilitiesBeanTreeMap.keySet();
       Set<Integer> keys2=exclusionsBeanTreeMap.keySet();

        for (FacilitiesBean key:keys)
        {
            Map<Integer,OptionsBeans> op;
            Set<Integer> keysetopt;
            op= facilitiesBeanTreeMap.get(key);
            keysetopt= op.keySet();


            for(int id:keysetopt)
            {

                if(key.getFacility_id()==1)
                property_type.add(op.get(id).getName());
                if(key.getFacility_id()==2)
                {
                    number_of_rooms.add(op.get(id).getName());
                }
                if(key.getFacility_id()==3)
                {
                    other_facilities.add(op.get(id).getName());
                }

            }

        }


         adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,property_type);
        property_type_spinner.setAdapter(adapter);
        adapter2 = new ArrayAdapter<String >(MainActivity.this, R.layout.support_simple_spinner_dropdown_item,other_facilities);
        property_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override       //action on spinner
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    property_choice =parent.getItemAtPosition(position).toString();
                    if(position!=0)
                    {
                        number_of_rooms_spinner.setEnabled(true);
                        for (FacilitiesBean key : keys) {
                                    Map<Integer, OptionsBeans> op = facilitiesBeanTreeMap.get(key);
                                    Set<Integer> ii = op.keySet();


                                    for (int id1 : ii) {
                                        if (op.get(id1).getName() == property_choice) {


                                            int found_option_id=op.get(id1).getId();
                                            int found_facility_id=key.getFacility_id();

                                            for (int exclusionkey:keys2)
                                            {
                                                if(exclusionsBeanTreeMap.get(exclusionkey).getFacility_id1()==found_facility_id && exclusionsBeanTreeMap.get(exclusionkey).getOptions_id1()==found_option_id)
                                                {

                                                    for (FacilitiesBean keyy:keys) {
                                                        Map<Integer,OptionsBeans> op11 = facilitiesBeanTreeMap.get(keyy);
                                                       Set<Integer> keysetopt = op11.keySet();


                                                        for (int iddd : keysetopt) {


                                                                int remove_id=exclusionsBeanTreeMap.get(exclusionkey).getOptions_id2();
                                                            if(keyy.getFacility_id()==2) {
                                                                number_of_rooms.clear();
                                                                number_of_rooms.add("select number of rooms");
                                                                if (op11.get(iddd).getId() != remove_id) {
                                                                    number_of_rooms.add(op11.get(iddd).getName());

                                                                }

                                                            }


                                                        }
                                                    }



                                                }

                                            }
                                        }

                                    }
                            }
                        adapter1 = new ArrayAdapter<String >(MainActivity.this, R.layout.support_simple_spinner_dropdown_item,number_of_rooms);
                        number_of_rooms_spinner.setAdapter(adapter1);
                        property_type_spinner.setEnabled(false);

                    }

                    else
                    {
                        number_of_rooms_spinner.setEnabled(false);
                        other_facilities_spinner.setEnabled(false);

                    }

            }

            @Override       //action on spinner
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        number_of_rooms_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 room_choice=parent.getItemAtPosition(position).toString();
                    if(position!=0)
                    {
                        other_facilities_spinner.setEnabled(true);
                        for (FacilitiesBean key : keys) {
                            Map<Integer, OptionsBeans> op = facilitiesBeanTreeMap.get(key);
                            Set<Integer> ii = op.keySet();


                            for (int id1 : ii) {
                                if (op.get(id1).getName() == room_choice) {
                                    int found_option_id=op.get(id1).getId();
                                    int found_facility_id=key.getFacility_id();
                                    for (int exclusionkey:keys2)
                                    {
                                        if(exclusionsBeanTreeMap.get(exclusionkey).getFacility_id1()==found_facility_id && exclusionsBeanTreeMap.get(exclusionkey).getOptions_id1()==found_option_id)
                                        {
                                            for (FacilitiesBean keyy:keys) {
                                                Map<Integer,OptionsBeans> op11 = facilitiesBeanTreeMap.get(keyy);
                                                Set<Integer> keysetopt = op11.keySet();


                                                for (int iddd : keysetopt) {
                                                    int remove_id=exclusionsBeanTreeMap.get(exclusionkey).getOptions_id2();
                                                    System.out.println("remove id "+remove_id);
                                                    System.out.println(op11.get(iddd).getId());
                                                    if(keyy.getFacility_id()==3)
                                                    {
                                                            System.out.println(op11.get(iddd).getId());
                                                            other_facilities.remove(op11.get(remove_id).getName());
                                                    }

                                                }
                                            }


                                            System.out.println(other_facilities);

                                        }

                                    }
                                }

                            }


                        }
                        adapter2 = new ArrayAdapter<String >(MainActivity.this, R.layout.support_simple_spinner_dropdown_item,other_facilities);
                        other_facilities_spinner.setAdapter(adapter2);
                    }
                    else
                    {
                        other_facilities_spinner.setEnabled(false);
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

            //action on spinner
        other_facilities_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                facility_choice= parent.getItemAtPosition(position).toString();
                if(position!=0) {
                    displayFinal(facility_choice, property_choice, room_choice);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
        //method for final output to display options and icons
    private void displayFinal(String facility_choice, String property_choice, String room_choice) {
        String optid1="",opticon1="",optid2="",opticon2="",optid3="",opticon3="";
        TextView outPut=findViewById(R.id.textView);
        Set<FacilitiesBean> facilitiesBeanSet=facilitiesBeanTreeMap.keySet();
        for (FacilitiesBean fb:facilitiesBeanSet ){
            Map<Integer,OptionsBeans> options=facilitiesBeanTreeMap.get(fb);
            Set<Integer> optionskey=options.keySet();
            for (int i:optionskey) {

                if(options.get(i).getName()==property_choice)
                {
                    opticon1=options.get(i).getIcon();
                    optid1=options.get(i).getName();

                }
                if(options.get(i).getName()==room_choice)
                {
                    opticon2=options.get(i).getIcon();
                    optid2=options.get(i).getName();

                }
                if(options.get(i).getName()==facility_choice)
                {
                    opticon3=options.get(i).getIcon();
                    optid3=options.get(i).getName();

                }

            }
        }

        outPut.setText(
                "property name : " + optid1 +"\n"+
                "property Icon :" + opticon1  +"\n"+
                " Rooms : " + optid2 +"\n"+
                "Room icon : " + opticon2 +"\n"+
                "Other facilities :" + optid3 +"\n"+
                "Facility icon :" + opticon3 );

    }



}