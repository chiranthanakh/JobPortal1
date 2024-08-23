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
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.sbd.gbd.Adapters.ImageAdaptor
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.databinding.ActivityAdminAdsBinding
import com.sbd.gbd.databinding.ActivityAdminLivingPlacessBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class AdminLivingPlacess : AppCompatActivity() {
    private lateinit var binding: ActivityAdminLivingPlacessBinding
    var saveCurrentDate: String? = null
    var saveCurrentTime: String? = null
    var title: String? = null
    var category: String? = null
    var rent_lease: String? = null
    var floore: String? = null
    var rentamount: String? = null
    var depositeamount: String? = null
    var location: String? = null
    var contactNumber: String? = null
    var verified: String? = null
    var nuBHK: String? = null
    var sqft: String? = null
    var water: String? = null
    private var taluk: String? = ""
    private var postedBy: String? = ""
    private var boarwell: Boolean? = false
    private var furnished: Boolean? = false
    private var parking: Boolean? = false
    private var gated: Boolean? = false
    private var immidateAvailable: Boolean? = false
    var discription: String? = null
    private var livingplace_name: EditText? = null
    private var livingplace_category: Spinner? = null
    private var livingplace_rent_lease: Spinner? = null
    private var livingplace_flore: EditText? = null
    private val livingplace_rent_advance: EditText? = null
    private var livingplace_rent_amount: EditText? = null
    private var livingplace_deposite_amount: EditText? = null
    private var livingplace_location: EditText? = null
    private var livingplace_contact_number: EditText? = null
    private var leavingplace_verify_or_nt: EditText? = null
    private var livingplace_number_of_bhk: EditText? = null
    private var livingplace_sqft: EditText? = null
    private var livingplace_water_facility: EditText? = null
    private var livingplace_vehicle_parking: EditText? = null
    private var livingplace_posted_by: EditText? = null
    private var livingplace_discription: EditText? = null
    private var ImageUri: Uri? = null
    private var loadingBar: ProgressDialog? = null
    private var productRandomKey: String? = null
    private var downloadImageUrl: String? = null
    private var MainimageUrl: String? = null
    private var ProductImagesRef: StorageReference? = null
    private var ProductsRef: DatabaseReference? = null
    var back_btn: ImageView? = null
    private var gridView: GridView? = null
    private var btn_corosel : ImageView? = null
    private var ll_selfie : LinearLayout? = null
    private var add_images : ImageView? = null

    var fileNameList: ArrayList<String> = ArrayList()
    var fileDoneList: ArrayList<String> = ArrayList()
    var locationList: ArrayList<String> = ArrayList()
    var locationMap = mutableMapOf<String, String>()
    var districtList : ArrayList<String> = ArrayList()
    private var arrayAdapter: ArrayAdapter<*>? = null
    private var districtAdapter: ArrayAdapter<*>? = null
    private lateinit var imageAdapter: ImageAdaptor
    private val selectedImages = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLivingPlacessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ProductImagesRef = FirebaseStorage.getInstance().reference.child("livingplace")
        ProductsRef = FirebaseDatabase.getInstance().reference.child("livingplaceforyou")
        initilize()
    }

    private fun initilize() {
        back_btn = findViewById(R.id.iv_nav_view)
        loadingBar = ProgressDialog(this)
        ll_selfie = findViewById(R.id.ll_selfie)
        gridView = findViewById(R.id.gridView)
        add_images?.setOnClickListener {  OpenGallery() }

        binding.furnishrGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.furnish_yes -> { furnished = true }
                    R.id.furnish_no -> { furnished = false }
                }
            }
        }
        binding.CarGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.car_yes -> { parking = true }
                    R.id.car_no -> { parking = false }
                }
            }
        }
        binding.gatedGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.gated_yes -> { gated = true }
                    R.id.gated_no -> { gated = false }
                }
            }
        }
        binding.immidateGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.immidate_yes -> { immidateAvailable = true }
                    R.id.immidate_no -> { immidateAvailable = false }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.data
            //InputProductImage.setImageURI(ImageUri);
            StoreProductInformation(data)
        }
    }

    private fun OpenGallery() {
        val galleryIntent = Intent()
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT)
        galleryIntent.setType("image/*")
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(galleryIntent, GalleryPick)
    }

    private fun StoreProductInformation(data: Intent) {
        downloadImageUrl = ""
        if (data.data != null) {
            val uri = data.data
            add_images?.setImageURI(uri)
            uploadTostorage(data, uri)
        } else if (data.clipData != null) {
            ll_selfie?.visibility = View.GONE
            gridView?.visibility = View.VISIBLE
            val clipData = data.clipData
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
       // fileNameList.add(fileName)
        fileDoneList.add("Uploading")
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("MMM dd")
        saveCurrentDate = currentDate.format(calendar.time)
        val currentTime = SimpleDateFormat("HH:mm a")
        saveCurrentTime = currentTime.format(calendar.time)
        productRandomKey = saveCurrentDate + saveCurrentTime
        val filePath = ProductImagesRef!!.child(uri!!.lastPathSegment + productRandomKey + ".jpg")
        val uploadTask = filePath.putFile(uri)
        uploadTask.addOnFailureListener { e: Exception ->
            val message = e.toString()
            Toast.makeText(this@AdminLivingPlacess, "Error: $message", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
            val urlTask =
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw (task.exception)!!
                    }
                    filePath.downloadUrl
                }.addOnCompleteListener { task: Task<Uri> ->
                    if (task.isSuccessful) {
                        if ((downloadImageUrl == "")) {
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
        title = livingplace_name?.text.toString()
        floore = livingplace_flore?.text.toString()
        location = livingplace_location?.text.toString()
        rentamount = livingplace_rent_amount?.text.toString()
        depositeamount = livingplace_deposite_amount?.text.toString()
        contactNumber = livingplace_contact_number!!.text.toString()
        nuBHK = livingplace_number_of_bhk?.text.toString()
        sqft = livingplace_sqft!!.text.toString()
        water = livingplace_water_facility?.text.toString()
       // parking = livingplace_vehicle_parking?.text.toString()
        //postedBY = livingplace_posted_by?.text.toString()

        discription = livingplace_discription!!.text.toString()
        if (TextUtils.isEmpty(downloadImageUrl)) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Please write product title...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(rent_lease) || rent_lease.equals("Please Select Type")) {
            Toast.makeText(this, "Please write product rent_lease...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Please write location...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(rentamount)) {
            Toast.makeText(this, "Please enter rentamount", Toast.LENGTH_SHORT).show()
        }else if (TextUtils.isEmpty(category) || category.equals("Please Select Rent or Lease")) {
            Toast.makeText(this, "Please select Building type", Toast.LENGTH_SHORT).show()
        }else if (TextUtils.isEmpty(depositeamount)) {
            Toast.makeText(this, "Please select Building type", Toast.LENGTH_SHORT).show()
        }
        else {
            SaveProductInfoToDatabase()
        }
    }

    private fun SaveProductInfoToDatabase() {
        loadingBar?.setTitle("List Your Property");
       loadingBar?.setMessage("please wait while we are adding the new Property.");
       loadingBar?.setCanceledOnTouchOutside(false);
       loadingBar?.show();
        val productMap = HashMap<String, Any?>()
        productMap[AppConstants.pid] = productRandomKey
        productMap["title"] = title
        productMap[AppConstants.category] = category
        productMap["rent_lease"] = rent_lease
        productMap["floore"] = floore
        productMap[AppConstants.image2] = downloadImageUrl
        productMap[AppConstants.image] = MainimageUrl
        productMap[AppConstants.location] = location
        productMap[AppConstants.Deposit] = depositeamount
        productMap["rentamount"] = rentamount
        productMap["contactNumber"] = contactNumber
        productMap["nuBHK"] = nuBHK
        productMap["area"] = sqft
        productMap["water"] = water
        productMap[AppConstants.postedOn] = saveCurrentDate
        productMap["Approval"] = 1
        productMap["parking"] = parking
       // productMap[AppConstants.postedBy] = postedBY
        productMap["discription"] = discription
        productMap[AppConstants.Status] = "1"
        ProductsRef?.child(productRandomKey!!)?.updateChildren(productMap)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@AdminLivingPlacess,
                        "Your Property added successfully. Please wait for approval",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingBar?.dismiss()
                    finish()
                } else {
                    val message = task.exception.toString()
                    Toast.makeText(this@AdminLivingPlacess, "Error: $message", Toast.LENGTH_SHORT)
                        .show()
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