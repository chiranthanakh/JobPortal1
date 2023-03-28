package com.chiranths.jobportal1.Activities.Construction

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.chiranths.jobportal1.Model.ConstructionModel
import com.chiranths.jobportal1.Adapters.ConstructorAdaptor
import android.os.Bundle
import com.chiranths.jobportal1.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.firebase.database.DatabaseError
import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.ImageView
import com.chiranths.jobportal1.Activities.BasicActivitys.TravelsListactivity
import com.chiranths.jobportal1.Activities.BasicActivitys.Travelsactivity
import java.lang.ClassCastException
import java.util.ArrayList
import java.util.HashMap

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
    var rv_construction: RecyclerView? = null
    var constructioninfo: ArrayList<ConstructionModel?> = ArrayList<ConstructionModel?>()
    private var constructionAdaptor: ConstructorAdaptor? = null
    var mHandler = Handler()
    var type: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_construction)
        initilize()
        fetchdata()
    }

    private fun initilize() {
        cv_contractors = findViewById(R.id.cv_contractors)
        cv_architects = findViewById(R.id.cv_architects)
        cv_interior_designers = findViewById(R.id.cv_interior_designers)
        cv_construction_meterials = findViewById(R.id.cv_construction_meterials)
        cv_hardware_welders = findViewById(R.id.cv_hardware_welders)
        cv_painters = findViewById(R.id.cv_carpenters)
        cv_carpenters = findViewById(R.id.cv_carpenters)
        cv_electricians = findViewById(R.id.cv_electricians)
        cv_plumbers = findViewById(R.id.cv_plumbers)
        cv_contractors?.setOnClickListener(this)
        cv_architects?.setOnClickListener(this)
        cv_interior_designers?.setOnClickListener(this)
        cv_construction_meterials?.setOnClickListener(this)
        cv_hardware_welders?.setOnClickListener(this)
        cv_painters?.setOnClickListener(this)
        cv_carpenters?.setOnClickListener(this)
        cv_electricians?.setOnClickListener(this)
        cv_plumbers?.setOnClickListener(this)
        rv_construction = findViewById(R.id.rv_constructions_relates)
        backarrow = findViewById(R.id.iv_const_backarrow)

        backarrow?.setOnClickListener {
            this.finish()
        }
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
                                    userData["number2"].toString(),
                                    userData["experience"].toString(),
                                    userData["servicess1"].toString(),
                                    userData["servicess2"].toString(),
                                    userData["servicess3"].toString(),
                                    userData["servicess4"].toString(),
                                    userData["discription"].toString(),
                                    userData["verified"].toString(),
                                    userData["image"].toString(),
                                    userData["image2"].toString()
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
                        ConstructorAdaptor(constructioninfo, this@ConstructionActivity)
                    val elayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@ConstructionActivity, RecyclerView.VERTICAL, false)
                    rv_construction!!.layoutManager =
                        GridLayoutManager(this@ConstructionActivity, 1)
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
            R.id.cv_contractors -> {
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
            }
        }
    }
}