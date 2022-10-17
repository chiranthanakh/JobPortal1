package com.chiranths.jobportal1.Model;

public class Corosolmodel {

    private String  imageurl, type;

    public Corosolmodel(String imageurl, String type) {
        this.imageurl = imageurl;
        this.type = type;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
