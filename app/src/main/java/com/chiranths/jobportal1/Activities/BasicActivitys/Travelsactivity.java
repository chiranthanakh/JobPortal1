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
import android.widget.LinearLayout;

import com.chiranths.jobportal1.Adapters.CenterHomeadaptor;
import com.chiranths.jobportal1.Adapters.TravelsAdaptor;
import com.chiranths.jobportal1.Model.ProductInfo;
import com.chiranths.jobportal1.Model.TravelsModel;
import com.chiranths.jobportal1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Travelsactivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ll_car,ll_tt,ll_bus,ll_auto,ll_transport,ll_heavyvehicles;
    ArrayList<TravelsModel> vehicleinfo =new ArrayList();
    private TravelsAdaptor travelsAdaptor;
    RecyclerView rv_travels;
    Handler mHandler = new Handler();
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelsactivity);
        initilize();
        fetchdata();
    }

    private void initilize() {

        ll_car = findViewById(R.id.ll_car);
        ll_bus = findViewById(R.id.ll_bus);
        ll_tt = findViewById(R.id.ll_tempotravel);
        ll_auto = findViewById(R.id.ll_auto);
        ll_transport = findViewById(R.id.ll_transports);
        ll_heavyvehicles = findViewById(R.id.ll_heavy_vehicles);
        ll_car.setOnClickListener(this);
        ll_bus.setOnClickListener(this);
        ll_transport.setOnClickListener(this);
        ll_tt.setOnClickListener(this);
        ll_auto.setOnClickListener(this);
        ll_heavyvehicles.setOnClickListener(this);

        rv_travels = findViewById(R.id.rv_travels);


    }

    private void fetchdata() {
        DatabaseReference productsinfo = FirebaseDatabase.getInstance().getReference().child("travelsforyou");
        productsinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            vehicleinfo.add(new TravelsModel(String.valueOf(userData.get("pid")),
                                    String.valueOf(userData.get("date")),
                                    String.valueOf(userData.get("time")),
                                    String.valueOf(userData.get("vehiclename")),
                                    String.valueOf(userData.get("category")),
                                    String.valueOf(userData.get("vehiclenumber")),
                                    String.valueOf(userData.get("costperkm")),
                                    String.valueOf(userData.get("contactnumber")),
                                    String.valueOf(userData.get("ownerNmae")),
                                    String.valueOf(userData.get("verified")),
                                    String.valueOf(userData.get("description")),
                                    String.valueOf(userData.get("image")),
                                    String.valueOf(userData.get("image2")),
                                    String.valueOf(userData.get("model"))));

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }

                    // Upcoming Event
                    travelsAdaptor = new TravelsAdaptor(vehicleinfo, Travelsactivity.this);
                    RecyclerView.LayoutManager elayoutManager = new LinearLayoutManager(Travelsactivity.this,RecyclerView.VERTICAL,false);
                    rv_travels.setLayoutManager(new GridLayoutManager(Travelsactivity.this, 1));
                    rv_travels.setItemAnimator(new DefaultItemAnimator());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            rv_travels.setAdapter(travelsAdaptor);
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
                Intent intent = new Intent(Travelsactivity.this, TravelsListactivity.class);
                bundle.putString("type","car");
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.ll_bus:
                Intent intent2 = new Intent(Travelsactivity.this, TravelsListactivity.class);
                bundle.putString("type","bus");
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;

            case R.id.ll_tempotravel:
                Intent intent3 = new Intent(Travelsactivity.this, TravelsListactivity.class);
                bundle.putString("type","tt");
                intent3.putExtras(bundle);
                startActivity(intent3);
                break;

            case R.id.ll_auto:
                Intent intent4 = new Intent(Travelsactivity.this, TravelsListactivity.class);
                bundle.putString("type","auto");
                intent4.putExtras(bundle);
                startActivity(intent4);
                break;

            case R.id.ll_transports:
                Intent intent5 = new Intent(Travelsactivity.this, TravelsListactivity.class);
                bundle.putString("type","transport");
                intent5.putExtras(bundle);
                startActivity(intent5);
                break;

            case R.id.ll_heavy_vehicles:
                Intent intent6 = new Intent(Travelsactivity.this, Travelsactivity.class);
                bundle.putString("type","HV");
                intent6.putExtras(bundle);
                startActivity(intent6);
                break;
        }
    }
}