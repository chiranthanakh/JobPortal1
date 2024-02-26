package com.sbd.gbd.Activities.Dashboard

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sbd.gbd.Utilitys.CalldetailsRecords
import com.sbd.gbd.Model.LayoutModel
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

class LayoutDetailsActivity : AppCompatActivity() {
    private var ads_cl_btn: MaterialButton? = null
    private var ads_whatsapp_btn: MaterialButton? = null
    var iv_back_ads: ImageView? = null
    var carouselView: CarouselView? = null
    private var productPrice: TextView? = null
    private var productDescription: TextView? = null
    private var productName: TextView? = null
    private var tv_topbar_productName: TextView? = null
    private var tv_ads_details_verify: TextView? = null
    private var tv_no_sites: TextView? = null
    private var tv_layout_facing: TextView? = null
    private var tv_layout_area: TextView? = null
    private var tv_place_location: TextView? = null
    private var tv_size_details: TextView? = null
    private var tv_prop_type: TextView? = null
    private var tv_future1: TextView? = null
    private var tv_future2: TextView? = null
    private var tv_future3: TextView? = null
    private var tv_future4: TextView? = null
    private var tv_contact_type: TextView? = null
    private var tv_ads_posted_on: TextView? = null
    private val tv_ads_posted: TextView? = null
    private val ads_details_not_verified: TextView? = null
    private var productID: String? = ""
    private val state = "Normal"
    private var number: String? = null
    private var page: String? = null
    private lateinit var url: Array<String>
    var calldetails = CalldetailsRecords()
    var utilitys = Utilitys()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layouts_details)
        productID = intent.getStringExtra(AppConstants.pid)
        page = intent.getStringExtra("page")
        ads_cl_btn = findViewById(R.id.ads_cl_btn)
        ads_whatsapp_btn = findViewById(R.id.ads_whtsapp_btn)
        carouselView = findViewById(R.id.ads_details_carouselView)
        tv_prop_type = findViewById(R.id.tv_prop_type)
        iv_back_ads = findViewById(R.id.iv_back_ads)
        //tv_ads_posted = findViewById(R.id.tv_ads_posted);
        tv_future1 = findViewById(R.id.tv_futures1)
        tv_future2 = findViewById(R.id.tv_futures2)
        tv_future3 = findViewById(R.id.tv_futures3)
        tv_future4 = findViewById(R.id.tv_futures4)
        tv_no_sites = findViewById(R.id.tv_sites_available)
        tv_layout_facing = findViewById(R.id.tv_layout_facing)
        tv_ads_details_verify = findViewById(R.id.ads_details_verifyed)
        // ads_details_not_verified = findViewById(R.id.ads_details_not_verified);
        tv_ads_posted_on = findViewById(R.id.tv_ads_posted_on)
        tv_contact_type = findViewById(R.id.tv_contact_who)
        tv_place_location = findViewById(R.id.ads_place_location)
        tv_size_details = findViewById(R.id.ads_size_details)
        productName = findViewById<View>(R.id.product_name_details) as TextView
        tv_topbar_productName = findViewById<View>(R.id.tv_topbar_productName) as TextView
        productDescription = findViewById<View>(R.id.product_description_details) as TextView
        productPrice = findViewById<View>(R.id.product_price_details) as TextView
        tv_layout_area = findViewById<View>(R.id.tv_layout_area) as TextView
        getProductDetails(productID)
        ads_cl_btn?.setOnClickListener(View.OnClickListener { view: View? ->
            utilitys.navigateCall(
                this@LayoutDetailsActivity,
                number,
                productName!!.text.toString()
            )
        })
        ads_whatsapp_btn?.setOnClickListener(View.OnClickListener { view: View? ->
            utilitys.navigateWhatsapp(
                this@LayoutDetailsActivity,
                number,
                productName!!.text.toString()
            )
        })
        iv_back_ads?.setOnClickListener(View.OnClickListener { finish() })
    }

    override fun onStart() {
        super.onStart()
        //CheckOrderState();
    }

    private fun getProductDetails(productID: String?) {
        val productsRef: DatabaseReference
        productsRef = if (page == "3") {
            FirebaseDatabase.getInstance().reference.child("Corosels")
        } else {
            FirebaseDatabase.getInstance().reference.child("layoutsforyou")
        }
        productsRef.child(productID!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue(LayoutModel::class.java) != null) {
                    val products = dataSnapshot.getValue(LayoutModel::class.java)
                    productName!!.text = products!!.pname
                    productPrice!!.text = "Rs." + products.price
                    productDescription!!.text = products.description
                    tv_topbar_productName!!.text = products.pname
                    tv_place_location!!.text = products.location
                    tv_size_details!!.text = products.propertysize
                    tv_prop_type!!.text = products.category
                    tv_layout_facing!!.text = products.facing
                    tv_no_sites!!.text = products.sitesAvailable
                    tv_layout_area!!.text = products.layoutarea
                    // tv_ads_posted.setText("Posted by : "+products.getPostedBy());
                    if (products.date == null) {
                        tv_ads_posted_on!!.visibility = View.GONE
                    } else {
                        tv_ads_posted_on!!.text = "Posted on " + products.date
                    }
                    url = products.image2!!.split("---".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    carouselView!!.setImageListener(imageListener)
                    carouselView!!.pageCount = url.size
                    val test = 1 //Integer.parseInt(products.getStatus());
                    if (test == 1) {
                        tv_ads_details_verify!!.visibility = View.GONE
                    } else {
                        //ads_details_not_verified.setVisibility(View.GONE);
                    }
                    number = products.number
                    tv_contact_type!!.text = products.postedBy
                    if (products.point1 != null) {
                        tv_future1!!.text = products.point1
                        tv_future2!!.text = products.point2
                        tv_future3!!.text = products.point3
                        tv_future4!!.text = products.point4
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    var imageListener = ImageListener { position, imageView ->
        Glide.with(this@LayoutDetailsActivity)
            .load(url[position])
            .into(imageView)
    }
}