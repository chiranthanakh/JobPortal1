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

class Admin_travels : AppCompatActivity() {
    var vehicleName: String? = null
    var category: String? = null
    var VehicleNumber: String? = null
    var costperKM: String? = null
    var contactDetails: String? = null
    var vehiclemodel: String? = null
    var ownerName: String? = null
    var saveCurrentDate: String? = null
    var saveCurrentTime: String? = null
    var discription: String? = null
    private var edt_vehicle_name: EditText? = null
    private var edt_travel_vehicle_number: EditText? = null
    private var edt_rupes_for_km: EditText? = null
    private var edt_travel_contact: EditText? = null
    private var edt_travel_vehicle_model: EditText? = null
    private var edt_owner_name: EditText? = null
    private var edt_travel_verified_not: EditText? = null
    private var edt_travel_discription: EditText? = null
    private var edt_travel_category: Spinner? = null
    private var ImageUri: Uri? = null
    private var productRandomKey: String? = null
    private var downloadImageUrl: String? = null
    private var MainimageUrl: String? = null
    private var ProductImagesRef: StorageReference? = null
    private var ProductsRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null
    var fileNameList: ArrayList<String> = ArrayList<String>()
    var fileDoneList: ArrayList<String> = ArrayList<String>()
    var ads_name: EditText? = null
    var back_btn: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding=ActivityTravelsBinding.inflate(layoutInflater)
        setContentView(R.layout.admin_travel)
        ProductImagesRef = FirebaseStorage.getInstance().reference.child("travels")
        ProductsRef = FirebaseDatabase.getInstance().reference.child("travelsforyou")
        val btn_add_image = findViewById<ImageView>(R.id.select_vehicle_images)
        val add_new_vehicle = findViewById<Button>(R.id.add_new_vehicle)
        back_btn = findViewById(R.id.iv_nav_view)
        edt_vehicle_name = findViewById<View>(R.id.edt_vehicle_name) as EditText
        edt_travel_category = findViewById(R.id.sp_vehicle_type)
        edt_travel_vehicle_number = findViewById(R.id.edt_travel_vehicle_number)
        edt_rupes_for_km = findViewById(R.id.edt_rupes_for_km)
        edt_travel_contact = findViewById(R.id.edt_travel_contact)
        edt_travel_vehicle_model = findViewById(R.id.edt_travel_vehicle_model)
        edt_owner_name = findViewById(R.id.edt_owner_name)
        edt_travel_verified_not = findViewById(R.id.edt_travel_verified_not)
        edt_travel_discription = findViewById(R.id.edt_travel_discription)
        loadingBar = ProgressDialog(this)
        btn_add_image.setOnClickListener { view: View? -> OpenGallery() }
        add_new_vehicle.setOnClickListener { view: View? -> ValidateProductData() }
        back_btn?.setOnClickListener(View.OnClickListener { view: View? -> finish() })

        edt_travel_category?.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            val frequencyArray = resources.getStringArray(R.array.vehicle_type)
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2>0){
                    // getDistrict(stateList[p2].mst_state_id.toInt())
                    category = frequencyArray.get(p2)
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
        fileNameList.add(fileName.toString())
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
            Toast.makeText(this@Admin_travels, "Error: $message", Toast.LENGTH_SHORT).show()
            loadingBar!!.dismiss()
        }.addOnSuccessListener {
            Toast.makeText(
                this@Admin_travels,
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
                        Toast.makeText(this@Admin_travels, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun ValidateProductData() {
        vehicleName = edt_vehicle_name?.text.toString()
        VehicleNumber = edt_travel_vehicle_number!!.text.toString()
        costperKM = edt_rupes_for_km!!.text.toString()
        contactDetails = edt_travel_contact!!.text.toString()
        vehiclemodel = edt_travel_vehicle_model!!.text.toString()
        ownerName = edt_owner_name!!.text.toString()
        discription = edt_travel_discription!!.text.toString()
        if (TextUtils.isEmpty(downloadImageUrl)) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(vehicleName)) {
            Toast.makeText(this, "Please enter vehicle name...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(costperKM)) {
            Toast.makeText(this, "Please write Price/KM...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(contactDetails)) {
            Toast.makeText(this, "Please enter contact details...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(category)) {
            Toast.makeText(this, "Please enter category", Toast.LENGTH_SHORT).show()
        } else {
            SaveProductInfoToDatabase()
        }
    }

    private fun SaveProductInfoToDatabase() {
        val productMap = HashMap<String, Any?>()
        productMap[AppConstants.pid] = productRandomKey + "_travel"
        productMap[AppConstants.date] = saveCurrentDate
        productMap[AppConstants.time] = saveCurrentTime
        productMap[AppConstants.description] = discription
        productMap[AppConstants.image2] = downloadImageUrl
        productMap[AppConstants.image] = MainimageUrl
        productMap[AppConstants.category] = category
        productMap["costperkm"] = costperKM
        productMap["vehiclename"] = vehicleName
        productMap["vehiclenumber"] = VehicleNumber
        productMap["contactnumber"] = contactDetails
        productMap["model"] = vehiclemodel
        productMap["ownerNmae"] = ownerName
        productMap[AppConstants.verified] = 1
        productMap[AppConstants.Status] = "1"
        ProductsRef!!.child(productRandomKey!!).updateChildren(productMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Intent intent = new Intent(AdminAddNewProductActivity.this, .class);
                    //startActivity(intent);
                    loadingBar!!.dismiss()
                    Toast.makeText(this@Admin_travels, "Product is added successfully..", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    loadingBar!!.dismiss()
                    val message = task.exception.toString()
                    Toast.makeText(this@Admin_travels, "Error: $message", Toast.LENGTH_SHORT).show()
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