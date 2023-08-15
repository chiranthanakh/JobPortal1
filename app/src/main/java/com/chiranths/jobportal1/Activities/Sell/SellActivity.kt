package com.chiranths.jobportal1.Activities.Sell

import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import android.os.Bundle
import com.chiranths.jobportal1.R
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.chiranths.jobportal1.Activities.Admin.*

class SellActivity : AppCompatActivity(), View.OnClickListener {
    var ll_post_property: LinearLayout? = null
    var ll_travel_posting: LinearLayout? = null
    var ll_post_constructions: LinearLayout? = null
    var ll_other_posting : LinearLayout? = null
    var ll_business_Lissting : LinearLayout? = null
    var ll_rent : LinearLayout? = null
    var iv_nav_view : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)
        ll_post_property = findViewById(R.id.ll_post_property)
        ll_travel_posting = findViewById(R.id.ll_travels_listing)
        ll_post_constructions = findViewById(R.id.ll_post_constructions)
        ll_other_posting = findViewById(R.id.ll_other_posting)
        ll_business_Lissting = findViewById(R.id.ll_business_Lissting)
        iv_nav_view = findViewById(R.id.iv_nav_back)
        ll_rent = findViewById(R.id.ll_rent)
        ll_post_property?.setOnClickListener(this)
        ll_travel_posting?.setOnClickListener(this)
        ll_post_constructions?.setOnClickListener(this)
        ll_business_Lissting?.setOnClickListener(this)
        ll_other_posting?.setOnClickListener(this)
        ll_rent?.setOnClickListener(this)
        iv_nav_view?.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_post_property -> {
                val intent = Intent(this, Admin_ads_dashboard::class.java)
                startActivity(intent)
            }
            R.id.ll_travels_listing -> {
                val intent2 = Intent(this, Admin_travels::class.java)
                startActivity(intent2)
            }
            R.id.ll_post_constructions -> {
                val intent3 = Intent(this, Admin_Construction::class.java)
                startActivity(intent3)
            }
            R.id.ll_business_Lissting -> {
                val intent2 = Intent(this, AdminBusinessListings::class.java)
                startActivity(intent2)
            }
            R.id.ll_other_posting -> {
                val intent2 = Intent(this, AdminBusinessListings::class.java)
                startActivity(intent2)
            }
            R.id.ll_rent -> {
                val intent2 = Intent(this, AdminLivingPlacess::class.java)
                startActivity(intent2)
            }
            R.id.iv_nav_back -> {
                finish()
            }
        }
    }
}