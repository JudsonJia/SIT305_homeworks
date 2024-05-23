package com.example.languagelearning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class WritingActivity extends AppCompatActivity {
    private TextView task;
    private Button check;
    private EditText essay;

    private String writingTask, Essay, language, topic, username, LlamaComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        task = findViewById(R.id.task);
        check = findViewById(R.id.check);
        essay = findViewById(R.id.Essay);

        topic = (String) getIntent().getExtras().get("topic");
        language = (String) getIntent().getExtras().get("language");
        username = (String) getIntent().getExtras().get("username");

        fetchWritingTask();

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Essay = String.valueOf(essay.getText());
                if (!Essay.isEmpty()) {
                    setContentView(R.layout.activity_writing_check_result);
                    TextView essay = findViewById(R.id.Essay_result);
                    essay.setText(Essay);
                    Button goBack = findViewById(R.id.goBack);

                    goBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(WritingActivity.this, LearningSelection.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                            finish(); // Close the current page
                        }
                    });

                    //get comment from Llama2
                    getComment(Essay, writingTask);

                }else {
                    Toast.makeText(WritingActivity.this, "Please input your essay.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //get task from Llama2
    private void fetchWritingTask() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, java.util.concurrent.TimeUnit.MINUTES).build()) // this will set the read timeout for 10mins (IMPORTANT: If not your request will exceed the default read timeout)
                .build();

        WritingApiService request = retrofit.create(WritingApiService.class);


        request.getWritingExercise(language, topic).enqueue(new Callback<writingResponse>() {
            @Override
            public void onResponse(@NonNull Call<writingResponse> call, @NonNull Response<writingResponse> response) {

                if (response.isSuccessful()) {
                    writingResponse quizResponse = response.body();
                    if (quizResponse != null) {
                        writingTask = quizResponse.getTask();
                        task.setText(writingTask);
                    }
                } else {
                    Toast.makeText(WritingActivity.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<writingResponse> call, @NonNull Throwable t) {
                Toast.makeText(WritingActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static interface  WritingApiService {
        @GET("getWritingExercise")
        Call<writingResponse> getWritingExercise(@Query("language") String language, @Query("topic") String topic);
    }


    private static class writingResponse {
        private String task;


        public String getTask() {
            return task;
        }

        public void setTask(String task) {
            this.task = task;
        }
    }

    private void getComment(String essay, String task) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, TimeUnit.MINUTES).build())
                .build();

        AIComment commentService = retrofit.create(AIComment.class);

        // Create request object
        Request request = new Request(essay, task);

        Call<Comment> call = commentService.sendEssay(request);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    Comment commentResponse = response.body();

                    // Get message content from parsed object
                    LlamaComment = commentResponse.getMessage();
                    TextView comment = findViewById(R.id.Comments);
                    comment.setText(LlamaComment);

                } else {
                    Toast.makeText(WritingActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                // Handle request failure
                Toast.makeText(WritingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static interface AIComment {
        @POST("/evaluateWriting")
        Call<Comment> sendEssay(@Body Request request);
    }

    public static class Comment {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class Request {
        private String writing;
        private String task;

        public String getEssay() {
            return writing;
        }

        public void setEssay(String essay) {
            this.writing = essay;
        }

        public String getTask() {
            return task;
        }

        public void setTask(String task) {
            this.task = task;
        }


        public Request(String essay, String task) {
            this.writing = essay;
            this.task = task;
        }
    }
}
