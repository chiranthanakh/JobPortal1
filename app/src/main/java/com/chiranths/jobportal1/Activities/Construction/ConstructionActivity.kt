package com.chiranths.jobportal1.Activities.Construction

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chiranths.jobportal1.Activities.BasicActivitys.SearchActivity
import com.chiranths.jobportal1.Adapters.BusinessCategoryAdaptor
import com.chiranths.jobportal1.Adapters.ConstructorAdaptor
import com.chiranths.jobportal1.Model.Categorymmodel
import com.chiranths.jobportal1.Model.ConstructionModel
import com.chiranths.jobportal1.R
import com.chiranths.jobportal1.Utilitys.AppConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ConstructionActivity : AppCompatActivity() {
    var backarrow : ImageView? = null
    var cv_plumbers: CardView? = null
    var Name: String? = null
    var category: String? = null
    var saveCurrentDate: String? = null
    var saveCurrentTime: String? = null
    var discription: String? = null
    var vehicleNumber: String? = null
    var llsearch: LinearLayout? = null
    var edt_const_search : EditText? = null
    var rv_construction: RecyclerView? = null
    private var rv_category: RecyclerView? = null
    var businesscatAdaptor: BusinessCategoryAdaptor? = null
    var bundle = Bundle()

    var constructioninfo: ArrayList<ConstructionModel?> = ArrayList<ConstructionModel?>()
    var categorylists = java.util.ArrayList<Categorymmodel>()

    private var constructionAdaptor: ConstructorAdaptor? = null
    var mHandler = Handler()
    var type: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_construction)
        initilize()
        fetchdata()
        fetchbusinessCategorys()
    }

    private fun initilize() {
        rv_construction = findViewById(R.id.rv_constructions_relates)
        rv_category = findViewById(R.id.id_gridview_construction)
        backarrow = findViewById(R.id.iv_const_backarrow)
        llsearch = findViewById(R.id.ll_search_business)
        edt_const_search = findViewById(R.id.edt_const_search)

        llsearch?.setOnClickListener {
            val intent = Intent(this@ConstructionActivity, SearchActivity::class.java)
            bundle.putString("searchtype", "const")
            intent.putExtras(bundle)
            startActivity(intent)
        }
        edt_const_search?.setOnClickListener {
            val intent = Intent(this@ConstructionActivity, SearchActivity::class.java)
            bundle.putString("searchtype", "const")
            intent.putExtras(bundle)
            startActivity(intent)
        }
        backarrow?.setOnClickListener {
            this.finish()
        }
    }

    private fun fetchbusinessCategorys() {
        val categorylist =
                FirebaseDatabase.getInstance().reference.child("constListing_category")
        categorylist.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            categorylists.add(
                                    Categorymmodel(
                                            userData!![AppConstants.pid].toString(),
                                            userData[AppConstants.image].toString(),
                                            userData[AppConstants.category].toString(),
                                            userData["subcategory"].toString()
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
                    //RecyclerView.LayoutManager nlayoutManager1 = new LinearLayoutManager(BusinessActivity.this, RecyclerView.HORIZONTAL, false);
                    val nlayoutManager1 = GridLayoutManager(this@ConstructionActivity, 4)
                    rv_category?.setLayoutManager(nlayoutManager1)
                    rv_category?.setItemAnimator(DefaultItemAnimator())
                    businesscatAdaptor =
                            BusinessCategoryAdaptor(categorylists, this@ConstructionActivity)
                    mHandler.post { rv_category?.setAdapter(businesscatAdaptor) }
                    // businesscatAdaptor.notifyItemRangeInserted(0, businesslist.size)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchdata() {
        val productsinfo = FirebaseDatabase.getInstance().reference.child("constructionforyou")
        productsinfo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            if (userData?.get(AppConstants.Status).toString().equals("2")) {
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
                            }
                        } catch (cce: ClassCastException) {
                            try {
                                val mString = dataMap[key].toString()
                                //addTextToView(mString);
                            } catch (cce2: ClassCastException) {
                            }
                        }
                    }

                    // Upcoming Event
                    constructionAdaptor = ConstructorAdaptor(constructioninfo, this@ConstructionActivity)
                    val elayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@ConstructionActivity, RecyclerView.VERTICAL, false)
                    rv_construction!!.layoutManager = GridLayoutManager(this@ConstructionActivity, 1)
                    rv_construction!!.itemAnimator = DefaultItemAnimator()
                    rv_construction!!.itemAnimator = DefaultItemAnimator()
                    mHandler.post { rv_construction!!.adapter = constructionAdaptor }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
  }