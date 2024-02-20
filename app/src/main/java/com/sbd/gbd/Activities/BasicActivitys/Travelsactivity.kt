package com.sbd.gbd.Activities.BasicActivitys

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sbd.gbd.Adapters.TravelsAdaptor
import com.sbd.gbd.Model.TravelsModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Travelsactivity : AppCompatActivity(), View.OnClickListener {
    var ll_car: LinearLayout? = null
    var ll_tt: LinearLayout? = null
    var ll_bus: LinearLayout? = null
    var ll_auto: LinearLayout? = null
    var iv_nav_view: ImageView? = null
    var ll_transport: LinearLayout? = null
    var ll_heavyvehicles: LinearLayout? = null
    var vehicleinfo: ArrayList<TravelsModel> = ArrayList<TravelsModel>()
    var vehicleinfofilter: ArrayList<TravelsModel> = ArrayList<TravelsModel>()

    private var travelsAdaptor: TravelsAdaptor? = null
    var rv_travels: RecyclerView? = null
    var mHandler = Handler()
    var type: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travelsactivity)
        initilize()
        fetchdata()
    }

    private fun initilize() {
        ll_car = findViewById(R.id.ll_car)
        ll_bus = findViewById(R.id.ll_bus)
        ll_tt = findViewById(R.id.ll_tempotravel)
        ll_auto = findViewById(R.id.ll_auto)
        ll_transport = findViewById(R.id.ll_transports)
        ll_heavyvehicles = findViewById(R.id.ll_heavy_vehicles)
        iv_nav_view = findViewById(R.id.iv_nav_view)
        ll_car?.setOnClickListener(this)
        ll_bus?.setOnClickListener(this)
        ll_transport?.setOnClickListener(this)
        ll_tt?.setOnClickListener(this)
        ll_auto?.setOnClickListener(this)
        ll_heavyvehicles?.setOnClickListener(this)
        rv_travels = findViewById(R.id.rv_travels)

        iv_nav_view?.setOnClickListener {
            finish()
        }
    }

    private fun fetchdata() {
        val productsinfo = FirebaseDatabase.getInstance().reference.child("travelsforyou").orderByChild(AppConstants.Status).equalTo(AppConstants.user)
        vehicleinfo.clear()
        productsinfo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            vehicleinfo.add(
                                TravelsModel(
                                    userData!![AppConstants.pid].toString(),
                                    userData[AppConstants.date].toString(),
                                    userData[AppConstants.time].toString(),
                                    userData["vehiclename"].toString(),
                                    userData[AppConstants.category].toString(),
                                    userData["vehiclenumber"].toString(),
                                    userData["costperkm"].toString(),
                                    userData["contactnumber"].toString(),
                                    userData["ownerNmae"].toString(),
                                    userData[AppConstants.verified].toString(),
                                    userData[AppConstants.description].toString(),
                                    userData[AppConstants.image].toString(),
                                    userData[AppConstants.image2].toString(),
                                    userData["model"].toString(),
                                    userData[AppConstants.Status].toString()
                                )
                            )
                        } catch (cce: ClassCastException) {

                        }
                    }


                    // Upcoming Event
                    travelsAdaptor = TravelsAdaptor(vehicleinfo, this@Travelsactivity)
                    val elayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@Travelsactivity, RecyclerView.VERTICAL, false)
                    rv_travels?.layoutManager = GridLayoutManager(this@Travelsactivity, 1)
                    rv_travels?.itemAnimator = DefaultItemAnimator()
                    mHandler.post {
                        rv_travels?.adapter = travelsAdaptor
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onClick(view: View) {
        val bundle = Bundle()
        when (view.id) {
            R.id.ll_car -> {
                vehicleinfofilter.clear()
                for (items in vehicleinfo ) {
                    if (items?.category.equals("cab")){
                        vehicleinfofilter.add(items)
                    }
                }
                addtofilteradaptor()
            }

            R.id.ll_bus -> {
                vehicleinfofilter.clear()
                for (items in vehicleinfo ) {
                    if (items?.category.equals("Bus")){
                        vehicleinfofilter.add(items)
                    }
                }
                addtofilteradaptor()
            }

            R.id.ll_tempotravel -> {
                vehicleinfofilter.clear()
                for (items in vehicleinfo ) {
                    if (items?.category.equals("Tempo Traveler")){
                        vehicleinfofilter.add(items)
                    }
                }
                addtofilteradaptor()
            }

            R.id.ll_auto -> {
                vehicleinfofilter.clear()
                for (items in vehicleinfo ) {
                    if (items?.category.equals("Auto")){
                        vehicleinfofilter.add(items)
                    }
                }
                addtofilteradaptor()
            }

            R.id.ll_transports -> {
                vehicleinfofilter.clear()
                for (items in vehicleinfo ) {
                    if (items?.category.equals("goods vehicles")){
                        vehicleinfofilter.add(items)
                    }
                }
                addtofilteradaptor()
            }

            R.id.ll_heavy_vehicles -> {
                vehicleinfofilter.clear()
                for (items in vehicleinfo ) {
                    if (items?.category.equals("Heavy Vehicles")){
                        vehicleinfofilter.add(items)
                    }
                }
                addtofilteradaptor()
            }
        }
    }

    private fun addtofilteradaptor() {
        vehicleinfofilter.clear()
       travelsAdaptor = TravelsAdaptor(vehicleinfofilter, this@Travelsactivity)
        val elayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(this@Travelsactivity, RecyclerView.VERTICAL, false)
        rv_travels?.layoutManager = GridLayoutManager(this@Travelsactivity, 1)
        rv_travels?.itemAnimator = DefaultItemAnimator()
        mHandler.post { rv_travels?.adapter = travelsAdaptor }
    }
}