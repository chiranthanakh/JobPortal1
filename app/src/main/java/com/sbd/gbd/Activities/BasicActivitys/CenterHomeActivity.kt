package com.sbd.gbd.Activities.BasicActivitys

import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Adapters.CenterHomeadaptor
import com.sbd.gbd.Model.HotelsModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants

class CenterHomeActivity : AppCompatActivity() {
    var productinfolist: ArrayList<HotelsModel> = ArrayList<HotelsModel>()
    private var centerHomeadaptor: CenterHomeadaptor? = null
    var rv_center_prop: RecyclerView? = null
    var iv_back_leving: ImageView? = null
    var mHandler = Handler()
    var type: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_center_home)
        val bundle = intent.extras
        type = bundle!!.getString("center")
        rv_center_prop = findViewById(R.id.rv_center_prop)
        iv_back_leving = findViewById(R.id.iv_back_leving)
        AsyncTask.execute { fetchdata() }

        iv_back_leving?.setOnClickListener {
            finish()
        }
    }

    private fun fetchdata() {
        val productsinfo = FirebaseDatabase.getInstance().reference.child("hotelsforyou").orderByChild(AppConstants.Status).equalTo(AppConstants.user)
        productsinfo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            productinfolist.add(
                                HotelsModel(
                                    userData!!["name"].toString(),
                                    userData[AppConstants.image].toString(),
                                    userData[AppConstants.image2].toString(),
                                    userData[AppConstants.pid].toString(),
                                    userData[AppConstants.date].toString(),
                                    userData[AppConstants.category].toString(),
                                    userData[AppConstants.price].toString(),
                                    userData["address"].toString(),
                                    userData["owner"].toString(),
                                    userData["alternative"].toString(),
                                    userData[AppConstants.number].toString(),
                                    userData["email"].toString(),
                                    userData["website"].toString(),
                                    userData["parking"].toString(),
                                    userData["discription"].toString(),
                                    userData["Rating"].toString(),
                                    userData[AppConstants.Status].toString(),
                                    userData[AppConstants.point1].toString(),
                                    userData[AppConstants.point2].toString(),
                                    userData[AppConstants.point3].toString(),
                                    userData["Approval"].toString()
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
                    centerHomeadaptor = CenterHomeadaptor(productinfolist, this@CenterHomeActivity)
                    val elayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@CenterHomeActivity, RecyclerView.VERTICAL, false)
                    rv_center_prop!!.layoutManager = GridLayoutManager(this@CenterHomeActivity, 1)
                    rv_center_prop!!.itemAnimator = DefaultItemAnimator()
                    mHandler.post { rv_center_prop!!.adapter = centerHomeadaptor }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}