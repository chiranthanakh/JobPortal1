package com.chiranths.jobportal1.Activities.Construction

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chiranths.jobportal1.Adapters.ConstructorAdaptor
import com.chiranths.jobportal1.Model.ConstructionModel
import com.chiranths.jobportal1.R
import com.chiranths.jobportal1.Utilitys.AppConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ConstructionListActivity : AppCompatActivity() {

    var rv_construction: RecyclerView? = null
    var constructioninfo: ArrayList<ConstructionModel?> = ArrayList<ConstructionModel?>()
    private var constructionAdaptor: ConstructorAdaptor? = null
    var mHandler = Handler()
    var type : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_construction_list)
        val bundle = intent.extras
        type = bundle?.getString("type")
        initilize()
        fetchdata()
    }

    private fun initilize() {
        rv_construction = findViewById(R.id.rv_constructionslist_relates)
    }

    private fun fetchdata() {
        val productsinfo = FirebaseDatabase.getInstance().reference.child("constructionforyou").orderByChild(
            AppConstants.category).equalTo(type);
        productsinfo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            constructioninfo.add(
                                ConstructionModel(
                                    userData!![AppConstants.pid].toString(),
                                    userData[AppConstants.date].toString(),
                                    userData[AppConstants.time].toString(),
                                    userData["name"].toString(),
                                    userData[AppConstants.category].toString(),
                                    userData["cost"].toString(),
                                    userData["number1"].toString(),
                                    userData["product_services"].toString(),
                                    userData["experience"].toString(),
                                    userData["servicess1"].toString(),
                                    userData["servicess2"].toString(),
                                    userData["servicess3"].toString(),
                                    userData["servicess4"].toString(),
                                    userData["discription"].toString(),
                                    userData[AppConstants.verified].toString(),
                                    userData[AppConstants.image].toString(),
                                    userData[AppConstants.image2].toString(),
                                    userData["owner"].toString(),
                                    userData["address"].toString(),
                                    userData[AppConstants.Status].toString(),
                                    userData["gst"].toString(),
                                    userData["workingHrs"].toString()

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
                    constructionAdaptor =
                        ConstructorAdaptor(constructioninfo, this@ConstructionListActivity)
                    val elayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@ConstructionListActivity, RecyclerView.VERTICAL, false)
                    rv_construction?.setLayoutManager(
                        GridLayoutManager(
                            this@ConstructionListActivity,
                            1
                        )
                    )
                    rv_construction?.setItemAnimator(DefaultItemAnimator())
                    rv_construction?.setItemAnimator(DefaultItemAnimator())
                    mHandler.post(Runnable { rv_construction?.setAdapter(constructionAdaptor) })
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}