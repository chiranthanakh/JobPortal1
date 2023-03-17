package com.chiranths.jobportal1.Activities.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.chiranths.jobportal1.Activities.LoanActivity.LoanFragment;
import com.chiranths.jobportal1.Activities.Profile.ProfileFragments;
import com.chiranths.jobportal1.Activities.Propertys.PropertyFragment;
import com.chiranths.jobportal1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StartingActivity extends AppCompatActivity implements View.OnClickListener{

    BottomNavigationView bottomNavShift;
    ProfileFragments profileFragment = new ProfileFragments();
    DashboardFragment startingFragment = new DashboardFragment();
    PropertyFragment propertyFragment = new PropertyFragment();
    LoanFragment loanFragment = new LoanFragment();
    FrameLayout frameLayout;
    DrawerLayout drawer_layout_main_d;
    ImageView iv_nav_view_d;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        iv_nav_view_d = findViewById(R.id.iv_nav_view_d);
        iv_nav_view_d.setOnClickListener(this);
        drawer_layout_main_d = findViewById(R.id.drawer_layout_main_d);

        initilize();
        progressDialog=new ProgressDialog(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, startingFragment).commit();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_nav_view_d:
                drawer_layout_main_d.openDrawer(GravityCompat.START);
                break;


        }
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
                    case R.id.it_loan:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loanFragment).commit();
                        break;
                    case R.id.it_property:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, propertyFragment).commit();
                        break;
                }
                return true;
            }
        });
    }


}