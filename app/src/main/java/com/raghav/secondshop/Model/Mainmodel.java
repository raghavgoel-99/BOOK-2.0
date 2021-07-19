package com.raghav.secondshop.Model;

public class Mainmodel { String image;
    String pname,price,description,pid;

    public Mainmodel() {
    }

    public Mainmodel(String image,String pname, String price, String description,String pid) {
        this.image=image;
        this.pname = pname;
        this.price = price;
        this.description = description;
        this.pid=pid;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}



