package com.chiranths.jobportal1.Activities.Dashboard;

import static android.content.Context.MODE_PRIVATE;

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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.chiranths.jobportal1.Activities.Admin.AdminDashboard;
import com.chiranths.jobportal1.Activities.BasicActivitys.CenterHomeActivity;
import com.chiranths.jobportal1.Activities.Construction.ConstructionActivity;
import com.chiranths.jobportal1.Activities.BasicActivitys.LivingPlaceActivity;
import com.chiranths.jobportal1.Activities.BasicActivitys.LoginActivity;
import com.chiranths.jobportal1.Activities.BasicActivitys.Travelsactivity;
import com.chiranths.jobportal1.Model.AdsModel;
import com.chiranths.jobportal1.Model.LayoutModel;
import com.chiranths.jobportal1.Model.ProductInfo;
import com.chiranths.jobportal1.Activities.BasicActivitys.SearchActivity;
import com.chiranths.jobportal1.Activities.BasicActivitys.SeeAllLayoutActivity;
import com.chiranths.jobportal1.Activities.BasicActivitys.UpcommingProjects;
import com.chiranths.jobportal1.Activities.Businesthings.BusinessActivity;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    String id, name = "", mail, pic;
    LinearLayout cv_jobs, cv_propertys, cv_servicess, cv_loans;
    ImageView iv_sell;
    LinearLayout search_layout, ll_rent, ll_commercial_rent, ll_hotel, ll_travels, ll_construction;
    EditText search;
    TextView tv_seeall_upcooming, tv_seeall_layouts;

    private ArrayList<UpcomingEvent> upcomingEventList = new ArrayList<>();
    RecyclerView recyclerViewEvent;
    FusedLocationProviderClient fusedLocationProviderClient;
    private BottomhomeRecyclarviewAdaptor bottomhomeRecyclarviewAdaptor;
    List<Address> addresses;
    private String productRandomKey;
    private DatabaseReference CallerRef;
    final int speedScroll = 150;
    final Handler handler = new Handler();
    ImageView  iv_bell;

    private ArrayList<NoticeBoard> noticeBoardList = new ArrayList<>();
    RecyclerView recyclerView, recyclarviewads, recyclar_Layouts;
    private CoroselListAdaptor coroselListAdaptor;
    AdsAdaptor adsAdaptor;
    LayoutsAdaptor layoutsAdaptor;
    String nameofuser, userNumber, useremail;
    final int duration = 50;
    final int pixelsToMove = 5;
    private final Handler mHandler2 = new Handler(Looper.getMainLooper());
    FrameLayout frameLayout;

    ArrayList<Corosolmodel> coroselimagelist = new ArrayList<Corosolmodel>();
    ArrayList<AdsModel> adslist = new ArrayList();
    ArrayList<LayoutModel> layoutslists = new ArrayList();
    ArrayList<ProductInfo> productinfolist = new ArrayList();
    private int[] images = {R.drawable.banner1,
            R.drawable.banner1, R.drawable.banner1};
    CarouselView carouselView;
    Handler mHandler = new Handler();
    TextView tv_location, tv_pincode;
    ProgressDialog progressDialog;
    TextView admin_btn;
    Bundle bundle = new Bundle();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dashboard_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sh = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();
        id = sh.getString("id", null);
        name = sh.getString("name", "");
        mail = sh.getString("mail", null);
        pic = sh.getString("pic", null);
        //  displayLocationSettingsRequest(getContext());
        progressDialog = new ProgressDialog(getContext());

        initilize(view);

    }

    private void initilize(View view) {
        iv_bell = view.findViewById(R.id.iv_bell);
        iv_bell.setOnClickListener(this);
        cv_jobs = view.findViewById(R.id.cv_jobs);
        cv_servicess = view.findViewById(R.id.cv_servicess1);
        cv_propertys = view.findViewById(R.id.cv_propertys);
        cv_loans = view.findViewById(R.id.cv_loans);
        tv_location = view.findViewById(R.id.tv_location);
        search_layout = view.findViewById(R.id.search_layout);
        search = view.findViewById(R.id.main_edt_search2);
        search.setInputType(InputType.TYPE_NULL); // disable soft input
        tv_seeall_upcooming = view.findViewById(R.id.tv_seeall_upcomming);
        tv_seeall_layouts = view.findViewById(R.id.tv_seeall_layouts);
        tv_seeall_layouts.setOnClickListener(this);
        tv_seeall_upcooming.setOnClickListener(this);
        search.setOnClickListener(this);
        search_layout.setOnClickListener(this);
        //  tv_pincode = view.findViewById(R.id.tv_pincode);
        //admin_btn = view.findViewById(R.id.admin_btn);
        iv_sell = view.findViewById(R.id.iv_sell);
        iv_sell.setOnClickListener(this);
        cv_loans.setOnClickListener(this);
        cv_propertys.setOnClickListener(this);
        cv_servicess.setOnClickListener(this);
        cv_jobs.setOnClickListener(this);
        ll_rent = view.findViewById(R.id.ll_home_rent);
        ll_commercial_rent = view.findViewById(R.id.ll_commercial_rent);
        ll_travels = view.findViewById(R.id.ll_travels);
        ll_construction = view.findViewById(R.id.ll_constructions);
        ll_construction.setOnClickListener(this);
        ll_rent.setOnClickListener(this);
        ll_travels.setOnClickListener(this);
        ll_commercial_rent.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.rv_home_event);
        recyclarviewads = view.findViewById(R.id.rv_adds_layots1);
        recyclerViewEvent = view.findViewById(R.id.rv_dash_prop);
        recyclar_Layouts = view.findViewById(R.id.rv_Layouts);

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
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                fetchlayouts();
            }
        });


        if (progressDialog != null) {
            if (!progressDialog.isShowing()) {

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
                Intent intent = new Intent(getContext(), AdminDashboard.class);
                startActivity(intent);
            }
        });
    }

    private void fetchcorosel() {
        DatabaseReference coroselimage = FirebaseDatabase.getInstance().getReference().child("Corosels");

        coroselimage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()) {
                        Object data = dataMap.get(key);
                        try {
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            coroselimagelist.add(new Corosolmodel(String.valueOf(userData.get("image")), String.valueOf(userData.get("type"))));
                        } catch (ClassCastException cce) {

                            try {
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            } catch (ClassCastException cce2) {

                            }
                        }
                    }

                    coroselListAdaptor = new CoroselListAdaptor(coroselimagelist, getContext());
                    RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                    recyclerView.setLayoutManager(nlayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    SnapHelper snapHelper = new PagerSnapHelper();
                    snapHelper.attachToRecyclerView(recyclerView);
                    snapHelper.onFling(20, 20);

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

                if (snapshot.exists()) {
                    adslist.clear();
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()) {
                        Object data = dataMap.get(key);
                        try {

                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            adslist.add(new AdsModel(String.valueOf(userData.get("image")), String.valueOf(userData.get("image2")), String.valueOf(userData.get("pid")), String.valueOf(userData.get("description")), String.valueOf(userData.get("date")),String.valueOf(userData.get("category")), String.valueOf(userData.get("price")),String.valueOf(userData.get("pname")),
                                    String.valueOf(userData.get("propertysize")), String.valueOf(userData.get("location")),String.valueOf(userData.get("number")),String.valueOf(userData.get("Status")), String.valueOf(userData.get("postedBy")),String.valueOf(userData.get("approvedBy")),String.valueOf(userData.get("facing")),String.valueOf(userData.get("ownership")),String.valueOf(userData.get("postedOn")),String.valueOf(userData.get("postedOn")),String.valueOf(userData.get("text1")), String.valueOf(userData.get("text2")),String.valueOf(userData.get("text3")) ,String.valueOf(userData.get("text4"))) );

                        } catch (ClassCastException cce) {

                            try {
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            } catch (ClassCastException cce2) {

                            }
                        }
                    }
                    Collections.shuffle(adslist);
                    adsAdaptor = new AdsAdaptor(adslist, getContext());
                    RecyclerView.LayoutManager n1layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                    recyclarviewads.setLayoutManager(n1layoutManager);
                    recyclarviewads.setItemAnimator(new DefaultItemAnimator());

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog != null) {
                                if (progressDialog.isShowing()) {
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
                if (snapshot.exists()) {
                    layoutslists.clear();
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()) {
                        Object data = dataMap.get(key);
                        try {
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            layoutslists.add( new LayoutModel(String.valueOf(userData.get("image")), String.valueOf(userData.get("image2")), String.valueOf(userData.get("pid")), String.valueOf(userData.get("description")), String.valueOf(userData.get("date")),String.valueOf(userData.get("category")), String.valueOf(userData.get("price")),String.valueOf(userData.get("pname")),
                                    String.valueOf(userData.get("propertysize")), String.valueOf(userData.get("location")),String.valueOf(userData.get("number")),String.valueOf(userData.get("Status")), String.valueOf(userData.get("sitesAvailable")),String.valueOf(userData.get("postedBy")),String.valueOf(userData.get("facing")),String.valueOf(userData.get("layoutarea")),String.valueOf(userData.get("point1")), String.valueOf(userData.get("point2")),String.valueOf(userData.get("point3")) ,String.valueOf(userData.get("point4"))) );

                        } catch (ClassCastException cce) {

                            try {
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            } catch (ClassCastException cce2) {

                            }
                        }
                    }
                    Collections.shuffle(layoutslists);
                    layoutsAdaptor = new LayoutsAdaptor(layoutslists, getContext());
                    RecyclerView.LayoutManager n1layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                    recyclar_Layouts.setLayoutManager(n1layoutManager);
                    recyclar_Layouts.setItemAnimator(new DefaultItemAnimator());

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog != null) {
                                if (progressDialog.isShowing()) {
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

                if (dataSnapshot.exists()) {
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    productinfolist.clear();
                    for (String key : dataMap.keySet()) {
                        Object data = dataMap.get(key);
                        try {

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
                                    String.valueOf(userData.get("type")),
                                    String.valueOf(userData.get("postedby"))));

                        } catch (ClassCastException cce) {
                            try {
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            } catch (ClassCastException cce2) {

                            }
                        }
                    }

                    // Upcoming Event
                    bottomhomeRecyclarviewAdaptor = new BottomhomeRecyclarviewAdaptor(productinfolist, getContext(), userNumber, nameofuser);
                    RecyclerView.LayoutManager elayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    recyclerViewEvent.setLayoutManager(new GridLayoutManager(getContext(), 1));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_jobs:
                //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                // startActivity(intent);
                Intent intent;
                if (name.equals("")) {
                    intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    String userId = GoogleSignIn.getLastSignedInAccount(getContext()).getId();
                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(userId)
                            .child("role")
                            .setValue("jobseeker").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                    // intent = new Intent(getApplicationContext(), RoleActivity.class);
                }

                break;

            case R.id.cv_propertys:

                Intent intent1 = new Intent(getContext(), PropertyActivity.class);
                startActivity(intent1);
                break;

            case R.id.cv_loans:

                Intent intent2 = new Intent(getContext(), LoanActivity.class);
                startActivity(intent2);
                break;

            case R.id.cv_servicess1:
                Intent intent3 = new Intent(getContext(), BusinessActivity.class);
                startActivity(intent3);
                break;


            case R.id.iv_sell:
                Intent intent4 = new Intent(getContext(), SellActivity.class);
                startActivity(intent4);
                break;


            case R.id.main_edt_search2:
                Intent intent5 = new Intent(getContext(), SearchActivity.class);
                startActivity(intent5);
                break;

            case R.id.search_layout:
                Intent intent6 = new Intent(getContext(), SearchActivity.class);
                bundle.putString("searchtype","property");
                intent6.putExtras(bundle);
                startActivity(intent6);
                break;

            case R.id.ll_home_rent:
                Intent intent7 = new Intent(getContext(), LivingPlaceActivity.class);
                bundle.putString("center", "hotel");
                intent7.putExtras(bundle);
                startActivity(intent7);
                break;

            case R.id.ll_commercial_rent:
                Intent intent8 = new Intent(getContext(), CenterHomeActivity.class);
                bundle.putString("center", "commercial");
                intent8.putExtras(bundle);
                startActivity(intent8);
                break;

            case R.id.ll_travels:
                Intent intent9 = new Intent(getContext(), Travelsactivity.class);
                bundle.putString("center", "hotel");
                intent9.putExtras(bundle);
                startActivity(intent9);
                break;

            case R.id.tv_seeall_upcomming:
                Intent intent10 = new Intent(getContext(), UpcommingProjects.class);
                intent10.putExtras(bundle);
                startActivity(intent10);
                break;

            case R.id.tv_seeall_layouts:
                Intent intent11 = new Intent(getContext(), SeeAllLayoutActivity.class);
                intent11.putExtras(bundle);
                startActivity(intent11);

                break;

            case R.id.ll_constructions:
                Intent intent12 = new Intent(getContext(), ConstructionActivity.class);
                intent12.putExtras(bundle);
                startActivity(intent12);
                break;
        }
    }

    //location fetch
    private void displayLocationSettingsRequest(Context context) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
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
