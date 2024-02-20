package com.sbd.gbd.Model

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
        approval: Int,
        ownerName : String?,
        timings : String?
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
        this.ownerName = ownerName
        this.timings = timings
    }
}
