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

import com.chiranths.jobportal1.Adapters.AllUpcommingadaptor;
import com.chiranths.jobportal1.Adapters.SeeallLayouts;
import com.chiranths.jobportal1.Model.PropertytModel;
import com.chiranths.jobportal1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpcommingProjects extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<PropertytModel> layoutlist = new ArrayList<PropertytModel>();
    private Handler mHandler = new Handler();
    AllUpcommingadaptor layoutAdaptor;
    ImageView back_toolbar_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcomming_projects);

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
            }
        });

    }

    private void initilize() {
        recyclerView = findViewById(R.id.recyclarview_allupcomming);
        back_toolbar_up = findViewById(R.id.back_toolbar_up);
        //  recyclerView.setHasFixedSize(true);
        GridLayoutManager mgrid = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(mgrid);
        back_toolbar_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void fetchads() {
        //PropertytModel propertytModel = new PropertytModel()
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


                            layoutlist.add(new PropertytModel(String.valueOf(userData.get("image")),String.valueOf(userData.get("pid")),String.valueOf(userData.get("description")),
                                    String.valueOf(userData.get("category")),String.valueOf(userData.get("price")),String.valueOf(userData.get("pname")),
                                    String.valueOf(userData.get("propertysize")),String.valueOf(userData.get("location")),String.valueOf(userData.get("number")),String.valueOf(userData.get("type"))));

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }
                    layoutAdaptor =new AllUpcommingadaptor(layoutlist, UpcommingProjects.this);
                    RecyclerView.LayoutManager n1layoutManager = new LinearLayoutManager(UpcommingProjects.this, RecyclerView.VERTICAL, false);
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

    @Override
    public void onClick(View view) {

    }
}