package com.chiranths.jobportal1.Activities.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

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
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chiranths.jobportal1.Activities.Admin.Admin_corosel;
import com.chiranths.jobportal1.Activities.BasicActivitys.CenterHomeActivity;
import com.chiranths.jobportal1.Activities.BasicActivitys.LoginActivity;
import com.chiranths.jobportal1.Activities.BasicActivitys.ProductInfo;
import com.chiranths.jobportal1.Activities.BasicActivitys.SearchActivity;
import com.chiranths.jobportal1.Activities.BasicActivitys.SeeAllLayoutActivity;
import com.chiranths.jobportal1.Activities.BasicActivitys.UpcommingProjects;
import com.chiranths.jobportal1.Activities.Businesthings.BusinessActivity;
import com.chiranths.jobportal1.Activities.Admin.Admincoroselimages;
import com.chiranths.jobportal1.Activities.LoanActivity.LoanActivity;
import com.chiranths.jobportal1.Activities.Propertys.PropertyActivity;
import com.chiranths.jobportal1.Activities.Sell.SellActivity;
import com.chiranths.jobportal1.Activities.jobs.MainActivity;
import com.chiranths.jobportal1.Adapters.AdsAdaptor;
import com.chiranths.jobportal1.Adapters.BottomhomeRecyclarviewAdaptor;
import com.chiranths.jobportal1.Adapters.CoroselListAdaptor;
import com.chiranths.jobportal1.Adapters.LayoutsAdaptor;
import com.chiranths.jobportal1.Model.Corosolmodel;
import com.chiranths.jobportal1.Model.NoticeBoard;
import com.chiranths.jobportal1.Model.UpcomingEvent;
import com.chiranths.jobportal1.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class StartingActivity extends AppCompatActivity implements View.OnClickListener,Calldetails {

    LinearLayout cv_jobs,cv_propertys,cv_servicess,cv_loans;
    ImageView iv_sell;
    LinearLayout search_layout,ll_rent,ll_commercial_rent,ll_hotel;
    EditText search;
    TextView tv_seeall_upcooming, tv_seeall_layouts;

    private ArrayList<UpcomingEvent> upcomingEventList = new ArrayList<>();
    RecyclerView recyclerViewEvent;
    private BottomhomeRecyclarviewAdaptor bottomhomeRecyclarviewAdaptor;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> addresses;
    private String productRandomKey;
    private DatabaseReference CallerRef;
    final int speedScroll = 150;
    final Handler handler = new Handler();

    private ArrayList<NoticeBoard> noticeBoardList = new ArrayList<>();
    RecyclerView recyclerView,recyclarviewads,recyclar_Layouts;
    private CoroselListAdaptor coroselListAdaptor;
    AdsAdaptor adsAdaptor;
    LayoutsAdaptor layoutsAdaptor;
    String id,name ="",mail,pic;
    String nameofuser, userNumber, useremail;
    final int duration = 50;
    final int pixelsToMove = 5;
    private final Handler mHandler2 = new Handler(Looper.getMainLooper());

    ArrayList<Corosolmodel> coroselimagelist =new ArrayList<Corosolmodel>();
    ArrayList adslist =new ArrayList();
    ArrayList layoutslists =new ArrayList();
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
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        SharedPreferences sh = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();
        id = sh.getString("id", null);
        name = sh.getString("name","");
        mail = sh.getString("mail",null);
        pic = sh.getString("pic",null);
        displayLocationSettingsRequest(this);
        progressDialog=new ProgressDialog(this);

        initilize();
    }

    @Override
    protected void onResume() {
        super.onResume();

       // initilize();
     }

    private void initilize()
    {
        drawer_layout = findViewById(R.id.drawer_layout_main);
        iv_nav_view = findViewById(R.id.iv_nav_view);
        iv_bell = findViewById(R.id.iv_bell);
        iv_bell.setOnClickListener(this);
        iv_nav_view.setOnClickListener(this);
        cv_jobs =  findViewById(R.id.cv_jobs);
        cv_servicess = findViewById(R.id.cv_servicess1);
        cv_propertys = findViewById(R.id.cv_propertys);
        cv_loans = findViewById(R.id.cv_loans);
        tv_location = findViewById(R.id.tv_location);
        search_layout = findViewById(R.id.search_layout);
        search = findViewById(R.id.main_edt_search2);
        tv_seeall_upcooming = findViewById(R.id.tv_seeall_upcomming);
        tv_seeall_layouts = findViewById(R.id.tv_seeall_layouts);
        tv_seeall_layouts.setOnClickListener(this);
        tv_seeall_upcooming.setOnClickListener(this);

        search.setOnClickListener(this);
        search_layout.setOnClickListener(this);
      //  tv_pincode = findViewById(R.id.tv_pincode);
        admin_btn = findViewById(R.id.admin_btn);
        iv_sell = findViewById(R.id.iv_sell);
        iv_sell.setOnClickListener(this);
        cv_loans.setOnClickListener(this);
        cv_propertys.setOnClickListener(this);
        cv_servicess.setOnClickListener(this);
        cv_jobs.setOnClickListener(this);
        ll_rent= findViewById(R.id.ll_rent);
        ll_commercial_rent = findViewById(R.id.ll_commercial_rent);
        ll_hotel =findViewById(R.id.ll_hotel);
        ll_rent.setOnClickListener(this);
        ll_hotel.setOnClickListener(this);
        ll_commercial_rent.setOnClickListener(this);

        recyclerView =findViewById(R.id.rv_home_event);
        recyclarviewads = findViewById(R.id.rv_adds_layots1);
        recyclerViewEvent = findViewById(R.id.rv_dash_prop);
        recyclar_Layouts = findViewById(R.id.rv_Layouts);

        //fetchcorosel();
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
                //fetchads();
            }
        });
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                fetchlayouts();
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
    private final Runnable SCROLLING_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            recyclerView.smoothScrollBy(pixelsToMove, 0);
            mHandler2.postDelayed(this, duration);
        }
    };


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
                            coroselimagelist.add(new Corosolmodel(String.valueOf(userData.get("image")),String.valueOf(userData.get("type"))));

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
                    SnapHelper snapHelper = new PagerSnapHelper();
                    snapHelper.attachToRecyclerView(recyclerView);
                    snapHelper.onFling(20,20);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(coroselListAdaptor);

                        }
                    });
                    coroselListAdaptor.notifyItemRangeInserted(0, coroselimagelist.size());

                   /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                                mHandler2.removeCallbacks(SCROLLING_RUNNABLE);
                                Handler postHandler = new Handler();
                                postHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(coroselListAdaptor);
                                        recyclerView.smoothScrollBy(pixelsToMove, 0);

                                        mHandler2.postDelayed(SCROLLING_RUNNABLE, 200);
                                    }
                                }, 2000);

                        }
                    });

                    mHandler.postDelayed(SCROLLING_RUNNABLE, 200);*/
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

                            adslist.add(userData.get("image")+"---"+userData.get("pid")+"---"+userData.get("category")+"---"+userData.get("price")+"---"+userData.get("propertysize")+"---"+userData.get("number")+"---"+userData.get("location"));

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }
                    Collections.shuffle(adslist);
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
                            adsAdaptor.notifyItemRangeInserted(0, adslist.size());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //layouts recyclarview
    private void fetchlayouts() {

        DatabaseReference adsimage = FirebaseDatabase.getInstance().getReference().child("layoutsforyou");

        adsimage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{

                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            layoutslists.add(userData.get("image")+"---"+userData.get("pid")+"---"+userData.get("category")+"---"+userData.get("price")+"---"+userData.get("propertysize")+"---"+userData.get("number")+"---"+userData.get("location"));

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }
                    Collections.shuffle(layoutslists);
                    layoutsAdaptor =new LayoutsAdaptor(layoutslists,StartingActivity.this);
                    RecyclerView.LayoutManager n1layoutManager = new LinearLayoutManager(StartingActivity.this, RecyclerView.HORIZONTAL, false);
                    recyclar_Layouts.setLayoutManager(n1layoutManager);
                    recyclar_Layouts.setItemAnimator(new DefaultItemAnimator());

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
                            recyclar_Layouts.setAdapter(layoutsAdaptor);
                            layoutsAdaptor.notifyItemRangeInserted(0, adslist.size());
                        }
                    });


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
                    bottomhomeRecyclarviewAdaptor = new BottomhomeRecyclarviewAdaptor(productinfolist, StartingActivity.this, userNumber,nameofuser);
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

           // Picasso.get().load(String.valueOf(coroselimagelist.get(position))).into(imageView);

            Glide.with(StartingActivity.this)
                    .load(String.valueOf(coroselimagelist.get(position)))
                    .into(imageView);
        }
    };


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.cv_jobs:

                //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
               // startActivity(intent);

                Intent intent;
                if(name.equals("")){
                   intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    String userId = GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getId();
                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(userId)
                            .child("role")
                            .setValue("jobseeker").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                   // intent = new Intent(getApplicationContext(), RoleActivity.class);
                }


                break;

            case R.id.cv_propertys:

                Intent intent1 = new Intent(getApplicationContext(), PropertyActivity.class);
                startActivity(intent1);
                break;

            case R.id.cv_loans:

                Intent intent2 = new Intent(getApplicationContext(), LoanActivity.class);
                startActivity(intent2);
                break;

            case R.id.cv_servicess1:
                Intent intent3 = new Intent(getApplicationContext(), BusinessActivity.class);
                startActivity(intent3);
                break;

            case R.id.iv_nav_view:
                drawer_layout.openDrawer(GravityCompat.START);
                break;

            case R.id.iv_sell:
                Intent intent4 = new Intent(getApplicationContext(), SellActivity.class);
                startActivity(intent4);
                break;

            case R.id.main_edt_search2:
                Intent intent5 = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent5);
                break;

            case R.id.search_layout:
                Intent intent6 = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent6);
                break;

            case R.id.ll_rent:
                Intent intent7 = new Intent(getApplicationContext(), CenterHomeActivity.class);
                bundle.putString("center","hotel");
                intent7.putExtras(bundle);
                startActivity(intent7);
                break;

            case R.id.ll_commercial_rent:
                Intent intent8 = new Intent(getApplicationContext(), CenterHomeActivity.class);
                bundle.putString("center","commercial");
                intent8.putExtras(bundle);
                startActivity(intent8);
                break;

            case R.id.ll_hotel:
                Intent intent9 = new Intent(getApplicationContext(), CenterHomeActivity.class);
                bundle.putString("center","hotel");
                intent9.putExtras(bundle);
                startActivity(intent9);
                break;

            case R.id.tv_seeall_upcomming:
                Intent intent10 = new Intent(getApplicationContext(), UpcommingProjects.class);
                intent10.putExtras(bundle);
                startActivity(intent10);
                break;

            case R.id.tv_seeall_layouts:
                Intent intent11 = new Intent(getApplicationContext(), SeeAllLayoutActivity.class);
                intent11.putExtras(bundle);
                startActivity(intent11);

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

    @Override
    public void callinfo(String number, String name, String caller_name,String caller_number) {
        CallerRef = FirebaseDatabase.getInstance().getReference().child("callDetails");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("date", saveCurrentDate);
            productMap.put("time", saveCurrentTime);
            productMap.put("called_name",name);
            productMap.put("called_number",number);
            productMap.put("caller_name", caller_name);
            productMap.put("caller_number",caller_number);

        CallerRef.child(productRandomKey).updateChildren(productMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                            }
                            else
                            {
                            }
                        }
                    });
        }

}