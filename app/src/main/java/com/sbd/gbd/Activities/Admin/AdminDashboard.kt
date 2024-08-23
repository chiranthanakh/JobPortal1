package com.sbd.gbd.Activities.Admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sbd.gbd.R

class AdminDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admincoroselimages)

        val btn_corosel = findViewById<Button>(R.id.btn_corosel)
        val btn_ads = findViewById<Button>(R.id.btn_ads)
        val btn_hot_deals = findViewById<Button>(R.id.btn_hot_deals)
        val btn_business_listing = findViewById<Button>(R.id.btn_business_listing)
        val btn_layouts = findViewById<Button>(R.id.btn_layouts)
        val btn_loan_offers = findViewById<Button>(R.id.btn_loan_offers)
        val btn_business_category = findViewById<Button>(R.id.btn_business_category)
        val btn_propertys_approval = findViewById<Button>(R.id.propertys_for_approval)
        val btn_business_approval = findViewById<Button>(R.id.business_for_approval)
        val btn_livingplace_add = findViewById<Button>(R.id.btn_livingplace)
        val btn_travels = findViewById<Button>(R.id.btn_travels)
        val btn_construction = findViewById<Button>(R.id.btn_construction)
        val btn_hotels = findViewById<Button>(R.id.btn_Hotels)
        val add_location = findViewById<Button>(R.id.add_locations)
        val travel_approval = findViewById<Button>(R.id.travel_approval)

        btn_hotels.setOnClickListener {
            val intent = Intent(this@AdminDashboard, Admin_hotels::class.java)
            startActivity(intent)
        }

        add_location.setOnClickListener {
            val intent = Intent(this@AdminDashboard, AdminLocations::class.java)
            startActivity(intent)
        }
        btn_construction.setOnClickListener { view: View? ->
            val intent = Intent(this@AdminDashboard, Admin_Construction::class.java)
            startActivity(intent)
        }
        btn_travels.setOnClickListener {
            val intent = Intent(this@AdminDashboard, Admin_travels::class.java)
            startActivity(intent)
        }
        btn_livingplace_add.setOnClickListener {
            val intent = Intent(this@AdminDashboard, Admin_ads_dashboard::class.java)
            intent.putExtra("page", "3")
            startActivity(intent)
        }
        btn_propertys_approval.setOnClickListener {
            val intent = Intent(this@AdminDashboard, AdminPropertyApproval::class.java)
            startActivity(intent)
        }
        btn_business_approval.setOnClickListener {
            val intent = Intent(this@AdminDashboard, AdminBusinessCategorys::class.java)
            startActivity(intent)
        }
        btn_business_category.setOnClickListener {
            val intent = Intent(this@AdminDashboard, AdminBusinessCategorys::class.java)
            startActivity(intent)
        }
        btn_loan_offers.setOnClickListener {
            val intent = Intent(this@AdminDashboard, AdminloanOffers::class.java)
            startActivity(intent)
        }
        btn_hot_deals.setOnClickListener {
            val intent = Intent(this@AdminDashboard, Admin_hotdeals_dashboard::class.java)
            startActivity(intent)
        }
        btn_layouts.setOnClickListener {
           // val intent = Intent(this@AdminDashboard, Admin_layouts_dashboard::class.java)
           // startActivity(intent)
            val intent = Intent(this@AdminDashboard, Admin_ads_dashboard::class.java)
            intent.putExtra("page", "2")
            startActivity(intent)
        }
        btn_corosel.setOnClickListener {
            val intent = Intent(this@AdminDashboard, Admin_corosel_dashboard::class.java)
            startActivity(intent)
        }
        btn_ads.setOnClickListener {
            val intent = Intent(this@AdminDashboard, Admin_ads_dashboard::class.java)
            intent.putExtra("page", "1")
            startActivity(intent)
        }
        btn_business_listing.setOnClickListener {
            val intent = Intent(this@AdminDashboard, AdminBusinessListings::class.java)
            startActivity(intent)
        }

        /////// approvals
        travel_approval.setOnClickListener {
            val intent = Intent(this@AdminDashboard, AdminTravelApproval::class.java)
            startActivity(intent)
        }
    }
}