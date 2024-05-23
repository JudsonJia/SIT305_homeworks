package com.example.languagelearning;

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

public class ReadingActivity extends AppCompatActivity {
    private List<Question> questions; // Store list of questions
    private int currentQuestionIndex = 0; // Current question index
    private int score = 0; // User score
    private TextView questionTextView; // TextView to display question
    private RadioGroup optionsRadioGroup; // RadioGroup for options
    private ProgressBar progressBar; // Progress bar
    private TextView passage; // TextView to display passage

    private String username;
    private String language;
    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        topic = (String) getIntent().getExtras().get("topic");
        language = (String) getIntent().getExtras().get("language");
        username = (String) getIntent().getExtras().get("username");
        initView();
        fetchData();
    }

    // Initialize views
    private void initView() {
        questionTextView = findViewById(R.id.questions);
        optionsRadioGroup = findViewById(R.id.answerZone);
        progressBar = findViewById(R.id.progressBar);
        passage = findViewById(R.id.passage);

        optionsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 根据选中的RadioButton检查答案
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

    // Display question at specified index
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

    // Check answer
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
                // 延迟一段时间后显示下一个问题或最终结果
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showFinalScore();
                    }

                }, 100);
            }

        }
    }

    // Show final score
    private void showFinalScore() {
        setContentView(R.layout.quiz_final_page);
        Button goBack = findViewById(R.id.goBack);
        TextView showScore = findViewById(R.id.show_score);
        double init_score = (double) score / questions.size() * 100.0;
        int init_score_int = (int) init_score;
        showScore.setText(String.format("%d", init_score_int));

        // Display each question and correct answer
        TextView question1 = findViewById(R.id.question_1);
        TextView answer1 = findViewById(R.id.answer_1);
        TextView question2 = findViewById(R.id.question_2);
        TextView answer2 = findViewById(R.id.answer_2);
        TextView question3 = findViewById(R.id.question_3);
        TextView answer3 = findViewById(R.id.answer_3);

        question1.setText(String.format("Question 1: %s", questions.get(0).getQuestionText()));
        answer1.setText(String.format("Correct Answer: %s", questions.get(0).getOptions().get(questions.get(0).getCorrectOptionIndex())));

        question2.setText(String.format("Question 2: %s", questions.get(1).getQuestionText()));
        answer2.setText(String.format("Correct Answer: %s", questions.get(1).getOptions().get(questions.get(1).getCorrectOptionIndex())));

        question3.setText(String.format("Question 3: %s", questions.get(2).getQuestionText()));
        answer3.setText(String.format("Correct Answer: %s", questions.get(2).getOptions().get(questions.get(2).getCorrectOptionIndex())));

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close current activity
            }
        });
    }

    // Fetch data from server
    private void fetchData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, java.util.concurrent.TimeUnit.MINUTES).build()) // 设置读取超时为10分钟
                .build();

        ReadingApiService request = retrofit.create(ReadingApiService.class);

        request.generateReadingComprehension(language, topic).enqueue(new Callback<ReadingComprehension>() {
            @Override
            public void onResponse(@NonNull Call<ReadingComprehension> call, @NonNull Response<ReadingComprehension> response) {
                if (response.isSuccessful()) {
                    ReadingComprehension readingResponse = response.body();

                    if (readingResponse != null) {
                        passage.setText(readingResponse.getPassage());
                        questions = readingResponse.getQuestions();
                        displayQuestion(currentQuestionIndex);
                    }
                } else {
                    Toast.makeText(ReadingActivity.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReadingComprehension> call, @NonNull Throwable t) {
                Toast.makeText(ReadingActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // API service interface
    interface ReadingApiService {
        @GET("generateReadingComprehension")
        Call<ReadingComprehension> generateReadingComprehension(@Query("language") String language, @Query("topic") String topic);
    }

    // Question class
    public static class Question {
        @SerializedName("question")
        private String question;
        @SerializedName("options")
        private List<String> options;
        @SerializedName("correct_answer")
        private String correct_answer;

        public Question() {
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

        // Return index of correct answer
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

    // Response class
    public static class ReadingComprehension {
        @SerializedName("passage")
        public String passage;
        @SerializedName("questions")
        public List<Question> questions;


        public List<Question> getQuestions() {
            return questions;
        }

        public void setQuestions(List<Question> questions) {
            this.questions = questions;
        }

        public String getPassage() {
            return passage;
        }

        public void setPassage(String passage) {
            this.passage = passage;
        }
    }
}