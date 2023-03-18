package com.chiranths.jobportal1.Activities.Sell

import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import android.os.Bundle
import com.chiranths.jobportal1.R
import android.content.Intent
import android.view.View
import com.chiranths.jobportal1.Activities.Admin.Admin_layouts_dashboard
import com.chiranths.jobportal1.Activities.Admin.AdminBusinessListings
import com.chiranths.jobportal1.Activities.Admin.Admin_Construction

class SellActivity : AppCompatActivity(), View.OnClickListener {
    var ll_post_property: LinearLayout? = null
    var ll_business_posting: LinearLayout? = null
    var ll_post_constructions: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)
        ll_post_property = findViewById(R.id.ll_post_property)
        ll_business_posting = findViewById(R.id.ll_business_posting)
        ll_post_constructions = findViewById(R.id.ll_post_constructions)
        ll_post_property?.setOnClickListener(this)
        ll_business_posting?.setOnClickListener(this)
        ll_post_constructions?.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_post_property -> {
                val intent = Intent(this, Admin_layouts_dashboard::class.java)
                startActivity(intent)
            }
            R.id.ll_business_posting -> {
                val intent2 = Intent(this, AdminBusinessListings::class.java)
                startActivity(intent2)
            }
            R.id.ll_post_constructions -> {
                val intent3 = Intent(this, Admin_Construction::class.java)
                startActivity(intent3)
            }
        }
    }
}