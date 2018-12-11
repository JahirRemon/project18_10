package com.example.mdjahirulislam.doobbi.model.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllItemsPriceResponseModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("price")
    @Expose
    private List<Price> price = null;

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

    public List<Price> getPrice() {
        return price;
    }

    public void setPrice(List<Price> price) {
        this.price = price;
    }


    public static class Price {

        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("item_id")
        @Expose
        private String itemId;
        @SerializedName("service_category_id")
        @Expose
        private String serviceCategoryId;
        @SerializedName("sales_price")
        @Expose
        private String salesPrice;
        @SerializedName("item_name")
        @Expose
        private String itemName;
        @SerializedName("service_category_name")
        @Expose
        private String serviceCategoryName;
        @SerializedName("category_name")
        @Expose
        private String categoryName;

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

        public String getServiceCategoryId() {
            return serviceCategoryId;
        }

        public void setServiceCategoryId(String serviceCategoryId) {
            this.serviceCategoryId = serviceCategoryId;
        }

        public String getSalesPrice() {
            return salesPrice;
        }

        public void setSalesPrice(String salesPrice) {
            this.salesPrice = salesPrice;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getServiceCategoryName() {
            return serviceCategoryName;
        }

        public void setServiceCategoryName(String serviceCategoryName) {
            this.serviceCategoryName = serviceCategoryName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        @Override
        public String toString() {
            return "Price{" +
                    "categoryId='" + categoryId + '\'' +
                    ", itemId='" + itemId + '\'' +
                    ", serviceCategoryId='" + serviceCategoryId + '\'' +
                    ", salesPrice='" + salesPrice + '\'' +
                    ", itemName='" + itemName + '\'' +
                    ", serviceCategoryName='" + serviceCategoryName + '\'' +
                    ", categoryName='" + categoryName + '\'' +
                    '}';
        }


    }

    @Override
    public String toString() {
        return "GetAllItemsPriceResponseModel{" +
                "status='" + status + '\'' +
                ", detail='" + detail + '\'' +
                ", price=" + price +
                '}';
    }
}
