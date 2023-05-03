package com.example.cloud.api;

public class ApiSumoner {
    public static ApiCollection callApi(){
        return ApiConfig.getClient("http://192.168.55.107:8080/").create(ApiCollection.class);

    }
}
