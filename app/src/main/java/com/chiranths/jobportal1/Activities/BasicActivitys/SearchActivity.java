package com.chiranths.jobportal1.Activities.BasicActivitys;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;

import com.chiranths.jobportal1.Activities.Propertys.AdminAddNewProductActivity;
import com.chiranths.jobportal1.Activities.Propertys.ProductViewHolder;
import com.chiranths.jobportal1.Activities.Propertys.Products;
import com.chiranths.jobportal1.Activities.Propertys.PropertyActivity;
import com.chiranths.jobportal1.Adapters.AdsAdaptor;
import com.chiranths.jobportal1.Adapters.PropertyAdaptor;
import com.chiranths.jobportal1.Model.FilterModel;
import com.chiranths.jobportal1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference ProductsRef;
    FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button btn_add;
    ImageView iv_sites,iv_green_land,iv_home,iv_commercial;
    Handler mHandler = new Handler();
    ArrayList propertylist =new ArrayList();
    ArrayList<FilterModel> Propertyfilterlist =new ArrayList();
    ArrayList greenlandlist =new ArrayList();
    ArrayList siteslist =new ArrayList();
    ArrayList Homeslist =new ArrayList();
    ArrayList Rentallist =new ArrayList();
    ArrayList adslist =new ArrayList();
    PropertyAdaptor propertyAdaptor;
    AdsAdaptor adsAdaptor;
    RecyclerView recyclarviewads;
    CardView cv_homes, cv_sites, cv_green,cv_comerical;
    EditText edt_filter;
    ArrayList filterarraylist = new ArrayList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
        edt_filter = findViewById(R.id.edt_filter);

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
        finterAdaptor();
    }


    @Override
    protected void onResume() {
        super.onResume();

        edt_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());
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

                            adslist.add(userData.get("image")+"---"+userData.get("pid")+"---"+userData.get("category")+"---"+userData.get("price")+"---"+userData.get("propertysize")+"---"+userData.get("number"));

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }

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

                            Propertyfilterlist.add(new FilterModel(String.valueOf(userData.get("pname")),String.valueOf(userData.get("description")),String.valueOf(userData.get("price")),String.valueOf(userData.get("image")),
                                    String.valueOf(userData.get("category")),String.valueOf(userData.get("pid")),"date","time",String.valueOf(userData.get("type")),String.valueOf(userData.get("propertysize")),String.valueOf(userData.get("location")),
                                    String.valueOf(userData.get("number"))));

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

                    propertyAdaptor =new PropertyAdaptor(propertylist, SearchActivity.this);
                    RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
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
                Intent intent = new Intent(SearchActivity.this, AdminAddNewProductActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_sites:

                propertyAdaptor =new PropertyAdaptor(siteslist, SearchActivity.this);
                RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
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

                propertyAdaptor =new PropertyAdaptor(Homeslist, SearchActivity.this);
                RecyclerView.LayoutManager nlayoutManager1 = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
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

                propertyAdaptor =new PropertyAdaptor(Rentallist, SearchActivity.this);
                RecyclerView.LayoutManager nlayoutManager2 = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
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

                propertyAdaptor =new PropertyAdaptor(greenlandlist, SearchActivity.this);
                RecyclerView.LayoutManager nlayoutManager3 = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
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

    private void filter(String text) {

        if(text.equals("")){
            //adaptorclass(false);
        }else {

            filterarraylist.clear();

            for (int i=0;i<Propertyfilterlist.size();i++){

                if(Propertyfilterlist.get(i).getLocation().toLowerCase().contains(text.toLowerCase()) ||
                        Propertyfilterlist.get(i).getPname().toLowerCase().contains(text.toLowerCase()) ||
                        Propertyfilterlist.get(i).getPrice().toLowerCase().contains(text.toLowerCase()) ||
                        Propertyfilterlist.get(i).getType().toLowerCase().contains(text.toLowerCase())){

                    filterarraylist.add(Propertyfilterlist.get(i).getImage()+"!!"+Propertyfilterlist.get(i).getPid()+"---"+Propertyfilterlist.get(i).getDescription()+"---"+
                            Propertyfilterlist.get(i).getCategory()+"---"+Propertyfilterlist.get(i).getPrice()+"---"+Propertyfilterlist.get(i).getPname()
                            +"---"+Propertyfilterlist.get(i).getSize()+"---"+Propertyfilterlist.get(i).getLocation()+"---"+Propertyfilterlist.get(i).getNumber()+"---"+Propertyfilterlist.get(i).getType());


                }
                if(filterarraylist.size()==0){
                   // ll_no_data_irfc.setVisibility(View.VISIBLE);
                }else {
                    //ll_no_data_irfc.setVisibility(View.GONE);

                }
            }
            propertyAdaptor =new PropertyAdaptor(filterarraylist, SearchActivity.this);
            RecyclerView.LayoutManager nlayoutManager3 = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(nlayoutManager3);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setAdapter(propertyAdaptor);
                }
            });
            propertyAdaptor.notifyItemRangeInserted(0, greenlandlist.size());
        }
    }


    private void finterAdaptor() {

        propertyAdaptor =new PropertyAdaptor(greenlandlist, SearchActivity.this);
        RecyclerView.LayoutManager nlayoutManager3 = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(nlayoutManager3);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(propertyAdaptor);
            }
        });
        propertyAdaptor.notifyItemRangeInserted(0, greenlandlist.size());

    }


}