package com.raghav.secondshop.Model;

public class myproductmodel { String image;
    String pname,price,description,time,date,pid;

    public myproductmodel() {
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public myproductmodel(String image, String pname, String price, String description, String time, String date, String pid) {
        this.image = image;
        this.pname = pname;
        this.price = price;
        this.description = description;
        this.time = time;
        this.date = date;
        this.pid=pid;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}



