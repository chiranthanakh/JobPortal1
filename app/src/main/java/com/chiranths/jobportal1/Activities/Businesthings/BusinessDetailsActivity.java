package com.chiranths.jobportal1.Activities.Businesthings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

public class BusinessDetailsActivity extends AppCompatActivity {

    private Button addToCartButton;
    private ImageView productImage;
    private TextView location,productDescription,productName,tv_topbar_productName;
    private String productID="", state = "Normal";
    CarouselView carouselView;
    String[] url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);

        productID = getIntent().getStringExtra("pid");
        carouselView =  findViewById(R.id.carouselView_business);
        productName = (TextView) findViewById(R.id.business_name_details);
        tv_topbar_productName = (TextView) findViewById(R.id.business_productName);
        productDescription = (TextView) findViewById(R.id.business_description_details);
        location = (TextView) findViewById(R.id.business_location);

        getbusinessDetails(productID);

    }


    private void getbusinessDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("BusinessListing");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Products products=dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    location.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    tv_topbar_productName.setText(products.getPname());
                    //Picasso.get().load(products.getImage()).into(productImage);
                    url = products.getImage().split("---");

                    carouselView.setPageCount(url.length);
                    carouselView.setImageListener(imageListener);
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
            //Picasso.get().load(url[position]).into(imageView);

            Glide.with(BusinessDetailsActivity.this)
                    .load(url[position])
                    .into(imageView);

        }
    };
}