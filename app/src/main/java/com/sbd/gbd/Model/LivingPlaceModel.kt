package com.sbd.gbd.Model

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
@Keep
class LivingPlaceModel(
    var pid: String,
    var saveCurrentDate: String = "",
    var saveCurrentTime: String = "",
    var pname: String = "",
    var category: String = "",
    var rent_lease: String = "",
    var buildingType: String = "",
    var floors: String = "",
    var rent: String = "",
    var deposit: String= "",
    var location: String = "",
    var number: String = "" ,
    var verified: String = "",
    var nuBhk: String = "",
    var propertysize: String = "",
    var water: String = "",
    var parking: String = "",
    var postedBy: String = "",
    var description: String = "",
    var image: String = "",
    var image2: String = "",
    var area : String = "",
    var bedrooms : String = "",
    var bathrooms : String = "",
    var balconey : String = "",
    var availableFrom : String = "",
    var postedOn : String = "",
    var status : String = "",
    var facing: String = "",
    var gated: String = "",
    var immidate: String = "",
    var type : String = "",
    var furnished : String = ""
){
    constructor(): this("", "")
}


