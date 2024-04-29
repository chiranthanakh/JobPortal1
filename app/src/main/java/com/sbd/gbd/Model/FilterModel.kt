package com.sbd.gbd.Model

class FilterModel {
    var pname: String? = null
    var description: String? = null
    var price: String? = null
    var image: String? = null
    var category: String? = null
    var pid: String? = null
    var date: String? = null
    var time: String? = null
    var type: String? = null
    var size: String? = null
    var location: String? = null
    var number: String? = null
    var status: String? = null

    constructor()
    constructor(
        pname: String?,
        description: String?,
        price: String?,
        image: String?,
        category: String?,
        pid: String?,
        date: String?,
        time: String?,
        type: String?,
        size: String?,
        location: String?,
        number: String?,
        status: String?
    ) {
        this.pname = pname
        this.description = description
        this.price = price
        this.image = image
        this.category = category
        this.pid = pid
        this.date = date
        this.time = time
        this.type = type
        this.size = size
        this.location = location
        this.number = number
        this.status = status
    }
}
