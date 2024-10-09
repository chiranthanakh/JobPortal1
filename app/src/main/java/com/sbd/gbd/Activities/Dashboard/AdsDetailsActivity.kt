package com.sbd.gbd.Activities.Dashboard

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sbd.gbd.Utilitys.CalldetailsRecords
import com.sbd.gbd.Model.AdsModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.Utilitys
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class AdsDetailsActivity : AppCompatActivity() {
    private var iv_back_ads: ImageView? = null
    private var ads_cl_btn: LinearLayout? = null
    private var ads_whatsapp_btn: LinearLayout? = null
    var carouselView: CarouselView? = null
    private var productPrice: TextView? = null
    private var productDescription: TextView? = null
    private var productName: TextView? = null
    private var tv_topbar_productName: TextView? = null
    private var tv_ads_details_verify: TextView? = null
    private var tv_ads_landmark: TextView? = null
    private var ads_tv_facing: TextView? = null
    private var ads_approved_by: TextView? = null
    private var tv_place_location: TextView? = null
    private var tv_size_details: TextView? = null
    private var tv_prop_type: TextView? = null
    private var tv_future1: TextView? = null
    private var tv_future2: TextView? = null
    private var tv_future3: TextView? = null
    private var tv_future4: TextView? = null
    private var tv_katha : TextView? = null
    private var ads_place_district : TextView? = null

    var tv_floor : TextView? = null
    var tv_no_bedrooms : TextView? = null
    var tv_bathrooms : TextView? = null
    var tv_hall : TextView? = null
    var tv_furnished : TextView? = null
    var tv_parking : TextView? = null
    var tv_gated : TextView? = null
    var gated : TextView? =null
    var ll_home_details : LinearLayout? = null
    var ll_bhk : LinearLayout? = null


    private var tv_ads_nearby: TextView? = null
    private var tv_ads_posted_on: TextView? = null
    private var tv_ads_posted: TextView? = null
    private var tv_bhk_number: TextView? = null
    private var productID: String? = ""
    private val state = "Normal"
    private var number: String? = null
    private var page: String? = null
    private lateinit var url: Array<String>
    var calldetails = CalldetailsRecords()
    var utilitys = Utilitys()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads_details)
        productID = intent.getStringExtra(AppConstants.pid)
        page = intent.getStringExtra("page")
        ads_cl_btn = findViewById(R.id.ll_call)
        ads_whatsapp_btn = findViewById(R.id.ll_whatsapp)
        carouselView = findViewById(R.id.ads_details_carouselView)
        tv_prop_type = findViewById(R.id.tv_prop_type)
        iv_back_ads = findViewById(R.id.iv_back_ads)
        tv_ads_posted = findViewById(R.id.tv_ads_posted)
        ads_tv_facing = findViewById(R.id.ads_tv_facing)
        tv_future1 = findViewById(R.id.tv_futures1)
        tv_future2 = findViewById(R.id.tv_futures2)
        tv_future3 = findViewById(R.id.tv_futures3)
        tv_future4 = findViewById(R.id.tv_futures4)
        tv_katha = findViewById(R.id.tv_katha)
        tv_ads_nearby = findViewById(R.id.tv_ads_nearby)
        ads_place_district = findViewById(R.id.ads_place_district)
        tv_floor = findViewById(R.id.tv_floor)
        tv_no_bedrooms = findViewById(R.id.tv_no_bedrooms)
        tv_bathrooms = findViewById(R.id.tv_bathrooms)
        tv_hall = findViewById(R.id.tv_hall)
        tv_gated = findViewById(R.id.tv_gated)
        tv_furnished = findViewById(R.id.tv_furnished)
        tv_parking = findViewById(R.id.tv_parking)
        ll_home_details = findViewById(R.id.ll_home_details)
        tv_bhk_number = findViewById(R.id.tv_bhk_number)
        ll_bhk = findViewById(R.id.ll_bhk)
        tv_ads_landmark = findViewById(R.id.tv_ads_landmark)
        tv_ads_details_verify = findViewById(R.id.ads_details_verifyed)
        ads_approved_by = findViewById(R.id.ads_approved_by)
        // ads_details_not_verified = findViewById(R.id.ads_details_not_verified);
        tv_ads_posted_on = findViewById(R.id.tv_ads_posted_on)
        tv_place_location = findViewById(R.id.ads_place_location)
        tv_size_details = findViewById(R.id.ads_size_details)
        productName = findViewById<View>(R.id.product_name_details) as TextView
        tv_topbar_productName = findViewById<View>(R.id.tv_topbar_productName) as TextView
        productDescription = findViewById<View>(R.id.product_description_details) as TextView
        productPrice = findViewById(R.id.product_price_details)
        getProductDetails(productID)
        ads_cl_btn?.setOnClickListener {
            utilitys.navigateCall(
                this@AdsDetailsActivity,
                number,
                productName!!.text.toString()
            )
        }
        ads_whatsapp_btn?.setOnClickListener {
            utilitys.navigateWhatsapp(
                this@AdsDetailsActivity,
                number,
                productName!!.text.toString()
            )
        }
        iv_back_ads?.setOnClickListener { finish() }
    }

    override fun onStart() {
        super.onStart()
        //CheckOrderState();
    }

    private fun getProductDetails(productID: String?) {
        val productsRef: DatabaseReference
        productsRef = if (page == "1") {
            FirebaseDatabase.getInstance().reference.child(AppConstants.ads)
        } else if (page == "2") {
            FirebaseDatabase.getInstance().reference.child(AppConstants.products)
        } else {
            FirebaseDatabase.getInstance().reference.child(AppConstants.layouts)
        }
        productsRef.child(productID!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue(AdsModel::class.java) != null) {
                    val products = dataSnapshot.getValue(AdsModel::class.java)
                    productName?.text = products!!.pname
                    productPrice?.text = "Rs." + products.price
                    productDescription?.text = products.description
                    tv_topbar_productName?.text = products.pname
                    tv_place_location?.text = products.location
                    tv_size_details?.text = products.propertysize
                    tv_prop_type?.text = products.type
                    tv_ads_posted?.text = products.postedBy
                    tv_ads_landmark?.text = products.city
                    ads_tv_facing?.text = products.facing
                    ads_approved_by?.text = products.approvedBy
                    if(products.type == "Green Land") {
                        tv_katha?.text = "Green Land"
                    } else {
                        tv_katha?.text = products.katha
                    }
                    tv_ads_nearby?.text = products.nearBy
                    ads_place_district?.text = products.taluk+"(T), ${products.district}(D)"
                    if (products.postedOn == null) {
                        tv_ads_posted_on?.visibility = View.GONE
                    } else {
                        tv_ads_posted_on?.text = "Posted on :"+products.postedOn
                    }
                    if(products.verified.equals("1")) {
                        tv_ads_details_verify?.text = AppConstants.notVerified
                    } else if(products.verified.equals("2")) {
                        tv_ads_details_verify?.text = AppConstants.verified1
                        tv_ads_details_verify?.setTextColor(getResources().getColor(R.color.yellow));
                    }
                    tv_floor?.text = products.floors
                    tv_no_bedrooms?.text = products.bedrooms
                    tv_bathrooms?.text = products.bathrooms
                    tv_hall?.text = products.balconey
                    tv_bhk_number?.text = products.nuBhk

                    url = products.image2?.split("---".toRegex())?.dropLastWhile { it.isEmpty() }!!.toTypedArray()
                    carouselView?.setImageListener(imageListener)
                    carouselView?.pageCount = url.size
                    number = products.number



                    if(products?.parking == "true") {
                        tv_parking?.setText("Available")
                    } else {
                        tv_parking?.setText("Not Available")
                    }
                    if(products?.furnished == "true") {
                        tv_furnished?.setText("Yes")
                    } else {
                        tv_furnished?.setText("No")
                    }
                    if(products?.gated == "true") {
                        tv_gated?.setText("Yes")
                    } else {
                        tv_gated?.setText("No")
                    }


                    if(products.type == "Green Land") {
                        ll_home_details?.visibility = View.GONE
                        ll_bhk?.visibility = View.GONE
                        if (products.road == "true") {
                            tv_future1?.text = "Road Available for this property"
                        } else {
                            tv_future1?.text = "Road Not Available for this property"
                        }
                        if (products.boarwell == "true") {
                            tv_future2?.text = "Boarwell available"
                        } else {
                            tv_future2?.text = "Boarwell Not Available"
                        }
                        if (products.fencing == "true") {
                            tv_future3?.text = "Fencing available"
                        } else {
                            tv_future3?.text = "Fencing not available"
                        }
                        tv_future4?.visibility = View.GONE
                    } else if(products.type == "Site") {
                        ll_home_details?.visibility = View.GONE
                        ll_bhk?.visibility = View.GONE
                        if (products.waterFacility == "true") {
                            tv_future1?.text = "water Facility Available for this property"
                        } else {
                            tv_future1?.text = "water Facility Not Available for this property"
                        }
                        if (products.electricity == "true") {
                            tv_future2?.text = "electricity connection available"
                        } else {
                            tv_future2?.text = "electricity connection Available"
                        }
                        if (products.sewage == "true") {
                            tv_future3?.text = "sewage connection available"
                        } else {
                            tv_future3?.text = "sewage connection not available"
                        }
                        tv_future4?.visibility = View.GONE
                    } else {
                        tv_future1?.visibility = View.GONE
                        tv_future2?.visibility = View.GONE
                        tv_future3?.visibility = View.GONE
                        tv_future4?.visibility = View.GONE
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    var imageListener = ImageListener { position, imageView ->
        Glide.with(this@AdsDetailsActivity)
            .load(url[position])
            .centerCrop()
            .into(imageView)
    }
}