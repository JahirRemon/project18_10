package com.example.mdjahirulislam.doobbi.model;

import java.io.Serializable;

public class CategoryItemsModel implements Serializable {

    private String categoryId;
    private String itemId;
    private String itemName;
    private String minPrice;
    private String image;

    public CategoryItemsModel(String categoryId, String itemId, String itemName, String minPrice, String image) {
        this.categoryId = categoryId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.minPrice = minPrice;
        this.image = image;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
