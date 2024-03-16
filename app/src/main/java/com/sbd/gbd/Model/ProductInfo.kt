package com.sbd.gbd.Model

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
@Keep
data class ProductInfo(
    var category: String,
    var date: String,
    var description: String,
    var image: String,
    var location: String,
    var number: String,
    var pid: String,
    var pname: String,
    var price: String,
    var propertysize: String,
    var time: String,
    var type: String,
    var posted: String
)
