package com.chiranths.jobportal1.Activities.HotDealsactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class HotDealsDetailsActivity extends AppCompatActivity {

    private Button btn_call;
    private ImageView productImage;
    private TextView productPrice,productDescription,productName,tv_topbar_productName,tv_posted_date,tv_hot_location;
    private String productID="", state = "Normal";
    CarouselView carouselView;
    String[] url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotdeals_details);
        productID = getIntent().getStringExtra("pid");
        btn_call =(Button) findViewById(R.id.tv_btn_call_hot);
        carouselView =  findViewById(R.id.hot_details_carouselView);
        productName = (TextView) findViewById(R.id.hot_name_details);
        tv_topbar_productName = (TextView) findViewById(R.id.tv_topbar_productName);
        productDescription = (TextView) findViewById(R.id.hot_description_details);
        productPrice = (TextView) findViewById(R.id.hot_price_details);
        tv_posted_date = findViewById(R.id.tv_posted_date);
        tv_hot_location = findViewById(R.id.tv_hot_location);
        getProductDetails(productID);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state.equals("Order Placed") || state.equals("Order Shipped")){
                    Toast.makeText(HotDealsDetailsActivity.this,"You can add Purchase more product, once your order is shipped or confirmed",Toast.LENGTH_LONG).show();
                }
                else
                {

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //CheckOrderState();
    }

    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("hotforyou");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Products products=dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    tv_topbar_productName.setText(products.getPname());
                    tv_hot_location.setText(products.getLocation());
                    url = products.getImage2().split("---");
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
            Picasso.get().load(url[position]).into(imageView);

        }
    };


}