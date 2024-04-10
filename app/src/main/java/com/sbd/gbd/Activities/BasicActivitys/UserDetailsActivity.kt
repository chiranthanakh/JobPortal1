package com.sbd.gbd.Activities.BasicActivitys

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.UtilityMethods
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sbd.gbd.Utilitys.PreferenceManager

class UserDetailsActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var edtPhone: EditText? = null
    private val edtOTP: EditText? = null
    private var edtEmail: EditText? = null
    private var edtName: EditText? = null
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    private val verifyOTPBtn: Button? = null
    private var submit: Button? = null
    private val verificationId: String? = null
    private var Profiles: DatabaseReference? = null
    private var ProfileImagesRef: StorageReference? = null
    private var productRandomKey: String? = null
    private var downloadImageUrl: String? = null
    private var MainProfileUrl: String? = null
    private var loadingBar: ProgressDialog? = null
    var fileNameList: ArrayList<String> = ArrayList<String>()
    var fileDoneList: ArrayList<String> = ArrayList<String>()
    var profileImage: ImageView? = null
    private var ImageUri: Uri? = null
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        preferenceManager= PreferenceManager(this);
        mAuth = FirebaseAuth.getInstance()
        Profiles = FirebaseDatabase.getInstance().reference.child("Profile")
        ProfileImagesRef = FirebaseStorage.getInstance().reference.child("ProfileImage")
        initilize()
    }

    private fun initilize() {
        edtPhone = findViewById(R.id.edt_phone_login)
        edtName = findViewById(R.id.edt_name_login)
        edtEmail = findViewById(R.id.edt_email_login)
        profileImage = findViewById(R.id.select_profile_image)
        submit = findViewById(R.id.btn_submit_login)
        loadingBar = ProgressDialog(this)
        val number = intent.getStringExtra("usernumber")
        edtPhone?.setText(number)
        profileImage?.setOnClickListener { OpenGallery() }

        submit?.setOnClickListener {
            val number = edtName?.getText().toString()
            val name = edtName?.getText().toString()
            val email = edtEmail?.getText().toString()
            if (number != "" && name != "" && email != "") {
                preferenceManager.saveLoginState(true)
                SaveProductInfoToDatabase()
                val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                val myEdit = sharedPreferences.edit()
                myEdit.putString("name", edtName?.getText().toString())
                myEdit.putString(AppConstants.number, edtPhone?.getText().toString())
                myEdit.commit()
                finish()
            } else {
                Toast.makeText(
                    this@UserDetailsActivity,
                    "Please Enter all details...",
                    Toast.LENGTH_SHORT
                ).show()
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

    private fun StoreProductInformation(data: Intent) {
        downloadImageUrl = ""
        var fileUri = data.data

        if (data.data != null) {
            profileImage?.setImageURI(fileUri)
        } else if (data.clipData != null) {
            val clipData = data.clipData
            fileUri = clipData?.getItemAt(0)?.uri
                profileImage?.setImageURI(fileUri)
        }

        val fileName = getFileName(fileUri)
        profileImage!!.setImageURI(fileUri)
        fileNameList.add(fileName!!)
        productRandomKey = UtilityMethods.getCurrentTimeDate()
        val filePath =
            ProfileImagesRef!!.child(fileUri!!.lastPathSegment + productRandomKey + ".jpg")
        val uploadTask = filePath.putFile(fileUri)
        uploadTask.addOnFailureListener { e ->
            val message = e.toString()
            Toast.makeText(this@UserDetailsActivity, "Error: $message", Toast.LENGTH_SHORT).show()
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
                            MainProfileUrl = task.result.toString()
                        } else {
                            downloadImageUrl = downloadImageUrl + "---" + task.result.toString()
                        }
                    }
                }
        }
    }

    private fun SaveProductInfoToDatabase() {
        val productMap = HashMap<String, Any?>()
        productMap["name"] = edtName!!.text.toString()
        productMap["Email"] = edtEmail!!.text.toString()
        productMap[AppConstants.image] = MainProfileUrl
        productMap[AppConstants.number] = edtPhone!!.text.toString()
        Profiles!!.child(edtPhone!!.text.toString()).updateChildren(productMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loadingBar!!.dismiss()
                    Toast.makeText(
                        this@UserDetailsActivity,
                        "Profile successfull",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    loadingBar!!.dismiss()
                    val message = task.exception.toString()
                    Toast.makeText(this@UserDetailsActivity, "Error: $message", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    @SuppressLint("Range")
    fun getFileName(uri: Uri?): String? {
        var result: String? = null
        if (uri?.scheme == "content") {
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
            result = uri?.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                if (cut != null) {
                    result = result?.substring(cut + 1)
                }
            }
        }
        return result
    }

    companion object {
        private const val GalleryPick = 1
    }
}