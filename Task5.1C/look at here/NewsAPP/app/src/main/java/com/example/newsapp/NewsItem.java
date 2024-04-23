package com.example.newsapp;

import java.io.Serializable;
import java.util.List;

public class NewsItem implements Serializable {

    private String title;
    private String description;

    private String Details;
    private String imageUrl;
    private List<NewsItem> relatedNews;

    public void setRelatedNews(List<NewsItem> relatedNews) {
        this.relatedNews = relatedNews;
    }

    public NewsItem(String title, String description, String details, String imageUrl) {
        this.title = title;
        this.description = description;
        Details = details;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public List<NewsItem> getRelatedNews() {
        return relatedNews;
    }
}
