package com.sbd.gbd.Activities.BasicActivitys

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sbd.gbd.Model.LivingPlaceModel
import com.sbd.gbd.Utilitys.AppConstants
import com.google.firebase.database.*
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.Utilitys
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class RentHomeDetails : AppCompatActivity() {

    var tv_rent_price : TextView? = null
    var tv_house_name : TextView? = null
    var tv_rent_posted_on : TextView? = null
    var tv_rent_deposite : TextView? = null
    var tv_rent_home_size : TextView? = null
    var tv_rent_furnished : TextView? = null
    var tv_rent_availableFrom : TextView? = null
    var tv_rent_location : TextView? = null
    var tv_rent_type : TextView? = null
    var tv_rent_no_bedrooms : TextView? = null
    var tv_rent_bathrooms : TextView? = null
    var tv_rent_balcaney : TextView? = null
    var tv_rent_floor : TextView? = null
    var tv_rent_discription : TextView? = null
    var tv_rent_posted_by : TextView? = null
    var tv_rent_parking : TextView? = null
    var tv_rent_status : TextView? = null
    var btn_rent_whatsApp : TextView? =null
    var btn_rent_call : TextView? =null
    private var productID = ""
    private lateinit var url: Array<String>
    var carouselView: CarouselView? = null
    private var number :String? = ""
    private var name :String? = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent_home_details)

        initilize()
    }

    private fun initilize() {
        productID = intent.getStringExtra(AppConstants.pid)!!
        tv_house_name = findViewById(R.id.tv_house_name);
        tv_rent_price = findViewById(R.id.tv_rent_price);
        tv_rent_posted_on = findViewById(R.id.tv_rent_posted_on);
        tv_rent_deposite = findViewById(R.id.tv_rent_deposite);
        tv_rent_home_size = findViewById(R.id.tv_rent_home_size);
        tv_rent_parking = findViewById(R.id.tv_rent_parking)
        tv_rent_furnished = findViewById(R.id.tv_rent_furnished);
        tv_rent_availableFrom = findViewById(R.id.tv_rent_availableFrom);
        tv_rent_location = findViewById(R.id.tv_rent_location);
        tv_rent_type = findViewById(R.id.tv_rent_type);
        tv_rent_no_bedrooms = findViewById(R.id.tv_rent_no_bedrooms);
        tv_rent_bathrooms = findViewById(R.id.tv_rent_bathrooms);
        tv_rent_balcaney = findViewById(R.id.tv_rent_balcaney);
        tv_rent_discription = findViewById(R.id.tv_rent_discription);
        tv_rent_floor = findViewById(R.id.tv_rent_floor)
        carouselView = findViewById(R.id.cv_rent)
        tv_rent_posted_by = findViewById(R.id.tv_rent_posted_by)
        tv_rent_status = findViewById(R.id.tv_rent_status)
        btn_rent_whatsApp = findViewById(R.id.btn_rent_whatsApp)
        btn_rent_call = findViewById(R.id.btn_rent_call)
        var utilitys = Utilitys()


        btn_rent_whatsApp?.setOnClickListener(){
            utilitys.navigateCall(
                this,
                number,
                name
            )
        }

        btn_rent_call?.setOnClickListener {
            utilitys.navigateCall(
                this,
                number,
                name
            )
        }

        getProductDetails(productID)
    }

    private fun getProductDetails(productID: String) {
        var productsRef: DatabaseReference? = null
        productsRef = FirebaseDatabase.getInstance().reference.child("livingplaceforyou")
        productsRef?.child(productID)?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue(LivingPlaceModel::class.java) != null) {
                    val products = dataSnapshot.getValue(LivingPlaceModel::class.java)
                    tv_house_name?.setText(products?.title)
                    tv_rent_price?.setText("Rs." + products?.rentamount)
                    tv_rent_discription?.setText(products?.discription)
                    tv_rent_location?.setText(products?.location)
                    tv_rent_home_size?.setText(products?.area)
                    tv_rent_type?.setText(products?.category)
                    tv_rent_deposite?.setText(products?.deposit)
                    tv_rent_parking?.setText(products?.parking)
                    number = products?.contactNumber
                    name = products?.postedBY
                    //tv_ads_posted.setText(products?.Postedby)
                    tv_rent_availableFrom?.setText(products?.availableFrom)
                    tv_rent_no_bedrooms?.setText(products?.bedroom)
                    tv_rent_bathrooms?.setText(products?.bathroom)
                    tv_rent_balcaney?.setText(products?.balconey)
                    tv_rent_floor?.setText(products?.floore)
                    tv_rent_posted_by?.setText(products?.postedBY)
                    if (products?.postedOn == null) {
                        tv_rent_posted_on?.setVisibility(View.GONE)
                    } else {
                        tv_rent_posted_on?.setText(products?.postedOn)
                    }
                    if(products?.status == "1"){

                    }else{
                       // tv_rent_status?.setText("Not Available")
                    }
                    url = products?.image2?.split("---")?.toTypedArray()!!
                    carouselView?.setImageListener(imageListener)
                    carouselView?.setPageCount(url.size)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    var imageListener =
        ImageListener { position, imageView ->
            Glide.with(this)
                .load(url[position])
                .into(imageView)
        }

}