package com.ginger.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ginger.Entities.Category;
import com.ginger.Entities.MealDetails;
import com.ginger.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private final List<String> ingredients;
    private final OnTaskItemClickListener listener;


    public IngredientsAdapter(List<String> ingredients, OnTaskItemClickListener listener) {
        this.ingredients = ingredients;
        this.listener = listener;

    }

    public interface OnTaskItemClickListener {
        void onItemClicked(int position);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_ingredients,parent,false);
        return new ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder( @NonNull IngredientsAdapter.ViewHolder holder, int position) {
        String item = ingredients.get(position);
      holder.text.setText(item);



//
    }

    @Override
    public int getItemCount() {
        return  ingredients.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView text;

        ViewHolder(@NonNull View itemView, OnTaskItemClickListener listener) {
            super(itemView);

            text = itemView.findViewById(R.id.tv_ingredient);

            itemView.setOnClickListener(v -> listener.onItemClicked(getAdapterPosition()));


        }




    }
}