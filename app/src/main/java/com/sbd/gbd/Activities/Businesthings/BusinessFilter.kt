package com.sbd.gbd.Activities.Businesthings

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
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

class BusinessFilter : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var businesslist = ArrayList<BusinessModel>()
    var businessAdaptor: BusinessAdaptor? = null
    var mHandler = Handler()
    var tv_cat_name: TextView? = null
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
        fetchbusiness(type)

        iv_nav_view.setOnClickListener {
            finish()
        }
    }

    private fun fetchbusiness(cat: String?) {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("BusinessListing").orderByChild(AppConstants.category).equalTo(cat)
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            if (userData!![AppConstants.Status].toString() == AppConstants.user) {
                                businesslist.add(
                                    BusinessModel(
                                        userData[AppConstants.pid].toString(),
                                        userData[AppConstants.date].toString(),
                                        userData[AppConstants.time].toString(),
                                        userData["Businessname"].toString(),
                                        userData["products"].toString(),
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
                                        userData["city"].toString(),
                                        userData["productServicess"].toString(),
                                        userData["workingHrs"].toString()
                                    )
                                )

                                iv_nodata?.visibility = View.GONE
                                loan_corosel?.visibility = View.VISIBLE

                            }
                        } catch (cce: ClassCastException) {

                        }
                    }
                    businessAdaptor = BusinessAdaptor(businesslist, this@BusinessFilter)
                    recyadaptor(businesslist)
                } else {

                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun recyadaptor(businesslist1: ArrayList<BusinessModel>) {
        val nlayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(this@BusinessFilter, RecyclerView.VERTICAL, false)
        recyclerView!!.layoutManager = nlayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        mHandler.post { recyclerView!!.adapter = businessAdaptor }
        businessAdaptor!!.notifyItemRangeInserted(0, businesslist1.size)
    }
}