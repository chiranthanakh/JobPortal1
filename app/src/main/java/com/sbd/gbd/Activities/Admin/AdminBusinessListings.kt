package com.sbd.gbd.Activities.Admin

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sbd.gbd.Adapters.ImageAdaptor
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import java.text.SimpleDateFormat
import java.util.Calendar

class AdminBusinessListings : AppCompatActivity() {
    private var CategoryName: String? = null
    private var Description: String? = null
    private var Price: String? = null
    private var Pname: String? = null
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    private var products: String? = null
    private var gst: String? = null
    private var from: String? = null
    private var productorservicess: String? = null
    private var location: String? = null
    private var number: String? = null
    private var owner: String? = null
    private val rating: String? = null
    private var email: String? = null
    private var open: String? = null
    private var close: String? = null
    private var InputProductName: EditText? = null
    private val Inputtype: EditText? = null
    private var businessopen: EditText? = null
    private var businessClose: EditText? = null
    private var InputProductDescription: EditText? = null
    private var InputProductPrice: AutoCompleteTextView? = null
    private var et_sell_name: EditText? = null
    private var et_location: EditText? = null
    private var et_number: EditText? = null
    private var et_ownername: EditText? = null
    private var et_email: EditText? = null
    private val et_rating: EditText? = null
    private var et_from: EditText? = null
    private var et_gst: EditText? = null
    private var et_pors: EditText? = null
    private var ImageUri: Uri? = null
    private var productRandomKey: String? = null
    private var downloadImageUrl: String? = null
    private var MainimageUrl: String? = null
    private var gridView: GridView? = null
    private var btn_corosel : ImageView? = null
    private var ll_selfie : LinearLayout? = null
    private var ProductImagesRef: StorageReference? = null
    private var ProductsRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null
    private var Business_City : TextInputEditText? = null
    private var business_category: AutoCompleteTextView? = null
    var fileNameList: ArrayList<String?> = ArrayList<String?>()
    var fileDoneList: ArrayList<String> = ArrayList<String>()
    var categoryList: ArrayList<String> = ArrayList<String>()
    var arrayAdapter: ArrayAdapter<*>? = null
     var BtypeAdaptor: ArrayAdapter<*>? = null
    var back_btn: ImageView? = null
    private lateinit var imageAdapter: ImageAdaptor
    private val selectedImages = mutableListOf<Uri>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_business_listings)
        ProductImagesRef = FirebaseStorage.getInstance().reference.child("business")
        ProductsRef = FirebaseDatabase.getInstance().reference.child("BusinessListing")
        btn_corosel = findViewById(R.id.select_image)
        val add_new_corosel = findViewById<Button>(R.id.add_new_Business)
        ll_selfie = findViewById(R.id.ll_selfie)
        InputProductName = findViewById<View>(R.id.Business_name) as EditText
        business_category = findViewById(R.id.Business_type_admin)
        InputProductDescription = findViewById<View>(R.id.Business_description) as EditText
        InputProductPrice = findViewById(R.id.Business_price_admin)
        et_sell_name = findViewById(R.id.Business_size)
        et_location = findViewById(R.id.Business_location_admin)
        et_number = findViewById(R.id.Business_number1)
        et_ownername = findViewById(R.id.Business_owner_name)
        et_email = findViewById(R.id.Business_email)
        businessopen = findViewById(R.id.business_open)
        businessClose = findViewById(R.id.business_close)
        gridView = findViewById(R.id.gridView)
        et_gst = findViewById(R.id.Business_gst)
        et_pors = findViewById(R.id.Business_pors)
        et_from = findViewById(R.id.Business_from)
        back_btn = findViewById(R.id.iv_nav_view)
        Business_City = findViewById(R.id.Business_City)
        loadingBar = ProgressDialog(this)
        fetchbusinessCategorys()
        imageAdapter = ImageAdaptor(this, selectedImages)
        gridView?.adapter = imageAdapter
        businessopen?.inputType = InputType.TYPE_NULL
        businessClose?.inputType = InputType.TYPE_NULL

        btn_corosel?.setOnClickListener { OpenGallery() }
        add_new_corosel.setOnClickListener { ValidateProductData() }
        back_btn?.setOnClickListener{ finish() }
        business_category?.setOnFocusChangeListener({ v: View?, hasFocus: Boolean -> if (hasFocus) business_category!!.showDropDown() })
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryList)

        InputProductPrice?.setOnFocusChangeListener({ v: View?, hasFocus: Boolean -> if (hasFocus) InputProductPrice!!.showDropDown() })
        BtypeAdaptor = ArrayAdapter(this, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.businessType))
        InputProductPrice!!.setAdapter(BtypeAdaptor)
        InputProductPrice!!.inputType = 0

        businessopen?.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                businessopen?.setText(SimpleDateFormat("HH:mm").format(cal.time).toString()+" "+"am")
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        businessClose?.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                businessClose?.setText(SimpleDateFormat("HH:mm").format(cal.time).toString()+" "+"pm")
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

    }

    private fun fetchbusinessCategorys() {
        val categorylist =
            FirebaseDatabase.getInstance().reference.child("BusinessListing_category").orderByChild(AppConstants.category).equalTo("Business")
        categorylist.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            categoryList.add(userData!!["subcategory"].toString())
                        } catch (cce: ClassCastException) {
                            try {
                                val mString = dataMap[key].toString()
                            } catch (cce2: ClassCastException) {
                            }
                        }
                    }
                    business_category!!.setAdapter(arrayAdapter)
                    business_category!!.inputType = 0
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
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
        fileNameList.add(fileName)
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
            Toast.makeText(this@AdminBusinessListings, "Error: $message", Toast.LENGTH_SHORT).show()
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

    private fun ValidateProductData() {
        Description = InputProductDescription!!.text.toString()
        Price = InputProductPrice!!.text.toString()
        Pname = InputProductName!!.text.toString()
        products = et_sell_name!!.text.toString()
        location = et_location!!.text.toString()
        number = et_number!!.text.toString()
        owner = et_ownername!!.text.toString()
        email = et_email!!.text.toString()
        gst = et_gst!!.text.toString()
        CategoryName = business_category!!.text.toString()
        open = businessopen!!.text.toString()
        close = businessopen!!.text.toString()
        from = et_from!!.text.toString()
        productorservicess = et_pors!!.text.toString()
        if (TextUtils.isEmpty(downloadImageUrl)) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Pname)) {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(open)) {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(close)) {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(from)) {
            Toast.makeText(this, "Please write business started year...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Business_City?.text.toString())) {
        Toast.makeText(this, "Please write business started year...", Toast.LENGTH_SHORT).show()
        } else {
            SaveProductInfoToDatabase()
        }
    }

    private fun SaveProductInfoToDatabase() {
      loadingBar?.setTitle("Posting Your Business");
      loadingBar?.setMessage("please wait while we are adding your Business");
      loadingBar?.setCanceledOnTouchOutside(false);
      loadingBar?.show();
        val productMap = HashMap<String, Any?>()
        productMap[AppConstants.pid] = productRandomKey
        productMap[AppConstants.date] = saveCurrentDate
        productMap[AppConstants.time] = saveCurrentTime
        productMap[AppConstants.description] = Description
        productMap[AppConstants.image2] = downloadImageUrl
        productMap[AppConstants.image] = MainimageUrl
        productMap[AppConstants.category] = CategoryName
        productMap[AppConstants.price] = Price
        productMap["Businessname"] = Pname
        productMap["Business_category"] = products
        productMap[AppConstants.location] = location
        productMap[AppConstants.number] = number
        productMap["owner"] = owner
        productMap["email"] = email
        productMap["rating"] = "4"
        productMap[AppConstants.Status] = "1"
        productMap["workingHrs"] = "$open to $close"
        productMap["gst"] = gst
        productMap["from"] = from
        productMap[AppConstants.city] = Business_City?.text.toString()
        productMap["productServicess"] = productorservicess
        ProductsRef!!.child(productRandomKey!!).updateChildren(productMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loadingBar!!.dismiss()
                    Toast.makeText(
                        this@AdminBusinessListings,
                        "Product is added successfully..",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingBar?.dismiss()
                    finish()
                } else {
                    loadingBar!!.dismiss()
                    val message = task.exception.toString()
                    Toast.makeText(
                        this@AdminBusinessListings,
                        "Error: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingBar?.dismiss()
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