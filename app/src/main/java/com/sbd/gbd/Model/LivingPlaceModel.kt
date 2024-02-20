package com.sbd.gbd.Model

class LivingPlaceModel(
    var pid: String,
    var saveCurrentDate: String = "",
    var saveCurrentTime: String = "",
    var title: String = "",
    var category: String = "",
    var rent_lease: String = "",
    var floore: String = "",
    var rentamount: String = "",
    var location: String = "",
    var contactNumber: String = "" ,
    var verified: String = "",
    var nuBHK: String = "",
    var sqft: String = "",
    var water: String = "",
    var parking: String = "",
    var postedBY: String = "",
    var discription: String = "",
    var image: String = "",
    var image2: String = "",
    var deposit : String = "",
    var area : String = "",
    var bedroom : String = "",
    var bathroom : String = "",
    var balconey : String = "",
    var availableFrom : String = "",
    var postedOn : String = "",
    var status : String = ""
){
    constructor(): this("", "")
}


