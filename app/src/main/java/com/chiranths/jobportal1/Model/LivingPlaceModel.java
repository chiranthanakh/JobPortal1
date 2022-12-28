package com.chiranths.jobportal1.Model;

public class LivingPlaceModel {
    String  pid,saveCurrentDate, saveCurrentTime,title,category,rent_lease,floore,rentamount,location,contactNumber,verified,nuBHK,sqft,water,parking,postedBY,discription,image,image2;

    public LivingPlaceModel(String pid, String saveCurrentDate, String saveCurrentTime, String title, String category, String rent_lease, String floore, String rentamount, String location, String contactNumber, String verified, String nuBHK, String sqft, String water, String parking, String postedBY, String discription, String image, String image2) {
        this.pid = pid;
        this.saveCurrentDate = saveCurrentDate;
        this.saveCurrentTime = saveCurrentTime;
        this.title = title;
        this.category = category;
        this.rent_lease = rent_lease;
        this.floore = floore;
        this.rentamount = rentamount;
        this.location = location;
        this.contactNumber = contactNumber;
        this.verified = verified;
        this.nuBHK = nuBHK;
        this.sqft = sqft;
        this.water = water;
        this.parking = parking;
        this.postedBY = postedBY;
        this.discription = discription;
        this.image = image;
        this.image2 = image2;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRent_lease() {
        return rent_lease;
    }

    public void setRent_lease(String rent_lease) {
        this.rent_lease = rent_lease;
    }

    public String getFloore() {
        return floore;
    }

    public void setFloore(String floore) {
        this.floore = floore;
    }

    public String getRentamount() {
        return rentamount;
    }

    public void setRentamount(String rentamount) {
        this.rentamount = rentamount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getNuBHK() {
        return nuBHK;
    }

    public void setNuBHK(String nuBHK) {
        this.nuBHK = nuBHK;
    }

    public String getSqft() {
        return sqft;
    }

    public void setSqft(String sqft) {
        this.sqft = sqft;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getPostedBY() {
        return postedBY;
    }

    public void setPostedBY(String postedBY) {
        this.postedBY = postedBY;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}
