package com.example.mdjahirulislam.doobbi.controller.connectionInterface;

import com.example.mdjahirulislam.doobbi.model.requestModel.InsertUserDataModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetCategoryItemResponseModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetItemWisePriceResponseModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetOrderListResponseModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetTadItemResponseModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetUserDetailsResponseModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.InsertUserResponseModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ConnectionAPI {

    @Multipart
    @POST("request")
    Call<InsertUserResponseModel> registration(
            @Part("password") RequestBody password,
            @Part("user") RequestBody user,
            @Part("function") RequestBody function,
            @Part("data") RequestBody dataModel
    );

    @Multipart
    @POST("request")
    Call<InsertUserResponseModel> uploadNewUserPhoto(
            @Part("password") RequestBody password,
            @Part("user") RequestBody user,
            @Part("function") RequestBody function,
            @Part("phone") RequestBody phone,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("request")
    Call<GetUserDetailsResponseModel> getUserDetails(
            @Part("password") RequestBody password,
            @Part("user") RequestBody user,
            @Part("function") RequestBody function,
            @Part("phone") RequestBody phone
    );


    @Multipart
    @POST("request")
    Call<GetUserDetailsResponseModel> getLogin(
            @Part("password") RequestBody password,
            @Part("user") RequestBody user,
            @Part("function") RequestBody function,
            @Part("phone") RequestBody phone,
            @Part("userpass") RequestBody pass
    );

    @Multipart
    @POST("request")
    Call<GetTadItemResponseModel> getTabItem(
            @Part("password") RequestBody password,
            @Part("user") RequestBody user,
            @Part("function") RequestBody function

    );

    @Multipart
    @POST("request")
    Call<GetCategoryItemResponseModel> getCategoryItem(
            @Part("password") RequestBody password,
            @Part("user") RequestBody user,
            @Part("categoryid") RequestBody categoryID,
            @Part("function") RequestBody function

    );

    @Multipart
    @POST("request")
    Call<GetItemWisePriceResponseModel> getCategoryItemWisePrice(
            @Part("password") RequestBody password,
            @Part("user") RequestBody user,
            @Part("itemid") RequestBody itemID,
            @Part("function") RequestBody function

    );

    @Multipart
    @POST("request")
    Call<InsertUserResponseModel> submitOrder(
            @Part("password") RequestBody password,
            @Part("user") RequestBody user,
            @Part("function") RequestBody function,
            @Part("phone") RequestBody phone,
            @Part("noofitem") RequestBody noOfItem,
            @Part("order") RequestBody order

            );

    @Multipart
    @POST("request")
    Call<GetOrderListResponseModel> getTemporaryOrderList(
            @Part("password") RequestBody password,
            @Part("user") RequestBody user,
            @Part("function") RequestBody function,
            @Part("phone") RequestBody phone

    );

    @Multipart
    @POST("request")
    Call<GetOrderListResponseModel> getSingleOrderDetails(
            @Part("password") RequestBody password,
            @Part("user") RequestBody user,
            @Part("function") RequestBody function,
            @Part("phone") RequestBody phone,
            @Part("orderid") RequestBody orderID

    );

}
