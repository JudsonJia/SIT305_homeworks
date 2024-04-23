package com.example.newsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NewsDetailFragment extends Fragment {

    private ImageView imageViewNews;
    private TextView textViewTitle;
    private TextView textViewContent;

    private TextView textViewRelated;
    private RecyclerView recyclerViewRelatedNews;

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageViewNews = view.findViewById(R.id.imageViewNews_2);
        textViewTitle = view.findViewById(R.id.textViewTitle_2);
        textViewContent = view.findViewById(R.id.textViewContent_2);
        recyclerViewRelatedNews = view.findViewById(R.id.recyclerViewRelatedNews);

        Bundle bundle = getArguments();
        if (bundle != null) {
            NewsItem newsItem = (NewsItem) bundle.getSerializable("newsItem");
            if (newsItem != null) {
                // Set up news images, headlines, and content
                // imageViewNews.setImageUrl(newsItem.getImageUrl());
                textViewTitle.setText(newsItem.getTitle());
                textViewContent.setText(newsItem.getDetails());

                // Set up an adapter for the list of related news
                List<NewsItem> relatedNewsList = newsItem.getRelatedNews();
                if (relatedNewsList != null && !relatedNewsList.isEmpty()) {
                    NewsAdapter newsAdapter = new NewsAdapter(getContext(), relatedNewsList);
                    recyclerViewRelatedNews.setLayoutManager(new LinearLayoutManager(getContext()));
                    RelatedNewsAdapter newsAdapter1 = new RelatedNewsAdapter(relatedNewsList);
                    recyclerViewRelatedNews.setAdapter(newsAdapter1);
                }
            }
        }
    }
}

