package com.example.mdjahirulislam.doobbi.model.requestModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class InsertOrderHistoryDBModel extends RealmObject {
    @PrimaryKey
    private String _id;
    private String userID;
    private String itemID;
    private String itemName;
    private String serviceID;
    private String serviceName;
    private String itemQuantity;
    private String totalPrice;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "InsertOrderHistoryDBModel{" +
                "_id='" + _id + '\'' +
                ", userID='" + userID + '\'' +
                ", itemID='" + itemID + '\'' +
                ", itemName='" + itemName + '\'' +
                ", serviceID='" + serviceID + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", itemQuantity='" + itemQuantity + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                '}';
    }

}
