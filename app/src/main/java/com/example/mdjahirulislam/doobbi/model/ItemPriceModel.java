package com.example.mdjahirulislam.doobbi.model;

public class ItemPriceModel {
    private String priceId;
    private String itemId;
    private String serviceId;
    private String serviceName;
    private String itemName;
    private String salesPrice;
    private String basePrice;
    private String discountPer;

    public ItemPriceModel(String priceId, String itemId, String serviceId, String serviceName, String itemName, String salesPrice, String basePrice, String discountPer) {
        this.priceId = priceId;
        this.itemId = itemId;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.itemName = itemName;
        this.salesPrice = salesPrice;
        this.basePrice = basePrice;
        this.discountPer = discountPer;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }


    @Override
    public String toString() {
        return "ItemPriceModel{" +
                "priceId='" + priceId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", itemName='" + itemName + '\'' +
                ", salesPrice='" + salesPrice + '\'' +
                ", basePrice='" + basePrice + '\'' +
                ", discountPer='" + discountPer + '\'' +
                '}';
    }
}
