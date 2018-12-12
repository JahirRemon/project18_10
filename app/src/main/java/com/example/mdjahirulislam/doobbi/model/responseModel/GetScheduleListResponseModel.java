package com.example.mdjahirulislam.doobbi.model.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetScheduleListResponseModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("orders")
    @Expose
    private List<GetOrderListResponseModel.Order> orders = null;

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

    public List<GetOrderListResponseModel.Order> getOrders() {
        return orders;
    }

    public void setOrders(List<GetOrderListResponseModel.Order> orders) {
        this.orders = orders;
    }


    public class Order {

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
        @SerializedName("status_detial")
        @Expose
        private String statusDetial;

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

        public String getStatusDetial() {
            return statusDetial;
        }

        public void setStatusDetial(String statusDetial) {
            this.statusDetial = statusDetial;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "orderId='" + orderId + '\'' +
                    ", totalItem='" + totalItem + '\'' +
                    ", totalPayableAmount='" + totalPayableAmount + '\'' +
                    ", createDate='" + createDate + '\'' +
                    ", customerName='" + customerName + '\'' +
                    ", phone='" + phone + '\'' +
                    ", statusDetial='" + statusDetial + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetOrderListResponseModel{" +
                "status='" + status + '\'' +
                ", detail='" + detail + '\'' +
                ", orders=" + orders +
                '}';
    }
}
