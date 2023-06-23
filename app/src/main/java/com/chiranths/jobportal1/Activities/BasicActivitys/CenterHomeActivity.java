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

import com.chiranths.jobportal1.Adapters.CenterHomeadaptor;
import com.chiranths.jobportal1.Model.HotelsModel;
import com.chiranths.jobportal1.Model.ProductInfo;
import com.chiranths.jobportal1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CenterHomeActivity extends AppCompatActivity {

    ArrayList<HotelsModel> productinfolist =new ArrayList();
    private CenterHomeadaptor centerHomeadaptor;
    RecyclerView rv_center_prop;
    Handler mHandler = new Handler();
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_home);

        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("center");

        rv_center_prop = findViewById(R.id.rv_center_prop);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                fetchdata();
            }
        });
    }

    private void fetchdata() {
        DatabaseReference productsinfo = FirebaseDatabase.getInstance().getReference().child("hotelsforyou");
        productsinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            productinfolist.add(new HotelsModel(
                                    String.valueOf(userData.get("name")),
                                    String.valueOf(userData.get("image")),
                                    String.valueOf(userData.get("image2")),
                                    String.valueOf(userData.get("pid")),
                                    String.valueOf(userData.get("date")),
                                    String.valueOf(userData.get("category")),
                                    String.valueOf(userData.get("price")),
                                    String.valueOf(userData.get("address")),
                                    String.valueOf(userData.get("owner")),
                                    String.valueOf(userData.get("alternative")),
                                    String.valueOf(userData.get("Number")),
                                    String.valueOf(userData.get("email")),
                                    String.valueOf(userData.get("website")),
                                    String.valueOf(userData.get("parking")),
                                    String.valueOf(userData.get("discription")),
                                    String.valueOf(userData.get("Rating")),
                                    String.valueOf(userData.get("Status")),
                                    String.valueOf(userData.get("point1")),
                                    String.valueOf(userData.get("point2")),
                                    String.valueOf(userData.get("point3")),
                                    String.valueOf(userData.get("Approval"))));

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }

                    // Upcoming Event
                    centerHomeadaptor = new CenterHomeadaptor(productinfolist, CenterHomeActivity.this);
                    RecyclerView.LayoutManager elayoutManager = new LinearLayoutManager(CenterHomeActivity.this,RecyclerView.VERTICAL,false);
                    rv_center_prop.setLayoutManager(new GridLayoutManager(CenterHomeActivity.this, 1));
                    rv_center_prop.setItemAnimator(new DefaultItemAnimator());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            rv_center_prop.setAdapter(centerHomeadaptor);

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