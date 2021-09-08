package com.ginger.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Blog;
import com.amplifyframework.datastore.generated.model.Todo;
import com.ginger.R;

import java.util.List;

public class BlogsAdapter extends RecyclerView.Adapter<BlogsAdapter.BlogHolder>{

    private final List<Blog> blogList;


    public BlogsAdapter(List<Blog> blogList) {
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public BlogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_card,parent,false);

        return new BlogHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull  BlogsAdapter.BlogHolder holder, int position) {
        Blog blog= blogList.get(position);
        holder.title.setText(blog.getTitle());
//        Log.i("adapter","the item title:"+task.getTitle());

        holder.body.setText(blog.getBody());
        holder.date.setText(blog.getCreatedAt().substring(0,10));
        holder.userName.setText(blog.getUserName());

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    static class BlogHolder extends RecyclerView.ViewHolder{

        private final TextView title;
        private final TextView body;
        private final TextView date;
        private final TextView userName;

        public BlogHolder(@NonNull  View itemView ) {

            super(itemView);

            title= itemView.findViewById(R.id.blogTitle);
            body= itemView.findViewById(R.id.blogBody);
            date= itemView.findViewById(R.id.blogDate);
            userName= itemView.findViewById(R.id.userName);

        }

    }
}
