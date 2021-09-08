package com.ginger.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ginger.R;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private final List<String> ingredients;


    public IngredientsAdapter(List<String> ingredients) {
        this.ingredients = ingredients;

    }

    public interface OnTaskItemClickListener {
        void onItemClicked(int position);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_ingredients,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( @NonNull ViewHolder holder, int position) {
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

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.tv_ingredient);



        }




    }
}