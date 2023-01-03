package com.chiranths.jobportal1.Model;

public class ConstructionModel {

    String pid,saveCurrentDate, saveCurrentTime,name,category,cost,contactDetails,contactDetails2,experience,service1,service2,service3,service4,discription,verified,image,image2;

    public ConstructionModel(String pid, String saveCurrentDate, String saveCurrentTime, String name, String category, String cost, String contactDetails, String contactDetails2, String experience, String service1, String service2, String service3, String service4, String discription, String verified, String image, String image2) {
        this.pid = pid;
        this.saveCurrentDate = saveCurrentDate;
        this.saveCurrentTime = saveCurrentTime;
        this.name = name;
        this.category = category;
        this.cost = cost;
        this.contactDetails = contactDetails;
        this.contactDetails2 = contactDetails2;
        this.experience = experience;
        this.service1 = service1;
        this.service2 = service2;
        this.service3 = service3;
        this.service4 = service4;
        this.discription = discription;
        this.verified = verified;
        this.image = image;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getContactDetails2() {
        return contactDetails2;
    }

    public void setContactDetails2(String contactDetails2) {
        this.contactDetails2 = contactDetails2;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getService1() {
        return service1;
    }

    public void setService1(String service1) {
        this.service1 = service1;
    }

    public String getService2() {
        return service2;
    }

    public void setService2(String service2) {
        this.service2 = service2;
    }

    public String getService3() {
        return service3;
    }

    public void setService3(String service3) {
        this.service3 = service3;
    }

    public String getService4() {
        return service4;
    }

    public void setService4(String service4) {
        this.service4 = service4;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
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
