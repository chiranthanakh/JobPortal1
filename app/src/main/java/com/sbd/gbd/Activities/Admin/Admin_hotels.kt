package com.sbd.gbd.Activities.Admin

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class Admin_hotels : AppCompatActivity() {

    private val GalleryPick = 1
    var saveCurrentDate : String? = null
    var saveCurrentTime : String? = null
    var name: String? = null
    var price: String? = null
    var address: String? = null
    var category: String? = null
    var rent_lease: String? = null
    var contact_person: String? = null
    var number: String? = null
    var alternative: String? = null
    var email: String? = null
    var website: String? = null
    var point1: String? = null
    var point2: String? = null
    var point3: String? = null
    var parking: String? = null
    var discription: String? = null
    private var edt_hotel_name: EditText? = null
    private  var edt_hotel_price:EditText? = null
    private  var edt_hotel_category:EditText? = null
    private  var edt_hotel_address:EditText? = null
    private  var edt_hotel_contact_person:EditText? = null
    private  var edt_hotel_number:EditText? = null
    private var edt_alternative_nu: EditText? = null
    private  var edt_hotel_email:EditText? = null
    private  var tv_hotel_website:EditText? = null
    private  var tv_hotel_fecility1:EditText? = null
    private  var tv_hotel_fecility2:EditText? = null
    private  var tv_hotel_fecility3:EditText? = null
    private  var tv_hotel_parking:EditText? = null
    private  var tv_hotel_discription:EditText? = null
    private var ImageUri: Uri? = null
    private var productRandomKey: String? = null
    private  var downloadImageUrl: String? = null
    private  var MainimageUrl: String? = null
    private var ProductImagesRef: StorageReference? = null
    private var ProductsRef: DatabaseReference? = null
    var fileNameList: ArrayList<String> = ArrayList<String>()
    var fileDoneList: ArrayList<Any> = ArrayList<Any>()
    var ads_name: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_hotels)
        ProductImagesRef = FirebaseStorage.getInstance().reference.child("hotels")
        ProductsRef = FirebaseDatabase.getInstance().reference.child("hotelsforyou")
        initilize()
    }

    private fun initilize() {
        val add_images = findViewById<ImageView>(R.id.select_livingplace_image)
        val btn_add_livingplace = findViewById<Button>(R.id.livingplace_submit)
        edt_hotel_name = findViewById(R.id.edt_hotel_name)
        edt_hotel_price = findViewById<EditText>(R.id.edt_hotel_price)
        edt_hotel_category = findViewById<EditText>(R.id.edt_hotel_category)
        edt_hotel_address = findViewById<EditText>(R.id.edt_hotel_address)
        edt_hotel_contact_person = findViewById<EditText>(R.id.edt_hotel_contact_person)
        edt_hotel_number = findViewById(R.id.edt_hotel_number)
        edt_alternative_nu = findViewById(R.id.edt_alternative_nu)
        edt_hotel_email = findViewById(R.id.edt_hotel_email)
        tv_hotel_website = findViewById(R.id.tv_hotel_website)
        tv_hotel_fecility1 = findViewById<EditText>(R.id.tv_hotel_fecility1)
        tv_hotel_fecility2 = findViewById<EditText>(R.id.tv_hotel_fecility2)
        tv_hotel_fecility3 = findViewById(R.id.tv_hotel_fecility3)
        tv_hotel_parking = findViewById<EditText>(R.id.tv_hotel_parking)
        tv_hotel_discription = findViewById<EditText>(R.id.tv_hotel_discription)
        add_images.setOnClickListener { OpenGallery() }
        btn_add_livingplace.setOnClickListener { ValidateProductData() }
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
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(galleryIntent, GalleryPick)
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
        fileName?.let { fileNameList.add(it) }
        fileDoneList.add("Uploading")
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("MMM dd")
        saveCurrentDate = currentDate.format(calendar.time)
        val currentTime = SimpleDateFormat("HH:mm a")
        saveCurrentTime = currentTime.format(calendar.time)
        productRandomKey = saveCurrentDate + saveCurrentTime
        val filePath = ProductImagesRef?.child(uri?.lastPathSegment + productRandomKey + ".jpg")
        val uploadTask = uri?.let { filePath?.putFile(it) }
        uploadTask?.addOnFailureListener { e ->
            val message = e.toString()
            Toast.makeText(this@Admin_hotels, "Error: $message", Toast.LENGTH_SHORT).show()
        }?.addOnSuccessListener {
            Toast.makeText(
                this@Admin_hotels,
                "Product Image uploaded Successfully...",
                Toast.LENGTH_SHORT
            ).show()
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                filePath?.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (downloadImageUrl == "") {
                        downloadImageUrl = task.result.toString()
                        MainimageUrl = task.result.toString()
                    } else {
                        downloadImageUrl = downloadImageUrl + "---" + task.result.toString()
                    }
                    println("url2---$downloadImageUrl")
                    Toast.makeText(
                        this@Admin_hotels,
                        "got the Product image Url Successfully...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun ValidateProductData() {
        name = edt_hotel_name?.text.toString()
        price = edt_hotel_price?.getText().toString()
        category = edt_hotel_category?.getText().toString()
        address = edt_hotel_address?.getText().toString()
        contact_person = edt_hotel_contact_person?.text.toString()
        number = edt_hotel_number?.getText().toString()
        alternative = edt_alternative_nu?.getText().toString()
        email = edt_hotel_email?.getText().toString()
        website = tv_hotel_website?.getText().toString()
        point1 = tv_hotel_fecility1?.getText().toString()
        point2 = tv_hotel_fecility2?.text.toString()
        point3 = tv_hotel_fecility3?.getText().toString()
        discription = tv_hotel_discription?.getText().toString()
        parking = tv_hotel_parking?.getText().toString()

        if (TextUtils.isEmpty(downloadImageUrl)) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write product title...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(category)) {
            Toast.makeText(this, "Please write product category...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please write location...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(number)) {
            Toast.makeText(this, "Please enter number", Toast.LENGTH_SHORT).show()
        } else {
            SaveProductInfoToDatabase()
        }
    }

    private fun SaveProductInfoToDatabase() {

        val productMap = HashMap<String, Any?>()
        productMap[AppConstants.pid] = productRandomKey
        productMap["name"] = name
        productMap[AppConstants.category] = category
        productMap["address"] = address
        productMap[AppConstants.image2] = downloadImageUrl
        productMap[AppConstants.image] = MainimageUrl
        productMap["alternative"] = alternative
        productMap[AppConstants.price] = price
        productMap[AppConstants.number] = number
        productMap["email"] = email
        productMap["website"] = website
        productMap[AppConstants.point1] = point1
        productMap[AppConstants.point2] = point2
        productMap[AppConstants.point3] = point3
        productMap["parking"] = parking
        productMap["owner"]  = contact_person
        productMap["discription"] = discription
        productMap["Rating"] = "4"
        productMap[AppConstants.Status] = "1"
        productMap["Approval"] = "1"

        ProductsRef!!.child(productRandomKey!!).updateChildren(productMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Intent intent = new Intent(AdminAddNewProductActivity.this, .class);
                    //startActivity(intent);
                    Toast.makeText(
                        this@Admin_hotels,
                        "Product is added successfully..",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val message = task.exception.toString()
                    Toast.makeText(this@Admin_hotels, "Error: $message", Toast.LENGTH_SHORT)
                        .show()
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

}