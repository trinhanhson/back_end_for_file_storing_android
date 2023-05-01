package com.example.cloud.api;

public class ApiSumoner {
    public static ApiCollection callApi(){
        return ApiConfig.getClient("//url here").create(ApiCollection.class);

    }
}
