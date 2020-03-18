package com.example.dailyfoodmenu.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SampleServices {
    private static IServices iServices = null;
    private static final String BASE_URL = "https://sample-rest-services.herokuapp.com/";

    public static IServices getServiceInstance(){
        if(iServices == null){
//            Gson gson = new GsonBuilder().setLenient().create();
            Gson gson=  new GsonBuilder().setDateFormat("dd.MM.yyyy").create();
            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            iServices = retrofit.create(IServices.class);
        }
        return iServices;
    }

    public static IServices getSampleAllActivatedFoodList(){
        if(iServices == null){
            Gson gson=  new GsonBuilder().setDateFormat("dd.MM.yyyy").create();
            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            iServices = retrofit.create(IServices.class);
        }
        return iServices;
    }

}
