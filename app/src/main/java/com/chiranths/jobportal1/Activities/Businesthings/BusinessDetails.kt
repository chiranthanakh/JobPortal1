package com.chiranths.jobportal1.Activities.Businesthings

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.chiranths.jobportal1.Activities.Propertys.Products
import com.chiranths.jobportal1.Adapters.BusinessAdaptor
import com.chiranths.jobportal1.Model.BusinessModel
import com.chiranths.jobportal1.R
import com.chiranths.jobportal1.Utilitys.AppConstants
import com.chiranths.jobportal1.Utilitys.Utilitys
import com.google.firebase.database.*
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class BusinessDetails : AppCompatActivity() {

    var business_name : TextView? = null
    var tv_business_cat : TextView? = null
    var tv_price : TextView? = null
    var tv_address : TextView? = null
    var tv_futures1 : TextView? = null
    var tv_futures2 : TextView? = null
    var tv_futures3 : TextView? = null
    var tv_discription : TextView? = null
    var carouselView: CarouselView? = null
    var bus_cl_btn : LinearLayout? = null
    var ads_whatsapp_btn : LinearLayout? = null
    var tv_product_servicess : TextView? = null
    var tv_business_timings : TextView? = null
    var tv_business_gst : TextView? =  null
    var tv_business_owner : TextView? =  null
    var number : String? = null
    var productName : String? = null
    var utilitys = Utilitys()


    lateinit var url: Array<String>
    private var productID = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_details2)
        productID = intent?.getStringExtra(AppConstants.pid).toString()
        initilize()
        getProductDetails(productID)
    }

    private fun initilize() {
        business_name = findViewById(R.id.business_name)
        tv_business_timings = findViewById(R.id.tv_business_timings)
        tv_business_gst = findViewById(R.id.tv_business_gst)
        tv_business_owner = findViewById(R.id.tv_business_owner)
        tv_business_cat = findViewById(R.id.Business_category)
        tv_price = findViewById(R.id.tv_business_prise)
        tv_address = findViewById(R.id.tv_address)
        tv_futures1 = findViewById(R.id.tv_futures1)
        tv_futures2 = findViewById(R.id.tv_futures2)
        tv_futures3 = findViewById(R.id.tv_futures3)
        tv_discription = findViewById(R.id.tv_business_description)
        bus_cl_btn = findViewById(R.id.bus_cl_btn)
        tv_product_servicess = findViewById(R.id.tv_business_pors)
        ads_whatsapp_btn = findViewById(R.id.ads_whatsapp_btn)
        carouselView = findViewById(R.id.business_carouselView)

        bus_cl_btn?.setOnClickListener{ view: View? ->
            utilitys.navigateCall(
                this,
                number,
                productName
            )
        }

        ads_whatsapp_btn?.setOnClickListener{ view: View? ->
            utilitys.navigateWhatsapp(
                this,
                number,
                productName
            )
        }
    }

    private fun getProductDetails(productID: String) {
        var productsRef: DatabaseReference? = null
        productsRef = FirebaseDatabase.getInstance().reference.child("BusinessListing")
        productsRef?.child(productID)?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               if (dataSnapshot.exists() && dataSnapshot.getValue(BusinessModel::class.java) != null) {
                    val products = dataSnapshot.getValue(BusinessModel::class.java)
                    business_name?.setText(products?.Businessname)
                    tv_business_cat?.setText(products?.Business_category)
                    tv_address?.setText(products?.location)
                    tv_price?.setText(products?.price)
                    tv_discription?.setText(products?.description)
                    number = products?.number
                    productName = products?.Businessname
                    tv_product_servicess?.setText(products?.Category)
                    tv_business_gst?.setText(products?.gst)
                    tv_business_owner?.setText(products?.owner)
                    tv_business_timings?.setText(products?.time)
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
                .load(url.get(position))
                .into(imageView)
        }
    }