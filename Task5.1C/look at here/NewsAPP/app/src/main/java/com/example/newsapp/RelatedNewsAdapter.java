package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RelatedNewsAdapter extends RecyclerView.Adapter<RelatedNewsAdapter.RelatedNewsViewHolder> {

    private List<NewsItem> relatedNewsList;


    public RelatedNewsAdapter(List<NewsItem> relatedNewsList) {
        this.relatedNewsList = relatedNewsList;
    }

    @NonNull
    @Override
    public RelatedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_item_layout, parent, false);
        return new RelatedNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedNewsViewHolder holder, int position) {
        NewsItem newsItem = relatedNewsList.get(position);
        holder.textViewRelatedNewsTitle.setText(newsItem.getTitle());
        holder.textViewRelatedNewsDescription.setText(newsItem.getDescription());

        // Glide.with(holder.itemView.getContext()).load(newsItem.getImageUrl()).into(holder.imageViewRelatedNews);
    }

    @Override
    public int getItemCount() {
        return relatedNewsList.size();
    }

    public static class RelatedNewsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewRelatedNews;
        TextView textViewRelatedNewsTitle;
        TextView textViewRelatedNewsDescription;

        public RelatedNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRelatedNews = itemView.findViewById(R.id.imageViewRelatedNews);
            textViewRelatedNewsTitle = itemView.findViewById(R.id.textViewRelatedNewsTitle);
            textViewRelatedNewsDescription = itemView.findViewById(R.id.textViewRelatedNewsDescription);
        }
    }
}
