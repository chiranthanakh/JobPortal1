package com.sbd.gbd.Activities.Construction

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sbd.gbd.Adapters.BusinessAdaptor
import com.sbd.gbd.Model.BusinessModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Adapters.ConstructorAdaptor
import com.sbd.gbd.Model.Categorymmodel
import com.sbd.gbd.Model.ConstructionModel

class ConstructionFilter : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var businesslist = ArrayList<BusinessModel>()
    var businessAdaptor: BusinessAdaptor? = null
    var mHandler = Handler()
    var tv_cat_name: TextView? = null
    var constructioninfo: ArrayList<ConstructionModel> = ArrayList<ConstructionModel>()
    private var constructionAdaptor: ConstructorAdaptor? = null
    var type: String? = null
    var iv_nodata: ImageView? = null
    var loan_corosel : LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_filter)
        recyclerView = findViewById(R.id.rv_business_filter)
        tv_cat_name = findViewById(R.id.tv_cat_name)
        iv_nodata = findViewById(R.id.iv_nodata)
        loan_corosel = findViewById(R.id.loan_corosel)

        var iv_nav_view = findViewById<ImageView>(R.id.iv_nav_view)
        val bundle = intent.extras
        val type = bundle!!.getString("center")
        tv_cat_name?.setText(type)
        fetchdata(type)

        iv_nav_view.setOnClickListener {
            finish()
        }
    }

    private fun fetchdata(cat: String?) {
        val productsinfo = FirebaseDatabase.getInstance().reference.child("constructionforyou").orderByChild(AppConstants.category).equalTo(cat)
        productsinfo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            if (userData?.get(AppConstants.Status).toString().equals(AppConstants.user)) {
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
                                        userData["description"].toString(),
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
                                iv_nodata?.visibility = View.GONE
                                loan_corosel?.visibility = View.VISIBLE
                            }
                        } catch (cce: ClassCastException) {

                        }
                    }

                    // Upcoming Event
                    constructionAdaptor = ConstructorAdaptor(constructioninfo, this@ConstructionFilter)
                    val elayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@ConstructionFilter, RecyclerView.VERTICAL, false)
                    recyclerView!!.layoutManager = GridLayoutManager(this@ConstructionFilter, 1)
                    recyclerView!!.itemAnimator = DefaultItemAnimator()
                    recyclerView!!.itemAnimator = DefaultItemAnimator()
                    mHandler.post { recyclerView!!.adapter = constructionAdaptor }
                } else {
                    if (constructioninfo.isEmpty()) {
                        iv_nodata?.visibility = View.VISIBLE
                    } else {
                        iv_nodata?.visibility = View.GONE
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


    private fun fetchbusiness(cat: String?) {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("constructionforyou").orderByChild(AppConstants.category).equalTo(cat)
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            if (userData!![AppConstants.category].toString() == cat) {
                                businesslist.add(
                                    BusinessModel(
                                        userData[AppConstants.pid].toString(),
                                        userData[AppConstants.date].toString(),
                                        userData[AppConstants.time].toString(),
                                        userData["Businessname"].toString(),
                                        userData[AppConstants.products].toString(),
                                        userData[AppConstants.category].toString(),
                                        userData[AppConstants.description].toString(),
                                        userData[AppConstants.price].toString(),
                                        userData[AppConstants.location].toString(),
                                        userData[AppConstants.number].toString(),
                                        userData["owner"].toString(),
                                        userData["email"].toString(),
                                        userData["rating"].toString(),
                                        userData[AppConstants.image].toString(),
                                        userData[AppConstants.image2].toString(),
                                        userData[AppConstants.Status].toString(),
                                        userData["gst"].toString(),
                                        userData["from"].toString(),
                                        userData["productServicess"].toString(),
                                        userData["workingHrs"].toString()
                                    )
                                )
                            }
                        } catch (cce: ClassCastException) {

                        }
                    }
                    businessAdaptor = BusinessAdaptor(businesslist, this@ConstructionFilter)
                    recyadaptor(businesslist)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun recyadaptor(businesslist1: ArrayList<BusinessModel>) {
        val nlayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(this@ConstructionFilter, RecyclerView.VERTICAL, false)
        recyclerView!!.layoutManager = nlayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        mHandler.post { recyclerView!!.adapter = businessAdaptor }
        businessAdaptor!!.notifyItemRangeInserted(0, businesslist1.size)
    }
}