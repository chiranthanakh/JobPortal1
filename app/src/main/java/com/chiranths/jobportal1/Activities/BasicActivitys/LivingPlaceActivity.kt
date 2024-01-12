package com.chiranths.jobportal1.Activities.BasicActivitys

import androidx.appcompat.app.AppCompatActivity
import com.chiranths.jobportal1.Model.LivingPlaceModel
import com.chiranths.jobportal1.Adapters.LivingPlaceAdaptor
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import com.chiranths.jobportal1.R
import android.os.AsyncTask
import android.os.Handler
import android.widget.ImageView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import com.chiranths.jobportal1.Utilitys.AppConstants
import com.google.firebase.database.DatabaseError
import java.lang.ClassCastException
import java.util.ArrayList
import java.util.HashMap

class LivingPlaceActivity : AppCompatActivity() {
    var productinfolist: ArrayList<LivingPlaceModel> = ArrayList<LivingPlaceModel>()
    private var livingPlaceAdaptor: LivingPlaceAdaptor? = null
    var rv_center_prop: RecyclerView? = null
    var mHandler = Handler()
    var backButton: ImageView? = null
    var type: String? = null
    var iv_back_leving : ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_center_home)
        val bundle = intent.extras
        type = bundle?.getString("center")
        //backButton = findViewById(R.id.back_tool)
        rv_center_prop = findViewById(R.id.rv_center_prop)
        iv_back_leving = findViewById(R.id.iv_back_leving);
        AsyncTask.execute { fetchdata() }

        iv_back_leving?.setOnClickListener {
            finish()
        }
    }

    private fun fetchdata() {
        val productsinfo = FirebaseDatabase.getInstance().reference.child("livingplaceforyou")
        productsinfo?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
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
                                    userData[AppConstants.Status].toString()
                                )
                            )
                        } catch (cce: ClassCastException) {
                            try {
                                val mString = dataMap[key].toString()
                                //addTextToView(mString);
                            } catch (cce2: ClassCastException) {
                            }
                        }
                    }

                    // Upcoming Event
                    livingPlaceAdaptor =
                        LivingPlaceAdaptor(productinfolist, this@LivingPlaceActivity)
                    val elayoutManager: RecyclerView.LayoutManager =
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