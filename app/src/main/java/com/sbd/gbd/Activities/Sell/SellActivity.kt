package com.sbd.gbd.Activities.Sell

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.sbd.gbd.Activities.Admin.AdminBusinessListings
import com.sbd.gbd.Activities.Admin.AdminLivingPlacess
import com.sbd.gbd.Activities.Admin.Admin_Construction
import com.sbd.gbd.Activities.Admin.Admin_ads_dashboard
import com.sbd.gbd.Activities.Admin.Admin_travels
import com.sbd.gbd.Activities.BasicActivitys.OtpLoginActivity
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.PreferenceManager
import com.sbd.gbd.Utilitys.UtilityMethods

class SellActivity : AppCompatActivity(), View.OnClickListener {
    var ll_post_property: LinearLayout? = null
    var ll_travel_posting: LinearLayout? = null
    var ll_post_constructions: LinearLayout? = null
    var ll_other_posting : LinearLayout? = null
    var ll_business_Lissting : LinearLayout? = null
    var ll_rent : LinearLayout? = null
    var iv_nav_view : ImageView? = null
    lateinit var preferenceManager: PreferenceManager


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
        preferenceManager= PreferenceManager(this);
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
                if (preferenceManager.getLoginState()) {
                    val intent = Intent(this, Admin_ads_dashboard::class.java)
                    intent.putExtra("page", "2")
                    startActivity(intent)
                } else {
                    UtilityMethods.showToast(this,"Please Login to process")
                    val intent = Intent(this, OtpLoginActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.ll_travels_listing -> {
                if (preferenceManager.getLoginState()) {
                    val intent2 = Intent(this, Admin_travels::class.java)
                    startActivity(intent2)
                } else {
                    UtilityMethods.showToast(this,"Please Login to process")
                    val intent = Intent(this, OtpLoginActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.ll_post_constructions -> {
                if (preferenceManager.getLoginState()) {
                    val intent3 = Intent(this, Admin_Construction::class.java)
                    startActivity(intent3)
                } else {
                    UtilityMethods.showToast(this,"Please Login to process")
                    val intent = Intent(this, OtpLoginActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.ll_business_Lissting -> {
                if (preferenceManager.getLoginState()) {
                    val intent2 = Intent(this, AdminBusinessListings::class.java)
                    startActivity(intent2)
                } else {
                    UtilityMethods.showToast(this,"Please Login to process")
                    val intent = Intent(this, OtpLoginActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.ll_other_posting -> {
                if (preferenceManager.getLoginState()) {
                    val intent2 = Intent(this, AdminBusinessListings::class.java)
                    startActivity(intent2)
                } else {
                    UtilityMethods.showToast(this,"Please Login to process")
                    val intent = Intent(this, OtpLoginActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.ll_rent -> {
                if (preferenceManager.getLoginState()) {
                    val intent = Intent(this@SellActivity, Admin_ads_dashboard::class.java)
                    intent.putExtra("page", "3")
                    startActivity(intent)
                } else {
                    UtilityMethods.showToast(this,"Please Login to process")
                    val intent = Intent(this, OtpLoginActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.iv_nav_back -> {
                finish()
            }
        }
    }
}