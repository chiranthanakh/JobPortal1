package com.sbd.gbd.Model

class LoanOffersModel(
    var pid: String? = "",
    @JvmField var bankname: String? = "",
    @JvmField var bankintrest: String? = "",
    @JvmField var bankamountprovid: String? = "",
    @JvmField var bankloantype: String? = "",
    var bankdiscription: String? = "",
    @JvmField var imageurl: String? = ""
)
