package com.ginger.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ginger.Entities.Meal;
import com.ginger.MealDetailsActivity;
import com.ginger.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {
    Bitmap bitmap;

    private final List<Meal> meals;
    private OnTaskItemClickListener listener;
    private Context context;

    public interface OnTaskItemClickListener {
        void onItemClicked(int position);
    }

    public MealAdapter(List<Meal> meals, OnTaskItemClickListener listener,Context context) {
        this.meals = meals;
        this.listener = listener;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_sub_category, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.mealName.setText(meal.getStrMeal());


        Uri uri = Uri.parse(meal.getStrMealThumb());
        Picasso.with(context).load(uri).into(holder.mealImage);

    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mealImage;
        private TextView mealName;


        ViewHolder(@NonNull View itemView, OnTaskItemClickListener listener) {
            super(itemView);

            mealImage = itemView.findViewById(R.id.img_dish);
            mealName = itemView.findViewById(R.id.tv_dish_name);

            // Go to task details
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }



}
