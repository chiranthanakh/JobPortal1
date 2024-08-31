package com.sbd.gbd.Activities.Dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Activities.Admin.AdminLivingPlacess
import com.sbd.gbd.Activities.Admin.AdminPropertyApproval
import com.sbd.gbd.Activities.Admin.Admin_ads_dashboard
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.PreferenceManager
import com.sbd.gbd.Utilitys.UtilityMethods
import com.sbd.gbd.databinding.ActivityAdminAdsBinding
import com.sbd.gbd.databinding.ActivityLocationSelectBinding

class LocationSelectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationSelectBinding
    private var locationList : ArrayList<String> = ArrayList()
    private var locationMap = mutableMapOf<String, String>()
    private var districtList = mutableSetOf<String>()
    private var districtAdapter: ArrayAdapter<*>? = null
    private lateinit var preferenceManager: PreferenceManager
    private var selectedLocation : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceManager= PreferenceManager(this)
        val page = intent.getStringExtra("page")
        if (preferenceManager.getDistrict() != "" && page != "1") {
            selectedLocation?.let { preferenceManager.saveDistrict(it) }
            val intent = Intent(this@LocationSelectActivity, StartingActivity::class.java)
            startActivity(intent)
            finish()
        }
        getlocations()
        initilize()
    }

    private fun initilize() {
        binding.spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0) {
                    val list = districtList.toList()
                    selectedLocation = locationMap.get(list[p2])
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.llOk.setOnClickListener {
            if(selectedLocation != ""){
                selectedLocation?.let { preferenceManager.saveDistrict(it) }
                val intent = Intent(this@LocationSelectActivity, StartingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                UtilityMethods.showToast(this,"Please Select Location")
            }
        }
        binding.tvPropertySearch.setOnClickListener{
            if(selectedLocation != ""){
                selectedLocation?.let { preferenceManager.saveDistrict(it) }
                val intent = Intent(this@LocationSelectActivity, StartingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                UtilityMethods.showToast(this,"Please Select Location")
            }
        }
        binding.tvPropertyPost.setOnClickListener{
            if(selectedLocation != ""){
                selectedLocation?.let { preferenceManager.saveDistrict(it) }
                val intent = Intent(this@LocationSelectActivity, Admin_ads_dashboard::class.java)
                startActivity(intent)
                finish()
            } else {
                UtilityMethods.showToast(this,"Please Select Location")
            }
        }
        binding.tvPropertyRent.setOnClickListener{
            if(selectedLocation != ""){
                selectedLocation?.let { preferenceManager.saveDistrict(it) }
                val intent = Intent(this@LocationSelectActivity, AdminLivingPlacess::class.java)
                startActivity(intent)
                finish()
            } else {
                UtilityMethods.showToast(this,"Please Select Location")
            }
        }
    }

    private fun getlocations() {
        val myDataRef = FirebaseDatabase.getInstance().reference.child(AppConstants.locations)
        myDataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataMap = dataSnapshot.value as HashMap<*, *>?
                districtList.clear()
                districtList.add("Select District")
                for (key in dataMap?.keys!!) {
                    val data = dataMap[key]
                    try {
                        val userData = data as HashMap<*, *>?
                        districtList.add(userData?.get(AppConstants.district)?.toString() ?: "select District")
                        userData?.get("id")?.let {
                            locationMap.put(userData.get(AppConstants.district).toString(), it.toString())
                        }
                    } catch (_: Exception) {
                    }
                }
                districtAdapter = ArrayAdapter(
                    this@LocationSelectActivity,
                    android.R.layout.simple_spinner_item,
                    districtList.toList()
                )
                districtAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spDistrict.adapter = districtAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to read value: ${error.message}")
            }
        })
    }

}