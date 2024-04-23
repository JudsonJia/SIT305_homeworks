package com.example.newsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends Fragment {

    private RecyclerView recyclerTopStories;
    private RecyclerView recyclerNewsList;

    public NewsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        recyclerTopStories = view.findViewById(R.id.recyclerTopStories);
        recyclerNewsList = view.findViewById(R.id.recyclerNewsList);

        // Set up a horizontal layout manager for recyclerTopStories
        recyclerTopStories.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        List<TopStoryItem> topStoriesList = new ArrayList<>();

        // Add the sample top story item to the list
        // Create top story item 1 and related news
        TopStoryItem topStoryItem1 = new TopStoryItem("Top Story Title 1", "Image_URL 1", "details");
        List<NewsItem> relatedNews1 = new ArrayList<>();
        relatedNews1.add(new NewsItem("Related News Title 1_1", "Description 1_1", "Details 1_1", "Image_URL 1_1"));
        relatedNews1.add(new NewsItem("Related News Title 1_2", "Description 1_2", "Details 1_2", "Image_URL 1_2"));
        topStoryItem1.setRelatedNews(relatedNews1);
        topStoriesList.add(topStoryItem1);


        // Create top story item 2 and related news
        TopStoryItem topStoryItem2 = new TopStoryItem("Top Story Title 2", "Image_URL 2", "details");
        List<NewsItem> relatedNews2 = new ArrayList<>();
        relatedNews2.add(new NewsItem("Related News Title 2_1", "Description 2_1", "Details 2_1", "Image_URL 2_1"));
        relatedNews2.add(new NewsItem("Related News Title 2_2", "Description 2_2", "Details 2_2", "Image_URL 2_2"));
        topStoryItem2.setRelatedNews(relatedNews2);
        topStoriesList.add(topStoryItem2);


        // Create top story item 3 and related news
        TopStoryItem topStoryItem3 = new TopStoryItem("Top Story Title 3", "Image_URL 3", "details");
        List<NewsItem> relatedNews3 = new ArrayList<>();
        relatedNews3.add(new NewsItem("Related News Title 3_1", "Description 3_1", "Details 3_1", "Image_URL 3_1"));
        relatedNews3.add(new NewsItem("Related News Title 3_2", "Description 3_2", "Details 3_2", "Image_URL 3_2"));
        topStoryItem3.setRelatedNews(relatedNews3);
        topStoriesList.add(topStoryItem3);
        // Add more top story items...

        // Create and set up an adapter for recyclerTopStories
        TopStoriesAdapter topStoriesAdapter = new TopStoriesAdapter(getContext(), topStoriesList);
        recyclerTopStories.setAdapter(topStoriesAdapter);

        // Set up a vertical layout manager for recyclerNewsList
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2); // 设置为两列
        recyclerNewsList.setLayoutManager(gridLayoutManager);

        // Add a sample news item to the list
        List<NewsItem> newsList = new ArrayList<>();

        NewsItem newsItem1 = new NewsItem("News_Title 1", "Description 1", "Details", "Image_URL 1");
        List<NewsItem> relatedNews_1 = new ArrayList<>();
        relatedNews_1.add(new NewsItem("Related_News_Title 1_1", "Related_Description 1_1", "Related_Details 1_1", "Related_Image_URL 1_1"));
        relatedNews_1.add(new NewsItem("Related_News_Title 1_2", "Related_Description 1_2", "Related_Details 1_2", "Related_Image_URL 1_2"));
        newsItem1.setRelatedNews(relatedNews_1);
        newsList.add(newsItem1);


        // Create news item 2 and related news
        NewsItem newsItem2 = new NewsItem("News_Title 2", "Description 2", "Details", "Image_URL 2");
        List<NewsItem> relatedNews_2 = new ArrayList<>();
        relatedNews_2.add(new NewsItem("Related_News_Title 2_1", "Related_Description 2_1", "Related_Details 2_1", "Related_Image_URL 2_1"));
        relatedNews_2.add(new NewsItem("Related_News_Title 2_2", "Related_Description 2_2", "Related_Details 2_2", "Related_Image_URL 2_2"));
        newsItem2.setRelatedNews(relatedNews_2);
        newsList.add(newsItem2);


        // Create news item 3 and related news
        NewsItem newsItem3 = new NewsItem("News_Title 3", "Description 3", "Details", "Image_URL 3");
        List<NewsItem> relatedNews_3 = new ArrayList<>();
        relatedNews_3.add(new NewsItem("Related_News_Title 3_1", "Related_Description 3_1", "Related_Details 3_1", "Related_Image_URL 3_1"));
        relatedNews_3.add(new NewsItem("Related_News_Title 3_2", "Related_Description 3_2", "Related_Details 3_2", "Related_Image_URL 3_2"));
        newsItem3.setRelatedNews(relatedNews_3);
        newsList.add(newsItem3);

        NewsItem newsItem4 = new NewsItem("News_Title 4", "Description 4", "Details", "Image_URL 4");
        List<NewsItem> relatedNews_4 = new ArrayList<>();
        relatedNews_4.add(new NewsItem("Related_News_Title 4_1", "Related_Description 4_1", "Related_Details 4_1", "Related_Image_URL 4_1"));
        relatedNews_4.add(new NewsItem("Related_News_Title 4_2", "Related_Description 4_2", "Related_Details 4_2", "Related_Image_URL 4_2"));
        newsItem4.setRelatedNews(relatedNews_4);
        newsList.add(newsItem4);

        NewsItem newsItem5 = new NewsItem("News_Title 5", "Description 5", "Details", "Image_URL 5");
        List<NewsItem> relatedNews_5 = new ArrayList<>();
        relatedNews_5.add(new NewsItem("Related_News_Title 5_1", "Related_Description 5_1", "Related_Details 5_1", "Related_Image_URL 5_1"));
        relatedNews_5.add(new NewsItem("Related_News_Title 5_2", "Related_Description 5_2", "Related_Details 5_2", "Related_Image_URL 5_2"));
        newsItem5.setRelatedNews(relatedNews_5);
        newsList.add(newsItem5);

        NewsItem newsItem6 = new NewsItem("News_Title 6", "Description 6", "Details", "Image_URL 6");
        List<NewsItem> relatedNews_6 = new ArrayList<>();
        relatedNews_6.add(new NewsItem("Related_News_Title 6_1", "Related_Description 6_1", "Related_Details 6_1", "Related_Image_URL 6_1"));
        relatedNews_6.add(new NewsItem("Related_News_Title 6_2", "Related_Description 6_2", "Related_Details 6_2", "Related_Image_URL 6_2"));
        newsItem6.setRelatedNews(relatedNews_6);
        newsList.add(newsItem6);
        // Add more news items...

        // Create and set up an adapter for recyclerNewsList
        NewsAdapter newsAdapter = new NewsAdapter(getContext(), newsList);
        recyclerNewsList.setAdapter(newsAdapter);

        // Set the click event for a news list item
        recyclerTopStories.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Get clicked news items
                NewsItem clickedNews = newsList.get(position);

                // Created Bundle to deliver news data to NewsDetailFragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsItem", clickedNews);

                // Start the NewsDetailFragment and pass the news data
                NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
                newsDetailFragment.setArguments(bundle);

                // use FragmentManager to start NewsDetailFragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, newsDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }));

        recyclerNewsList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Get clicked news items
                NewsItem clickedNews = newsList.get(position);

                // Created Bundle to deliver news data to NewsDetailFragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsItem", clickedNews);

                // Start the NewsDetailFragment and pass the news data
                NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
                newsDetailFragment.setArguments(bundle);

                // use FragmentManager to start NewsDetailFragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, newsDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }));

        return view;
    }
}
