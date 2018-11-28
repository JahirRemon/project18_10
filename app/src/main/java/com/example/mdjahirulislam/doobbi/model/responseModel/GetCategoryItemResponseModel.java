package com.example.mdjahirulislam.doobbi.model.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCategoryItemResponseModel {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("category")
    @Expose
    private List<Category> category = null;

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

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public static class Category {

        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("item_id")
        @Expose
        private String itemId;
        @SerializedName("item_name")
        @Expose
        private String itemName;
        @SerializedName("min_price")
        @Expose
        private String minPrice;
        @SerializedName("image")
        @Expose
        private Object image;

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

        public Object getImage() {
            return image;
        }

        public void setImage(Object image) {
            this.image = image;
        }

        @Override
        public String toString() {
            return "Category{" +
                    "categoryId='" + categoryId + '\'' +
                    ", itemId='" + itemId + '\'' +
                    ", itemName='" + itemName + '\'' +
                    ", minPrice='" + minPrice + '\'' +
                    ", image=" + image +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetCategoryItemResponseModel{" +
                "total=" + total +
                ", status='" + status + '\'' +
                ", detail='" + detail + '\'' +
                ", category=" + category +
                '}';
    }
}
