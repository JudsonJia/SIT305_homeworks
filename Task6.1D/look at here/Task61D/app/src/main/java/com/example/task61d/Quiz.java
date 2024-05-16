package com.example.task61d;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Quiz extends AppCompatActivity {
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private ProgressBar progressBar;

    private String description;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_content);
        description = (String) getIntent().getExtras().get("description");
        quiz();
        fetchData();
    }

    private void quiz() {
        questionTextView = findViewById(R.id.questions);
        optionsRadioGroup = findViewById(R.id.answerZone);
        progressBar = findViewById(R.id.progressBar);
        optionsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.answer0) {
                    checkAnswer(0, group.findViewById(checkedId));
                } else if (checkedId == R.id.answer1) {
                    checkAnswer(1, group.findViewById(checkedId));
                } else if (checkedId == R.id.answer2) {
                    checkAnswer(2, group.findViewById(checkedId));
                } else if (checkedId == R.id.answer3) {
                    checkAnswer(3, group.findViewById(checkedId));
                }
            }
        });
    }

    private void displayQuestion(int questionIndex) {
        if (questions != null && !questions.isEmpty() && questionIndex < questions.size()) {
            Question question = questions.get(questionIndex);
            questionTextView.setText(question.getQuestionText());
            optionsRadioGroup.clearCheck();

            for (int i = 0; i < optionsRadioGroup.getChildCount(); i++) {
                RadioButton optionButton = (RadioButton) optionsRadioGroup.getChildAt(i);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        optionButton.setBackground(getResources().getDrawable(R.drawable.normal_option));
                    }
                }, 100);

                optionButton.setText(question.getOptions().get(i));
            }
        }
    }

    private void checkAnswer(int selectedOptionIndex, RadioButton clicked) {
        if (questions != null && !questions.isEmpty() && currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            currentQuestionIndex++;
            progressBar.setProgress(currentQuestionIndex);
            TextView progressText = findViewById(R.id.progressNumber);
            progressText.setText(String.format("%d/%d", currentQuestionIndex, questions.size()));
            if (selectedOptionIndex == currentQuestion.getCorrectOptionIndex()) {
                score++;
                clicked.setBackground(getResources().getDrawable(R.drawable.change_style_true));
            } else {
                clicked.setBackground(getResources().getDrawable(R.drawable.change_style_false));
            }
            if (currentQuestionIndex < questions.size()) {
                displayQuestion(currentQuestionIndex);
            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setContentView(R.layout.final_page);
                        Button finish = findViewById(R.id.quit);
                        Button restart = findViewById(R.id.restart);
                        TextView showScore = findViewById(R.id.show_score);
                        double init_score = (double) score / questions.size() * 100.0;
                        int init_score_int = (int) init_score;
                        showScore.setText(String.format("%d", init_score_int));
                        TextView question1 = findViewById(R.id.question_1);
                        TextView answer1 = findViewById(R.id.answer_1);
                        TextView question2 = findViewById(R.id.question_2);
                        TextView answer2 = findViewById(R.id.answer_2);
                        TextView question3 = findViewById(R.id.question_3);
                        TextView answer3 = findViewById(R.id.answer_3);

                        question1.setText(String.format("Question1: %s", questions.get(0).getQuestionText()));
                        answer1.setText(String.format("Correct Answer: %s", questions.get(0).getOptions().get(questions.get(0).getCorrectOptionIndex())));

                        question2.setText(String.format("Question2: %s", questions.get(1).getQuestionText()));
                        answer2.setText(String.format("Correct Answer: %s", questions.get(1).getOptions().get(questions.get(1).getCorrectOptionIndex())));

                        question3.setText(String.format("Question3: %s", questions.get(2).getQuestionText()));
                        answer3.setText(String.format("Correct Answer: %s", questions.get(2).getOptions().get(questions.get(2).getCorrectOptionIndex())));
                        finish.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finishAffinity();
                            }
                        });
                        restart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                restartApp();
                            }
                        });
                    }
                }, 100);
            }
        }
    }

    private void fetchData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, java.util.concurrent.TimeUnit.MINUTES).build()) // this will set the read timeout for 10mins (IMPORTANT: If not your request will exceed the default read timeout)
                .build();

        QuizApiService request = retrofit.create(QuizApiService.class);


        request.getQuiz(description).enqueue(new Callback<QuizResponse>(){
            @Override
            public void onResponse(@NonNull Call<QuizResponse> call, @NonNull Response<QuizResponse> response) {

                if (response.isSuccessful()) {
                    QuizResponse quizResponse = response.body();
                    if(quizResponse != null){
                        questions = quizResponse.getQuiz();
                        displayQuestion(currentQuestionIndex);
                    }
                } else {
                    Toast.makeText(Quiz.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuizResponse> call, @NonNull Throwable t) {
                Toast.makeText(Quiz.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void restartApp() {
        Intent i = new Intent(this, this.getClass());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    interface QuizApiService {
        @GET("getQuiz")
        Call<QuizResponse> getQuiz(@Query("topic") String topic);
    }
}