package com.sbd.gbd.Activities.Admin

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.UtilityMethods
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Admin_ads_dashboard : AppCompatActivity() {
    private var CategoryName: String? = null
    private var Description: String? = null
    private var Price: String? = null
    private var Pname: String? = null
    private var postedBy: String? = null
    private var katha: String? = null
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    private var propertysize: String? = null
    private var location: String? = null
    private var number: String? = null
    private var ownerName: String? = null
    private var facing: String? = null
    private var approvedBy: String? = null
    private var InputProductName: EditText? = null
    private val Inputtype: EditText? = null
    private var InputProductDescription: EditText? = null
    private var ads_ownerShip: EditText? = null
    private var ownerORagent: String? = null
    private var ads_facing: EditText? = null
    private var ads_approved_by: EditText? = null
    private var InputProductPrice: EditText? = null
    private var et_size: EditText? = null
    private var et_location: EditText? = null
    private var et_number: EditText? = null
    private var edt_katha : EditText? = null
    private var et_verified: EditText? = null
    private var et_text1: EditText? = null
    private var et_text2: EditText? = null
    private var et_text3: EditText? = null
    private var et_text4: EditText? = null
    private var propertyOwner: EditText? = null
    private var ImageUri: Uri? = null
    private var productRandomKey: String? = null
    private var downloadImageUrl: String? = null
    private var MainimageUrl: String? = null
    private var ProductImagesRef: StorageReference? = null
    private var ProductsRef: DatabaseReference? = null
    private var rbButton: RadioGroup? = null
    private var loadingBar: ProgressDialog? = null
    private var propertyType: Spinner? = null
    var fileNameList: ArrayList<String> = ArrayList<String>()
    var fileDoneList: ArrayList<String> = ArrayList<String>()
    var categoryList: ArrayList<String> = ArrayList<String>()
    private var ads_name: EditText? = null
    private var arrayAdapter: ArrayAdapter<*>? = null
    var backBtn: ImageView? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_ads)
        val propertyPage = intent.getStringExtra("page")
        //CategoryName = "cqat";
        if (propertyPage == "2") {
            ProductImagesRef = FirebaseStorage.getInstance().reference.child("Product Images")
            ProductsRef = FirebaseDatabase.getInstance().reference.child("Products")
        } else {
            ProductImagesRef = FirebaseStorage.getInstance().reference.child("ads")
            ProductsRef = FirebaseDatabase.getInstance().reference.child("adsforyou")
        }
        val btn_corosel = findViewById<ImageView>(R.id.select_corosel_image)
        val add_new_corosel = findViewById<Button>(R.id.add_new_ads)
        InputProductName = findViewById<View>(R.id.ads_name) as EditText
        InputProductDescription = findViewById<View>(R.id.ads_description) as EditText
        InputProductPrice = findViewById<View>(R.id.ads_price_admin) as EditText
        ads_facing = findViewById(R.id.ads_facing)
        edt_katha = findViewById(R.id.edt_property_katha)

        ads_approved_by = findViewById(R.id.ads_approved_by)
        ads_approved_by?.visibility = View.GONE
        rbButton = findViewById(R.id.rb_data)
        ads_name = findViewById(R.id.ads_name)
        et_size = findViewById(R.id.ads_size)
        et_text1 = findViewById(R.id.ads_text1)
        et_text2 = findViewById(R.id.ads_text2)
        et_text3 = findViewById(R.id.ads_text3)
        et_text4 = findViewById(R.id.ads_text4)
        backBtn = findViewById(R.id.iv_nav_view)
        et_location = findViewById(R.id.ads_location_admin)
        et_number = findViewById(R.id.edt_contact_number)
        et_verified = findViewById(R.id.ads_verify_or_nt)
        propertyOwner = findViewById(R.id.edt_owner_name)
        loadingBar = ProgressDialog(this)
        propertyType = findViewById(R.id.sp_property_type)
        btn_corosel.setOnClickListener { OpenGallery() }
        backBtn?.setOnClickListener(View.OnClickListener { finish() })
        add_new_corosel.setOnClickListener { ValidateProductData() }
        postedBy = "owner"
        initilize()
    }


    private fun initilize() {

        rbButton?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { arg0, id ->
            when (id) {
                R.id.rb_owner -> {
                    postedBy = "owner"
                }

                R.id.rb_agent -> {
                    postedBy = "agent"
                }
            }

        })

        propertyType?.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2>0){
                    val propertyArray = resources.getStringArray(R.array.property_type)
                    CategoryName = propertyArray[p2]
                }
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

    private fun ValidateProductData() {
        Description = InputProductDescription!!.text.toString()
        Price = InputProductPrice!!.text.toString()
        Pname = InputProductName!!.text.toString()
        propertysize = et_size!!.text.toString()
        location = et_location!!.text.toString()
        number = et_number!!.text.toString()
        facing = ads_facing?.text.toString()
        ownerName = propertyOwner?.text.toString()
        approvedBy = ads_approved_by?.text.toString()
        katha = edt_katha?.text.toString()
        if (TextUtils.isEmpty(downloadImageUrl)) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(number) && number.toString().length != 10) {
            Toast.makeText(this, "Please enter contact number...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Pname)) {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(CategoryName)) {
            Toast.makeText(this, "Please enter category", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(facing)) {
            Toast.makeText(this, "Please enter facing", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(postedBy)) {
            Toast.makeText(this, "Please Select whether you owner or agent", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(katha)) {
            Toast.makeText(this, "Please enter katha type", Toast.LENGTH_SHORT).show()
        }  else {
            SaveProductInfoToDatabase()
        }
    }

    private fun StoreProductInformation(data: Intent) {

        /*loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();*/
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
        fileNameList.add(fileName.toString())
        fileDoneList.add("Uploading")
        productRandomKey = UtilityMethods.getCurrentTimeDate()
        val filePath = ProductImagesRef!!.child(uri!!.lastPathSegment + productRandomKey + ".jpg")
        val uploadTask = filePath.putFile(uri)
        uploadTask.addOnFailureListener { e ->
            val message = e.toString()
            Toast.makeText(this@Admin_ads_dashboard, "Error: $message", Toast.LENGTH_SHORT).show()
            loadingBar!!.dismiss()
        }.addOnSuccessListener {
            Toast.makeText(
                this@Admin_ads_dashboard,
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
                        Toast.makeText(
                            this@Admin_ads_dashboard,
                            "got the Product image Url Successfully...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun SaveProductInfoToDatabase() {
        val productMap = HashMap<String, Any?>()
        productMap[AppConstants.pid] = productRandomKey
        productMap[AppConstants.date] = UtilityMethods.getDate()
        productMap[AppConstants.time] = UtilityMethods.getTime()
        productMap[AppConstants.description] = Description
        productMap[AppConstants.image2] = downloadImageUrl
        productMap[AppConstants.image] = MainimageUrl
        productMap[AppConstants.category] = CategoryName
        productMap[AppConstants.type] = CategoryName
        productMap[AppConstants.price] = Price
        productMap[AppConstants.pname] = Pname
        productMap[AppConstants.katha] = katha
        productMap[AppConstants.propertysize] = propertysize
        productMap[AppConstants.location] = location
        productMap[AppConstants.number] = number
        productMap[AppConstants.verified] = "1"
        productMap[AppConstants.postedOn] = UtilityMethods.getDate()
        productMap[AppConstants.postedBy] = postedBy
        productMap[AppConstants.facing] = facing
        productMap[AppConstants.ownership] = ownerName
        productMap[AppConstants.payment] = ""
        productMap[AppConstants.text1] = et_text1!!.text.toString()
        productMap[AppConstants.text2] = et_text2!!.text.toString()
        productMap[AppConstants.text3] = et_text3!!.text.toString()
        productMap[AppConstants.text4] = et_text4!!.text.toString()
        productMap[AppConstants.Status] = "1"
        ProductsRef!!.child(productRandomKey!!).updateChildren(productMap + AppConstants.profileinfoadd(this))
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    loadingBar!!.dismiss()
                    UtilityMethods.showToast(
                        this@Admin_ads_dashboard, "Property is added successfully..")

                } else {
                    loadingBar!!.dismiss()
                    val message = task.exception.toString()
                    UtilityMethods.showToast(this@Admin_ads_dashboard, "Error: $message")
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
                cursor?.close()
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