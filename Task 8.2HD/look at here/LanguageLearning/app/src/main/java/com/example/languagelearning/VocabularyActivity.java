package com.example.languagelearning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class VocabularyActivity extends AppCompatActivity {
    private Button generateButton, goback;
    private RecyclerView vocabularyRecyclerView;
    private VocabularyAdapter vocabularyAdapter;
    private List<String> vocabularyList = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();

    private String topic, language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        generateButton = findViewById(R.id.regenerateButton);
        goback = findViewById(R.id.goBack);
        vocabularyRecyclerView = findViewById(R.id.vocabularyRecyclerView);

        vocabularyAdapter = new VocabularyAdapter(vocabularyList);
        vocabularyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        vocabularyRecyclerView.setAdapter(vocabularyAdapter);

        topic = getIntent().getStringExtra("topic");
        language = getIntent().getStringExtra("language");

        fetchVocabulary(topic, language);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    fetchVocabulary(topic, language);
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to the previous page
            }
        });
    }

    private void fetchVocabulary(String topic, String language) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, java.util.concurrent.TimeUnit.MINUTES).build()) // this will set the read timeout for 10mins (IMPORTANT: If not your request will exceed the default read timeout)
                .build();

        VocabularyActivity.VocabularyApiService request = retrofit.create(VocabularyActivity.VocabularyApiService.class);


        request.generateVocabulary(language, topic).enqueue(new Callback<Vocabulary>() {
            @Override
            public void onResponse(@NonNull Call<Vocabulary> call, @NonNull Response<Vocabulary> response) {

                if (response.isSuccessful()) {
                    Vocabulary Response = response.body();
                    if (Response != null) {
                        vocabularyList.clear();
                        vocabularyList.addAll(response.body().getVocabulary());
                        vocabularyAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(VocabularyActivity.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Vocabulary> call, @NonNull Throwable t) {
                Toast.makeText(VocabularyActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    interface VocabularyApiService {
        @GET("generateVocabulary")
        Call<VocabularyActivity.Vocabulary> generateVocabulary(@Query("language") String language, @Query("topic") String topic);
    }

    public static class Vocabulary {
        public Vocabulary(List<String> vocabulary) {
            this.vocabulary = vocabulary;
        }

        public List<String> getVocabulary() {
            return vocabulary;
        }

        public void setVocabulary(List<String> vocabulary) {
            this.vocabulary = vocabulary;
        }

        List<String> vocabulary;


    }

    public static class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.VocabularyViewHolder> {
        private List<String> vocabularyList;

        public VocabularyAdapter(List<String> vocabularyList) {
            this.vocabularyList = vocabularyList;
        }

        @NonNull
        @Override
        public VocabularyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocabulary, parent, false);
            return new VocabularyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VocabularyViewHolder holder, int position) {
            String word = vocabularyList.get(position);
            holder.wordTextView.setText(word);
        }

        @Override
        public int getItemCount() {
            return vocabularyList.size();
        }

        public static class VocabularyViewHolder extends RecyclerView.ViewHolder {
            TextView wordTextView;

            public VocabularyViewHolder(@NonNull View itemView) {
                super(itemView);
                wordTextView = itemView.findViewById(R.id.wordTextView);
            }
        }
    }
}

