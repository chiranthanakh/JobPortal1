package com.sbd.gbd.Activities.HotDealsactivity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sbd.gbd.Utilitys.CalldetailsRecords
import com.sbd.gbd.Model.HotDealsModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.Utilitys
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class HotDealsDetailsActivity : AppCompatActivity() {
    private val btn_call: Button? = null
    private val productImage: ImageView? = null
    private var iv_hot_back: ImageView? = null
    private var hd_cl_btn: LinearLayout? = null
    private var hd_whatsapp_btn: LinearLayout? = null
    private var productPrice: TextView? = null
    private var productDescription: TextView? = null
    private var productName: TextView? = null
    private var tv_topbar_productName: TextView? = null
    private var tv_posted_date: TextView? = null
    private var tv_hot_location: TextView? = null
    private var tv_size: TextView? = null
    private var tv_owner_broker: TextView? = null
    private var tv_typeof_post: TextView? = null
    private var tv_approval_hotdeal: TextView? = null
    private var tv_special_option1: TextView? = null
    private var tv_special_option2: TextView? = null
    private var tv_hot_owner: TextView? = null
    private var tv_hot_timings: TextView? = null
    private var page: String? = null
    private var productID: String? = ""
    private val state = "Normal"
    var carouselView: CarouselView? = null
    private val calldetails = CalldetailsRecords()
    private var number: String? = null
    private var products: HotDealsModel? = null
    lateinit var url: Array<String>
    var utilitys = Utilitys()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotdeals_details)
        productID = intent.getStringExtra(AppConstants.pid)
        page = intent.getStringExtra("page")
        carouselView = findViewById(R.id.hot_details_carouselView)
        productName = findViewById<View>(R.id.hot_name_details) as TextView
        tv_size = findViewById(R.id.hot_size_details)
        iv_hot_back = findViewById(R.id.iv_hot_back)
        hd_cl_btn = findViewById(R.id.hd_cl_btn)
        tv_owner_broker = findViewById(R.id.tv_owner_broker)
        hd_whatsapp_btn = findViewById(R.id.hd_whtsapp_btn)
        tv_topbar_productName = findViewById<View>(R.id.tv_topbar_productName) as TextView
        productDescription = findViewById<View>(R.id.hot_description_details) as TextView
        productPrice = findViewById<View>(R.id.hot_price_details) as TextView
        tv_posted_date = findViewById(R.id.tv_hot_posted_date)
        tv_typeof_post = findViewById(R.id.tv_typeof_post)
        tv_approval_hotdeal = findViewById(R.id.tv_approval_hotdeal)
        tv_hot_location = findViewById(R.id.tv_hot_location)
        tv_special_option1 = findViewById(R.id.tv_special_option1)
        tv_special_option2 = findViewById(R.id.tv_special_option2)
        tv_hot_owner = findViewById(R.id.tv_hot_owner)
        tv_hot_timings = findViewById(R.id.tv_hot_timings)
        getProductDetails(productID)
        hd_cl_btn?.setOnClickListener(View.OnClickListener { view: View? ->
            utilitys.navigateCall(
                this,
                products!!.number,
                products!!.pname
            )
        })
        hd_whatsapp_btn?.setOnClickListener(View.OnClickListener { view: View? ->
            utilitys.navigateWhatsapp(
                this,
                products!!.number,
                products!!.pname
            )
        })
        iv_hot_back?.setOnClickListener(View.OnClickListener { view: View? -> finish() })
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
            FirebaseDatabase.getInstance().reference.child("hotforyou")
        }
        productsRef.child(productID!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    products = dataSnapshot.getValue(HotDealsModel::class.java)
                    productName!!.text = products!!.pname
                    productPrice!!.text = products!!.price
                    productDescription!!.text = products!!.description
                    tv_topbar_productName!!.text = products!!.pname
                    tv_size!!.text = products!!.propertysize
                    tv_hot_owner!!.text = products!!.ownerName
                    tv_hot_location!!.text = products!!.location
                    tv_hot_timings!!.text = products!!.timings
                    url = products!!.image2!!.split("---".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    carouselView!!.setImageListener(imageListener)
                    carouselView!!.pageCount = url.size
                    number = products!!.number
                    tv_owner_broker!!.text = products!!.postedby
                    tv_typeof_post!!.text = products!!.type
                    if (products!!.text1 == null || products!!.text1 == "") {
                        tv_special_option1!!.visibility = View.GONE
                    } else {
                        tv_special_option1!!.text = products!!.text1
                    }
                    if (products!!.text2 == null || products!!.text2 == "") {
                        tv_special_option2!!.visibility = View.GONE
                    } else {
                        tv_special_option2!!.text = products!!.text1
                    }
                    tv_posted_date!!.text = "Posted on " + products!!.date
                    /*if(products.getApproval().toString()=="1"){
                        tv_approval_hotdeal.setText("approved not property");
                    }else {
                        tv_approval_hotdeal.setText("approved property");
                    }*/
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    var imageListener = ImageListener { position, imageView ->
        Glide.with(this@HotDealsDetailsActivity)
            .load(url[position])
            .into(imageView)
    }
}