package com.example.mdjahirulislam.doobbi.model.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetItemWisePriceResponseModel {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("image")
    @Expose
    private Object image;
    @SerializedName("item")
    @Expose
    private List<Item> item = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public static class Item {

        @SerializedName("price_id")
        @Expose
        private String priceId;
        @SerializedName("item_id")
        @Expose
        private String itemId;
        @SerializedName("service_id")
        @Expose
        private String serviceId;
        @SerializedName("service_name")
        @Expose
        private String serviceName;
        @SerializedName("sales_price")
        @Expose
        private String salesPrice;
        @SerializedName("base_price")
        @Expose
        private String basePrice;
        @SerializedName("discount_per")
        @Expose
        private String discountPer;

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
            return "Item{" +
                    "priceId='" + priceId + '\'' +
                    ", itemId='" + itemId + '\'' +
                    ", serviceId='" + serviceId + '\'' +
                    ", serviceName='" + serviceName + '\'' +
                    ", salesPrice='" + salesPrice + '\'' +
                    ", basePrice='" + basePrice + '\'' +
                    ", discountPer='" + discountPer + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetItemWisePriceResponseModel{" +
                "total=" + total +
                ", status='" + status + '\'' +
                ", detail='" + detail + '\'' +
                ", itemName='" + itemName + '\'' +
                ", image=" + image +
                ", item=" + item +
                '}';
    }
}
