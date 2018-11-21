package com.example.mdjahirulislam.doobbi.model;

public class CategoryItemsModel {

    private String price;
    private String description;
    private String posterURL;

    public CategoryItemsModel(String price, String description, String posterURL) {
        this.price = price;
        this.description = description;
        this.posterURL = posterURL;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterURL() {
        return posterURL;
    }
}
