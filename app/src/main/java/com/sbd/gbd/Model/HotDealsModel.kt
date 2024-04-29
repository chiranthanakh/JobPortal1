package com.sbd.gbd.Model

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
@Keep
 class HotDealsModel{
    var pname: String? = ""
    var description: String? = ""
    var price: String? = ""
    var image: String? = ""
    var category: String? = ""
    var pid: String? = ""
    var date: String? = ""
    var time: String? = ""
    var type: String? = ""
    var propertysize: String? = ""
    var location: String? = ""
    var number: String? = ""
    var image2: String? = ""
    var text1: String? = ""
    var text2: String? = ""
    var text3: String? = ""
    var text4: String? = ""
    var postedby: String? = ""
    var postedOn: String? = ""
    var approval = 0
    var ownerName : String? = ""
    var timings : String? =""
}
