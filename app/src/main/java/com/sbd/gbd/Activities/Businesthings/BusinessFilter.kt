package com.sbd.gbd.Activities.Businesthings

import android.os.Bundle
import android.os.Handler
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
    var filterbusinesslist = ArrayList<BusinessModel>()
    var businessAdaptor: BusinessAdaptor? = null
    var mHandler = Handler()
    var tv_cat_name: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_filter)
        recyclerView = findViewById(R.id.rv_business_filter)
        tv_cat_name = findViewById(R.id.tv_cat_name)
        val bundle = intent.extras
        val type = bundle!!.getString("center")
        tv_cat_name?.setText(type)
        fetchbusiness(type)
    }

    private fun fetchbusiness(cat: String?) {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("BusinessListing").orderByChild(AppConstants.Status).equalTo(AppConstants.user)
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
                                        userData["productServicess"].toString(),
                                        userData["workingHrs"].toString()
                                    )
                                )
                            }
                        } catch (cce: ClassCastException) {
                            try {
                                val mString = dataMap[key].toString()
                                //addTextToView(mString);
                            } catch (cce2: ClassCastException) {
                            }
                        }
                    }
                    businessAdaptor = BusinessAdaptor(businesslist, this@BusinessFilter)
                    recyadaptor(businesslist)
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