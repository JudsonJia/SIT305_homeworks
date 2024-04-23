package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopStoriesAdapter extends RecyclerView.Adapter<TopStoriesAdapter.ViewHolder> {

    private final List<TopStoryItem> topStoriesList;
    private final Context context;

    public TopStoriesAdapter(Context context, List<TopStoryItem> topStoriesList) {
        this.context = context;
        this.topStoriesList = topStoriesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_top_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TopStoryItem topStoryItem = topStoriesList.get(position);
        holder.textTopStoryTitle.setText(topStoryItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return topStoriesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Define the view elements of the top story item, such as titles, images, and so on
        TextView textTopStoryTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTopStoryTitle = itemView.findViewById(R.id.textTopStoryTitle);
        }
    }
}
