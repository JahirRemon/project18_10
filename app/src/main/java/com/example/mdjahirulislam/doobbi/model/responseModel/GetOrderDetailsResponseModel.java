package com.example.mdjahirulislam.doobbi.model.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetOrderDetailsResponseModel {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("total_item")
    @Expose
    private String totalItem;
    @SerializedName("total_payable_amount")
    @Expose
    private String totalPayableAmount;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(String totalItem) {
        this.totalItem = totalItem;
    }

    public String getTotalPayableAmount() {
        return totalPayableAmount;
    }

    public void setTotalPayableAmount(String totalPayableAmount) {
        this.totalPayableAmount = totalPayableAmount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {

        @SerializedName("item_name")
        @Expose
        private String itemName;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("service_name")
        @Expose
        private String serviceName;
        @SerializedName("item_count")
        @Expose
        private String itemCount;
        @SerializedName("per_item_price")
        @Expose
        private String perItemPrice;

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getItemCount() {
            return itemCount;
        }

        public void setItemCount(String itemCount) {
            this.itemCount = itemCount;
        }

        public String getPerItemPrice() {
            return perItemPrice;
        }

        public void setPerItemPrice(String perItemPrice) {
            this.perItemPrice = perItemPrice;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "itemName='" + itemName + '\'' +
                    ", categoryName='" + categoryName + '\'' +
                    ", serviceName='" + serviceName + '\'' +
                    ", itemCount='" + itemCount + '\'' +
                    ", perItemPrice='" + perItemPrice + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "GetOrderDetailsResponseModel{" +
                "orderId='" + orderId + '\'' +
                ", totalItem='" + totalItem + '\'' +
                ", totalPayableAmount='" + totalPayableAmount + '\'' +
                ", createDate='" + createDate + '\'' +
                ", customerName='" + customerName + '\'' +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                ", detail='" + detail + '\'' +
                ", items=" + items +
                '}';
    }
}
