package com.sbd.gbd.Activities.Travels

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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
import com.sbd.gbd.Activities.Admin.Admin_travels
import com.sbd.gbd.Activities.BasicActivitys.OtpLoginActivity
import com.sbd.gbd.Utilitys.PreferenceManager
import com.sbd.gbd.Utilitys.UtilityMethods
import com.sbd.gbd.databinding.ActivityTravelsactivityBinding
import kotlinx.coroutines.launch

class Travelsactivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityTravelsactivityBinding
    var vehicleinfo: ArrayList<TravelsModel> = ArrayList()
    var vehicleinfofilter: ArrayList<TravelsModel> = ArrayList()
    private var travelsAdaptor: TravelsAdaptor? = null
    var mHandler = Handler()
    var type: String? = null
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityTravelsactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initilize()
        lifecycleScope.launch {
            fetchdata()
        }
    }

    private fun initilize() {
        preferenceManager= PreferenceManager(this);
        binding.llCar.setOnClickListener(this)
        binding.llBus.setOnClickListener(this)
        binding.llTransports.setOnClickListener(this)
        binding.llTempotravel.setOnClickListener(this)
        binding.llAuto.setOnClickListener(this)
        binding.llHeavyVehicles.setOnClickListener(this)

        binding.ivNavView.setOnClickListener {
            finish()
        }

        binding.btnAddVehicle.setOnClickListener {
            if (preferenceManager.getLoginState()) {
                val intent = Intent(this, Admin_travels::class.java)
                startActivity(intent)
            } else {
                UtilityMethods.showToast(this,"Please Login to process")
                val intent = Intent(this, OtpLoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun fetchdata() {
        val productsinfo = FirebaseDatabase.getInstance().reference.child("travelsforyou").orderByChild(AppConstants.Status).equalTo(AppConstants.user)
        vehicleinfo.clear()
        productsinfo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<*, *>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<*, *>?
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
                        } catch (_: ClassCastException) {

                        }
                    }

                    travelsAdaptor = TravelsAdaptor(vehicleinfo, this@Travelsactivity)
                    LinearLayoutManager(this@Travelsactivity, RecyclerView.VERTICAL, false)
                    binding.rvTravels.layoutManager = GridLayoutManager(this@Travelsactivity, 1)
                    binding.rvTravels.itemAnimator = DefaultItemAnimator()
                    mHandler.post {
                        binding.rvTravels.adapter = travelsAdaptor
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_car -> {
                vehicleinfofilter.clear()
                for (items in vehicleinfo ) {
                    if (items.category.equals("cab")){
                        vehicleinfofilter.add(items)
                    }
                }
                addtofilteradaptor()
            }

            R.id.ll_bus -> {
                vehicleinfofilter.clear()
                for (items in vehicleinfo ) {
                    if (items.category.equals("Bus")){
                        vehicleinfofilter.add(items)
                    }
                }
                addtofilteradaptor()
            }

            R.id.ll_tempotravel -> {
                vehicleinfofilter.clear()
                for (items in vehicleinfo ) {
                    if (items.category.equals("Tempo Traveler")){
                        vehicleinfofilter.add(items)
                    }
                }
                addtofilteradaptor()
            }

            R.id.ll_auto -> {
                vehicleinfofilter.clear()
                for (items in vehicleinfo ) {
                    if (items.category.equals("Auto")){
                        vehicleinfofilter.add(items)
                    }
                }
                addtofilteradaptor()
            }

            R.id.ll_transports -> {
                vehicleinfofilter.clear()
                for (items in vehicleinfo ) {
                    if (items.category.equals("goods vehicles")){
                        vehicleinfofilter.add(items)
                    }
                }
                addtofilteradaptor()
            }

            R.id.ll_heavy_vehicles -> {
                vehicleinfofilter.clear()
                for (items in vehicleinfo ) {
                    if (items.category.equals("Heavy Vehicles")){
                        vehicleinfofilter.add(items)
                    }
                }
                addtofilteradaptor()
            }
        }
    }

    private fun addtofilteradaptor() {
       travelsAdaptor = TravelsAdaptor(vehicleinfofilter, this@Travelsactivity)
        LinearLayoutManager(this@Travelsactivity, RecyclerView.VERTICAL, false)
        binding.rvTravels.layoutManager = GridLayoutManager(this@Travelsactivity, 1)
        binding.rvTravels.itemAnimator = DefaultItemAnimator()
        mHandler.post { binding.rvTravels.adapter = travelsAdaptor }
    }
}