package com.sbd.gbd.Activities.Admin

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
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

class AdminBusinessCategorys : AppCompatActivity() {
    private var category: String? = null
    private var subcategory: String? = null
    private var sp_category: Spinner? = null
    private var et_subcategory: EditText? = null
    private var productRandomKey: String? = null
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    private var ProductsRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null
    var fileNameList: ArrayList<String> = ArrayList<String>()
    var fileDoneList: ArrayList<String> = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_business_categorys)
        ProductsRef = FirebaseDatabase.getInstance().reference.child("BusinessListing_category")
        val add_new_category = findViewById<Button>(R.id.add_category)
        sp_category = findViewById(R.id.sp_category_name)
        et_subcategory = findViewById(R.id.et_subcategory)
        loadingBar = ProgressDialog(this)
        add_new_category.setOnClickListener { ValidateProductData() }

        sp_category?.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            var corosel = resources.getStringArray(R.array.businessCat)
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2>0){
                   category =  corosel[p2]
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun ValidateProductData() {
        subcategory = et_subcategory!!.text.toString()
        if (category.equals("Select Category")) {
            Toast.makeText(this, "Please Select Business Category", Toast.LENGTH_SHORT).show()
        } else {
            if (TextUtils.isEmpty(category)) {
                Toast.makeText(this, "Please Select Business Category", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(subcategory)) {
                Toast.makeText(this, "Please Enter business Type", Toast.LENGTH_SHORT).show()
            }
            else {
                SaveProductInfoToDatabase()
            }
        }
    }

    private fun SaveProductInfoToDatabase() {
        val productMap = HashMap<String, Any?>()
        productMap[AppConstants.pid] = UtilityMethods.getCurrentTimeDate()
        productMap[AppConstants.date] = UtilityMethods.getDate()
        productMap[AppConstants.time] = UtilityMethods.getTime()
        productMap[AppConstants.category] = category
        productMap["subcategory"] = subcategory
        productMap[AppConstants.Status] = "1"
        ProductsRef?.child(UtilityMethods.getCurrentTimeDate()!!)?.updateChildren(productMap)
            ?.addOnCompleteListener(object : OnCompleteListener<Void?> {
                override fun onComplete(task: Task<Void?>) {
                    if (task.isSuccessful) {
                        loadingBar!!.dismiss()
                        Toast.makeText(
                            this@AdminBusinessCategorys,
                            "Your Business Listed Successfully, Please wait for Approval",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        loadingBar!!.dismiss()
                        val message = task.exception.toString()
                        Toast.makeText(
                            this@AdminBusinessCategorys,
                            "Error: $message",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }
}