package com.chiranths.jobportal1.Activities.Businesthings;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chiranths.jobportal1.Adapters.BusinessAdaptor;
import com.chiranths.jobportal1.Model.BusinessModel;
import com.chiranths.jobportal1.R;
import com.chiranths.jobportal1.Utilitys.AppConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BusinessFilter extends AppCompatActivity {

    private DatabaseReference ProductsRef;
    RecyclerView recyclerView;
    ArrayList<BusinessModel> businesslist =new ArrayList<BusinessModel>();
    ArrayList<BusinessModel> filterbusinesslist =new ArrayList<BusinessModel>();
    BusinessAdaptor businessAdaptor;
    Handler mHandler = new Handler();
    TextView tv_cat_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_filter);

        recyclerView = findViewById(R.id.rv_business_filter);
        tv_cat_name = findViewById(R.id.tv_cat_name);
        Bundle bundle = getIntent().getExtras();
        String type = bundle.getString("center");
        tv_cat_name.setText(type);
        fetchbusiness(type);
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
                            if(String.valueOf(userData.get(AppConstants.category)).equals(cat)){
                                businesslist.add(new BusinessModel(String.valueOf(userData.get(AppConstants.pid)),String.valueOf(userData.get(AppConstants.date)),String.valueOf(userData.get(AppConstants.time)),String.valueOf(userData.get("Businessname")),String.valueOf(userData.get("products")),String.valueOf(userData.get(AppConstants.category)),String.valueOf(userData.get(AppConstants.description)),
                                        String.valueOf(userData.get(AppConstants.price)),String.valueOf(userData.get(AppConstants.location)),String.valueOf(userData.get(AppConstants.number)),String.valueOf(userData.get("owner")),String.valueOf(userData.get("email")),String.valueOf(userData.get("rating")),
                                        String.valueOf(userData.get(AppConstants.image)),String.valueOf(userData.get(AppConstants.image2)),String.valueOf(userData.get(AppConstants.Status)),String.valueOf(userData.get("gst")),String.valueOf(userData.get("from")),String.valueOf(userData.get("productServicess")),String.valueOf(userData.get("workingHrs"))));
                            }
                        }catch (ClassCastException cce){

                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            }catch (ClassCastException cce2){

                            }
                        }
                    }
                    businessAdaptor =new BusinessAdaptor(businesslist, BusinessFilter.this);
                    recyadaptor(businesslist);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void recyadaptor(ArrayList<BusinessModel> businesslist1){
        RecyclerView.LayoutManager nlayoutManager = new LinearLayoutManager(BusinessFilter.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(nlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(businessAdaptor);
            }
        });
        businessAdaptor.notifyItemRangeInserted(0, businesslist1.size());
    }
  }