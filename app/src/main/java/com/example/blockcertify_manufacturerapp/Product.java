package com.example.blockcertify_manufacturerapp;

public class Product {

    private String Name,Price,Details,CompanyId,ManufacturingDate,ExpiryDate,Status;
    public Product(){

    }

    public String getManufacturingDate() {
        return ManufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        ManufacturingDate = manufacturingDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Product(String name, String price, String details, String companyid, String manufacturingDate, String expiry, String status) {
        Name = name;
        Price = price;
        Details = details;
        CompanyId = companyid;
        ManufacturingDate = manufacturingDate;
        ExpiryDate = expiry;
        Status = status;
    }




    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }


    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

}
