package com.sbd.gbd.Activities.Construction

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sbd.gbd.Utilitys.CalldetailsRecords
import com.sbd.gbd.Model.ConstructionModel
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.Utilitys
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*
import com.sbd.gbd.R
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class ConstructionDetailsActivity : AppCompatActivity() {
    private var iv_back_ads: ImageView? = null
    private var ads_cl_btn: MaterialButton? = null
    private var ads_whatsapp_btn: MaterialButton? = null
    var carouselView: CarouselView? = null
    private var productDescription: TextView? = null
    private var productName: TextView? = null
    private var const_gst: TextView? = null
    private var tv_topbar_productName: TextView? = null
    private var tv_ads_details_verify: TextView? = null
    private var tv_working_hrs: TextView? = null
    private var ads_approved_by: TextView? = null
    private var const_location: TextView? = null
    private var const_price_details: TextView? = null
    private var const_category: TextView? = null
    private var tv_product_service : TextView? = null
    private var tv_future1: TextView? = null
    private var tv_future2: TextView? = null
    private var tv_future3: TextView? = null
    private var tv_future4: TextView? = null
    private var tv_ads_posted_on: TextView? = null
    private var tv_experience : TextView? = null
    private var tv_owner : TextView? = null
    private var tv_pincode : TextView? = null
    private var productID: String? = ""
    private val state = "Normal"
    private var number: String? = null
    private var page: String? = null
    private lateinit var url: Array<String>
    var calldetails = CalldetailsRecords()
    var utilitys = Utilitys()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_const_details)
        productID = intent.getStringExtra(AppConstants.pid)
        page = intent.getStringExtra("page")
        ads_cl_btn = findViewById(R.id.ads_cl_btn)
        ads_whatsapp_btn = findViewById(R.id.ads_whtsapp_btn)
        carouselView = findViewById(R.id.ads_details_carouselView)
        const_category = findViewById(R.id.const_category)
        iv_back_ads = findViewById(R.id.iv_back_ads)
        tv_owner = findViewById(R.id.tv_owner)
        tv_product_service = findViewById(R.id.tv_product_service)
        tv_pincode = findViewById(R.id.tv_pincode)
        const_gst = findViewById(R.id.const_gst)
        tv_future1 = findViewById(R.id.tv_futures1)
        tv_future2 = findViewById(R.id.tv_futures2)
        tv_future3 = findViewById(R.id.tv_futures3)
        tv_future4 = findViewById(R.id.tv_futures4)
        tv_working_hrs = findViewById(R.id.tv_open_hrs)
        tv_ads_details_verify = findViewById(R.id.ads_details_verifyed)
        ads_approved_by = findViewById(R.id.ads_approved_by)
        tv_experience = findViewById(R.id.tv_experience)
        tv_ads_posted_on = findViewById(R.id.tv_ads_posted_on)
        const_location = findViewById(R.id.const_location)
        const_price_details = findViewById(R.id.const_price)
        productName = findViewById<View>(R.id.product_name_details) as TextView
        tv_topbar_productName = findViewById<View>(R.id.tv_topbar_productName) as TextView
        productDescription = findViewById<View>(R.id.product_description_details) as TextView
        getProductDetails(productID)
        ads_cl_btn?.setOnClickListener(View.OnClickListener { view: View? ->
            utilitys.navigateCall(
                this@ConstructionDetailsActivity,
                number,
                productName!!.text.toString()
            )
        })
        ads_whatsapp_btn?.setOnClickListener(View.OnClickListener { view: View? ->
            utilitys.navigateWhatsapp(
                this@ConstructionDetailsActivity,
                number,
                productName!!.text.toString()
            )
        })
        iv_back_ads?.setOnClickListener(View.OnClickListener { finish() })
    }

    private fun getProductDetails(productID: String?) {
        val productsRef: DatabaseReference
        productsRef = FirebaseDatabase.getInstance().reference.child("constructionforyou")
        productsRef.child(productID.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               //if (dataSnapshot.exists() && dataSnapshot.getValue(ConstructionModel.class) != null){
                val products = dataSnapshot.getValue(ConstructionModel::class.java)
                productName?.text = products?.name
                const_price_details?.text = "Rs." + products?.cost
                productDescription?.text = products?.discription
                tv_topbar_productName?.text = products?.name
                const_location?.text = products?.address
                const_category?.text = products?.category
                const_gst?.text = products?.gst
                tv_product_service?.text = products?.product_services
                tv_working_hrs?.text = products?.workingHrs
                tv_experience?.text = products?.experience
                tv_owner?.text = products?.owner
                url = products?.image2?.split("---")!!.toTypedArray()
                carouselView?.setImageListener(imageListener)
                carouselView?.pageCount = url.size
                number = products?.contactDetails
                if (products?.service1 != null && products?.service1 !== "") {
                    tv_future1?.text = products.service1
                    tv_future2?.text = products.service2
                    tv_future3?.text = products.service3
                    tv_future4?.text = products.service4
                } else {
                    tv_future1?.visibility = View.GONE
                    tv_future2?.visibility = View.GONE
                    tv_future3?.visibility = View.GONE
                    tv_future4?.visibility = View.GONE
                }
            //}
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    var imageListener = ImageListener { position, imageView ->
        Glide.with(this@ConstructionDetailsActivity)
            .load(url[position])
            .into(imageView)
    }
}