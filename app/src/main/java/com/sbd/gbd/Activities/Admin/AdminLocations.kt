package com.sbd.gbd.Activities.Admin

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.UtilityMethods

class AdminLocations : AppCompatActivity() {


    private var et_district: EditText? = null
    private var et_taluk: EditText? = null
    private var et_location_id: EditText? = null
    var district : String? = null
    var taluk : String? = null
    var locationId : String? = null

    private var ProductsRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null
    var fileNameList: ArrayList<String> = ArrayList<String>()
    var fileDoneList: ArrayList<String> = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_locations)
        ProductsRef = FirebaseDatabase.getInstance().reference.child("Locations")
        val add_new_category = findViewById<Button>(R.id.add_category)
        et_district = findViewById(R.id.et_district)
        et_taluk = findViewById(R.id.et_taluk)
        et_location_id = findViewById(R.id.et_location_id)

        loadingBar = ProgressDialog(this)
        add_new_category.setOnClickListener { ValidateProductData() }

    }

    private fun ValidateProductData() {
        district = et_district?.text.toString()
        taluk = et_taluk?.text.toString()
        locationId = et_location_id?.text.toString()

        if (district == null) {
            Toast.makeText(this, "Enter District", Toast.LENGTH_SHORT).show()
        } else if (taluk == null) {
            Toast.makeText(this, "Enter Taluk", Toast.LENGTH_SHORT).show()
        } else if (locationId == null) {
            Toast.makeText(this, "Enter Location Id", Toast.LENGTH_SHORT).show()
        } else {
            SaveProductInfoToDatabase()
        }
    }

    private fun SaveProductInfoToDatabase() {
        val productMap = HashMap<String, Any?>()
        productMap[AppConstants.pid] = UtilityMethods.getCurrentTimeDate()
        productMap[AppConstants.date] = UtilityMethods.getDate()
        productMap[AppConstants.time] = UtilityMethods.getTime()
        productMap[AppConstants.district] = district
        productMap[AppConstants.taluk] = taluk
        productMap["id"] = locationId
        ProductsRef?.child(UtilityMethods.getCurrentTimeDate()!!)?.updateChildren(productMap)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loadingBar!!.dismiss()
                    Toast.makeText(
                        this@AdminLocations,
                        "Location Added Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    loadingBar!!.dismiss()
                    val message = task.exception.toString()
                    Log.d("fnffh",message)
                    Toast.makeText(
                        this@AdminLocations,
                        "Error: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}