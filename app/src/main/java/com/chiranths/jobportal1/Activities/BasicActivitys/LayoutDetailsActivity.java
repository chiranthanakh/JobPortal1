package com.chiranths.jobportal1.Activities.BasicActivitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.chiranths.jobportal1.Activities.Propertys.Products;
import com.chiranths.jobportal1.CalldetailsRecords;
import com.chiranths.jobportal1.Model.LayoutModel;
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

public class LayoutDetailsActivity extends AppCompatActivity {

    private MaterialButton ads_cl_btn, ads_whatsapp_btn;
     ImageView iv_back_ads;
    CarouselView carouselView;
    private TextView productPrice,productDescription,productName,tv_topbar_productName,tv_ads_details_verify,tv_no_sites,tv_layout_facing,tv_layout_area,
            tv_place_location,tv_size_details,tv_prop_type,tv_future1,tv_future2,tv_future3,tv_future4,tv_contact_type,tv_ads_posted_on,tv_ads_posted,ads_details_not_verified;
    private String productID="", state = "Normal",number,page;
    private String[] url;
    CalldetailsRecords calldetails = new CalldetailsRecords() ;
    Utilitys utilitys = new Utilitys();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layouts_details);
        productID = getIntent().getStringExtra("pid");
        page = getIntent().getStringExtra("page");
        ads_cl_btn = findViewById(R.id.ads_cl_btn);
        ads_whatsapp_btn = findViewById(R.id.ads_whtsapp_btn);
        carouselView =  findViewById(R.id.ads_details_carouselView);
        tv_prop_type = findViewById(R.id.tv_prop_type);
        iv_back_ads = findViewById(R.id.iv_back_ads);
        //tv_ads_posted = findViewById(R.id.tv_ads_posted);
        tv_future1 = findViewById(R.id.tv_futures1);
        tv_future2 = findViewById(R.id.tv_futures2);
        tv_future3 = findViewById(R.id.tv_futures3);
        tv_future4 = findViewById(R.id.tv_futures4);
        tv_no_sites = findViewById(R.id.tv_sites_available);
        tv_layout_facing = findViewById(R.id.tv_layout_facing);
        tv_ads_details_verify = findViewById(R.id.ads_details_verifyed);
       // ads_details_not_verified = findViewById(R.id.ads_details_not_verified);
        tv_ads_posted_on = findViewById(R.id.tv_ads_posted_on);
        tv_contact_type = findViewById(R.id.tv_contact_who);
        tv_place_location = findViewById(R.id.ads_place_location);
        tv_size_details = findViewById(R.id.ads_size_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        tv_topbar_productName = (TextView) findViewById(R.id.tv_topbar_productName);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        tv_layout_area = (TextView) findViewById(R.id.tv_layout_area);
        getProductDetails(productID);

        ads_cl_btn.setOnClickListener(view ->
                utilitys.navigateCall(LayoutDetailsActivity.this,number,productName.getText().toString()));

        ads_whatsapp_btn.setOnClickListener(view -> {
            utilitys.navigateWhatsapp(LayoutDetailsActivity.this,number,productName.getText().toString());
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
        productsRef = FirebaseDatabase.getInstance().getReference().child("layoutsforyou");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue(LayoutModel.class) != null){
                    LayoutModel products=dataSnapshot.getValue(LayoutModel.class);
                    productName.setText(products.getPname());
                    productPrice.setText("Rs."+products.getPrice());
                    productDescription.setText(products.getDescription());
                    tv_topbar_productName.setText(products.getPname());
                    tv_place_location.setText(products.getLocation());
                    tv_size_details.setText(products.getPropertysize());
                    tv_prop_type.setText(products.getCategory());
                    tv_layout_facing.setText(products.getFacing());
                    tv_no_sites.setText(products.getSitesAvailable());
                    tv_layout_area.setText(products.getLayoutarea());
                   // tv_ads_posted.setText("Posted by : "+products.getPostedBy());
                    if(products.getDate()==null){
                        tv_ads_posted_on.setVisibility(View.GONE);
                    }else {
                        tv_ads_posted_on.setText("Posted on "+products.getDate());
                    }
                    url = products.getImage2().split("---");
                    carouselView.setImageListener(imageListener);
                    carouselView.setPageCount(url.length);
                    int test = 1;//Integer.parseInt(products.getStatus());
                    if(test == 1){
                        tv_ads_details_verify.setVisibility(View.GONE);
                    }else {
                        //ads_details_not_verified.setVisibility(View.GONE);
                    }
                    number = products.getNumber();
                    tv_contact_type.setText(products.getPostedBy());
                    if(products.getPoint1()!=null){
                        tv_future1.setText(products.getPoint1());
                        tv_future2.setText(products.getPoint2());
                        tv_future3.setText(products.getPoint3());
                        tv_future4.setText(products.getPoint4());
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
            Glide.with(LayoutDetailsActivity.this)
                    .load(url[position])
                    .into(imageView);

        }
    };
}