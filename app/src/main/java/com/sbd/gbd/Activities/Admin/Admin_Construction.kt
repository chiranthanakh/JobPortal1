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
import android.util.Log
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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sbd.gbd.Adapters.BusinessCategoryAdaptor
import com.sbd.gbd.Adapters.ImageAdaptor
import com.sbd.gbd.Model.Categorymmodel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import java.text.SimpleDateFormat
import java.util.Calendar

class Admin_Construction : AppCompatActivity() {
    var Name: String? = null
    var category: String? = null
    var cost: String? = null
    var contactDetails: String? = null
    var contactDetails2: String? = null
    var experience: String? = null
    var service1: String? = null
    var service2: String? = null
    var open: String? = null
    var close: String? = null
    var service3: String? = null
    var service4: String? = null
    var saveCurrentDate: String? = null
    var saveCurrentTime: String? = null
    var discription: String? = null
    var address: String? = null
    var owner: String? = null
    var product_services: String? = null
    var gst: String? = null
    val list: ArrayList<String> = ArrayList<String>()
    private var edt_construction_name: EditText? = null
    private var edt_construction_number: EditText? = null
    private var edt_construction_cost: EditText? = null
    private var etd_opening_time: EditText? = null
    private var edt_closing_time: EditText? = null
    private var edt_construction_experience: EditText? = null
    private var edt_construction_servicessoffer_1: EditText? = null
    private var edt_construction_servicessoffer_2: EditText? = null
    private var edt_construction_servicessoffer_3: EditText? = null
    private var edt_construction_servicessoffer_4: EditText? = null
    private var edt_construction_discription: EditText? = null
    private var edt_product_services: EditText? = null
    private var edt_construction_address: EditText? = null
    private var edt_construction_gst: EditText? = null
    private var edt_construction_owner: EditText? = null
    private var ImageUri: Uri? = null
    private var edt_construction_category: AutoCompleteTextView? = null
    private var productRandomKey: String? = null
    private var downloadImageUrl: String? = null
    private var MainimageUrl: String? = null
    private var ProductImagesRef: StorageReference? = null
    private var ProductsRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null
    var fileNameList: ArrayList<String?> = ArrayList<String?>()
    var fileDoneList: ArrayList<String> = ArrayList<String>()
    var back_btn: ImageView? = null
    private var gridView: GridView? = null
    private var btn_corosel : ImageView? = null
    private var ll_selfie : LinearLayout? = null
    private lateinit var imageAdapter: ImageAdaptor
    private val selectedImages = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_construction)

        ProductImagesRef = FirebaseStorage.getInstance().reference.child("construction")
        ProductsRef = FirebaseDatabase.getInstance().reference.child("constructionforyou")
        val add_new_construction = findViewById<Button>(R.id.add_new_construction)
        edt_construction_name = findViewById<View>(R.id.edt_construction_name) as EditText
        edt_construction_category = findViewById(R.id.edt_construction_category)
        edt_construction_number =
            findViewById<View>(R.id.edt_construction_contact_number) as EditText
        edt_product_services = findViewById<View>(R.id.edt_product_services) as EditText
        edt_construction_cost = findViewById<View>(R.id.edt_construction_cost) as EditText
        edt_construction_experience = findViewById(R.id.edt_construction_experience)
        etd_opening_time = findViewById(R.id.etd_opening_time)
        edt_closing_time = findViewById(R.id.etd_closing_time)

        ll_selfie = findViewById(R.id.ll_selfie)
        gridView = findViewById(R.id.gridView)
        btn_corosel = findViewById(R.id.select_image)

        edt_construction_servicessoffer_1 = findViewById(R.id.edt_construction_servicessoffer_1)
        edt_construction_servicessoffer_2 = findViewById(R.id.edt_construction_servicessoffer_2)
        edt_construction_servicessoffer_3 = findViewById(R.id.edt_construction_servicessoffer_3)
        edt_construction_discription = findViewById(R.id.edt_construction_discription)
        edt_construction_address = findViewById(R.id.edt_construction_address)
        edt_construction_gst = findViewById(R.id.edt_construction_gst)
        edt_construction_owner = findViewById(R.id.edt_construction_owner)
        back_btn = findViewById(R.id.iv_nav_view)
        loadingBar = ProgressDialog(this)
        btn_corosel?.setOnClickListener { OpenGallery() }
        add_new_construction.setOnClickListener { ValidateProductData() }
        fetchbusinessCategorys()
        /*val list: ArrayList<String> = ArrayList<String>()
        list.add("Contractors")
        list.add("Architect")
        list.add("Interior Designer")
        list.add("Construction Meterials")
        list.add("Hardwares")
        list.add("Painters")
        list.add("Carpenters")
        list.add("Electrician")
        list.add("Plumber")
        list.add("Other")*/

        val arrayAdapter: ArrayAdapter<*> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        edt_construction_category?.setAdapter(arrayAdapter)
        edt_construction_category?.setInputType(0)
        edt_construction_category?.setOnFocusChangeListener(OnFocusChangeListener { v: View?, hasFocus: Boolean -> if (hasFocus) edt_construction_category?.showDropDown() })
        back_btn?.setOnClickListener(View.OnClickListener { view: View? -> finish() })

        imageAdapter = ImageAdaptor(this, selectedImages)
        gridView?.adapter = imageAdapter
        etd_opening_time?.inputType = InputType.TYPE_NULL
        edt_closing_time?.inputType = InputType.TYPE_NULL

        etd_opening_time?.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                etd_opening_time?.setText(SimpleDateFormat("HH:mm").format(cal.time).toString()+" "+"am")
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        edt_closing_time?.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                edt_closing_time?.setText(SimpleDateFormat("HH:mm").format(cal.time).toString()+" "+"pm")
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
    }

    private fun fetchbusinessCategorys() {
        var categorylist = FirebaseDatabase.getInstance().reference.child("BusinessListing_category").orderByChild(AppConstants.category).equalTo("Construction")
        categorylist.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            list.add(userData?.get("subcategory").toString())
                        } catch (cce: ClassCastException) {

                        }
                    }
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
            Log.d("error12345",message)
            edt_construction_discription?.setText(message)
            Toast.makeText(this@Admin_Construction, "$message", Toast.LENGTH_SHORT).show()
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
        Name = edt_construction_name!!.text.toString()
        category = edt_construction_category!!.text.toString()
        experience = edt_construction_experience!!.text.toString()
        cost = edt_construction_cost!!.text.toString()
        contactDetails = edt_construction_number!!.text.toString()
        discription = edt_construction_discription!!.text.toString()
        owner = edt_construction_owner!!.text.toString()
        address = edt_construction_address!!.text.toString()
        gst = edt_construction_gst!!.text.toString()
        open = etd_opening_time!!.text.toString()
        close = edt_closing_time!!.text.toString()
        product_services = edt_product_services!!.text.toString()
        if (TextUtils.isEmpty(downloadImageUrl)) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Name)) {
            Toast.makeText(this, "Please enter vehicle name...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(cost)) {
            Toast.makeText(this, "Please write Price/KM...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(contactDetails)) {
            Toast.makeText(this, "Please enter contact details...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(open)) {
            Toast.makeText(this, "Please enter opening timings...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(close)) {
            Toast.makeText(this, "Please enter closing timings...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(category)) {
            Toast.makeText(this, "Please enter category", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(product_services)) {
            Toast.makeText(this, "please enter product or services", Toast.LENGTH_SHORT).show()
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
        productMap[AppConstants.description] = discription
        productMap[AppConstants.image2] = downloadImageUrl
        productMap[AppConstants.image] = MainimageUrl
        productMap[AppConstants.category] = category
        productMap["workingHrs"] = "$open to $close"
        productMap["cost"] = cost
        productMap["name"] = Name
        productMap["number1"] = contactDetails
        productMap["product_services"] = product_services
        productMap["servicess1"] = service1
        productMap["servicess2"] = service2
        productMap["servicess3"] = service3
        productMap["servicess4"] = service4
        productMap[AppConstants.verified] = "1"
        productMap["experience"] = experience
        productMap["owner"] = owner
        productMap["address"] = address
        productMap["gst"] = gst
        productMap[AppConstants.Status] = "1"
        ProductsRef?.child(productRandomKey!!)?.updateChildren(productMap)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loadingBar!!.dismiss()
                    Toast.makeText(
                        this@Admin_Construction,
                        "Business is added successfully. Wait for approval",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    loadingBar!!.dismiss()
                    val message = task.exception.toString()
                    Toast.makeText(this@Admin_Construction, "Error: $message", Toast.LENGTH_SHORT)
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

    companion object {
        private const val GalleryPick = 1
    }
}