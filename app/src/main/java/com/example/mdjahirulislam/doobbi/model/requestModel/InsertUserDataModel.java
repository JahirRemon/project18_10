package com.example.mdjahirulislam.doobbi.model.requestModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsertUserDataModel {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("another_phone")
    @Expose
    private String anotherPhone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("flat_no")
    @Expose
    private String flatNo;
    @SerializedName("road_no")
    @Expose
    private String roadNo;
    @SerializedName("house_no")
    @Expose
    private String houseNo;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("password")
    @Expose
    private String password;

    private String image;

    public InsertUserDataModel(String image) {
        this.image = image;
    }

    public InsertUserDataModel() {
    }

    public InsertUserDataModel(String name, String phone, String anotherPhone, String email, String address, String flatNo, String roadNo, String houseNo, String area, String latitude, String longitude, String password) {
        this.name = name;
        this.phone = phone;
        this.anotherPhone = anotherPhone;
        this.email = email;
        this.address = address;
        this.flatNo = flatNo;
        this.roadNo = roadNo;
        this.houseNo = houseNo;
        this.area = area;
        this.latitude = latitude;
        this.longitude = longitude;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAnotherPhone() {
        return anotherPhone;
    }

    public void setAnotherPhone(String anotherPhone) {
        this.anotherPhone = anotherPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getRoadNo() {
        return roadNo;
    }

    public void setRoadNo(String roadNo) {
        this.roadNo = roadNo;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "InsertUserDataModel{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", anotherPhone='" + anotherPhone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", flatNo='" + flatNo + '\'' +
                ", roadNo='" + roadNo + '\'' +
                ", houseNo='" + houseNo + '\'' +
                ", area='" + area + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


//    private String name;
//    private String phone;
//    private String another_phone;
//    private String email;
//    private String flat_no;
//    private String road_no;
//    private String house_no;
//    private String area;
//    private String latitude;
//    private String longitude;
//    private String password;
//
//
//    public InsertUserDataModel(String name, String phone, String another_phone, String email, String flat_no, String road_no, String house_no, String area, String latitude, String longitude, String password) {
//        this.name = name;
//        this.phone = phone;
//        this.another_phone = another_phone;
//        this.email = email;
//        this.flat_no = flat_no;
//        this.road_no = road_no;
//        this.house_no = house_no;
//        this.area = area;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.password = password;
//    }
//
//    public InsertUserDataModel() {
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getAnother_phone() {
//        return another_phone;
//    }
//
//    public void setAnother_phone(String another_phone) {
//        this.another_phone = another_phone;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//
//    public String getFlat_no() {
//        return flat_no;
//    }
//
//    public void setFlat_no(String flat_no) {
//        this.flat_no = flat_no;
//    }
//
//    public String getRoad_no() {
//        return road_no;
//    }
//
//    public void setRoad_no(String road_no) {
//        this.road_no = road_no;
//    }
//
//    public String getHouse_no() {
//        return house_no;
//    }
//
//    public void setHouse_no(String house_no) {
//        this.house_no = house_no;
//    }
//
//    public String getArea() {
//        return area;
//    }
//
//    public void setArea(String area) {
//        this.area = area;
//    }
//
//    public String getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(String latitude) {
//        this.latitude = latitude;
//    }
//
//    public String getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(String longitude) {
//        this.longitude = longitude;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    @Override
//    public String toString() {
//        return "InsertUserDataModel{" +
//                "name='" + name + '\'' +
//                ", phone='" + phone + '\'' +
//                ", another_phone='" + another_phone + '\'' +
//                ", email='" + email + '\'' +
//                ", flat_no='" + flat_no + '\'' +
//                ", road_no='" + road_no + '\'' +
//                ", house_no='" + house_no + '\'' +
//                ", area='" + area + '\'' +
//                ", latitude='" + latitude + '\'' +
//                ", longitude='" + longitude + '\'' +
//                ", password='" + password + '\'' +
//                '}';
//    }
}