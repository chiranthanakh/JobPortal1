package com.sbd.gbd.Model

import androidx.annotation.Keep

@Keep
data class BusinessModel(
    var pid: String? = "",
    var date: String? = "",
    var time: String? = "",
    var Businessname: String? = "",
    var Business_category: String? = "",
    var category: String? = "",
    var description: String? = "",
    var price: String? = "",
    var location: String? = "",
    var number: String? = "",
    var owner: String? = "",
    var email: String? = "",
    var rating: String? = "",
    var image: String? = "",
    var image2: String? = "",
    var status: String? = "",
    var gst : String? = "",
    var from: String? = "",
    var city: String? = "",
    var productServicess: String = "",
    var workingHrs: String = ""
)


