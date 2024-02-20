package com.sbd.gbd.Model;

public class ProductInfo {

    String category,date,description,image,location,number,pid,pname,price,propertysize,time,type,posted;

    public ProductInfo(String category, String date, String description, String image, String location, String number, String pid, String pname, String price, String propertysize, String time, String type, String posted) {
        this.category = category;
        this.date = date;
        this.description = description;
        this.image = image;
        this.location = location;
        this.number = number;
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.propertysize = propertysize;
        this.time = time;
        this.type = type;
        this.posted = posted;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPropertysize() {
        return propertysize;
    }

    public void setPropertysize(String propertysize) {
        this.propertysize = propertysize;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
