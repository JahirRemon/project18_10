package com.example.mdjahirulislam.doobbi.model.requestModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public  class InsertUserDataModel extends RealmObject{

    public String clint_id;
    private String clint_image_path;
    private String name;
    private String phone;
    private String another_phone;
    private String email;
    private String address;
    private String flat_no;
    private String road_no;
    private String house_no;
    private String area;
    private String latitude;
    private String longitude;
    private String password;



    public InsertUserDataModel() {
    }

    public InsertUserDataModel(String name, String phone, String another_phone, String email, String address, String flat_no, String road_no, String house_no, String area, String latitude, String longitude, String password) {
        this.name = name;
        this.phone = phone;
        this.another_phone = another_phone;
        this.email = email;
        this.address = address;
        this.flat_no = flat_no;
        this.road_no = road_no;
        this.house_no = house_no;
        this.area = area;
        this.latitude = latitude;
        this.longitude = longitude;
        this.password = password;
    }
    public String getClint_id() {
        return clint_id;
    }

    public void setClint_id(String clint_id) {
        this.clint_id = clint_id;
    }

    public String getClint_image_path() {
        return clint_image_path;
    }

    public void setClint_image_path(String clint_image_path) {
        this.clint_image_path = clint_image_path;
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

    public String getAnother_phone() {
        return another_phone;
    }

    public void setAnother_phone(String another_phone) {
        this.another_phone = another_phone;
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

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }

    public String getRoad_no() {
        return road_no;
    }

    public void setRoad_no(String road_no) {
        this.road_no = road_no;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
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
                ", another_phone='" + another_phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", flat_no='" + flat_no + '\'' +
                ", roadNo='" + road_no + '\'' +
                ", houseNo='" + house_no + '\'' +
                ", area='" + area + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}