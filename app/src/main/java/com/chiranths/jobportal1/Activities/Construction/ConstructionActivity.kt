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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ConstructionActivity : AppCompatActivity(), View.OnClickListener {
    var cv_contractors: CardView? = null
    var cv_architects: CardView? = null
    var cv_interior_designers: CardView? = null
    var cv_construction_meterials: CardView? = null
    var cv_hardware_welders: CardView? = null
    var cv_painters: CardView? = null
    var cv_carpenters: CardView? = null
    var cv_electricians: CardView? = null
    var backarrow : ImageView? = null
    var cv_plumbers: CardView? = null
    var Name: String? = null
    var category: String? = null
    var cost: String? = null
    var contactDetails: String? = null
    var contactDetails2: String? = null
    var experience: String? = null
    var service1: String? = null
    var service2: String? = null
    var service3: String? = null
    var service4: String? = null
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
                                    userData!!["pid"].toString(),
                                    userData["image"].toString(),
                                    userData["category"].toString(),
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
                            constructioninfo.add(
                                ConstructionModel(
                                    userData!!["pid"].toString(),
                                    userData["date"].toString(),
                                    userData["time"].toString(),
                                    userData["name"].toString(),
                                    userData["category"].toString(),
                                    userData["cost"].toString(),
                                    userData["number1"].toString(),
                                    userData["product_services"].toString(),
                                    userData["experience"].toString(),
                                    userData["servicess1"].toString(),
                                    userData["servicess2"].toString(),
                                    userData["servicess3"].toString(),
                                    userData["servicess4"].toString(),
                                    userData["discription"].toString(),
                                    userData["verified"].toString(),
                                    userData["image"].toString(),
                                    userData["image2"].toString(),
                                    userData["owner"].toString(),
                                    userData["address"].toString(),
                                    userData["status"].toString(),
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

    override fun onClick(view: View) {
        val bundle = Bundle()
        when (view.id) {
           /* R.id.cv_contractors -> {
                val intent = Intent(this, ConstructionListActivity::class.java)
                bundle.putString("type", "Contractors")
                intent.putExtras(bundle)
                startActivity(intent)
            }
            R.id.cv_architects -> {
                val intent2 = Intent(this, ConstructionListActivity::class.java)
                bundle.putString("type", "Architect")
                intent2.putExtras(bundle)
                startActivity(intent2)
            }
            R.id.cv_interior_designers -> {
                val intent3 = Intent(this, ConstructionListActivity::class.java)
                bundle.putString("type", "Interior Designer")
                intent3.putExtras(bundle)
                startActivity(intent3)
            }
            R.id.cv_construction_meterials -> {
                val intent4 = Intent(this, ConstructionListActivity::class.java)
                bundle.putString("type", "Construction Meterials")
                intent4.putExtras(bundle)
                startActivity(intent4)
            }
            R.id.cv_hardware_welders -> {
                val intent5 = Intent(this, ConstructionListActivity::class.java)
                bundle.putString("type", "Hardwares")
                intent5.putExtras(bundle)
                startActivity(intent5)
            }
            R.id.cv_painters -> {
                val intent6 = Intent(this, ConstructionListActivity::class.java)
                bundle.putString("type", "Painters")
                intent6.putExtras(bundle)
                startActivity(intent6)
            }
            R.id.cv_carpenters -> {
                val intent7 = Intent(this, ConstructionListActivity::class.java)
                bundle.putString("type", "Carpenters")
                intent7.putExtras(bundle)
                startActivity(intent7)
            }
            R.id.cv_electricians -> {
                val intent8 = Intent(this, ConstructionListActivity::class.java)
                bundle.putString("type", "Electrician")
                intent8.putExtras(bundle)
                startActivity(intent8)
            }
            R.id.cv_plumbers -> {
                val intent9 = Intent(this, ConstructionListActivity::class.java)
                bundle.putString("type", "Plumber")
                intent9.putExtras(bundle)
                startActivity(intent9)
            }*/
        }
    }
}