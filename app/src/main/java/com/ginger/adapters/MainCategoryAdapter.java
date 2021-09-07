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
import com.ginger.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {

    private final List<Category> categories;
    private final OnTaskItemClickListener listener;
    private Context context;


    public MainCategoryAdapter(List<Category> categories, OnTaskItemClickListener listener, Context context) {
        this.categories = categories;
        this.listener = listener;
        this.context=context;
    }

    public interface OnTaskItemClickListener {
        void onItemClicked(int position);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_main_category,parent,false);
        return new ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder( @NonNull MainCategoryAdapter.ViewHolder holder, int position) {
        Category item = categories.get(position);
        if (!item.getStrCategory().equals("Pork")) {
            holder.name.setText(item.getStrCategory());
            String uri = "@drawable/" + item.getStrCategory().toLowerCase(Locale.ROOT);
            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());

//            Drawable res = context.getResources().getDrawable(imageResource);
            Picasso.with(context).load(imageResource).resize(500,500).into(holder.image);
//            holder.image.setImageDrawable(res);
        }



//
    }

    @Override
    public int getItemCount() {
        return  categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView name;

        ViewHolder(@NonNull View itemView, OnTaskItemClickListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_dish_name);
            image = itemView.findViewById(R.id.img_dish);

            itemView.setOnClickListener(v -> listener.onItemClicked(getAdapterPosition()));


        }




    }
}
