package com.example.dailyfoodmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {
    List<FoodModel> foodList = new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context;
    CustomItemClickListener listener;

    public CustomRecyclerAdapter(List<FoodModel> foodList, Context context, CustomItemClickListener listener) {
        this.foodList = foodList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.new_root_food, parent, false);
        final ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, vh.getPosition());
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCalorie.setText(Float.toString(foodList.get(position).getCalorie()));
        holder.txtFoodDetail.setText(foodList.get(position).getFoodDescription());
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtCalorie,txtFoodDetail;
        LinearLayout cardRow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCalorie = (TextView) itemView.findViewById(R.id.txtCalorie);
            txtFoodDetail = (TextView) itemView.findViewById(R.id.txtFoodDetail);
        }
    }
}
