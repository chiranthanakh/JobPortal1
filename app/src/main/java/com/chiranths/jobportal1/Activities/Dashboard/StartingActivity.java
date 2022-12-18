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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.chiranths.jobportal1.Activities.Profile.ProfileActivity;
import com.chiranths.jobportal1.Activities.Profile.ProfileFragment;
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
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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

public class StartingActivity extends AppCompatActivity{

    BottomNavigationView bottomNavShift;
    ProfileFragment profileFragment = new ProfileFragment();
    StartingFragment startingFragment = new StartingFragment();
    FrameLayout frameLayout;
    DrawerLayout drawer_layout;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        progressDialog=new ProgressDialog(this);
        initilize();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, startingFragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

       // initilize();
     }

    private void initilize()
    {
        drawer_layout = findViewById(R.id.drawer_layout_main);
        bottomNavShift = findViewById(R.id.bottomNavShift);
        frameLayout = findViewById(R.id.fragment_container);

        bottomNavShift.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
                        break;
                    case R.id.Home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, startingFragment).commit();
                        break;
                }
                return true;
            }
        });
    }
}