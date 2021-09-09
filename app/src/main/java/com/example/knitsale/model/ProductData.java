package com.example.knitsale.model;

import java.util.ArrayList;

public class ProductData {
    String title , price, description, urlproductimg1 , urlproductimg2 , urlproductimg3 , urlproductimg4;
    String productid, sellerid;     // (use in MainActivity) for show all details of  selectProduct( use in selectProductDetails)

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public ProductData(String title, String price, String description, String urlproductimg1, String urlproductimg2, String urlproductimg3, String urlproductimg4 ){
        this.title =title;
        this.price = price;
        this.description = description;
        this.urlproductimg1 = urlproductimg1;
        this.urlproductimg2 = urlproductimg2;
        this.urlproductimg3 = urlproductimg3;
        this.urlproductimg4 = urlproductimg4;
    }
    public ProductData(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getUrlproductimg1() {
        return urlproductimg1;
    }

    public void setUrlproductimg1(String urlproductimg1) {
        this.urlproductimg1 = urlproductimg1;
    }

    public String getUrlproductimg2() {
        return urlproductimg2;
    }

    public void setUrlproductimg2(String urlproductimg2) {
        this.urlproductimg2 = urlproductimg2;
    }

    public String getUrlproductimg3() {
        return urlproductimg3;
    }

    public void setUrlproductimg3(String urlproductimg3) {
        this.urlproductimg3 = urlproductimg3;
    }

    public String getUrlproductimg4() {
        return urlproductimg4;
    }

    public void setUrlproductimg4(String urlproductimg4) {
        this.urlproductimg4 = urlproductimg4;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ProductData){
            ProductData sendMessage =(ProductData) obj;
            return this.productid.equals(sendMessage.getProductid());
        }
        else
            return false;
    }
}
