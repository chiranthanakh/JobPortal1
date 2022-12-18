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

import com.chiranths.jobportal1.Adapters.BottomhomeRecyclarviewAdaptor;
import com.chiranths.jobportal1.Adapters.CenterHomeadaptor;
import com.chiranths.jobportal1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CenterHomeActivity extends AppCompatActivity {

    ArrayList<ProductInfo> productinfolist =new ArrayList();
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

        DatabaseReference productsinfo = FirebaseDatabase.getInstance().getReference().child("hotforyou");

        productsinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{

                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            productinfolist.add(new ProductInfo(String.valueOf(userData.get("category")),
                                    String.valueOf(userData.get("date")),
                                    String.valueOf(userData.get("description")),
                                    String.valueOf(userData.get("image")),
                                    String.valueOf(userData.get("location")),
                                    String.valueOf(userData.get("number")),
                                    String.valueOf(userData.get("pid")),
                                    String.valueOf(userData.get("pname")),
                                    String.valueOf(userData.get("price")),
                                    String.valueOf(userData.get("propertysize")),
                                    String.valueOf(userData.get("time")),
                                    String.valueOf(userData.get("type")),
                                    String.valueOf(userData.get("postedby"))));

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