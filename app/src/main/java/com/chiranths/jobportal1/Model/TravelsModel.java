package com.chiranths.jobportal1.Model;

public class TravelsModel {

    String  pid,saveCurrentDate, saveCurrentTime,vehicle,category,VehicleNumber,costperKM,contactDetails,ownerName,verified,discription,image,image2,model;

    public TravelsModel(String pid, String saveCurrentDate, String saveCurrentTime, String vehicle, String category, String vehicleNumber, String costperKM, String contactDetails, String ownerName, String verified, String discription, String image, String image2, String model) {
        this.pid = pid;
        this.saveCurrentDate = saveCurrentDate;
        this.saveCurrentTime = saveCurrentTime;
        this.vehicle = vehicle;
        this.category = category;
        this.VehicleNumber = vehicleNumber;
        this.costperKM = costperKM;
        this.contactDetails = contactDetails;
        this.ownerName = ownerName;
        this.verified = verified;
        this.discription = discription;
        this.image = image;
        this.image2 = image2;
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSaveCurrentDate() {
        return saveCurrentDate;
    }

    public void setSaveCurrentDate(String saveCurrentDate) {
        this.saveCurrentDate = saveCurrentDate;
    }

    public String getSaveCurrentTime() {
        return saveCurrentTime;
    }

    public void setSaveCurrentTime(String saveCurrentTime) {
        this.saveCurrentTime = saveCurrentTime;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVehicleNumber() {
        return VehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        VehicleNumber = vehicleNumber;
    }

    public String getCostperKM() {
        return costperKM;
    }

    public void setCostperKM(String costperKM) {
        this.costperKM = costperKM;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }
}
