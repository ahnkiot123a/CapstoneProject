package com.koit.capstonproject_version_1.model;

public class SuggestedProduct {
    private String barcode, name, price, unit, categoryName;

    public SuggestedProduct() {
    }

    public SuggestedProduct(String barcode, String name, String price, String unit, String categoryName) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.categoryName = categoryName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "SuggestedProduct{" +
                "barcode='" + barcode + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", unit='" + unit + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
