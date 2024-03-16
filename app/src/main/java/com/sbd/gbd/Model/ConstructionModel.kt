package com.sbd.gbd.Model

import androidx.annotation.Keep

@Keep
data class ConstructionModel(
    var pid: String? = "",
    var saveCurrentDate: String? = "",
    var saveCurrentTime: String? = "",
    var name: String? = "",
    var category: String? = "",
    var cost: String? = "",
    var contactDetails: String? = "",
    var product_services: String? = "",
    var experience: String? = "",
    var service1: String? = "",
    var service2: String? = "",
    var service3: String? = "",
    var service4: String? = "",
    var description: String? = "",
    var verified: String? = "",
    var image: String? = "",
    var image2: String? = "",
    var owner: String? = "",
    var address: String? = "",
    var status: String? = "",
    var gst: String? = "",
    var workingHrs: String? = ""
)