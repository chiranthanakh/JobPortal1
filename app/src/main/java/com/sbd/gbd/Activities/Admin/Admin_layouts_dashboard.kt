package com.sbd.gbd.Activities.Admin

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.TextUtils
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Calendar

class Admin_layouts_dashboard : AppCompatActivity() {
    private var CategoryName: String? = null
    private var Description: String? = null
    private var Price: String? = null
    private var facing: String? = null
    private var nuOfSites: String? = null
    private var layoutArea: String? = null
    private var Pname: String? = null
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    private var propertysize: String? = null
    private var location: String? = null
    private var number: String? = null
    private var postedBy: String? = null
    private var InputProductName: EditText? = null
    private val Inputtype: EditText? = null
    private var InputProductDescription: EditText? = null
    private var InputProductPrice: EditText? = null
    private var et_size: EditText? = null
    private var et_location: EditText? = null
    private var et_number: EditText? = null
    private var et_no_available_sites: EditText? = null
    private var et_future1: EditText? = null
    private var et_future2: EditText? = null
    private var et_future3: EditText? = null
    private var et_future4: EditText? = null
    private var edt_layout_facing: EditText? = null
    private var edt_layout_area: EditText? = null
    private lateinit var cb_posted_owner: CheckBox
    private lateinit var cb_posted_broker: CheckBox
    private var category: AutoCompleteTextView? = null
    private var placement: Spinner? = null
    private var productRandomKey: String? = ""
    private var downloadImageUrl: String? = ""
    private var MainimageUrl: String? = ""
    private var postplace: String? = ""
    private var ProductImagesRef: StorageReference? = null
    private var ProductsRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null
    var fileNameList: ArrayList<String> = ArrayList<String>()
    var fileDoneList: ArrayList<String> = ArrayList<String>()
    var ads_name: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_layouts)

        //CategoryName = "cqat";
        ProductImagesRef = FirebaseStorage.getInstance().reference.child("layouts")
        ProductsRef = FirebaseDatabase.getInstance().reference.child("layoutsforyou")
        val btn_corosel = findViewById<ImageView>(R.id.select_corosel_image)
        val add_new_corosel = findViewById<Button>(R.id.add_new_ads)
        InputProductName = findViewById<View>(R.id.ads_name) as EditText
        category = findViewById(R.id.ads_type_admin)
        InputProductDescription = findViewById<View>(R.id.ads_description) as EditText
        InputProductPrice = findViewById<View>(R.id.ads_price_admin) as EditText
        ads_name = findViewById(R.id.ads_name)
        et_size = findViewById(R.id.ads_size)
        et_location = findViewById(R.id.ads_location_admin)
        et_number = findViewById(R.id.ads_contact_number)
        cb_posted_owner = findViewById(R.id.cb_posted_owner)
        cb_posted_broker = findViewById(R.id.cb_posted_broker)
        et_no_available_sites = findViewById(R.id.no_available_sites)
        edt_layout_facing = findViewById(R.id.edt_layout_facing)
        edt_layout_area = findViewById(R.id.edt_layout_area)
        placement = findViewById(R.id.sp_placement)
        et_future1 = findViewById(R.id.ads_text1)
        et_future2 = findViewById(R.id.ads_text2)
        et_future3 = findViewById(R.id.ads_text3)
        et_future4 = findViewById(R.id.ads_text4)
        loadingBar = ProgressDialog(this)
        postedBy = "owner"
        val list: ArrayList<String> = ArrayList<String>()
        list.add("Site")
        list.add("site in Layout")
        list.add("Layout")
        list.add("form land")
        list.add("home")
        val arrayAdapter: ArrayAdapter<*> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        category?.setAdapter(arrayAdapter)
        category?.setInputType(0)
        category?.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus -> if (hasFocus) category?.showDropDown() })
        btn_corosel.setOnClickListener { OpenGallery() }
        add_new_corosel.setOnClickListener { ValidateProductData() }
        cb_posted_owner?.setOnClickListener(View.OnClickListener {
            if (cb_posted_broker.isChecked()) {
                cb_posted_broker.setChecked(false)
                postedBy = "owner"
            }
        })
        cb_posted_broker?.setOnClickListener(View.OnClickListener {
            if (cb_posted_owner.isChecked()) {
                cb_posted_owner.setChecked(false)
                postedBy = "broker"
            }
        })

        placement?.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
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
            StoreProductInformation(data)
        }
    }

    private fun ValidateProductData() {
        Description = InputProductDescription!!.text.toString()
        Price = InputProductPrice!!.text.toString()
        nuOfSites = et_no_available_sites!!.text.toString()
        Pname = InputProductName!!.text.toString()
        propertysize = et_size!!.text.toString()
        location = et_location!!.text.toString()
        number = et_number!!.text.toString()
        CategoryName = category!!.text.toString()
        facing = edt_layout_facing!!.text.toString()
        layoutArea = edt_layout_area!!.text.toString()
        if (TextUtils.isEmpty(downloadImageUrl)) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Pname)) {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(CategoryName)) {
            Toast.makeText(this, "Please enter category", Toast.LENGTH_SHORT).show()
        } else {
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
            Toast.makeText(this@Admin_layouts_dashboard, "Error: $message", Toast.LENGTH_SHORT)
                .show()
            loadingBar!!.dismiss()
        }.addOnSuccessListener {
            Toast.makeText(
                this@Admin_layouts_dashboard,
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
                            this@Admin_layouts_dashboard,
                            "got the Product image Url Successfully...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun SaveProductInfoToDatabase() {
        if(postplace == AppConstants.carousel) {
            ProductImagesRef = FirebaseStorage.getInstance().reference.child("Corosel")
            ProductsRef = FirebaseDatabase.getInstance().reference.child("Corosels")
            CategoryName = "Layout"
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
        productMap[AppConstants.propertysize] = propertysize
        productMap[AppConstants.location] = location
        productMap[AppConstants.number] = number
        productMap[AppConstants.Status] = "1"
        productMap[AppConstants.sitesAvailable] = nuOfSites
        productMap[AppConstants.postedBy] = postedBy
        productMap[AppConstants.facing] = facing
        productMap[AppConstants.layoutarea] = layoutArea
        productMap[AppConstants.point1] = et_future1!!.text.toString()
        productMap[AppConstants.point2] = et_future2!!.text.toString()
        productMap[AppConstants.point3] = et_future3!!.text.toString()
        productMap[AppConstants.point4] = et_future4!!.text.toString()
        ProductsRef!!.child(productRandomKey!!).updateChildren(productMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loadingBar!!.dismiss()
                    Toast.makeText(
                        this@Admin_layouts_dashboard,
                        "Product is added successfully..",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loadingBar!!.dismiss()
                    val message = task.exception.toString()
                    Toast.makeText(
                        this@Admin_layouts_dashboard,
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