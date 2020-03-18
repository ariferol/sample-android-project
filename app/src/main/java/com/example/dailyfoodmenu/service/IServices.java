package com.example.dailyfoodmenu.service;

import com.example.dailyfoodmenu.FoodModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IServices {

    @GET("getweeklymenu")
    Call<List<FoodModel>> getSampleFoodList();

    @GET("getfoods")
    Call<List<FoodModel>> getSampleAllActivatedFoodList();
}

