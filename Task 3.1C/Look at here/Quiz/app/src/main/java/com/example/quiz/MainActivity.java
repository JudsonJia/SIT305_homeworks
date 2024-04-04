package com.example.quiz;

import static com.example.quiz.R.drawable.*;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private ProgressBar progressBar;
    private String name;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText username = findViewById(R.id.userName);
        Button start = findViewById(R.id.startButton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = username.getText().toString();
                quiz();
            }
        });

    }

    private void quiz() {
        setContentView(R.layout.quiz_content);
        TextView welcomeMessage = findViewById(R.id.welcome);
        welcomeMessage.setText(String.format("Welcome %s!", name));
        questionTextView = findViewById(R.id.questions);
        optionsRadioGroup = findViewById(R.id.answerZone);
        progressBar = findViewById(R.id.progressBar);
        questions = loadQuizQuestions();
        displayQuestion(currentQuestionIndex);
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

    private void checkAnswer(int selectedOptionIndex, RadioButton clicked) {
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
                    TextView congratulation = findViewById(R.id.congratulation);
                    congratulation.setText("Congratulation " + name + "!");
                    TextView showScore = findViewById(R.id.show_score);
                    double init_score = (double) score / questions.size() * 100.0;
                    int init_score_int = (int) init_score;
                    showScore.setText(String.format("%d", init_score_int));
                    finish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.exit(0);
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

    private List<Question> loadQuizQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("What is the capital of France?", Arrays.asList("Paris", "London", "Berlin", "Marseille"), 0));
        questions.add(new Question("What is the largest planet in our solar system?", Arrays.asList("Earth", "Jupiter", "Mars", "Sun"), 1));
        questions.add(new Question("What is the capital of China?", Arrays.asList("Beijing", "Xi'an", "Shanghai", "Taiwan"), 0));
        questions.add(new Question("What is the capital of Australia?", Arrays.asList("Melbourne", "Sydney", "Canberra", "NSW"), 2));
        questions.add(new Question("What is the capital of Japan?", Arrays.asList("Hokkaido", "Nara-ken", "Osaka", "Tokyo"), 3));
        // Add more questions as needed
        return questions;
    }

    public void restartApp() {
        Intent i = new Intent(this, this.getClass());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

}


