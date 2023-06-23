package com.chiranths.jobportal1.Model

class ConstructionModel {
    var pid: String? = ""
    var saveCurrentDate: String? = ""
    var saveCurrentTime: String? = ""
    var name: String? = ""
    var category: String? = ""
    var cost: String? = ""
    var contactDetails: String? = ""
    var product_services: String? = ""
    var experience: String? = ""
    var service1: String? = ""
    var service2: String? = ""
    var service3: String? = ""
    var service4: String? = ""
    var discription: String? = ""
    var verified: String? = ""
    var image: String? = ""
    var image2: String? = ""
    var owner: String? = ""
    var address: String? = ""
    var status: String? = ""
    var gst: String? = ""
    var workingHrs: String? = ""

    constructor(
        pid: String?,
        saveCurrentDate: String?,
        saveCurrentTime: String?,
        name: String?,
        category: String?,
        cost: String?,
        contactDetails: String?,
        product_services: String?,
        experience: String?,
        service1: String?,
        service2: String?,
        service3: String?,
        service4: String?,
        discription: String?,
        verified: String?,
        image: String?,
        image2: String?,
        owner: String?,
        address: String?,
        status: String?,
        gst: String?,
        workingHrs: String?
    ) {
        this.pid = pid
        this.saveCurrentDate = saveCurrentDate
        this.saveCurrentTime = saveCurrentTime
        this.name = name
        this.category = category
        this.cost = cost
        this.contactDetails = contactDetails
        this.product_services = product_services
        this.experience = experience
        this.service1 = service1
        this.service2 = service2
        this.service3 = service3
        this.service4 = service4
        this.discription = discription
        this.verified = verified
        this.image = image
        this.image2 = image2
        this.owner = owner
        this.address = address
        this.status = status
        this.gst = gst
        this.workingHrs = workingHrs
    }

    constructor() {}

}