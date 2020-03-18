package com.example.dailyfoodmenu;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author arif.erol
 */
public class FoodModel implements Serializable
{
    private Date foodDate;
    private float calorie;
    private String foodDescription;

    public FoodModel() {
    }

    public FoodModel(Date foodDate, float calorie, String foodDescription) {
        this.foodDate = foodDate;
        this.calorie = calorie;
        this.foodDescription = foodDescription;
    }

    public Date getFoodDate() {
        return foodDate;
    }

    public void setFoodDate(Date foodDate) {
        this.foodDate = foodDate;
    }

    public float getCalorie() {
        return calorie;
    }

    public void setCalorie(float calorie) {
        this.calorie = calorie;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }


}


