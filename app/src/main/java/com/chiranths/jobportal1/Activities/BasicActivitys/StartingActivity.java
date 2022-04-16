package com.chiranths.jobportal1.Activities.BasicActivitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chiranths.jobportal1.Activities.BasicActivitys.LoginActivity;
import com.chiranths.jobportal1.Activities.ExtraClass.Admincoroselimages;
import com.chiranths.jobportal1.Activities.LoanActivity.LoanActivity;
import com.chiranths.jobportal1.Activities.Propertys.PropertyActivity;
import com.chiranths.jobportal1.Activities.Servicess.ServicesActivity;
import com.chiranths.jobportal1.Adapters.AdsAdaptor;
import com.chiranths.jobportal1.Adapters.BottomhomeRecyclarviewAdaptor;
import com.chiranths.jobportal1.Adapters.CoroselListAdaptor;
import com.chiranths.jobportal1.Adapters.HomeEventAdapter;
import com.chiranths.jobportal1.Adapters.HomeNoticeBoardAdapter;
import com.chiranths.jobportal1.Model.NoticeBoard;
import com.chiranths.jobportal1.Model.UpcomingEvent;
import com.chiranths.jobportal1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StartingActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout cv_jobs,cv_propertys,cv_servicess,cv_loans;

    private ArrayList<UpcomingEvent> upcomingEventList = new ArrayList<>();
    RecyclerView recyclerViewEvent;
    private BottomhomeRecyclarviewAdaptor bottomhomeRecyclarviewAdaptor;

    final int speedScroll = 150;
    final Handler handler = new Handler();

    private ArrayList<NoticeBoard> noticeBoardList = new ArrayList<>();
    RecyclerView recyclerView,recyclarviewads;
    private CoroselListAdaptor coroselListAdaptor;
    AdsAdaptor adsAdaptor;
    String id,name,mail,pic;
    FrameLayout admin_btn;
    ArrayList coroselimagelist =new ArrayList();
    ArrayList adslist =new ArrayList();
    ArrayList<ProductInfo> productinfolist =new ArrayList();
    private int[] images = {R.drawable.banner1,
            R.drawable.banner1, R.drawable.banner1};
    CarouselView carouselView;
    DrawerLayout drawer_layout;
    ImageView iv_nav_view;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        SharedPreferences sh = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();
        id = sh.getString("id", null);
        name = sh.getString("name",null);
        mail = sh.getString("mail",null);
        pic = sh.getString("pic",null);


        initilize();

    }

    private void initilize()
    {

        drawer_layout = findViewById(R.id.drawer_layout_main);
        iv_nav_view = findViewById(R.id.iv_nav_view);
        iv_nav_view.setOnClickListener(this);

        cv_jobs =  findViewById(R.id.cv_jobs);
        cv_servicess = findViewById(R.id.cv_servicess);
        cv_propertys = findViewById(R.id.cv_propertys);
        cv_loans = findViewById(R.id.cv_loans);
        admin_btn = findViewById(R.id.admin_btn);
        cv_loans.setOnClickListener(this);
        cv_propertys.setOnClickListener(this);
        cv_propertys.setOnClickListener(this);
        cv_jobs.setOnClickListener(this);

        recyclerView =(RecyclerView)findViewById(R.id.rv_home_event);
        recyclarviewads = findViewById(R.id.rv_adds_layots);
        recyclerViewEvent = (RecyclerView)findViewById(R.id.rv_dash_prop);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                fetchcorosel();
            }
        });
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                fetchdata();
            }
        });

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                fetchads();
            }
        });


        admin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartingActivity.this, Admincoroselimages.class);
                startActivity(intent);
            }
        });
    }


    private void fetchcorosel() {
        DatabaseReference coroselimage = FirebaseDatabase.getInstance().getReference().child("Corosels");

        coroselimage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{

                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            coroselimagelist.add(userData.get("image"));

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }

                    coroselListAdaptor =new CoroselListAdaptor(coroselimagelist,StartingActivity.this);
                    RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(StartingActivity.this, RecyclerView.HORIZONTAL, false);
                    recyclerView.setLayoutManager(nlayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(coroselListAdaptor);
                        }
                    });
                    coroselListAdaptor.notifyItemRangeInserted(0, coroselimagelist.size());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchads() {

        DatabaseReference adsimage = FirebaseDatabase.getInstance().getReference().child("adsforyou");

        adsimage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{

                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            adslist.add(userData.get("image"));

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }

                    adsAdaptor =new AdsAdaptor(adslist,StartingActivity.this);
                    RecyclerView.LayoutManager n1layoutManager = new LinearLayoutManager(StartingActivity.this, RecyclerView.HORIZONTAL, false);
                    recyclarviewads.setLayoutManager(n1layoutManager);
                    recyclarviewads.setItemAnimator(new DefaultItemAnimator());

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclarviewads.setAdapter(adsAdaptor);
                        }
                    });
                   // recyclarviewads.notifyItemRangeInserted(0, adslist.size());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchdata() {

        DatabaseReference productsinfo = FirebaseDatabase.getInstance().getReference().child("Products");

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
                                    String.valueOf(userData.get("type"))));

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }

                    // Upcoming Event
                    bottomhomeRecyclarviewAdaptor = new BottomhomeRecyclarviewAdaptor(productinfolist, StartingActivity.this);
                    RecyclerView.LayoutManager elayoutManager = new LinearLayoutManager(StartingActivity.this,RecyclerView.VERTICAL,false);
                    recyclerViewEvent.setLayoutManager(new GridLayoutManager(StartingActivity.this, 2));
                    recyclerViewEvent.setItemAnimator(new DefaultItemAnimator());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerViewEvent.setAdapter(bottomhomeRecyclarviewAdaptor);

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            Picasso.get().load(String.valueOf(coroselimagelist.get(position))).into(imageView);
        }
    };


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.cv_jobs:

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                /*Intent intent;
                if(name.equals(null)){
                   intent = new Intent(getApplicationContext(), LoginActivity.class);
                }else {
                    intent = new Intent(getApplicationContext(), RoleActivity.class);
                }
                startActivity(intent);
                finish();*/

                break;

            case R.id.cv_propertys:

                Intent intent1 = new Intent(getApplicationContext(), PropertyActivity.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.cv_loans:

                Intent intent2 = new Intent(getApplicationContext(), LoanActivity.class);
                startActivity(intent2);

                break;
            case R.id.iv_nav_view:
                drawer_layout.openDrawer(GravityCompat.START);
                break;


        }


    }
}