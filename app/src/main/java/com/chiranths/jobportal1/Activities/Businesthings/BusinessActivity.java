package com.chiranths.jobportal1.Activities.Businesthings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chiranths.jobportal1.Activities.Admin.Business.Admin_business_listings;
import com.chiranths.jobportal1.Activities.BasicActivitys.SearchActivity;
import com.chiranths.jobportal1.Activities.Dashboard.StartingActivity;
import com.chiranths.jobportal1.Activities.Propertys.PropertyActivity;
import com.chiranths.jobportal1.Adapters.BusinessAdaptor;
import com.chiranths.jobportal1.Adapters.BusinessCategoryAdaptor;

import com.chiranths.jobportal1.Adapters.PropertyAdaptor;
import com.chiranths.jobportal1.Model.BusinessModel;
import com.chiranths.jobportal1.Model.Categorymmodel;
import com.chiranths.jobportal1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BusinessActivity extends AppCompatActivity implements View.OnClickListener,FilterCategory {

    private DatabaseReference ProductsRef;
     RecyclerView recyclerView;
    private RecyclerView gridView;
    ImageView back;
    LinearLayout btnListbusiness,llsearch;
    ArrayList<BusinessModel> businesslist =new ArrayList<BusinessModel>();
    ArrayList<BusinessModel> filterbusinesslist =new ArrayList<BusinessModel>();
    ArrayList<Categorymmodel> categorylists =new ArrayList<Categorymmodel>();
    BusinessCategoryAdaptor businesscatAdaptor;
    BusinessAdaptor businessAdaptor;
    Handler mHandler = new Handler();
    Bundle bundle = new Bundle();

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
      //  recyclerView = findViewById(R.id.recycler_business);
        llsearch = findViewById(R.id.ll_search_business);
        btnListbusiness = findViewById(R.id.btn_list_business);
        back = findViewById(R.id.back_toolbar_business);
        gridView = findViewById(R.id.id_gridview);
        //recyclerView.setHasFixedSize(true);
       // GridLayoutManager mgrid = new GridLayoutManager(this,2);
        fetchbusiness("");
        fetchbusinessCategorys();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnListbusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(BusinessActivity.this, Admin_business_listings.class);
               // startActivity(intent);

                recyadaptor(businesslist);

            }
        });

        llsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BusinessActivity.this, SearchActivity.class);
                bundle.putString("searchtype","business");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void fetchbusinessCategorys() {

        DatabaseReference categorylist = FirebaseDatabase.getInstance().getReference().child("BusinessListing_category");

        categorylist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{

                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            categorylists.add(new Categorymmodel(String.valueOf(userData.get("pid")),String.valueOf(userData.get("image")),String.valueOf(userData.get("category")), String.valueOf(userData.get("subcategory"))));


                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }

                    RecyclerView.LayoutManager nlayoutManager1 = new LinearLayoutManager(BusinessActivity.this, RecyclerView.VERTICAL, false);
                    gridView.setLayoutManager(nlayoutManager1);
                    gridView.setItemAnimator(new DefaultItemAnimator());
                    businesscatAdaptor =new BusinessCategoryAdaptor(categorylists, BusinessActivity.this);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            gridView.setAdapter(businesscatAdaptor);
                        }
                    });
                    businesscatAdaptor.notifyItemRangeInserted(0, businesslist.size());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void fetchbusiness(String cat) {
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

                            if(String.valueOf(userData.get("Business_category")).equals(cat)){
                                businesslist.add(new BusinessModel(String.valueOf(userData.get("pid")),String.valueOf(userData.get("date")),String.valueOf(userData.get("time")),
                                        String.valueOf(userData.get("Businessname")),String.valueOf(userData.get("Business_category")),String.valueOf(userData.get("description")),
                                        String.valueOf(userData.get("price")),String.valueOf(userData.get("location")),String.valueOf(userData.get("number")),String.valueOf(userData.get("owner")),String.valueOf(userData.get("email")),String.valueOf(userData.get("rating")),
                                        String.valueOf(userData.get("image")),String.valueOf(userData.get("image2"))));
                            }
                            filterbusinesslist.add(new BusinessModel(String.valueOf(userData.get("pid")),String.valueOf(userData.get("date")),String.valueOf(userData.get("time")),
                                        String.valueOf(userData.get("Businessname")),String.valueOf(userData.get("Business_category")),String.valueOf(userData.get("description")),
                                        String.valueOf(userData.get("price")),String.valueOf(userData.get("location")),String.valueOf(userData.get("number")),String.valueOf(userData.get("owner")),String.valueOf(userData.get("email")),String.valueOf(userData.get("rating")),
                                        String.valueOf(userData.get("image")),String.valueOf(userData.get("image2"))));

                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }

                    if(cat.equals("")){
                        recyadaptor(filterbusinesslist);
                    }else {
                        recyadaptor(filterbusinesslist);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void recyadaptor(ArrayList<BusinessModel> businesslist1){
        recyclerView = findViewById(R.id.recycler_business);
        RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(BusinessActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(nlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        businessAdaptor =new BusinessAdaptor(businesslist1, this);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(businessAdaptor);
            }
        });
        businessAdaptor.notifyItemRangeInserted(0, businesslist1.size());
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void filter(String cat) {

        //fetchbusiness(cat);
        recyadaptor(businesslist);

        /*if(cat!=null){
            filterbusinesslist.clear();
            for (int i=0;i<businesslist.size();i++){

                if(businesslist.get(i).getBusiness_category().equals(cat)){
                    filterbusinesslist.add(new BusinessModel(businesslist.get(i).getPid(),businesslist.get(i).getDate(),businesslist.get(i).getTime(),
                            businesslist.get(i).getBusinessname(),businesslist.get(i).getBusiness_category(),businesslist.get(i).getDescription(),businesslist.get(i).getPrice(),
                            businesslist.get(i).getLocation(),businesslist.get(i).getNumber(),businesslist.get(i).getOwner(),businesslist.get(i).getEmail(),
                            businesslist.get(i).getRating(),businesslist.get(i).getImage(),businesslist.get(i).getImage2()));
                }
            }

            recyadaptor(filterbusinesslist);

        }*/
        }

}