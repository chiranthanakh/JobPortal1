package com.chiranths.jobportal1.Activities.BasicActivitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chiranths.jobportal1.Activities.BasicActivitys.LoginActivity;
import com.chiranths.jobportal1.Activities.ExtraClass.Admincoroselimages;
import com.chiranths.jobportal1.Activities.LoanActivity.LoanActivity;
import com.chiranths.jobportal1.Activities.Propertys.PropertyActivity;
import com.chiranths.jobportal1.Activities.Servicess.ServicesActivity;
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

    CardView cv_jobs,cv_propertys,cv_servicess,cv_loans;

    private List<UpcomingEvent> upcomingEventList = new ArrayList<>();
    RecyclerView recyclerViewEvent;
    private HomeEventAdapter eventHomeAdapter;

    private ArrayList<NoticeBoard> noticeBoardList = new ArrayList<>();
    RecyclerView recyclerView;
    private HomeNoticeBoardAdapter homeNoticeBoardAdapter;
    String id,name,mail,pic;
    FrameLayout admin_btn;
    ArrayList coroselimagelist =new ArrayList();
    ArrayList<ProductInfo> productinfolist =new ArrayList();
    private int[] images = {R.drawable.banner1,
            R.drawable.banner1, R.drawable.banner1};
    CarouselView carouselView;

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
        cv_jobs =  findViewById(R.id.cv_jobs);
        cv_servicess = findViewById(R.id.cv_servicess);
        cv_propertys = findViewById(R.id.cv_propertys);
        cv_loans = findViewById(R.id.cv_loans);
        admin_btn = findViewById(R.id.admin_btn);
        cv_loans.setOnClickListener(this);
        cv_propertys.setOnClickListener(this);
        cv_propertys.setOnClickListener(this);
        cv_jobs.setOnClickListener(this);

        recyclerView =(RecyclerView)findViewById(R.id.rv_home_notice);
        recyclerViewEvent = (RecyclerView)findViewById(R.id.rv_home_event);

        fetchcorosel();
        fetchdata();

        admin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartingActivity.this, Admincoroselimages.class);
                startActivity(intent);
            }
        });
    }

    private void adapters() {



        //Home Notice Board recycler view
        homeNoticeBoardAdapter =new HomeNoticeBoardAdapter(noticeBoardList);
        RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(nlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(homeNoticeBoardAdapter);
        prepareNoticeData();

    }

    private  void prepareNoticeData()
    {
        NoticeBoard noticeBoard = new NoticeBoard("Notice 1","It’s a one stop solution interactive portal","12 Mar 2020");
        noticeBoardList.add(noticeBoard);

       // noticeBoard = new NoticeBoard("Notice 2","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        //noticeBoardList.add(noticeBoard);


    }


    private  void prepareEventData()
    {
        UpcomingEvent upcomingEvent = new UpcomingEvent("Event 1","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities.","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);

        upcomingEvent = new UpcomingEvent("Event 2","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);

        upcomingEvent = new UpcomingEvent("Event 3","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);

        upcomingEvent = new UpcomingEvent("Event 4","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);

        upcomingEvent = new UpcomingEvent("Event 5","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);

        upcomingEvent = new UpcomingEvent("Event 6","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);

        upcomingEvent = new UpcomingEvent("Event 7","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);

        upcomingEvent = new UpcomingEvent("Event 8","It’s your personal HR Management System login. It’s a one stop solution interactive portal to enable you with complete HR related activities","12 Mar 2020");
        upcomingEventList.add(upcomingEvent);

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

                    carouselView = findViewById(R.id.carouselView);
                    carouselView.setImageListener(imageListener);
                    carouselView.setPageCount(coroselimagelist.size());

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
                    eventHomeAdapter = new HomeEventAdapter(productinfolist);
                    RecyclerView.LayoutManager elayoutManager = new LinearLayoutManager(StartingActivity.this,RecyclerView.VERTICAL,false);
                    recyclerViewEvent.setLayoutManager(new GridLayoutManager(StartingActivity.this, 2));
                    recyclerViewEvent.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewEvent.setAdapter(eventHomeAdapter);
                    prepareEventData();

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
               /* if(name.equals(null)){
                    intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                }else {
                    intent1 = new Intent(getApplicationContext(), AdminAddNewProductActivity.class);
                }*/
                startActivity(intent1);
                finish();
                break;

            case R.id.cv_loans:

                Intent intent2 = new Intent(getApplicationContext(), LoanActivity.class);
               /* if(name.equals(null)){
                    intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                }else {
                    intent1 = new Intent(getApplicationContext(), AdminAddNewProductActivity.class);
                }*/
                startActivity(intent2);

                break;


        }


    }
}