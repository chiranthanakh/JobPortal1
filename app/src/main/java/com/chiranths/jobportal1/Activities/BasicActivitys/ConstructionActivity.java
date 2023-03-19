package com.chiranths.jobportal1.Activities.BasicActivitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.chiranths.jobportal1.Adapters.ConstructorAdaptor;
import com.chiranths.jobportal1.Adapters.TravelsAdaptor;
import com.chiranths.jobportal1.Model.ConstructionModel;
import com.chiranths.jobportal1.Model.TravelsModel;
import com.chiranths.jobportal1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ConstructionActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cv_contractors,cv_architects,cv_interior_designers,cv_construction_meterials,cv_hardware_welders,
            cv_painters,cv_carpenters,cv_electricians,cv_plumbers;
    String Name,category,cost,contactDetails,contactDetails2,experience,service1,service2,service3,service4,saveCurrentDate,saveCurrentTime,discription,vehicleNumber;
    RecyclerView rv_construction;
    ArrayList<ConstructionModel> constructioninfo =new ArrayList();
    private ConstructorAdaptor constructionAdaptor;
    Handler mHandler = new Handler();
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construction);

        initilize();
        fetchdata();
    }

    private void initilize() {

        cv_contractors = findViewById(R.id.cv_contractors);
        cv_architects = findViewById(R.id.cv_architects);
        cv_interior_designers = findViewById(R.id.cv_interior_designers);
        cv_construction_meterials = findViewById(R.id.cv_construction_meterials);
        cv_hardware_welders = findViewById(R.id.cv_hardware_welders);
        cv_painters = findViewById(R.id.cv_carpenters);
        cv_carpenters = findViewById(R.id.cv_carpenters);
        cv_electricians = findViewById(R.id.cv_electricians);
        cv_plumbers = findViewById(R.id.cv_plumbers);

        cv_contractors.setOnClickListener(this);
        cv_architects.setOnClickListener(this);
        cv_interior_designers.setOnClickListener(this);
        cv_construction_meterials.setOnClickListener(this);
        cv_hardware_welders.setOnClickListener(this);
        cv_painters.setOnClickListener(this);
        cv_carpenters.setOnClickListener(this);
        cv_electricians.setOnClickListener(this);
        cv_plumbers.setOnClickListener(this);
        rv_construction = findViewById(R.id.rv_constructions_relates);


    }

    private void fetchdata() {
        DatabaseReference productsinfo = FirebaseDatabase.getInstance().getReference().child("constructionforyou");
        productsinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            constructioninfo.add(new ConstructionModel(String.valueOf(userData.get("pid")),
                                    String.valueOf(userData.get("date")),
                                    String.valueOf(userData.get("time")),
                                    String.valueOf(userData.get("name")),
                                    String.valueOf(userData.get("category")),
                                    String.valueOf(userData.get("cost")),
                                    String.valueOf(userData.get("number1")),
                                    String.valueOf(userData.get("number2")),
                                    String.valueOf(userData.get("experience")),
                                    String.valueOf(userData.get("servicess1")),
                                    String.valueOf(userData.get("servicess2")),
                                    String.valueOf(userData.get("servicess3")),
                                    String.valueOf(userData.get("servicess4")),
                                    String.valueOf(userData.get("discription")),
                                    String.valueOf(userData.get("verified")),
                                    String.valueOf(userData.get("image")),
                                    String.valueOf(userData.get("image2"))));

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }

                    // Upcoming Event
                    constructionAdaptor = new ConstructorAdaptor(constructioninfo, ConstructionActivity.this);
                    RecyclerView.LayoutManager elayoutManager = new LinearLayoutManager(ConstructionActivity.this,RecyclerView.VERTICAL,false);
                    rv_construction.setLayoutManager(new GridLayoutManager(ConstructionActivity.this, 1));
                    rv_construction.setItemAnimator(new DefaultItemAnimator());
                    rv_construction.setItemAnimator(new DefaultItemAnimator());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            rv_construction.setAdapter(constructionAdaptor);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ll_car:
                Intent intent = new Intent(this, TravelsListactivity.class);
                bundle.putString("type","car");
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.ll_bus:
                Intent intent2 = new Intent(this, TravelsListactivity.class);
                bundle.putString("type","bus");
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;

            case R.id.ll_tempotravel:
                Intent intent3 = new Intent(this, TravelsListactivity.class);
                bundle.putString("type","tt");
                intent3.putExtras(bundle);
                startActivity(intent3);
                break;

            case R.id.ll_auto:
                Intent intent4 = new Intent(this, TravelsListactivity.class);
                bundle.putString("type","auto");
                intent4.putExtras(bundle);
                startActivity(intent4);
                break;

            case R.id.ll_transports:
                Intent intent5 = new Intent(this, TravelsListactivity.class);
                bundle.putString("type","transport");
                intent5.putExtras(bundle);
                startActivity(intent5);
                break;

            case R.id.ll_heavy_vehicles:
                Intent intent6 = new Intent(this, Travelsactivity.class);
                bundle.putString("type","HV");
                intent6.putExtras(bundle);
                startActivity(intent6);
                break;
        }
    }
}