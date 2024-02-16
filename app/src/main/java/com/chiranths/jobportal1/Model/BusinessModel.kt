package com.chiranths.jobportal1.Model

class BusinessModel {
    var pid: String? = ""
    var date: String? = ""
    var time: String? = ""
    var Businessname: String? = ""
    var Business_category: String? = ""
    var Category: String? = ""
    var description: String? = ""
    var price: String? = ""
    var location: String? = ""
    var number: String? = ""
    var owner: String? = ""
    var email: String? = ""
    var rating: String? = ""
    var image: String? = ""
    var image2: String? = ""
    var status: String? = ""
    var gst : String? = ""
    var from: String? = ""
    var productServicess: String = ""
    var workingHrs: String = ""


    constructor() {}
    constructor(
        pid: String?,
        date: String?,
        time: String?,
        businessname: String?,
        business_category: String?,
        category: String?,
        description: String?,
        price: String?,
        location: String?,
        number: String?,
        owner: String?,
        email: String?,
        rating: String?,
        image: String?,
        image2: String?,
        status: String?,
        gst: String?,
        from: String?,
        productServicess: String,
        workingHrs : String
    ) {
        this.pid = pid
        this.date = date
        this.time = time
        this.Businessname = businessname
        this.Business_category = business_category
        this.description = description
        this.price = price
        this.location = location
        this.number = number
        this.owner = owner
        this.email = email
        this.rating = rating
        this.image = image
        this.image2 = image2
        this.status = status
        this.gst = gst
        this.from = from
        this.productServicess = productServicess
        this.workingHrs = workingHrs
    }
}