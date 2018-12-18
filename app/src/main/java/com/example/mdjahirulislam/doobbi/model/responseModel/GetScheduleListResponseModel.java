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
    @SerializedName("schedule")
    @Expose
    private List<Schedule> schedule = null;

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

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    public static class Schedule {

        @SerializedName("sid")
        @Expose
        private String sid;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("sc_date")
        @Expose
        private String scDate;
        @SerializedName("sc_time")
        @Expose
        private String scTime;
        @SerializedName("status_detial")
        @Expose
        private String statusDetial;

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getScDate() {
            return scDate;
        }

        public void setScDate(String scDate) {
            this.scDate = scDate;
        }

        public String getScTime() {
            return scTime;
        }

        public void setScTime(String scTime) {
            this.scTime = scTime;
        }

        public String getStatusDetial() {
            return statusDetial;
        }

        public void setStatusDetial(String statusDetial) {
            this.statusDetial = statusDetial;
        }

        @Override
        public String toString() {
            return "Schedule{" +
                    "sid='" + sid + '\'' +
                    ", phone='" + phone + '\'' +
                    ", customerName='" + customerName + '\'' +
                    ", scDate='" + scDate + '\'' +
                    ", scTime='" + scTime + '\'' +
                    ", statusDetial='" + statusDetial + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetScheduleListResponseModel{" +
                "status='" + status + '\'' +
                ", detail='" + detail + '\'' +
                ", schedule=" + schedule +
                '}';
    }
}
