package com.sbd.gbd.Activities.Admin

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.UtilityMethods
import com.sbd.gbd.databinding.ActivityAdminAddNewProductBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AdminAddNewProductActivity : AppCompatActivity() {
    private var CategoryName: String? = null
    private var Description: String? = null
    private var Price: String? = null
    private var Pname: String? = null
    private var ownerORagent: String? = null
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    private var propertysize: String? = null
    private var location: String? = null
    private var number: String? = null
    private var type: String? = null
    private var katha: String? = null
    private var ownerName: String? = null
    private var facing: String? = null
    private var AddNewProductButton: AppCompatButton? = null
    private var InputProductImage: ImageView? = null
    private var InputProductName: EditText? = null
    private var InputProductDescription: EditText? = null
    private var InputProductPrice: EditText? = null
    private var propertyType: EditText? = null
    private var et_size: EditText? = null
    private var et_location: EditText? = null
    private var et_number: EditText? = null
    private val ImageUri: Uri? = null
    private var productRandomKey: String? = null
    private var downloadImageUrl: String? = null
    private var ProductImagesRef: StorageReference? = null
    private var ProductsRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null
    var fileNameList: ArrayList<String?> = ArrayList<String?>()
    var fileDoneList: ArrayList<String> = ArrayList<String>()
    lateinit var binding: ActivityAdminAddNewProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddNewProductBinding.inflate(layoutInflater)

        //CategoryName = getIntent().getExtras().get(AppConstants.category).toString();
        CategoryName = "cqat"
        ProductImagesRef = FirebaseStorage.getInstance().reference.child("Product Images")
        ProductsRef = FirebaseDatabase.getInstance().reference.child("Products")
        et_size = findViewById(R.id.product_size)
        et_location = findViewById(R.id.product_location_admin)
        et_number = findViewById(R.id.contact_number)
        loadingBar = ProgressDialog(this)
        binding.selectProductImage!!.setOnClickListener { OpenGallery() }
        binding.addNewProduct.setOnClickListener { ValidateProductData() }

        initilize()
    }

    private fun initilize() {

        binding.rbData.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_owner -> {
                    ownerORagent = AppConstants.owner
                }
                R.id.rb_agent -> {
                    ownerORagent = AppConstants.agent
                }
            }
        }

        binding.spPropertyType.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2>0){
                    val propertyArray = resources.getStringArray(R.array.property_type)
                    type = propertyArray[p2]
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    private fun OpenGallery() {
        val intent = Intent()
        intent.setType("image/*")
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            /*ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);*/
            StoreProductInformation(data)
        }

        /*if (requestCode == 1 && resultCode == RESULT_OK){
            if (data.getClipData() != null){
                //Toast.makeText(this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();
                int totalItems = data.getClipData().getItemCount();
                for (int i = 0;i < totalItems; i++){
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileName(fileUri);
                    fileNameList.add(fileName);
                    fileDoneList.add("Uploading");

                    StoreProductInformation(data);
                }
            } else if (data.getData() != null){
                Toast.makeText(this, "Selected Single File", Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    private fun ValidateProductData() {
        Description = binding.productDiscription.text.toString()
        Price = binding.productPrice.text.toString()
        Pname = binding.projectName.text.toString()
        propertysize = et_size!!.text.toString()
        location = et_location!!.text.toString()
        number = et_number!!.text.toString()
        katha = binding.edtPropertyKatha.text.toString()
        ownerName = binding.edtOwnerName.text.toString()
        facing = binding.edtPropertyFacing.text.toString()

        if (TextUtils.isEmpty(downloadImageUrl)) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show()
        }else if (TextUtils.isEmpty(ownerORagent)) {
            Toast.makeText(this, "Please select whether are you owner or agent", Toast.LENGTH_SHORT).show()
        }else if (TextUtils.isEmpty(type)) {
            Toast.makeText(this, "Please select property type...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(this, "Please enter property Price...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Pname)) {
            Toast.makeText(this, "Please enter property name...", Toast.LENGTH_SHORT).show()
        }else if (TextUtils.isEmpty(katha)) {
            Toast.makeText(this, "Please write property katha...", Toast.LENGTH_SHORT).show()
        }else if (TextUtils.isEmpty(ownerName)) {
            Toast.makeText(this, "Please enter property owner name...", Toast.LENGTH_SHORT).show()
        }else if (TextUtils.isEmpty(facing)) {
            Toast.makeText(this, "Please enter property facing...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "Please enter property description...", Toast.LENGTH_SHORT).show()
        } else {
            SaveProductInfoToDatabase()
        }
    }

    private fun StoreProductInformation(data: Intent) {
        loadingBar?.setTitle("Property Posting");
        loadingBar?.setMessage("please wait while we are adding the new Property.");
        loadingBar?.setCanceledOnTouchOutside(false);
        loadingBar?.show();
        downloadImageUrl = ""
        val totalItems = data.clipData!!.itemCount
        for (i in 0 until totalItems) {
            val fileUri = data.clipData!!.getItemAt(i).uri
            val fileName = getFileName(fileUri)
            fileNameList.add(fileName)
            fileDoneList.add("Uploading")
            productRandomKey = UtilityMethods.getCurrentTimeDate()
            val filePath = ProductImagesRef!!.child(fileUri.lastPathSegment + productRandomKey + ".jpg")
            val uploadTask = filePath.putFile(fileUri)
            uploadTask.addOnFailureListener { e ->
                val message = e.toString()
                UtilityMethods.showToast(this@AdminAddNewProductActivity,"Error: $message", )
                loadingBar!!.dismiss()
            }.addOnSuccessListener {
                UtilityMethods.showToast(this@AdminAddNewProductActivity,"Property Image uploaded Successfully..." )
                val urlTask = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    filePath.downloadUrl
                }
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            downloadImageUrl = if (downloadImageUrl == "") {
                                task.result.toString()
                            } else {
                                downloadImageUrl + "---" + task.result.toString()
                            }
                            UtilityMethods.showToast(this@AdminAddNewProductActivity,"got the Property image Url Successfully..." )
                        }
                    }
            }
        }
    }

    private fun SaveProductInfoToDatabase() {
        val productMap = HashMap<String, Any?>()
        productMap[AppConstants.pid] = productRandomKey
        productMap[AppConstants.owner] = ownerORagent
        productMap[AppConstants.date] = saveCurrentDate
        productMap[AppConstants.time] = saveCurrentTime
        productMap[AppConstants.description] = Description
        productMap[AppConstants.image] = downloadImageUrl
        productMap[AppConstants.category] = CategoryName
        productMap[AppConstants.price] = Price
        productMap[AppConstants.pname] = Pname
        productMap[AppConstants.type] = type
        productMap["Approval"] = 1
        productMap[AppConstants.propertysize] = propertysize
        productMap[AppConstants.location] = location
        productMap[AppConstants.number] = number
        ProductsRef!!.child(productRandomKey!!).updateChildren(productMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Intent intent = new Intent(AdminAddNewProductActivity.this, .class);
                    //startActivity(intent);
                    loadingBar!!.dismiss()
                    Toast.makeText(
                        this@AdminAddNewProductActivity,
                        "Product is added successfully..",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loadingBar!!.dismiss()
                    val message = task.exception.toString()
                    Toast.makeText(
                        this@AdminAddNewProductActivity,
                        "Error: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
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
