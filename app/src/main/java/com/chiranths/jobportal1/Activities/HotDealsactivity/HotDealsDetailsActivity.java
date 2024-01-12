package com.chiranths.jobportal1.Activities.HotDealsactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.chiranths.jobportal1.CalldetailsRecords;
import com.chiranths.jobportal1.Model.HotDealsModel;
import com.chiranths.jobportal1.R;
import com.chiranths.jobportal1.Utilitys.AppConstants;
import com.chiranths.jobportal1.Utilitys.Utilitys;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class HotDealsDetailsActivity extends AppCompatActivity {

    private Button btn_call;
    private ImageView productImage,iv_hot_back;
    private LinearLayout hd_cl_btn, hd_whatsapp_btn;
    private TextView productPrice,productDescription,productName,tv_topbar_productName,
            tv_posted_date,tv_hot_location,tv_size,tv_owner_broker,tv_typeof_post,tv_approval_hotdeal,
            tv_special_option1,tv_special_option2,tv_hot_owner,tv_hot_timings;
    private String productID="", state = "Normal";
    CarouselView carouselView;
    private CalldetailsRecords calldetails = new CalldetailsRecords() ;
    private String number;
    private HotDealsModel products;
    String[] url;
    Utilitys utilitys = new Utilitys();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotdeals_details);
        productID = getIntent().getStringExtra(AppConstants.pid);
        carouselView =  findViewById(R.id.hot_details_carouselView);
        productName = (TextView) findViewById(R.id.hot_name_details);
        tv_size = findViewById(R.id.hot_size_details);
        iv_hot_back = findViewById(R.id.iv_hot_back);
        hd_cl_btn = findViewById(R.id.hd_cl_btn);
        tv_owner_broker = findViewById(R.id.tv_owner_broker);
        hd_whatsapp_btn = findViewById(R.id.hd_whtsapp_btn);
        tv_topbar_productName = (TextView) findViewById(R.id.tv_topbar_productName);
        productDescription = (TextView) findViewById(R.id.hot_description_details);
        productPrice = (TextView) findViewById(R.id.hot_price_details);
        tv_posted_date = findViewById(R.id.tv_hot_posted_date);
        tv_typeof_post = findViewById(R.id.tv_typeof_post);
        tv_approval_hotdeal = findViewById(R.id.tv_approval_hotdeal);
        tv_hot_location = findViewById(R.id.tv_hot_location);
        tv_special_option1 = findViewById(R.id.tv_special_option1);
        tv_special_option2 = findViewById(R.id.tv_special_option2);
        tv_hot_owner = findViewById(R.id.tv_hot_owner);
        tv_hot_timings = findViewById(R.id.tv_hot_timings);
        getProductDetails(productID);

        hd_cl_btn.setOnClickListener(view -> {
            utilitys.navigateCall(this,products.getNumber(),products.getPname());
        });

        hd_whatsapp_btn.setOnClickListener(view -> {
            utilitys.navigateWhatsapp(this,products.getNumber(),products.getPname());
        });

        iv_hot_back.setOnClickListener(view -> {
            finish();
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
                    products=dataSnapshot.getValue(HotDealsModel.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    tv_topbar_productName.setText(products.getPname());
                    tv_size.setText(products.getPropertysize());
                    tv_hot_owner .setText(products.getOwnerName());
                    tv_hot_location.setText(products.getLocation());
                    tv_hot_timings.setText(products.getTimings());
                    url = products.getImage2().split("---");
                    carouselView.setImageListener(imageListener);
                    carouselView.setPageCount(url.length);
                    number = products.getNumber();
                    tv_owner_broker.setText(products.getPostedby());
                    tv_typeof_post.setText(products.getType());
                    if(products.getText1() == null || products.getText1().equals("")){
                        tv_special_option1.setVisibility(View.GONE);
                    }else {
                        tv_special_option1.setText(products.getText1());
                    }
                    if(products.getText2() == null || products.getText2().equals("")){
                        tv_special_option2.setVisibility(View.GONE);
                    }else {
                        tv_special_option2.setText(products.getText1());
                    }
                    tv_posted_date.setText("Posted on "+products.getDate());
                    /*if(products.getApproval().toString()=="1"){
                        tv_approval_hotdeal.setText("approved not property");
                    }else {
                        tv_approval_hotdeal.setText("approved property");
                    }*/
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

            Glide.with(HotDealsDetailsActivity.this)
                    .load(url[position])
                    .into(imageView);

        }
    };
}