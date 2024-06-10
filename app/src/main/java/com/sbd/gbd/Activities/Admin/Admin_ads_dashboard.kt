package com.sbd.gbd.Activities.Admin

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.cast.framework.media.ImagePicker
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.UtilityMethods
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sbd.gbd.Adapters.ImageAdaptor

class Admin_ads_dashboard : AppCompatActivity() {
    private var CategoryName: String? = null
    private var Description: String? = null
    private var Price: String? = null
    private var Taluk: String? = null
    private var Pname: String? = null
    private var postedBy: String? = null
    private var katha: String? = null
    private var propertysize: String? = null
    private var location: String? = null
    private var number: String? = null
    private var ownerName: String? = null
    private var facing: String? = null
    private var approvedBy: String? = null
    private var InputProductName: EditText? = null
    private var InputProductDescription: EditText? = null
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
    private var ads_landmark : EditText? = null
    private var sp_taluk : Spinner? = null
    private var gridView: GridView? = null
    private var btn_corosel : ImageView? = null
    private var ll_selfie : LinearLayout? = null
    var fileNameList: ArrayList<String> = ArrayList<String>()
    var fileDoneList: ArrayList<String> = ArrayList<String>()
    var locationList: ArrayList<String> = ArrayList<String>()
    private var ads_name: EditText? = null
    private var arrayAdapter: ArrayAdapter<*>? = null
    var backBtn: ImageView? = null
    private lateinit var imageAdapter: ImageAdaptor
    private val selectedImages = mutableListOf<Uri>()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_ads)
        val propertyPage = intent.getStringExtra("page")
        if (propertyPage == "2") {
            ProductImagesRef = FirebaseStorage.getInstance().reference.child("Product Images")
            ProductsRef = FirebaseDatabase.getInstance().reference.child(AppConstants.products)
        } else {
            ProductImagesRef = FirebaseStorage.getInstance().reference.child("ads")
            ProductsRef = FirebaseDatabase.getInstance().reference.child("adsforyou")
        }
        btn_corosel = findViewById<ImageView>(R.id.select_image)
        val add_new_corosel = findViewById<Button>(R.id.add_new_ads)
        InputProductName = findViewById<View>(R.id.ads_name) as EditText
        InputProductDescription = findViewById<View>(R.id.ads_description) as EditText
        InputProductPrice = findViewById<View>(R.id.ads_price_admin) as EditText
        ads_facing = findViewById(R.id.ads_facing)
        edt_katha = findViewById(R.id.edt_property_katha)
        gridView = findViewById(R.id.gridView)
        ads_landmark = findViewById(R.id.ads_landmark)
        ads_approved_by = findViewById(R.id.ads_approved_by)
        ads_approved_by?.visibility = View.GONE
        rbButton = findViewById(R.id.rb_data)
        ads_name = findViewById(R.id.ads_name)
        sp_taluk = findViewById(R.id.sp_taluk)
        ll_selfie = findViewById(R.id.ll_selfie)
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
        imageAdapter = ImageAdaptor(this, selectedImages)
        gridView?.adapter = imageAdapter
        btn_corosel?.setOnClickListener { OpenGallery() }
        backBtn?.setOnClickListener(View.OnClickListener { finish() })
        add_new_corosel.setOnClickListener { ValidateProductData() }
        postedBy = "owner"
        initilize()
        getlocations()
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
        sp_taluk?.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2>0){
                    val propertyArray = resources.getStringArray(R.array.property_type)
                    Taluk = propertyArray[p2]
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
        } else if (TextUtils.isEmpty(CategoryName) || CategoryName.equals("")) {
            Toast.makeText(this, "Please enter category", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(facing)) {
            Toast.makeText(this, "Please enter facing", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(postedBy)) {
            Toast.makeText(this, "Please Select whether you owner or agent", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(katha)) {
            Toast.makeText(this, "Please enter katha type", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(ads_landmark?.text.toString())) {
            Toast.makeText(this, "Please enter Landmark or City name", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(location)) {
        Toast.makeText(this, "Please enter Location", Toast.LENGTH_SHORT).show()
        }else if (TextUtils.isEmpty(Taluk)) {
            Toast.makeText(this, "Please select Taluk", Toast.LENGTH_SHORT).show()
        } else {
            SaveProductInfoToDatabase()
        }
    }

    private fun StoreProductInformation(data: Intent) {
        downloadImageUrl = ""
        if (data.data != null) {
            val uri = data.data
            btn_corosel?.setImageURI(uri)
            uploadTostorage(data, uri)
        } else if (data.clipData != null) {
            ll_selfie?.visibility = View.GONE
            gridView?.visibility = View.VISIBLE
            val clipData = data.clipData
            btn_corosel?.setImageURI(clipData?.getItemAt(0)?.uri)
            for (i in 0 until clipData!!.itemCount) {
                val uri = clipData.getItemAt(i).uri
                selectedImages.add(uri)
                uploadTostorage(data, uri)
            }
            imageAdapter.notifyDataSetChanged()
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
                    }
                }
        }
    }

    private fun SaveProductInfoToDatabase() {
       loadingBar?.setTitle("Posting New Property");
       loadingBar?.setMessage("please wait while we are listing your property");
       loadingBar?.setCanceledOnTouchOutside(false);
       loadingBar?.show();
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
        productMap[AppConstants.city] = ads_landmark?.text.toString()
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
                    loadingBar?.dismiss()
                    finish()
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

    private fun getlocations() {
        val myDataRef = FirebaseDatabase.getInstance().reference.child("Locations")
        myDataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataMap = dataSnapshot.value as HashMap<*, *>?
                for (key in dataMap!!.keys) {
                    val data = dataMap[key]
                    try {
                        val userData = data as HashMap<String, Any>?
                        locationList.add(userData!!["taluk"].toString())
                    }catch (cce: ClassCastException) {
                    }
                }
                arrayAdapter = ArrayAdapter(this@Admin_ads_dashboard, android.R.layout.simple_spinner_item, locationList)
                arrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sp_taluk?.adapter = arrayAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to read value: ${error.message}")
            }
        })
    }

    companion object {
        private const val GalleryPick = 1
    }
}