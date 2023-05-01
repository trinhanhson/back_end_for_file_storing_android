package com.example.cloud.api;

import com.example.cloud.model.NguoiDung;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiCollection {
    @FormUrlEncoded
    @POST("login")
    Call<NguoiDung> login(@Field("username") String username,
                          @Field("password") String password);

    @FormUrlEncoded
    @POST("signup")
    Call<NguoiDung> signup(@Field("username") String username,
                           @Field("password") String password);

    @Multipart
    @POST("uploadFile")
    Call<ResponseBody> uploadFile(
            @Part MultipartBody.Part file,
            @Part("path") RequestBody path
    );

    @GET("downloadOneFile")
    Call<ResponseBody> downloadOneFile(@Query("fileName") String filename,
                                       @Query("username") String username);

    @GET("downloadNameAll")
    Call<List<String>> downloadNameAll(@Query("path") String path);

    @FormUrlEncoded
    @POST("deleteFile")
    Call<ResponseBody> deleteFile(@Field("fileName") String fileName,
                                  @Field("username") String username);

}
