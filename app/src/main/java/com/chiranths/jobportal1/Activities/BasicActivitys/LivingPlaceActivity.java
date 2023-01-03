package com.chiranths.jobportal1.Activities.BasicActivitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.chiranths.jobportal1.Adapters.CenterHomeadaptor;
import com.chiranths.jobportal1.Adapters.LivingPlaceAdaptor;
import com.chiranths.jobportal1.Model.LivingPlaceModel;
import com.chiranths.jobportal1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class LivingPlaceActivity extends AppCompatActivity {

    ArrayList<LivingPlaceModel> productinfolist =new ArrayList();
    private LivingPlaceAdaptor livingPlaceAdaptor;
    RecyclerView rv_center_prop;
    Handler mHandler = new Handler();
    ImageView backButton;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_home);

        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("center");
        //backButton = findViewById(R.id.back_tool)

        rv_center_prop = findViewById(R.id.rv_center_prop);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                fetchdata();
            }
        });
    }

    private void fetchdata() {
        DatabaseReference productsinfo = FirebaseDatabase.getInstance().getReference().child("livingplaceforyou");
        productsinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            productinfolist.add(new LivingPlaceModel(
                                    String.valueOf(userData.get("pid")),
                                    String.valueOf(userData.get("saveCurrentDate")),
                                    String.valueOf(userData.get("saveCurrentTime")),
                                    String.valueOf(userData.get("title")),
                                    String.valueOf(userData.get("category")),
                                    String.valueOf(userData.get("rent_lease")),
                                    String.valueOf(userData.get("floore")),
                                    String.valueOf(userData.get("rentamount")),
                                    String.valueOf(userData.get("location")),
                                    String.valueOf(userData.get("contactNumber")),
                                    String.valueOf(userData.get("Approval")),
                                    String.valueOf(userData.get("nuBHK")),
                                    String.valueOf(userData.get("sqft")),
                                    String.valueOf(userData.get("water")),
                                    String.valueOf(userData.get("parking")),
                                    String.valueOf(userData.get("postedBY")),
                                    String.valueOf(userData.get("discription")),
                                    String.valueOf(userData.get("image2")),
                                    String.valueOf(userData.get("image"))
                            ));

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }

                    // Upcoming Event
                    livingPlaceAdaptor = new LivingPlaceAdaptor(productinfolist, LivingPlaceActivity.this);
                    RecyclerView.LayoutManager elayoutManager = new LinearLayoutManager(LivingPlaceActivity.this,RecyclerView.VERTICAL,false);
                    rv_center_prop.setLayoutManager(new GridLayoutManager(LivingPlaceActivity.this, 1));
                    rv_center_prop.setItemAnimator(new DefaultItemAnimator());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            rv_center_prop.setAdapter(livingPlaceAdaptor);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}