package com.raghav.secondshop.Model;

public class cartmodel {
    String Product,quantity,price,location,pid;

    public cartmodel() {
    }

    public cartmodel(String Product, String quantity, String price, String location,String pid) {
        this.Product = Product;
        this.price = price;
        this.location = location;
        this.pid=pid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String Product) {
        this.Product = Product;
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
}

