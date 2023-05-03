package com.example.cloud.api;

import com.example.cloud.model.NguoiDung;
import com.example.cloud.model.Tep;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiCollection {

    @POST("login")
    Call<NguoiDung> login(@Body NguoiDung nguoiDung);

    @FormUrlEncoded
    @POST("login1")
    Call<NguoiDung> login1(@Field("username") String username, @Field("password") String password);

    @POST("signup")
    Call<NguoiDung> signup(@Body NguoiDung nguoiDung);

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
    Call<List<Tep>> downloadNameAll(@Query("path") String path);

    @GET("downloadNameFileOfType")
    Call<List<Tep>>  downloadNameFileOfType(@Query("username") String username,
                                            @Query("loai") String loai);

    @FormUrlEncoded
    @POST("deleteFile")
    Call<ResponseBody> deleteFile(@Field("fileName") String fileName,
                                  @Field("username") String username);

}
