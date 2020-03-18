package com.example.dailyfoodmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<FoodModel> foodList = new ArrayList<>();

    public CustomAdapter(Context context, List<FoodModel> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.row_food,null);
        TextView txtCalorie = (TextView) row.findViewById(R.id.txtCalorie);
        TextView txtFoodDetail = (TextView) row.findViewById(R.id.txtFoodDetail);
        FoodModel rowModel = foodList.get(position);
//        txtFoodDate.setText(FoodAppUtil.convertDateToStr(rowModel.getFoodDate()));
        txtCalorie.setText(Float.toString(rowModel.getCalorie()));
        txtFoodDetail.setText(rowModel.getFoodDescription());
        return row;
    }
}
