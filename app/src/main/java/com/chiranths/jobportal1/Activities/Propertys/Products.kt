package com.chiranths.jobportal1.Activities.Propertys

class Products {
    var pname: String? = null
    var description: String? = null
    var price: String? = null
    var image: String? = null
    var category: String? = null
    var pid: String? = null
    var date: String? = null
    var time: String? = null
    var type: String? = null
    var propertysize: String? = null
    var location: String? = null
    var number: String? = null
    var image2: String? = null
    var text1: String? = null
    var text2: String? = null
    var text3: String? = null
    var text4: String? = null
    var postedby: String? = null
    var postedOn: String? = null
    var approval = 0

    constructor() {}
    constructor(
        postedOn: String?,
        pname: String?,
        description: String?,
        price: String?,
        image: String?,
        category: String?,
        pid: String?,
        date: String?,
        time: String?,
        type: String?,
        propertysize: String?,
        location: String?,
        number: String?,
        image2: String?,
        text1: String?,
        text2: String?,
        text3: String?,
        text4: String?,
        postedby: String?,
        approval: Int
    ) {
        this.postedOn = postedOn
        this.pname = pname
        this.description = description
        this.price = price
        this.image = image
        this.category = category
        this.pid = pid
        this.date = date
        this.time = time
        this.type = type
        this.propertysize = propertysize
        this.location = location
        this.number = number
        this.image2 = image2
        this.text1 = text1
        this.text2 = text2
        this.text3 = text3
        this.text4 = text4
        this.postedby = postedby
        this.approval = approval
    }
}