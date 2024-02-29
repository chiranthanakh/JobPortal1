package com.sbd.gbd.Model;

public class PropertytModel {

    private String image,pid,description,category,price,pname,propertysize,location,number,type;

    public PropertytModel(String image, String pid, String description, String category, String price, String pname, String propertysize, String location, String number, String type) {
        this.image = image;
        this.pid = pid;
        this.description = description;
        this.category = category;
        this.price = price;
        this.pname = pname;
        this.propertysize = propertysize;
        this.location = location;
        this.number = number;
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPropertysize() {
        return propertysize;
    }

    public void setPropertysize(String propertysize) {
        this.propertysize = propertysize;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
