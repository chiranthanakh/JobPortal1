package com.chiranths.jobportal1.Model;

public class BusinessModel {

    private String pid,date,time,Businessname,Business_category,description,price,location,number,owner,email,rating,image,image2,status;

    public BusinessModel(String pid, String date, String time, String businessname, String business_category, String description, String price, String location, String number, String owner, String email, String rating, String image, String image2, String status) {
        this.pid = pid;
        this.date = date;
        this.time = time;
        Businessname = businessname;
        Business_category = business_category;
        this.description = description;
        this.price = price;
        this.location = location;
        this.number = number;
        this.owner = owner;
        this.email = email;
        this.rating = rating;
        this.image = image;
        this.image2 = image2;
        this.status = status;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBusinessname() {
        return Businessname;
    }

    public void setBusinessname(String businessname) {
        Businessname = businessname;
    }

    public String getBusiness_category() {
        return Business_category;
    }

    public void setBusiness_category(String business_category) {
        Business_category = business_category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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