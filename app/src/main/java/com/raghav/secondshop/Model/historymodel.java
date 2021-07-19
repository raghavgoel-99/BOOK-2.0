package com.raghav.secondshop.Model;

public class historymodel { String location,date,time,price,pname;

    public historymodel() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public historymodel(String location, String date, String time, String quantity, String price, String pname) {
        this.location = location;
        this.date = date;
        this.time = time;
        this.price = price;
        this.pname = pname;
    }
}
