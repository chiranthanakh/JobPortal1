package com.sbd.gbd.Activities.Propertys

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class PropertyDetailsActivity : AppCompatActivity() {
    private var productPrice: TextView? = null
    private var productDescription: TextView? = null
    private var productName: TextView? = null
    private var tv_topbar_productName: TextView? = null
    private var productID: String? = ""
    private val state = "Normal"
    var carouselView: CarouselView? = null
    lateinit var url: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_details)
        productID = intent.getStringExtra(AppConstants.pid)
        // addToCartButton =(Button) findViewById(R.id.property_call_btn);
        carouselView = findViewById(R.id.carouselView)
        productName = findViewById<View>(R.id.product_name_details) as TextView
        tv_topbar_productName = findViewById<View>(R.id.tv_topbar_productName) as TextView
        productDescription = findViewById<View>(R.id.product_description_details) as TextView
        productPrice = findViewById<View>(R.id.product_price_details) as TextView
        getProductDetails(productID)

    }


    private fun getProductDetails(productID: String?) {
        val productsRef = FirebaseDatabase.getInstance().reference.child(AppConstants.products)
        productsRef.child(productID!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val products = dataSnapshot.getValue(Products::class.java)
                    productName!!.text = products!!.pname
                    productPrice!!.text = products.price
                    productDescription!!.text = products.description
                    tv_topbar_productName!!.text = products.pname
                    //Picasso.get().load(products.getImage()).into(productImage);
                    url = products.image!!.split("---".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    carouselView!!.pageCount = url.size
                    carouselView!!.setImageListener(imageListener)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    var imageListener =
        ImageListener { position, imageView -> // Picasso.get().load(url[position]).into(imageView);
            Glide.with(this@PropertyDetailsActivity)
                .load(url[position])
                .into(imageView)
        }
}