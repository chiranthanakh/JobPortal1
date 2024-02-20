package com.sbd.gbd.Activities.BasicActivitys

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sbd.gbd.Model.HotelsModel
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.Utilitys
import com.google.firebase.database.*
import com.sbd.gbd.R
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class HotelsDetails : AppCompatActivity() {

    var tv_rent_price : TextView? = null
    var tv_hotel_name : TextView? = null
    var tv_rent_posted_on : TextView? = null
    var tv_hotel_category : TextView? = null
    var tv_hotel_email : TextView? = null
    var tv_rent_furnished : TextView? = null
    var tv_rent_availableFrom : TextView? = null
    var tv_hotel_location : TextView? = null
    var tv_rent_type : TextView? = null
    var tv_hotel_owner : TextView? = null
    var tv_rent_bathrooms : TextView? = null
    var tv_rent_balcaney : TextView? = null
    var tv_hotel_website : TextView? = null
    var tv_hotel_discription : TextView? = null
    var tv_rent_posted_by : TextView? = null
    var tv_hotel_parking : TextView? = null
    var tv_rent_status : TextView? = null
    var btn_hotel_whatsApp : TextView? =null
    var btn_hotel_call : TextView? =null
    var iv_hotel_back : ImageView? = null
    private var productID = ""
    private lateinit var url: Array<String>
    var carouselView: CarouselView? = null
    var utilitys = Utilitys()
    private var number : String? = ""
    private var name : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialog)

        initilize()
    }

    private fun initilize() {
        productID = intent.getStringExtra(AppConstants.pid)!!
        tv_hotel_name = findViewById(R.id.tv_hotel_name);
        tv_rent_price = findViewById(R.id.tv_hotel_price);
        tv_rent_posted_on = findViewById(R.id.tv_rent_posted_on);
        tv_hotel_category = findViewById(R.id.tv_hotel_category);
        tv_hotel_email = findViewById(R.id.tv_hotel_email);
        tv_hotel_parking = findViewById(R.id.tv_hotel_parking)
        tv_rent_furnished = findViewById(R.id.tv_rent_furnished);
        tv_rent_availableFrom = findViewById(R.id.tv_rent_availableFrom);
        tv_hotel_location = findViewById(R.id.tv_hotel_location);
        tv_rent_type = findViewById(R.id.tv_rent_type);
        tv_hotel_owner = findViewById(R.id.tv_hotel_owner);
        tv_rent_bathrooms = findViewById(R.id.tv_rent_bathrooms);
        tv_rent_balcaney = findViewById(R.id.tv_rent_balcaney);
        tv_hotel_discription = findViewById(R.id.tv_hotel_discription);
        tv_hotel_website = findViewById(R.id.tv_hotel_website)
        carouselView = findViewById(R.id.cv_rent)
        btn_hotel_call = findViewById(R.id.btn_hotel_call)
        tv_rent_status = findViewById(R.id.tv_rent_status)
        btn_hotel_whatsApp = findViewById(R.id.btn_hotel_whatsApp)
        iv_hotel_back = findViewById(R.id.iv_hotel_back)

        btn_hotel_whatsApp?.setOnClickListener(){
            utilitys.navigateWhatsapp(this, number, name)
        }

        btn_hotel_call?.setOnClickListener(){
            utilitys.navigateCall(this, number, name)
        }

        iv_hotel_back?.setOnClickListener(){
            finish()
        }
        getProductDetails(productID)
    }

    private fun getProductDetails(productID: String) {
        var productsRef: DatabaseReference? = null
        productsRef = FirebaseDatabase.getInstance().reference.child("hotelsforyou")
        productsRef?.child(productID)?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue(HotelsModel::class.java) != null) {
                    val products = dataSnapshot.getValue(HotelsModel::class.java)
                    tv_hotel_name?.setText(products?.name)
                    tv_rent_price?.setText("Rs." + products?.price)
                    tv_hotel_discription?.setText(products?.discription)
                    tv_hotel_location?.setText(products?.address)
                    tv_hotel_email?.setText(products?.email)
                    tv_hotel_category?.setText(products?.category)
                    tv_hotel_parking?.setText(products?.parking)
                    tv_hotel_category?.setText(products?.category)
                    tv_hotel_owner?.setText(products?.owner)
                    tv_hotel_website?.setText(products?.website)


                    name = products?.name
                    number = products?.Number
                    //tv_ads_posted.setText(products?.Postedby)
                   // tv_rent_availableFrom?.setText(products?.availableFrom)
                    //tv_hotel_owner?.setText(products?.)
                   // tv_rent_bathrooms?.setText(products?.bathroom)
                    //tv_rent_balcaney?.setText(products?.balconey)
                   // tv_rent_floor?.setText(products?.floore)
                    //tv_rent_posted_by?.setText(products?.postedBY)
                   /* if (products?.postedOn == null) {
                        tv_rent_posted_on?.setVisibility(View.GONE)
                    } else {
                        tv_rent_posted_on?.setText(products?.postedOn)
                    }*/
                    if(products?.status == "1"){

                    }else{
                        tv_rent_status?.setText("Not Available")
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