package com.ginger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ginger.filter.MealItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MealItemAdapter extends RecyclerView.Adapter<MealItemAdapter.ViewHolder>{
    Bitmap bitmap;

    private final List<MealItem> meals;
    private OnMealItemClickListener listener;

    public interface OnMealItemClickListener {
        void onItemClicked(int position);
    }

    public MealItemAdapter(List<MealItem> meals, OnMealItemClickListener listener) {
        this.meals = meals;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_card_layout, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MealItemAdapter.ViewHolder holder, int position) {
        MealItem meal = meals.get(position);
        holder.mealName.setText(meal.getTitle());
//        holder.servings.setText(meal.getServings());
//        holder.pricePerServing.setText(meal.getPricePerServing());
//        holder.readyInMinutes.setText(meal.getReadyInMinutes());
//        holder.sourceUrl.setText(meal.getSourceUrl());

        if (meal.getImage() != null && !meal.getImage().equals("")) {
            new MealItemAdapter.GetImageFromUrl(holder.mealImage).execute(meal.getImage());
        }
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mealImage;
        private TextView mealName;
//        private TextView servings;
//        private TextView pricePerServing;
//        private TextView readyInMinutes;
//        private TextView sourceUrl;


        ViewHolder(@NonNull View itemView, OnMealItemClickListener listener) {
            super(itemView);

            mealImage = itemView.findViewById(R.id.meal_image);
            mealName = itemView.findViewById(R.id.meal_name);
//            servings = itemView.findViewById(R.id.servings);
//            pricePerServing = itemView.findViewById(R.id.pricePerServing);
//            readyInMinutes = itemView.findViewById(R.id.readyInMinutes);
//            sourceUrl = itemView.findViewById(R.id.sourceUrl);


            // Go to meal details
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }

    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public GetImageFromUrl(ImageView img) {
            this.imageView = img;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
            bitmap = null;
            InputStream inputStream;
            try {
                inputStream = new java.net.URL(stringUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
}
