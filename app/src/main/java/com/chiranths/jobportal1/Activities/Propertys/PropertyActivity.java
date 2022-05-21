package com.chiranths.jobportal1.Activities.Propertys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.chiranths.jobportal1.Activities.BasicActivitys.StartingActivity;
import com.chiranths.jobportal1.Adapters.AdsAdaptor;
import com.chiranths.jobportal1.Adapters.CoroselListAdaptor;
import com.chiranths.jobportal1.Adapters.PropertyAdaptor;
import com.chiranths.jobportal1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class PropertyActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference ProductsRef;
    FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button btn_add;
    ImageView iv_sites,iv_green_land,iv_home,iv_commercial;
    Handler mHandler = new Handler();
    ArrayList propertylist =new ArrayList();
    ArrayList greenlandlist =new ArrayList();
    ArrayList siteslist =new ArrayList();
    ArrayList Homeslist =new ArrayList();
    ArrayList Rentallist =new ArrayList();
    ArrayList adslist =new ArrayList();
    PropertyAdaptor propertyAdaptor;
    AdsAdaptor adsAdaptor;
    RecyclerView recyclarviewads;
    CardView cv_homes, cv_sites, cv_green,cv_comerical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

          if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme);
            //when dark mode is enabled, we use the dark theme
        } else {
            setTheme(R.style.JobPortaltheam); //default app theme
        }


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.app_blue));
        }



        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        initilize();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                fetchads();
            }
        });


    }

    private void initilize() {

        btn_add = findViewById(R.id.btn_add_property);
        btn_add.setOnClickListener(this);
        iv_sites = findViewById(R.id.iv_sites);
        iv_commercial = findViewById(R.id.iv_commercial);
        iv_green_land = findViewById(R.id.iv_green_land);
        iv_home = findViewById(R.id.iv_home);

        iv_sites.setOnClickListener(this);
        iv_green_land.setOnClickListener(this);
        iv_commercial.setOnClickListener(this);
        iv_home.setOnClickListener(this);

        recyclarviewads = findViewById(R.id.rv_adds_layots2);
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager mgrid = new GridLayoutManager(this,1);

        recyclerView.setLayoutManager(mgrid);
        fetchcorosel();
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                    adsAdaptor =new AdsAdaptor(adslist,PropertyActivity.this);
                    RecyclerView.LayoutManager n1layoutManager = new LinearLayoutManager(PropertyActivity.this, RecyclerView.HORIZONTAL, false);
                    recyclarviewads.setLayoutManager(n1layoutManager);
                    recyclarviewads.setItemAnimator(new DefaultItemAnimator());

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
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


    private void fetchcorosel() {
        DatabaseReference coroselimage = FirebaseDatabase.getInstance().getReference().child("Products");

        coroselimage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{

                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            propertylist.add(userData.get("image")+"!!"+userData.get("pid")+"---"+userData.get("description")+"---"+
                                    userData.get("category")+"---"+userData.get("price")+"---"+userData.get("pname")
                                    +"---"+userData.get("propertysize")+"---"+userData.get("location")+"---"+userData.get("number")+"---"+userData.get("type"));

                            if(userData.get("type").equals("sites")){

                                siteslist.add(userData.get("type")+"!!"+userData.get("pid")+"---"+userData.get("description")+"---"+
                                        userData.get("category")+"---"+userData.get("price")+"---"+userData.get("pname")
                                        +"---"+userData.get("propertysize")+"---"+userData.get("location")+"---"+userData.get("number")+"---"+userData.get("type"));

                            }else if(userData.get("type").equals("homes")){

                                Homeslist.add(userData.get("image")+"!!"+userData.get("pid")+"---"+userData.get("description")+"---"+
                                        userData.get("category")+"---"+userData.get("price")+"---"+userData.get("pname")
                                        +"---"+userData.get("propertysize")+"---"+userData.get("location")+"---"+userData.get("number")+"---"+userData.get("type"));


                            }else if(userData.get("type").equals("greenland")){

                                greenlandlist.add(userData.get("image")+"!!"+userData.get("pid")+"---"+userData.get("description")+"---"+
                                        userData.get("category")+"---"+userData.get("price")+"---"+userData.get("pname")
                                        +"---"+userData.get("propertysize")+"---"+userData.get("location")+"---"+userData.get("number")+"---"+userData.get("type"));


                            }else if(userData.get("type").equals("rental")){

                                Rentallist.add(userData.get("image")+"!!"+userData.get("pid")+"---"+userData.get("description")+"---"+
                                        userData.get("category")+"---"+userData.get("price")+"---"+userData.get("pname")
                                        +"---"+userData.get("propertysize")+"---"+userData.get("location")+"---"+userData.get("number")+"---"+userData.get("type"));

                            };

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }

                    propertyAdaptor =new PropertyAdaptor(propertylist, PropertyActivity.this);
                    RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(PropertyActivity.this, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(nlayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(propertyAdaptor);
                        }
                    });
                    propertyAdaptor.notifyItemRangeInserted(0, propertylist.size());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btn_add_property:
                Intent intent = new Intent(PropertyActivity.this, AdminAddNewProductActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_sites:

                propertyAdaptor =new PropertyAdaptor(siteslist, PropertyActivity.this);
                RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(PropertyActivity.this, RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(nlayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(propertyAdaptor);
                    }
                });
                propertyAdaptor.notifyItemRangeInserted(0, siteslist.size());

                break;

            case R.id.iv_home:

                propertyAdaptor =new PropertyAdaptor(Homeslist, PropertyActivity.this);
                RecyclerView.LayoutManager nlayoutManager1 = new LinearLayoutManager(PropertyActivity.this, RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(nlayoutManager1);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(propertyAdaptor);
                    }
                });
                propertyAdaptor.notifyItemRangeInserted(0, Homeslist.size());

                break;

            case R.id.iv_commercial:

                propertyAdaptor =new PropertyAdaptor(Rentallist, PropertyActivity.this);
                RecyclerView.LayoutManager nlayoutManager2 = new LinearLayoutManager(PropertyActivity.this, RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(nlayoutManager2);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(propertyAdaptor);
                    }
                });
                propertyAdaptor.notifyItemRangeInserted(0, Rentallist.size());

                break;

            case R.id.iv_green_land:

                propertyAdaptor =new PropertyAdaptor(greenlandlist, PropertyActivity.this);
                RecyclerView.LayoutManager nlayoutManager3 = new LinearLayoutManager(PropertyActivity.this, RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(nlayoutManager3);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(propertyAdaptor);
                    }
                });
                propertyAdaptor.notifyItemRangeInserted(0, greenlandlist.size());

                break;
        }

    }
}