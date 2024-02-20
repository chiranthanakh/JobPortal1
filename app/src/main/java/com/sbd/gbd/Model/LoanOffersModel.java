package com.sbd.gbd.Model;

public class LoanOffersModel {

    String pid,bankname,bankintrest,bankamountprovid,bankloantype,bankdiscription,imageurl;

    public LoanOffersModel(String pid,String bankname, String bankintrest, String bankamountprovid, String bankloantype, String bankdiscription, String imageurl) {
        this.pid = pid;
        this.bankname = bankname;
        this.bankintrest = bankintrest;
        this.bankamountprovid = bankamountprovid;
        this.bankloantype = bankloantype;
        this.bankdiscription = bankdiscription;
        this.imageurl = imageurl;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankintrest() {
        return bankintrest;
    }

    public void setBankintrest(String bankintrest) {
        this.bankintrest = bankintrest;
    }

    public String getBankamountprovid() {
        return bankamountprovid;
    }

    public void setBankamountprovid(String bankamountprovid) {
        this.bankamountprovid = bankamountprovid;
    }

    public String getBankloantype() {
        return bankloantype;
    }

    public void setBankloantype(String bankloantype) {
        this.bankloantype = bankloantype;
    }

    public String getBankdiscription() {
        return bankdiscription;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setBankdiscription(String bankdiscription) {
        this.bankdiscription = bankdiscription;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
