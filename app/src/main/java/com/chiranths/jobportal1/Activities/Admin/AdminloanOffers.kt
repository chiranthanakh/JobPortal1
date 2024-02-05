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

class AdminloanOffers : AppCompatActivity() {
    private var BankName: String? = null
    private var BankIntrestRate: String? = null
    private var BankLoanType: String? = null
    private var BankLoanamount: String? = null
    private var BankLOanDiscription: String? = null
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    var et_bankname: EditText? = null
    var et_loantype: EditText? = null
    var et_loanintrestrate: EditText? = null
    var et_loanamount: EditText? = null
    var et_loandiscription: EditText? = null
    private var ImageUri: Uri? = null
    private var productRandomKey: String? = null
    private var downloadImageUrl: String? = null
    private var MainimageUrl: String? = null
    private var loanImagesRef: StorageReference? = null
    private var loanRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null
    private var postplace: String? = ""
    var fileNameList: ArrayList<String> = ArrayList<String>()
    var fileDoneList: ArrayList<String> = ArrayList<String>()
    private var placement: Spinner? = null
    var ads_name: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_loanoffers)
        loanImagesRef = FirebaseStorage.getInstance().reference.child("bank")
        loanRef = FirebaseDatabase.getInstance().reference.child("banksadsforyou")
        val btn_corosel = findViewById<ImageView>(R.id.loan_image)
        val add_new_corosel = findViewById<Button>(R.id.btn_loanoffer)
        et_bankname = findViewById<View>(R.id.et_bank_name) as EditText
        et_loantype = findViewById<View>(R.id.et_loan_type) as EditText
        et_loanamount = findViewById<View>(R.id.et_loanamount_upto) as EditText
        et_loanintrestrate = findViewById<View>(R.id.et_intrestrate) as EditText
        et_loandiscription = findViewById(R.id.et_loan_description)
        ads_name = findViewById(R.id.ads_name)
        placement = findViewById(R.id.sp_placement)
        loadingBar = ProgressDialog(this)
        btn_corosel.setOnClickListener { OpenGallery() }
        add_new_corosel.setOnClickListener { ValidateProductData() }

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
            ImageUri = data.data
            //InputProductImage.setImageURI(ImageUri);
            StoreProductInformation(data)
        }
    }

    private fun ValidateProductData() {
        BankName = et_bankname!!.text.toString()
        BankLoanType = et_loantype!!.text.toString()
        BankIntrestRate = et_loanintrestrate!!.text.toString()
        BankLOanDiscription = et_loandiscription!!.text.toString()
        BankLoanamount = et_loanamount!!.text.toString()
        if (TextUtils.isEmpty(downloadImageUrl)) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(BankLOanDiscription)) {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(BankIntrestRate)) {
            Toast.makeText(this, "Please write intrest Rate...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(BankLoanType)) {
            Toast.makeText(this, "Please write LoanType...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(BankName)) {
            Toast.makeText(this, "Please enter Bank name", Toast.LENGTH_SHORT).show()
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
        val filePath = loanImagesRef!!.child(uri!!.lastPathSegment + productRandomKey + ".jpg")
        val uploadTask = filePath.putFile(uri)
        uploadTask.addOnFailureListener { e ->
            val message = e.toString()
            Toast.makeText(this@AdminloanOffers, "Error: $message", Toast.LENGTH_SHORT).show()
            loadingBar!!.dismiss()
        }.addOnSuccessListener {
            Toast.makeText(
                this@AdminloanOffers,
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
                            this@AdminloanOffers,
                            "got the Product image Url Successfully...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun SaveProductInfoToDatabase() {
        if(postplace == AppConstants.carousel) {
            loanImagesRef = FirebaseStorage.getInstance().reference.child("Corosel")
            loanRef = FirebaseDatabase.getInstance().reference.child("Corosels")
        }
        val productMap = HashMap<String, Any?>()
        productMap[AppConstants.pid] = productRandomKey
        productMap[AppConstants.date] = saveCurrentDate
        productMap[AppConstants.time] = saveCurrentTime
        productMap[AppConstants.image2] = downloadImageUrl
        productMap[AppConstants.postedOn] = saveCurrentDate
        productMap[AppConstants.image] = MainimageUrl
        productMap[AppConstants.category] = "Loan"
        productMap["bankName"] = BankName
        productMap["loantype"] = BankLoanType
        productMap["loanamount"] = BankLoanamount
        productMap["intrestrate"] = BankIntrestRate
        productMap[AppConstants.description] = BankLOanDiscription
        productMap[AppConstants.Status] = 1
        loanRef!!.child(productRandomKey!!).updateChildren(productMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Intent intent = new Intent(AdminAddNewProductActivity.this, .class);
                    //startActivity(intent);
                    loadingBar!!.dismiss()
                    Toast.makeText(
                        this@AdminloanOffers,
                        "Product is added successfully..",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loadingBar!!.dismiss()
                    val message = task.exception.toString()
                    Toast.makeText(this@AdminloanOffers, "Error: $message", Toast.LENGTH_SHORT)
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
