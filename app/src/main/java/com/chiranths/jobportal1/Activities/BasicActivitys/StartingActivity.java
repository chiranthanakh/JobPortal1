package com.chiranths.jobportal1.Activities.BasicActivitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class StartingActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout cv_jobs,cv_propertys,cv_servicess,cv_loans;

    private ArrayList<UpcomingEvent> upcomingEventList = new ArrayList<>();
    RecyclerView recyclerViewEvent;
    private BottomhomeRecyclarviewAdaptor bottomhomeRecyclarviewAdaptor;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> addresses;

    final int speedScroll = 150;
    final Handler handler = new Handler();

    private ArrayList<NoticeBoard> noticeBoardList = new ArrayList<>();
    RecyclerView recyclerView,recyclarviewads;
    private CoroselListAdaptor coroselListAdaptor;
    AdsAdaptor adsAdaptor;
    String id,name,mail,pic;

    ArrayList coroselimagelist =new ArrayList();
    ArrayList adslist =new ArrayList();
    ArrayList<ProductInfo> productinfolist =new ArrayList();
    private int[] images = {R.drawable.banner1,
            R.drawable.banner1, R.drawable.banner1};
    CarouselView carouselView;
    DrawerLayout drawer_layout;
    ImageView iv_nav_view,iv_bell;
    Handler mHandler = new Handler();
    TextView tv_location,tv_pincode;
    ProgressDialog progressDialog;

    TextView admin_btn;

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

        displayLocationSettingsRequest(this);
        progressDialog=new ProgressDialog(this);

        initilize();

    }

    private void initilize()
    {

        drawer_layout = findViewById(R.id.drawer_layout_main);
        iv_nav_view = findViewById(R.id.iv_nav_view);
        iv_bell = findViewById(R.id.iv_bell);
        iv_bell.setOnClickListener(this);
        iv_nav_view.setOnClickListener(this);
        cv_jobs =  findViewById(R.id.cv_jobs);
        cv_servicess = findViewById(R.id.cv_servicess);
        cv_propertys = findViewById(R.id.cv_propertys);
        cv_loans = findViewById(R.id.cv_loans);
        tv_location = findViewById(R.id.tv_location);
        tv_pincode = findViewById(R.id.tv_pincode);
        admin_btn = findViewById(R.id.admin_btn);
        cv_loans.setOnClickListener(this);
        cv_propertys.setOnClickListener(this);
        cv_propertys.setOnClickListener(this);
        cv_jobs.setOnClickListener(this);

        recyclerView =(RecyclerView)findViewById(R.id.rv_home_event);
        recyclarviewads = findViewById(R.id.rv_adds_layots1);
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

        if(progressDialog!=null)
        {
            if(!progressDialog.isShowing()) {

                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        fetchads();
                    }
                });
            }

        }


        iv_bell.setOnClickListener(new View.OnClickListener() {
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

                            adslist.add(userData.get("image")+"---"+userData.get("pid")+"---"+userData.get("category")+"---"+userData.get("price")+"---"+userData.get("propertysize")+"---"+userData.get("number"));

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

                            if(progressDialog!=null)
                            {
                                if(progressDialog.isShowing())
                                {
                                    progressDialog.dismiss();
                                }
                            }

                            recyclarviewads.setAdapter(adsAdaptor);

                        }
                    });
                    adsAdaptor.notifyItemRangeInserted(0, adslist.size());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                    recyclerViewEvent.setLayoutManager(new GridLayoutManager(StartingActivity.this, 1));
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

    //location fetch
    private void displayLocationSettingsRequest(Context context) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else{
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();
                    if(location !=null){
                        Geocoder geocoder = new Geocoder(StartingActivity.this, Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                            // textView.setText(addresses.get(0).getLocality() + addresses.get(0).getPostalCode() + addresses.get(0).getThoroughfare() + addresses.get(0).getSubLocality() + addresses.get(0).getSubLocality());
                          //  tv_location.setText(addresses.get(0).getLocality());
                          //  tv_pincode.setText(addresses.get(0).getSubLocality()+" - "+addresses.get(0).getPostalCode());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}