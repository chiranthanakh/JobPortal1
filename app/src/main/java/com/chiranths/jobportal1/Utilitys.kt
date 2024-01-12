package com.chiranths.jobportal1

import android.Manifest
import com.chiranths.jobportal1.CalldetailsRecords
import android.content.pm.PackageManager
import android.content.SharedPreferences
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import com.chiranths.jobportal1.Activities.OtpLoginActivity
import com.gun0912.tedpermission.PermissionListener

class Utilitys {
    var calldetails = CalldetailsRecords()
    private var permissionListener: PermissionListener? = null

    private fun checkWriteExternalPermission(context: Context): Boolean {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val res = context.checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    fun navigateCall(context: Context, cnumber: String?, cname: String?) {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val res = context.checkCallingOrSelfPermission(permission)
        val sh = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val nameofuser = sh.getString("name", "")
        val userNumber = sh.getString("number", "")
        val useremail = sh.getString("email", "")
        if (userNumber != "") {
            calldetails.callinfo(userNumber, nameofuser, cnumber, cname)
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$cnumber")
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    (context as Activity),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    100
                )
            } else {
                context.startActivity(callIntent)
            }
        } else {
            val intent = Intent(context, OtpLoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    fun navigateWhatsapp(context: Context, cnumber: String?, cname: String?) {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val res = context.checkCallingOrSelfPermission(permission)
        val sh = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val nameofuser = sh.getString("name", "")
        val userNumber = sh.getString("number", "")
        val useremail = sh.getString("email", "")
        if (userNumber != "") {
            val url = "https://api.whatsapp.com/send?phone=91$cnumber"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    (context as Activity),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    100
                )
            } else {
                context.startActivity(i)
            }
            calldetails.callinfo(userNumber, nameofuser, cnumber, cname)
        } else {
            val intent = Intent(context, OtpLoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}