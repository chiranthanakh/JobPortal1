package com.chiranths.jobportal1.Activities.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.chiranths.jobportal1.Activities.Profile.ProfileFragment;
import com.chiranths.jobportal1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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