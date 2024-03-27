package com.sbd.gbd.Model

class Model {
    var id: String? = null
    var mail: String? = null
    var name: String? = null
    var profilepic: String? = null
    var role: String? = null
    var aboutJob: String? = null
    var addationalInfo: String? = null
    var adminId: String? = null
    var userName: String? = null
    var resumeLink: String? = null
    var userId: String? = null
    @JvmField
    var companyName: String? = null
    var jobLastDate: String? = null
    var jobSalary: String? = null
    var jobStartDate: String? = null
    @JvmField
    var jobTitle: String? = null
    var skillsRequired: String? = null
    var totalOpenings: String? = null

    constructor()
    constructor(
        id: String?,
        mail: String?,
        name: String?,
        profilepic: String?,
        role: String?,
        aboutJob: String?,
        addationalInfo: String?,
        adminId: String?,
        userName: String?,
        resumeLink: String?,
        userId: String?,
        companyName: String?,
        jobLastDate: String?,
        jobSalary: String?,
        jobStartDate: String?,
        jobTitle: String?,
        skillsRequired: String?,
        totalOpenings: String?
    ) {
        this.id = id
        this.mail = mail
        this.name = name
        this.profilepic = profilepic
        this.role = role
        this.aboutJob = aboutJob
        this.addationalInfo = addationalInfo
        this.adminId = adminId
        this.userName = userName
        this.resumeLink = resumeLink
        this.userId = userId
        this.companyName = companyName
        this.jobLastDate = jobLastDate
        this.jobSalary = jobSalary
        this.jobStartDate = jobStartDate
        this.jobTitle = jobTitle
        this.skillsRequired = skillsRequired
        this.totalOpenings = totalOpenings
    }
}
