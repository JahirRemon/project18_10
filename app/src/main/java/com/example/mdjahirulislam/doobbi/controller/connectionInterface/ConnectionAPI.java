package com.example.mdjahirulislam.doobbi.controller.connectionInterface;

import com.example.mdjahirulislam.doobbi.model.requestModel.InsertUserDataModel;
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


}
