package com.chiranths.jobportal1.Activities.BasicActivitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.chiranths.jobportal1.Activities.HotDealsactivity.HotDealsDetailsActivity;
import com.chiranths.jobportal1.Activities.Propertys.Products;
import com.chiranths.jobportal1.R;
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
    private TextView productPrice,productDescription,productName,tv_topbar_productName,
            tv_place_location,tv_size_details,tv_prop_type,tv_future1,tv_future2,tv_future3,tv_future4;
    private String productID="", state = "Normal",number;
    private String[] url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_details);
        productID = getIntent().getStringExtra("pid");
        ads_cl_btn = findViewById(R.id.ads_cl_btn);
        ads_whatsapp_btn = findViewById(R.id.ads_whtsapp_btn);
        carouselView =  findViewById(R.id.ads_details_carouselView);
        tv_prop_type = findViewById(R.id.tv_prop_type);
        iv_back_ads = findViewById(R.id.iv_back_ads);
        tv_future1 = findViewById(R.id.tv_futures1);
        tv_future2 = findViewById(R.id.tv_futures2);
        tv_future3 = findViewById(R.id.tv_futures3);
        tv_future4 = findViewById(R.id.tv_futures4);
        tv_place_location = findViewById(R.id.ads_place_location);
        tv_size_details = findViewById(R.id.ads_size_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        tv_topbar_productName = (TextView) findViewById(R.id.tv_topbar_productName);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        getProductDetails(productID);

        ads_cl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                String nameofuser = sh.getString("name", "");
                String userNumber = sh.getString("number","");
                String useremail = sh.getString("email","");

                if(!userNumber.equals("")){

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+ number));
                    startActivity(callIntent);

                }else {
                    Intent intent = new Intent(AdsDetailsActivity.this, UserDetailsActivity.class);
                    startActivity(intent);
                }
            }
        });

        ads_whatsapp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                String nameofuser = sh.getString("name", "");
                String userNumber = sh.getString("number","");
                String useremail = sh.getString("email","");

                if(!userNumber.equals("")){

                    String url = "https://api.whatsapp.com/send?phone="+"91"+number;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }else {
                    Intent intent = new Intent(AdsDetailsActivity.this, UserDetailsActivity.class);
                    startActivity(intent);
                }

            }
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
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("adsforyou");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Products products=dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    tv_topbar_productName.setText(products.getPname());
                    tv_place_location.setText(products.getLocation());
                    tv_size_details.setText(products.getPropertysize());
                    tv_prop_type.setText(products.getCategory());
                    url = products.getImage2().split("---");
                    carouselView.setPageCount(url.length);
                    carouselView.setImageListener(imageListener);
                    number = products.getNumber();
                    if(products.getText1()!=null){

                        tv_future1.setText(products.getText1());
                        tv_future2.setText(products.getText2());
                        tv_future3.setText(products.getText3());
                        tv_future4.setText(products.getText4());

                    }
                   // Picasso.get().load(products.getImage()).into(productImage);

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