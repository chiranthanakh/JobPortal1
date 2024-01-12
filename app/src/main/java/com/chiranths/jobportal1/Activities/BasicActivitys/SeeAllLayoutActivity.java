package com.chiranths.jobportal1.Activities.BasicActivitys;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chiranths.jobportal1.Activities.Propertys.PropertyActivity;
import com.chiranths.jobportal1.Adapters.AdsAdaptor;
import com.chiranths.jobportal1.Adapters.LayoutsAdaptor;
import com.chiranths.jobportal1.Adapters.SeeallLayouts;
import com.chiranths.jobportal1.R;
import com.chiranths.jobportal1.Utilitys.AppConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SeeAllLayoutActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List layoutlist = new ArrayList();
    private Handler mHandler = new Handler();
    SeeallLayouts layoutAdaptor;
    ArrayList layoutslists = new ArrayList();
    SeeallLayouts layoutsAdaptor;
    ImageView back_toolbar_layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_layout);

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

        initilize();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                fetchads();
                  //fetchlayouts();
            }
        });

    }

    private void initilize() {
        recyclerView = findViewById(R.id.recyclarview_alllayouts);
      //  recyclerView.setHasFixedSize(true);
        GridLayoutManager mgrid = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(mgrid);
        back_toolbar_layouts = findViewById(R.id.back_toolbar_layouts);

        back_toolbar_layouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void fetchads() {

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

                            layoutlist.add(userData.get(AppConstants.image)+"!!"+userData.get(AppConstants.pid)+"---"+userData.get(AppConstants.description)+"---"+
                                    userData.get(AppConstants.category)+"---"+userData.get(AppConstants.price)+"---"+userData.get(AppConstants.pname)
                                    +"---"+userData.get(AppConstants.propertysize)+"---"+userData.get(AppConstants.location)+"---"+userData.get(AppConstants.number)+"---"+userData.get("type"));

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }
                    layoutAdaptor =new SeeallLayouts(layoutlist, SeeAllLayoutActivity.this);
                    RecyclerView.LayoutManager n1layoutManager = new LinearLayoutManager(SeeAllLayoutActivity.this, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(n1layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(layoutAdaptor);
                        }
                    });
                    layoutAdaptor.notifyItemRangeInserted(0, layoutlist.size());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchlayouts() {
        DatabaseReference adsimage = FirebaseDatabase.getInstance().getReference().child("layoutsforyou");
        adsimage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    layoutslists.clear();
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()) {
                        Object data = dataMap.get(key);
                        try {
                            HashMap userData = (HashMap) data;
                            layoutlist.add(userData.get(AppConstants.image)+"!!"+userData.get(AppConstants.pid)+"---"+userData.get(AppConstants.description)+"---"+
                                    userData.get(AppConstants.category)+"---"+userData.get(AppConstants.price)+"---"+userData.get(AppConstants.pname)
                                    +"---"+userData.get(AppConstants.propertysize)+"---"+userData.get(AppConstants.location)+"---"+userData.get(AppConstants.number)+"---"+userData.get("type"));

                            layoutlist.add(userData.get(AppConstants.image)+"!!"+userData.get(AppConstants.pid)+"---"+userData.get(AppConstants.description)+"---"+
                                    userData.get(AppConstants.category)+"---"+userData.get(AppConstants.price)+"---"+userData.get(AppConstants.pname)
                                    +"---"+userData.get(AppConstants.propertysize)+"---"+userData.get(AppConstants.location)+"---"+userData.get(AppConstants.number));


                        } catch (ClassCastException cce) {

                            try {
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            } catch (ClassCastException cce2) {

                            }
                        }
                    }
                    Collections.shuffle(layoutslists);
                    layoutsAdaptor = new SeeallLayouts(layoutslists, SeeAllLayoutActivity.this);
                    RecyclerView.LayoutManager n1layoutManager = new LinearLayoutManager(SeeAllLayoutActivity.this, RecyclerView.HORIZONTAL, false);
                    recyclerView.setLayoutManager(n1layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                          /*  if (progressDialog != null) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }*/
                            recyclerView.setAdapter(layoutsAdaptor);
                          //  layoutsAdaptor.notifyItemRangeInserted(0, adslist.size());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onClick(View view) {

    }
}
