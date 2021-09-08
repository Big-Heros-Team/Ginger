package com.ginger.adapters;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Recipe;
import com.ginger.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private final List<Recipe> recipes;
    private final OnTaskItemClickListener listener;
    Context context;


    public FavoritesAdapter(List<Recipe> recipes, OnTaskItemClickListener listener,Context context) {
        this.recipes = recipes;
        this.listener = listener;
        this.context=context;

    }

    public interface OnTaskItemClickListener {
        void onItemClicked(int position);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_favortie,parent,false);
        return new ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder( @NonNull FavoritesAdapter.ViewHolder holder, int position) {
        Recipe item = recipes.get(position);
        holder.text.setText(item.getName());
        Uri uri = Uri.parse(item.getPhoto());
        Picasso.with(context).load(uri).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return  recipes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView text;
        private final ImageView image;

        ViewHolder(@NonNull View itemView, OnTaskItemClickListener listener) {
            super(itemView);

            text = itemView.findViewById(R.id.dish_name_favorite);
            image = itemView.findViewById(R.id.img_favorite);


            itemView.setOnClickListener(v -> listener.onItemClicked(getAdapterPosition()));


        }




    }
}
