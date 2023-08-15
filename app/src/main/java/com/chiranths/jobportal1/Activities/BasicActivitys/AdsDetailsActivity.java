package com.chiranths.jobportal1.Activities.BasicActivitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.chiranths.jobportal1.Activities.Propertys.Products;
import com.chiranths.jobportal1.CalldetailsRecords;
import com.chiranths.jobportal1.Model.AdsModel;
import com.chiranths.jobportal1.R;
import com.chiranths.jobportal1.Utilitys;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class AdsDetailsActivity extends AppCompatActivity {

    private ImageView iv_back_ads;
    private MaterialButton ads_cl_btn, ads_whatsapp_btn;
    CarouselView carouselView;
    private TextView productPrice, productDescription, productName, tv_topbar_productName, tv_ads_details_verify, tv_ads_ownership, ads_tv_facing, ads_approved_by,
            tv_place_location, tv_size_details, tv_prop_type, tv_future1, tv_future2, tv_future3, tv_future4, tv_contact_type, tv_ads_posted_on, tv_ads_posted, ads_details_not_verified;
    private String productID = "", state = "Normal", number, page;
    private String[] url;
    CalldetailsRecords calldetails = new CalldetailsRecords();
    Utilitys utilitys = new Utilitys();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_details);
        productID = getIntent().getStringExtra("pid");
        page = getIntent().getStringExtra("page");
        ads_cl_btn = findViewById(R.id.ads_cl_btn);
        ads_whatsapp_btn = findViewById(R.id.ads_whtsapp_btn);
        carouselView = findViewById(R.id.ads_details_carouselView);
        tv_prop_type = findViewById(R.id.tv_prop_type);
        iv_back_ads = findViewById(R.id.iv_back_ads);
        tv_ads_posted = findViewById(R.id.tv_ads_posted);
        ads_tv_facing = findViewById(R.id.ads_tv_facing);
        tv_future1 = findViewById(R.id.tv_futures1);
        tv_future2 = findViewById(R.id.tv_futures2);
        tv_future3 = findViewById(R.id.tv_futures3);
        tv_future4 = findViewById(R.id.tv_futures4);
        tv_ads_ownership = findViewById(R.id.tv_ads_ownership);
        tv_ads_details_verify = findViewById(R.id.ads_details_verifyed);
        ads_approved_by = findViewById(R.id.ads_approved_by);
        // ads_details_not_verified = findViewById(R.id.ads_details_not_verified);
        tv_ads_posted_on = findViewById(R.id.tv_ads_posted_on);
        tv_place_location = findViewById(R.id.ads_place_location);
        tv_size_details = findViewById(R.id.ads_size_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        tv_topbar_productName = (TextView) findViewById(R.id.tv_topbar_productName);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        getProductDetails(productID);

        ads_cl_btn.setOnClickListener(view ->
                utilitys.navigateCall(AdsDetailsActivity.this, number, productName.getText().toString()));

        ads_whatsapp_btn.setOnClickListener(view -> {

            utilitys.navigateWhatsapp(AdsDetailsActivity.this, number, productName.getText().toString());
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
        if (page.equals("1")) {
            productsRef = FirebaseDatabase.getInstance().getReference().child("adsforyou");
        } else if (page.equals("2")) {
            productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        } else {
            productsRef = FirebaseDatabase.getInstance().getReference().child("layoutsforyou");
        }
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue(AdsModel.class) != null) {
                    AdsModel products = dataSnapshot.getValue(AdsModel.class);
                    productName.setText(products.getPname());
                    productPrice.setText("Rs." + products.getPrice());
                    productDescription.setText(products.getDescription());
                    tv_topbar_productName.setText(products.getPname());
                    tv_place_location.setText(products.getLocation());
                    tv_size_details.setText(products.getPropertysize());
                    tv_prop_type.setText(products.getCategory());
                    tv_ads_posted.setText(products.getPostedby());
                    tv_ads_ownership.setText(products.getOwnership());
                    ads_tv_facing.setText(products.getFacing());
                    ads_approved_by.setText(products.getApprovedBy());
                    if (products.getPostedOn() == null) {
                        tv_ads_posted_on.setVisibility(View.GONE);
                    } else {
                        tv_ads_posted_on.setText(products.getPostedOn());
                    }
                    url = products.getImage2().split("---");
                    carouselView.setImageListener(imageListener);
                    carouselView.setPageCount(url.length);

                    number = products.getNumber();
                    if (products.getText1() != null && products.getText1() != "") {
                        tv_future1.setText(products.getText1());
                        tv_future2.setText(products.getText2());
                        tv_future3.setText(products.getText3());
                        tv_future4.setText(products.getText4());
                    } else {
                        tv_future1.setVisibility(View.GONE);
                        tv_future2.setVisibility(View.GONE);
                        tv_future3.setVisibility(View.GONE);
                        tv_future4.setVisibility(View.GONE);
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