package com.chiranths.jobportal1.Activities.Propertys;



public class Products {


    private String pname, description, price, image, category, pid, date, time,type,propertysize
            ,location,number,image2,text1,text2,text3,text4,Postedby,postedOn;

    int Approval;
    public Products()
    {

    }

    public Products(String postedOn,String pname, String description, String price, String image, String category, String pid, String date, String time, String type, String propertysize, String location, String number, String image2, String text1, String text2, String text3, String text4, String postedby, int approval) {
        this.postedOn = postedOn;
        this.pname = pname;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.type = type;
        this.propertysize = propertysize;
        this.location = location;
        this.number = number;
        this.image2 = image2;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
        this.Postedby = postedby;
        this.Approval = approval;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public int getApproval() {
        return Approval;
    }
    public void setApproval(int approval) {
        this.Approval = approval;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String getText4() {
        return text4;
    }

    public void setText4(String text4) {
        this.text4 = text4;
    }

    public String getPostedby() {
        return Postedby;
    }

    public void setPostedby(String postedby) {
        this.Postedby = postedby;
    }
}
