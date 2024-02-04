package com.chiranths.jobportal1.Activities.Admin;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chiranths.jobportal1.Activities.BasicActivitys.SearchActivity;
import com.chiranths.jobportal1.Activities.Propertys.AdminAddNewProductActivity;
import com.chiranths.jobportal1.Activities.Propertys.ProductViewHolder;
import com.chiranths.jobportal1.Activities.Propertys.Products;
import com.chiranths.jobportal1.Adapters.admin.AdminAdsAdaptor;
import com.chiranths.jobportal1.Adapters.admin.AdminPropertyAdaptor;
import com.chiranths.jobportal1.R;
import com.chiranths.jobportal1.Utilitys.AppConstants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminPropertyApproval extends AppCompatActivity implements View.OnClickListener, Adminapproveorremove {

    private DatabaseReference ProductsRef;
    FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button btn_add;
    LinearLayout search;
    ImageView iv_back_toolbar;
    ImageView iv_sites, iv_green_land, iv_home, iv_commercial;
    Handler mHandler = new Handler();
    ArrayList propertylist = new ArrayList();
    ArrayList greenlandlist = new ArrayList();
    ArrayList siteslist = new ArrayList();
    ArrayList Homeslist = new ArrayList();
    ArrayList Rentallist = new ArrayList();
    ArrayList adslist = new ArrayList();
    AdminPropertyAdaptor propertyAdaptor;
    AdminAdsAdaptor adsAdaptor;
    RecyclerView recyclarviewads;
    CardView cv_homes, cv_sites, cv_green, cv_comerical;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_property_approval);

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
        iv_sites = findViewById(R.id.iv_sites_admin);
        iv_commercial = findViewById(R.id.iv_commercial_admin);
        iv_green_land = findViewById(R.id.iv_green_land_admin);
        iv_home = findViewById(R.id.iv_home_admin);
        search = findViewById(R.id.llsearch_property);
        iv_sites.setOnClickListener(this);
        iv_green_land.setOnClickListener(this);
        iv_commercial.setOnClickListener(this);
        iv_home.setOnClickListener(this);
        iv_back_toolbar = findViewById(R.id.iv_back_toolbar_admin);
        iv_back_toolbar.setOnClickListener(this);

        recyclarviewads = findViewById(R.id.rv_adds_layots2_admin);
        recyclerView = findViewById(R.id.recycler_menu_admin);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager mgrid = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(mgrid);
        fetchcorosel();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPropertyApproval.this, SearchActivity.class);
                bundle.putString("searchtype", "property");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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

                if (snapshot.exists()) {

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()) {
                        Object data = dataMap.get(key);
                        try {

                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            adslist.add(userData.get(AppConstants.image) + "---" + userData.get(AppConstants.pid) + "---" + userData.get(AppConstants.category) + "---" + userData.get(AppConstants.price) + "---" + userData.get(AppConstants.propertysize) + "---" + userData.get(AppConstants.number) + "---" + userData.get(AppConstants.location) + "---" + userData.get("Approval"));

                        } catch (ClassCastException cce) {

                            try {
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            } catch (ClassCastException cce2) {

                            }
                        }
                    }
                    adsAdaptor = new AdminAdsAdaptor(adslist, AdminPropertyApproval.this);
                    RecyclerView.LayoutManager n1layoutManager = new LinearLayoutManager(AdminPropertyApproval.this, RecyclerView.HORIZONTAL, false);
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

                if (snapshot.exists()) {

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()) {
                        Object data = dataMap.get(key);
                        try {

                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            propertylist.add(userData.get(AppConstants.image) + "!!" + userData.get(AppConstants.pid) + "---" + userData.get(AppConstants.description) + "---" +
                                    userData.get(AppConstants.category) + "---" + userData.get(AppConstants.price) + "---" + userData.get(AppConstants.pname)
                                    + "---" + userData.get(AppConstants.propertysize) + "---" + userData.get(AppConstants.location) + "---" + userData.get(AppConstants.number) + "---" + userData.get(AppConstants.type) + "---" + userData.get("Approval"));


                        } catch (ClassCastException cce) {

                            try {
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            } catch (ClassCastException cce2) {

                            }
                        }
                    }

                    propertyAdaptor = new AdminPropertyAdaptor(propertylist, AdminPropertyApproval.this);
                    RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(AdminPropertyApproval.this, RecyclerView.VERTICAL, false);
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

        switch (view.getId()) {
            case R.id.btn_add_property:
                Intent intent = new Intent(AdminPropertyApproval.this, AdminAddNewProductActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_sites:

                propertyAdaptor = new AdminPropertyAdaptor(siteslist, AdminPropertyApproval.this);
                RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(AdminPropertyApproval.this, RecyclerView.VERTICAL, false);
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
                propertyAdaptor = new AdminPropertyAdaptor(Homeslist, AdminPropertyApproval.this);
                RecyclerView.LayoutManager nlayoutManager1 = new LinearLayoutManager(AdminPropertyApproval.this, RecyclerView.VERTICAL, false);
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
                propertyAdaptor = new AdminPropertyAdaptor(Rentallist, AdminPropertyApproval.this);
                RecyclerView.LayoutManager nlayoutManager2 = new LinearLayoutManager(AdminPropertyApproval.this, RecyclerView.VERTICAL, false);
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
                propertyAdaptor = new AdminPropertyAdaptor(greenlandlist, AdminPropertyApproval.this);
                RecyclerView.LayoutManager nlayoutManager3 = new LinearLayoutManager(AdminPropertyApproval.this, RecyclerView.VERTICAL, false);
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

            /*case R.id.iv_back_toolbar:
                finish();
                break;*/
        }

    }

    @Override
    public void sendpdi(String type, String pid, int state) {
        FirebaseDatabase.getInstance().getReference().child(type).child(pid).child("Approval").setValue(state);
    }
}