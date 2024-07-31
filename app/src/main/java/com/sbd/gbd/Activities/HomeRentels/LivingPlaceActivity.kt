package com.sbd.gbd.Activities.HomeRentels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.sbd.gbd.Model.LivingPlaceModel
import com.sbd.gbd.Adapters.LivingPlaceAdaptor
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import com.sbd.gbd.R
import android.os.AsyncTask
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import com.sbd.gbd.Utilitys.AppConstants
import com.google.firebase.database.DatabaseError
import com.sbd.gbd.Activities.Admin.AdminLivingPlacess
import com.sbd.gbd.Activities.BasicActivitys.OtpLoginActivity
import com.sbd.gbd.Utilitys.PreferenceManager
import com.sbd.gbd.Utilitys.UtilityMethods
import java.lang.ClassCastException
import java.util.ArrayList
import java.util.HashMap

class LivingPlaceActivity : AppCompatActivity() {
    var productinfolist: ArrayList<LivingPlaceModel> = ArrayList<LivingPlaceModel>()
    private var livingPlaceAdaptor: LivingPlaceAdaptor? = null
    var rv_center_prop: RecyclerView? = null
    var mHandler = Handler()
    var type: String? = null
    var iv_back_leving : ImageView? = null
    var btn_add_hotel : AppCompatButton? = null
    var tv_heading : TextView? = null
    var ll_layout : LinearLayout? = null
    var iv_nodata : ImageView? = null
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_center_home)
        val bundle = intent.extras
        type = bundle?.getString("center")
        rv_center_prop = findViewById(R.id.rv_center_prop)
        iv_back_leving = findViewById(R.id.iv_back_leving)
        btn_add_hotel = findViewById(R.id.btn_add_hotel)
        tv_heading = findViewById(R.id.tv_heading)
        iv_nodata = findViewById(R.id.iv_nodata)
        ll_layout = findViewById(R.id.ll_layout)
        tv_heading?.text = "Homes or Commercial Placess "
        preferenceManager= PreferenceManager(this)
        AsyncTask.execute{ fetchdata() }

        btn_add_hotel?.setOnClickListener {
            if (preferenceManager.getLoginState()) {
                val intent = Intent(this, AdminLivingPlacess::class.java)
                startActivity(intent)
            } else {
                UtilityMethods.showToast(this,"Please Login to process")
                val intent = Intent(this, OtpLoginActivity::class.java)
                startActivity(intent)
            }
        }
        iv_back_leving?.setOnClickListener {
            finish()
        }
    }

    private fun fetchdata() {
        val productsinfo = FirebaseDatabase.getInstance().reference.child("livingplaceforyou").orderByChild(AppConstants.Status).equalTo(AppConstants.user)
        productsinfo?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<*, *>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<*, *>?
                            ll_layout?.visibility = View.VISIBLE
                            iv_nodata?.visibility = View.GONE

                            productinfolist.add(
                                LivingPlaceModel(
                                    userData!![AppConstants.pid].toString(),
                                    userData["saveCurrentDate"].toString(),
                                    userData["saveCurrentTime"].toString(),
                                    userData["title"].toString(),
                                    userData[AppConstants.category].toString(),
                                    userData["rent_lease"].toString(),
                                    userData["floore"].toString(),
                                    userData["rentamount"].toString(),
                                    userData[AppConstants.location].toString(),
                                    userData["contactNumber"].toString(),
                                    userData["Approval"].toString(),
                                    userData["nuBHK"].toString(),
                                    userData["sqft"].toString(),
                                    userData["water"].toString(),
                                    userData["parking"].toString(),
                                    userData[AppConstants.postedBy].toString(),
                                    userData["discription"].toString(),
                                    userData[AppConstants.image2].toString(),
                                    userData[AppConstants.image].toString(),
                                    userData[AppConstants.Deposit].toString(),
                                    "","","","","","",
                                    AppConstants.user
                                )
                            )
                        } catch (_: ClassCastException) {
                        }
                    }

                    livingPlaceAdaptor =
                        LivingPlaceAdaptor(productinfolist, this@LivingPlaceActivity)
                    LinearLayoutManager(this@LivingPlaceActivity, RecyclerView.VERTICAL, false)
                    rv_center_prop!!.layoutManager = GridLayoutManager(this@LivingPlaceActivity, 1)
                    rv_center_prop!!.itemAnimator = DefaultItemAnimator()
                    mHandler.post { rv_center_prop!!.adapter = livingPlaceAdaptor }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}