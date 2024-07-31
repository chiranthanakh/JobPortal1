package com.sbd.gbd.Utilitys

import android.content.Context
import com.sbd.gbd.BuildConfig

object AppConstants {
    const val image = "image"
    const val image2 = "image2"
    const val pid = "pid"
    const val description = "description"
    const val date = "date"
    const val category = "category"
    const val price = "price"
    const val city = "city"
    const val pname = "pname"
    const val katha = "katha"
    const val propertysize = "propertysize"
    const val location = "location"
    const val number = "number"
    const val Status = "status"
    const val sitesAvailable = "sitesAvailable"
    const val postedBy = "postedBy"
    const val facing = "facing"
    const val layoutarea = "layoutarea"
    const val approvedBy = "approvedBy"
    const val ownership = "ownership"
    const val postedOn = "postedOn"
    const val verified = "verified"
    const val nearby = "nearBy"
    const val payment = "payment"
    const val type = "type"
    const val time = "time"
    const val text1 = "text1"
    const val text2 = "text2"
    const val text3 = "text3"
    const val text4 = "text4"
    const val point1 = "point1"
    const val point2 = "point2"
    const val point3 = "point3"
    const val point4 = "point4"
    const val carousel = "Carousel"
    const val profile = "Profile"
    const val owner = "owner"
    const val agent = "agent"
    const val notVerified = "VERIFIED PROPERTY"
    const val verified1 = "VERIFIED"
    const val Deposit = "deposit"
    const val district = "district"
    const val taluk = "taluk"
    const val products = "Products"
    const val hotdeals = "hotforyou"
    const val layouts = "layoutsforyou"
    const val ads = "adsforyou"
    const val Profiles = "Profile"
    const val construction = "constructionforyou"


    fun profileinfoadd(contest: Context): HashMap<String, Any?> {
        val sh = contest.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val productMap = HashMap<String, Any?>()
        productMap["loginUser"] = sh?.getString("name", "")
        productMap["loginNumber"] = sh?.getString(AppConstants.number, "")
        productMap["loginEmail"] = sh?.getString("email", "")
        productMap[time] = UtilityMethods.getDate()
        productMap[date] = UtilityMethods.getTime()
        return productMap
    }


    val user = when {
        BuildConfig.FLAVOR == "dev" -> if (BuildConfig.BUILD_TYPE == "debug") "2" else "1"
        BuildConfig.FLAVOR == "prod" -> if (BuildConfig.BUILD_TYPE == "debug") "1" else "2"
        BuildConfig.FLAVOR == "stag" -> "3"
        else -> "2"
    }

    // 1 dev
    // 2 prod
    //const val user = "1"

}