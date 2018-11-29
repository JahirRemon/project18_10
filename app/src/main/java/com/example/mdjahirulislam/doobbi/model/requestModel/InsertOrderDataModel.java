package com.example.mdjahirulislam.doobbi.model.requestModel;

public class InsertOrderDataModel {

    private String itemId;
    private String serviceId;
    private String quantity;
    private String price;

    public InsertOrderDataModel() {
    }

    public InsertOrderDataModel(String itemId, String serviceId, String quantity, String price) {
        this.itemId = itemId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
