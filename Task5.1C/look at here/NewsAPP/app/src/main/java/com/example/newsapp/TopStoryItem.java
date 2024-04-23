package com.example.newsapp;

import java.util.List;

public class TopStoryItem {

    private String title;
    private String imageUrl;

    private String Details;
    private List<NewsItem> relatedNews;

    public List<NewsItem> getRelatedNews() {
        return relatedNews;
    }

    public void setRelatedNews(List<NewsItem> relatedNews) {
        this.relatedNews = relatedNews;
    }

    public TopStoryItem(String title, String imageUrl, String details) {
        this.title = title;
        this.imageUrl = imageUrl;
        Details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDetails() {
        return this.Details;
    }

    public void setDetails(String details) {
        Details = details;
    }
}
