package com.example.languagelearning;

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


import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class QuizActivity extends AppCompatActivity {
    private List<Question> questions; // List of quiz questions
    private int currentQuestionIndex = 0; // Index of the current question
    private int score = 0; // User's score
    private TextView questionTextView; // TextView to display the question
    private RadioGroup optionsRadioGroup; // RadioGroup to display the options
    private ProgressBar progressBar; // ProgressBar to show the progress

    private String username; // Username of the user
    private String language; // Language selected by the user
    private String topic; // Topic selected by the user

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        topic = (String) getIntent().getExtras().get("topic");
        language = (String) getIntent().getExtras().get("language");
        username = (String) getIntent().getExtras().get("username");
        quiz(); // Initialize the quiz UI
        fetchData(); // Fetch the quiz questions from the server
    }

    // Initialize the quiz UI
    private void quiz() {
        questionTextView = findViewById(R.id.questions);
        optionsRadioGroup = findViewById(R.id.answerZone);
        progressBar = findViewById(R.id.progressBar);
        optionsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which option is selected and call the checkAnswer method
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

    // Display the question and options
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

    // Check if the selected option is correct
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
                        setContentView(R.layout.quiz_final_page);
                        Button goBack = findViewById(R.id.goBack);
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

                        goBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(QuizActivity.this, LearningSelection.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                                finish(); // Close the current page
                            }
                        });
                    }
                }, 100);
            }
        }
    }

    // Fetch the quiz questions from the server
    private void fetchData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, java.util.concurrent.TimeUnit.MINUTES).build()) // Set the read timeout to 10 minutes
                .build();

        QuizApiService request = retrofit.create(QuizApiService.class);

        // Call the getQuiz method with the selected language and topic
        request.getQuiz(language, topic).enqueue(new Callback<QuizResponse>(){
            @Override
            public void onResponse(@NonNull Call<QuizResponse> call, @NonNull Response<QuizResponse> response) {
                if (response.isSuccessful()) {
                    QuizResponse quizResponse = response.body();
                    if(quizResponse != null){
                        questions = quizResponse.getQuiz();
                        displayQuestion(currentQuestionIndex);
                    }
                } else {
                    Toast.makeText(QuizActivity.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuizResponse> call, @NonNull Throwable t) {
                Toast.makeText(QuizActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Interface for the Retrofit API service
    interface QuizApiService {
        @GET("getQuiz")
        Call<QuizResponse> getQuiz(@Query("language") String language, @Query("topic") String topic);
    }

    // Question class to hold question data
    public static class Question {
        @SerializedName("question")
        private String question; // Question text
        @SerializedName("options")
        private List<String> options; // List of options
        @SerializedName("correct_answer")
        private String correct_answer; // Correct answer

        private String topic; // Topic of the question

        public Question() {
        }

        // Getters and setters for the fields

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public void setOptions(List<String> options) {
            this.options = options;
        }

        public void setCorrect_answer(String correct_answer) {
            this.correct_answer = correct_answer;
        }

        public Question(String questionText, List<String> options, String correctAnswer) {
            this.question = questionText;
            this.options = options;
            this.correct_answer = correctAnswer;
        }

        public String getQuestionText() {
            return question;
        }

        public List<String> getOptions() {
            return options;
        }

        public String getCorrectAnswer() {
            return correct_answer;
        }

        // Get the index of the correct option
        public int getCorrectOptionIndex() {
            switch (correct_answer) {
                case "A":
                    return 0;
                case "B":
                    return 1;
                case "C":
                    return 2;
                case "D":
                    return 3;
                default:
                    return -1;
            }
        }
    }

    // QuizResponse class to hold the list of questions
    public static class QuizResponse {
        public List<Question> quiz;

        public List<Question> getQuiz() {
            return quiz;
        }

        public void setQuiz(List<Question> quiz) {
            this.quiz = quiz;
        }
    }
}