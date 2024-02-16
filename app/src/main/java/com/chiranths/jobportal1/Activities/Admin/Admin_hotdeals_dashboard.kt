package com.chiranths.jobportal1.Activities.Admin

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chiranths.jobportal1.R
import com.chiranths.jobportal1.Utilitys.AppConstants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Calendar

class Admin_hotdeals_dashboard : AppCompatActivity() {
    private var CategoryName: String? = null
    private var Description: String? = null
    private var Price: String? = null
    private var Pname: String? = null
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    private var propertysize: String? = null
    private var location: String? = null
    private var number: String? = null
    private var ownerName: String? = null
    private var timings: String? = null
    private var InputProductName: EditText? = null
    private var Inputtype: EditText? = null
    private var InputProductDescription: EditText? = null
    private var et_contact_person: EditText? = null
    private var et_timings: EditText? = null
    private var InputProductPrice: EditText? = null
    private var et_size: EditText? = null
    private var et_location: EditText? = null
    private var et_number: EditText? = null
    private var et_posted_by: EditText? = null
    private var et_hot_text1: EditText? = null
    private var et_hot_text2: EditText? = null
    private var ImageUri: Uri? = null
    private var productRandomKey: String? = null
    private var downloadImageUrl: String? = null
    private var MainimageUrl: String? = null
    var sp_placement: Spinner? = null
    private var ProductImagesRef: StorageReference? = null
    private var ProductsRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null
    private var postplace: String? = ""
    var fileNameList: ArrayList<String> = ArrayList<String>()
    var fileDoneList: ArrayList<String> = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_hotdeals)
        CategoryName = "hotdeals"
        ProductImagesRef = FirebaseStorage.getInstance().reference.child("hot")
        ProductsRef = FirebaseDatabase.getInstance().reference.child("hotforyou")
        val btn_corosel = findViewById<ImageView>(R.id.select_hot_image)
        val add_new_corosel = findViewById<Button>(R.id.add_new_hot)
        InputProductName = findViewById<View>(R.id.hot_name) as EditText
        Inputtype = findViewById<View>(R.id.hot_type_admin) as EditText
        InputProductDescription = findViewById<View>(R.id.hot_description) as EditText
        InputProductPrice = findViewById<View>(R.id.hot_price_admin) as EditText
        sp_placement = findViewById(R.id.sp_placement)
        et_size = findViewById(R.id.hot_size)
        et_timings = findViewById(R.id.et_timings)
        et_contact_person = findViewById(R.id.et_contact_person)
        et_location = findViewById(R.id.hot_location_admin)
        et_number = findViewById(R.id.contact_number3)
        et_posted_by = findViewById(R.id.et_owner_broker)
        loadingBar = ProgressDialog(this)
        et_hot_text1 = findViewById(R.id.et_hot_text1)
        et_hot_text2 = findViewById(R.id.et_hot_text2)
        btn_corosel.setOnClickListener { OpenGallery() }
        add_new_corosel.setOnClickListener { ValidateProductData() }

        sp_placement?.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            val placement = resources.getStringArray(R.array.placement)
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                postplace = placement.get(p2)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun OpenGallery() {
        val galleryIntent = Intent()
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT)
        galleryIntent.setType("image/*")
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(galleryIntent, GalleryPick)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.data
            //InputProductImage.setImageURI(ImageUri);
            StoreProductInformation(data)
        }
    }

    private fun StoreProductInformation(data: Intent) {
        downloadImageUrl = ""

        // If the user selected only one image
        if (data.data != null) {
            val uri = data.data
            uploadTostorage(data, uri)
        } else if (data.clipData != null) {
            val clipData = data.clipData
            for (i in 0 until clipData!!.itemCount) {
                val uri = clipData.getItemAt(i).uri
                uploadTostorage(data, uri)
            }
        }
    }

    private fun uploadTostorage(data: Intent, uri: Uri?) {
        val fileName = getFileName(uri)
        fileNameList.add(fileName!!)
        fileDoneList.add("Uploading")
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("MMM dd")
        saveCurrentDate = currentDate.format(calendar.time)
        val currentTime = SimpleDateFormat("HH:mm a")
        saveCurrentTime = currentTime.format(calendar.time)
        productRandomKey = saveCurrentDate + saveCurrentTime
        val filePath = ProductImagesRef!!.child(uri!!.lastPathSegment + productRandomKey + ".jpg")
        val uploadTask = filePath.putFile(uri)
        uploadTask.addOnFailureListener { e ->
            val message = e.toString()
            Toast.makeText(this@Admin_hotdeals_dashboard, "Error: $message", Toast.LENGTH_SHORT)
                .show()
            loadingBar!!.dismiss()
        }.addOnSuccessListener {
            Toast.makeText(
                this@Admin_hotdeals_dashboard,
                "Product Image uploaded Successfully...",
                Toast.LENGTH_SHORT
            ).show()
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                filePath.downloadUrl
            }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (downloadImageUrl == "") {
                            downloadImageUrl = task.result.toString()
                            MainimageUrl = task.result.toString()
                        } else {
                            downloadImageUrl = downloadImageUrl + "---" + task.result.toString()
                        }
                        println("url2---$downloadImageUrl")
                        Toast.makeText(
                            this@Admin_hotdeals_dashboard,
                            "got the Product image Url Successfully...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun ValidateProductData() {
        Description = InputProductDescription!!.text.toString()
        Price = InputProductPrice!!.text.toString()
        Pname = InputProductName!!.text.toString()
        propertysize = et_size!!.text.toString()
        location = et_location!!.text.toString()
        number = et_number!!.text.toString()
        timings = et_timings!!.text.toString()
        ownerName = et_contact_person!!.text.toString()
        if (TextUtils.isEmpty(downloadImageUrl)) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Pname)) {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(ownerName)) {
            Toast.makeText(this, "Please write contact person name...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(timings)) {
            Toast.makeText(this, "Please write contact timings...", Toast.LENGTH_SHORT).show()
        } else {
            SaveProductInfoToDatabase()
        }
    }

    private fun SaveProductInfoToDatabase() {
        if(postplace == AppConstants.carousel) {
            ProductImagesRef = FirebaseStorage.getInstance().reference.child("Corosel")
            ProductsRef = FirebaseDatabase.getInstance().reference.child("Corosels")
            CategoryName = "hotdeals"
        }
        val productMap = HashMap<String, Any?>()
        productMap[AppConstants.pid] = productRandomKey
        productMap[AppConstants.date] = saveCurrentDate
        productMap[AppConstants.time] = saveCurrentTime
        productMap[AppConstants.description] = Description
        productMap[AppConstants.image2] = downloadImageUrl
        productMap[AppConstants.image] = MainimageUrl
        productMap[AppConstants.category] = CategoryName
        productMap[AppConstants.price] = Price
        productMap[AppConstants.pname] = Pname
        productMap["Approval"] = 1
        productMap[AppConstants.propertysize] = propertysize
        productMap[AppConstants.location] = location
        productMap[AppConstants.number] = number
        productMap["timings"] = timings
        productMap["ownerName"] = ownerName
        productMap[AppConstants.type] = Inputtype!!.text.toString()
        productMap[AppConstants.postedBy] = et_posted_by!!.text.toString()
        productMap[AppConstants.text1] = et_hot_text1!!.text.toString()
        productMap[AppConstants.text2] = et_hot_text2!!.text.toString()
        productMap[AppConstants.Status] = "1"
        ProductsRef!!.child(productRandomKey!!).updateChildren(productMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Intent intent = new Intent(AdminAddNewProductActivity.this, .class);
                    //startActivity(intent);
                    loadingBar!!.dismiss()
                    Toast.makeText(
                        this@Admin_hotdeals_dashboard,
                        "Product is added successfully..",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loadingBar!!.dismiss()
                    val message = task.exception.toString()
                    Toast.makeText(
                        this@Admin_hotdeals_dashboard,
                        "Error: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    @SuppressLint("Range")
    fun getFileName(uri: Uri?): String? {
        var result: String? = null
        if (uri!!.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    companion object {
        private const val GalleryPick = 1
    }
}