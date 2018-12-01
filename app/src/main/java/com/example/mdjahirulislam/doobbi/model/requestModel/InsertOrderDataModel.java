package com.example.mdjahirulislam.doobbi.model.requestModel;

public class InsertOrderDataModel {

    private String item_id;
    private String service_id;
    private String quantity;
    private String price;

    public InsertOrderDataModel() {
    }

    public InsertOrderDataModel(String item_id, String service_id, String quantity, String price) {
        this.item_id = item_id;
        this.service_id = service_id;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
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

    @Override
    public String toString() {
        return "InsertOrderDataModel{" +
                "itemId='" + item_id + '\'' +
                ", serviceId='" + service_id + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
