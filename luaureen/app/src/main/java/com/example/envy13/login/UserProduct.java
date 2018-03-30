package com.example.envy13.login;

/**
 * Created by Envy 13 on 3/14/2018.
 */

public class UserProduct {

    String email, contact, location, productName, productAmount, productDescription, prodImage;

    public UserProduct(){
        //default constructor
    }
//      for initializing the variables
    public UserProduct(String email, String contact, String location, String productName, String productAmount, String productDescription, String prodImage){
        this.email = email;
        this.contact = contact;
        this.location = location;
        this.productName = productName;
        this.productAmount = productAmount;
        this.productDescription = productDescription;
        this.prodImage = prodImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getprodImage() {
        return prodImage;
    }

    public void setprodImage(String prodImage) {
        this.prodImage = prodImage;
    }
}
