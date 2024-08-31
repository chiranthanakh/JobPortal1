package com.sbd.gbd.Model

import androidx.annotation.Keep

@Keep
data class AdsModel(
    var image: String? = "",
    var image2: String? = "",
    var pid: String? = "",
    var description: String? = "",
    var date: String? = "",
    var category: String? = "",
    var price: String? = "",
    var pname: String? = "",
    var propertysize: String? = "",
    var location: String? = "",
    var number: String? = "",
    var road: String? = "",
    var nearBy: String? = "",
    var boarwell: String? = "",
    var fencing: String? = "",
    var status: String? = "2",
    var postedBy: String? = "",
    var approvedBy: String? = "",
    var type: String? = "",
    var facing: String? = "",
    var ownership: String?= "",
    var postedOn: String?="",
    var verified: String?="",
    var katha: String?="",
    var text1: String? = "",
    var text2: String? = "",
    var text3: String? = "",
    var text4: String? = "",
    var city: String? = "",
    var taluk: String?="",
    var district: String?="",
)