package com.chiranths.jobportal1.Model;

public class Categorymmodel {

    private String pid,image,category,subcategory;

    public Categorymmodel(String pid, String image, String category, String subcategory) {
        this.pid = pid;
        this.image = image;
        this.category = category;
        this.subcategory = subcategory;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
}
