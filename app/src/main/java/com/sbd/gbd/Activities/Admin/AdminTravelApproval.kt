package com.sbd.gbd.Activities.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sbd.gbd.Adapters.TravelsAdaptor
import com.sbd.gbd.Model.TravelsModel
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.databinding.ActivityAdminTravelApprovalBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminTravelApproval : AppCompatActivity() {

    private lateinit var binding: ActivityAdminTravelApprovalBinding
    var vehicleinfo: ArrayList<TravelsModel> = ArrayList<TravelsModel>()
    private var travelsAdaptor: TravelsAdaptor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminTravelApprovalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchdata()
    }

    private fun fetchdata() {
        val productsinfo = FirebaseDatabase.getInstance().reference.child("travelsforyou")
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
                    travelsAdaptor = TravelsAdaptor(vehicleinfo, this@AdminTravelApproval)
                    val elayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@AdminTravelApproval, RecyclerView.VERTICAL, false)
                    /* rv_travels?.layoutManager = GridLayoutManager(this@AdminTravelApproval, 1)
                     rv_travels?.itemAnimator = DefaultItemAnimator()
                     mHandler.post {
                         rv_travels?.adapter = travelsAdaptor
                     }*/
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}