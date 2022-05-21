package com.chiranths.jobportal1.Activities.Businesthings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.chiranths.jobportal1.Activities.Propertys.PropertyActivity;
import com.chiranths.jobportal1.Adapters.BusinessAdaptor;
import com.chiranths.jobportal1.Adapters.PropertyAdaptor;
import com.chiranths.jobportal1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BusinessActivity extends AppCompatActivity {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList businesslist =new ArrayList();
    BusinessAdaptor businessAdaptor;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

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

    }

    private void initilize() {

        recyclerView = findViewById(R.id.recycler_business);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager mgrid = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(mgrid);
        fetchbusiness();

    }

    private void fetchbusiness() {
        DatabaseReference coroselimage = FirebaseDatabase.getInstance().getReference().child("BusinessListing");

        coroselimage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{

                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            businesslist.add(userData.get("image")+"!!"+userData.get("type")+"---"+userData.get("name")+"---"+
                                    userData.get("description")+"---"+userData.get("servicess")+"---"+userData.get("pname")
                                   +"---"+userData.get("location")+"---"+userData.get("number")+"---"+userData.get("pid"));


                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }

                    businessAdaptor =new BusinessAdaptor(businesslist, BusinessActivity.this);
                    RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(BusinessActivity.this, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(nlayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(businessAdaptor);
                        }
                    });
                    businessAdaptor.notifyItemRangeInserted(0, businesslist.size());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}