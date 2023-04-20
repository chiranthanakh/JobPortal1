package com.chiranths.jobportal1.Activities.BasicActivitys;

import static android.Manifest.permission.CALL_PHONE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.chiranths.jobportal1.Activities.HotDealsactivity.HotDealsDetailsActivity;
import com.chiranths.jobportal1.Activities.Propertys.Products;
import com.chiranths.jobportal1.CalldetailsRecords;
import com.chiranths.jobportal1.R;
import com.chiranths.jobportal1.Utilitys;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class AdsDetailsActivity extends AppCompatActivity {

    private ImageView ads_cl_btn, ads_whatsapp_btn,iv_back_ads;
    CarouselView carouselView;
    private TextView productPrice,productDescription,productName,tv_topbar_productName,tv_ads_details_verify,
            tv_place_location,tv_size_details,tv_prop_type,tv_future1,tv_future2,tv_future3,tv_future4,tv_contact_type,tv_ads_posted_on,tv_ads_posted,ads_details_not_verified;
    private String productID="", state = "Normal",number,page;
    private String[] url;
    CalldetailsRecords calldetails = new CalldetailsRecords() ;
    Utilitys utilitys = new Utilitys();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_details);
        productID = getIntent().getStringExtra("pid");
        page = getIntent().getStringExtra("page");
        ads_cl_btn = findViewById(R.id.ads_cl_btn);
        ads_whatsapp_btn = findViewById(R.id.ads_whtsapp_btn);
        carouselView =  findViewById(R.id.ads_details_carouselView);
        tv_prop_type = findViewById(R.id.tv_prop_type);
        iv_back_ads = findViewById(R.id.iv_back_ads);
        tv_ads_posted = findViewById(R.id.tv_ads_posted);
        tv_future1 = findViewById(R.id.tv_futures1);
        tv_future2 = findViewById(R.id.tv_futures2);
        tv_future3 = findViewById(R.id.tv_futures3);
        tv_future4 = findViewById(R.id.tv_futures4);
        tv_ads_details_verify = findViewById(R.id.ads_details_verifyed);
        ads_details_not_verified = findViewById(R.id.ads_details_not_verified);
        tv_ads_posted_on = findViewById(R.id.tv_ads_posted_on);
        tv_contact_type = findViewById(R.id.tv_contact_who);
        tv_place_location = findViewById(R.id.ads_place_location);
        tv_size_details = findViewById(R.id.ads_size_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        tv_topbar_productName = (TextView) findViewById(R.id.tv_topbar_productName);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        getProductDetails(productID);

        ads_cl_btn.setOnClickListener(view ->
                utilitys.navigateCall(AdsDetailsActivity.this,number,productName.getText().toString()));

        ads_whatsapp_btn.setOnClickListener(view -> {

            utilitys.navigateWhatsapp(AdsDetailsActivity.this,number,productName.getText().toString());
        });

        iv_back_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //CheckOrderState();
    }

    private void getProductDetails(String productID) {
        DatabaseReference productsRef;
        if(page.equals("1")){
            productsRef = FirebaseDatabase.getInstance().getReference().child("adsforyou");
        }else  {
             productsRef = FirebaseDatabase.getInstance().getReference().child("layoutsforyou");
        }
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue(Products.class) != null){
                    Products products=dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    tv_topbar_productName.setText(products.getPname());
                    tv_place_location.setText(products.getLocation());
                    tv_size_details.setText(products.getPropertysize());
                    tv_prop_type.setText(products.getCategory());
                    tv_ads_posted.setText("Posted by : "+products.getPostedby());
                    if(products.getPostedOn()==null){
                        tv_ads_posted_on.setVisibility(View.GONE);
                    }else {
                        tv_ads_posted_on.setText("Posted on "+products.getPostedOn());
                    }
                    url = products.getImage2().split("---");
                    carouselView.setImageListener(imageListener);
                    carouselView.setPageCount(url.length);
                    int test = products.getApproval();
                    if(test == 1){
                        tv_ads_details_verify.setVisibility(View.GONE);
                    }else {
                        ads_details_not_verified.setVisibility(View.GONE);
                    }
                    number = products.getNumber();
                    tv_contact_type.setText("Context "+products.getPostedby());
                    if(products.getText1()!=null){
                        tv_future1.setText(products.getText1());
                        tv_future2.setText(products.getText2());
                        tv_future3.setText(products.getText3());
                        tv_future4.setText(products.getText4());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(AdsDetailsActivity.this)
                    .load(url[position])
                    .into(imageView);

        }
    };
}