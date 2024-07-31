package com.sbd.gbd.Utilitys

import android.R
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


object UtilityMethods {

    var locationList: ArrayList<String> = ArrayList()
    var locationMap = mutableMapOf<String, String>()
    val districtList : ArrayList<String> = ArrayList()
    fun showToast(context: Context, message:String){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }

    fun getCurrentTimeDate() :String {
        val calendar: Calendar = Calendar.getInstance()
        val currentDate: Date = calendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate: String = dateFormat.format(currentDate)
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        val formattedTime: String = timeFormat.format(currentDate)
         return formattedDate+formattedTime
    }

    fun getTime(): String {
        val calendar: Calendar = Calendar.getInstance()
        val currentDate: Date = calendar.time
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        val formattedTime: String = timeFormat.format(currentDate)
        return formattedTime
    }
    fun getDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val currentDate: Date = calendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate: String = dateFormat.format(currentDate)
        return formattedDate
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
            .isConnected
    }

    // Function to compress image


    fun from(context: Context, uri: Uri): File {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputFile = File(context.cacheDir, "temp_image.jpg")
        val outputStream = FileOutputStream(outputFile)
        inputStream?.copyTo(outputStream)
        outputStream.close()
        inputStream?.close()
        return outputFile
    }

    @SuppressLint("NewApi")
    fun getPathFromUri(context: Context, uri: Uri?): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val cursor = context.contentResolver.query(
                uri!!,
                arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE),
                null,
                null
            )
            cursor!!.moveToFirst()

            val displayName =
                cursor.getString(cursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            val size = cursor.getLong(cursor!!.getColumnIndex(OpenableColumns.SIZE))

            val file = File(context.filesDir.toString() + "/" + displayName)
            try {
                val fileOutputStream = FileOutputStream(file)
                val inputStream = context.contentResolver.openInputStream(uri)
                val buffers = ByteArray(1024)
                var read: Int
                while ((inputStream!!.read(buffers).also { read = it }) != -1) {
                    fileOutputStream.write(buffers, 0, read)
                }
                inputStream.close()
                fileOutputStream.close()
                return file.path
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun getFileName(contentResolver: ContentResolver, uri: Uri?): String? {
        var result: String? = null
        if (uri?.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor.use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri?.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result
    }

     fun CompressImage(contentResolver: ContentResolver,uri: Uri): ByteArray {
        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream)
        return outputStream.toByteArray()
    }

    private fun getlocations() {
        districtList.add("Select District")
        val myDataRef = FirebaseDatabase.getInstance().reference.child("Locations")
        myDataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataMap = dataSnapshot.value as HashMap<*, *>?
                for (key in dataMap!!.keys) {
                    val data = dataMap[key]
                    try {
                        val userData = data as HashMap<*, *>?
                        districtList.add(userData!!["district"].toString())
                        locationMap.put(userData["taluk"].toString(),userData["district"].toString())
                    } catch (_: ClassCastException) {
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to read value: ${error.message}")
            }
        })
    }

    fun getDistricts(): ArrayList<String> {
        getlocations()
        return districtList
    }

    fun getTaluks(district: String): ArrayList<String> {
        locationList.clear()
        locationList.add("Select Taluk")
        locationMap.forEach{
            if (district == it.value) {
                locationList.add(it.key)
            }
        }
        return locationList
    }
}