package com.example.task61d;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    private List<Question> mQuestions;

    private String username;

    private ImageButton goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        goBack = findViewById(R.id.goBack_history);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String type;
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("type")) {
            type = Objects.requireNonNull(extras.getString("type"));
            username = Objects.requireNonNull(extras.getString("username"));
            // 然后继续执行后续操作
        } else {
            // 处理参数为空的情况
            type = "total";
        }


        // 根据类型加载不同的问题列表
        if (type.equals("total")) {
            // 加载所有问题
            mQuestions = getAllQuestions();
        } else if (type.equals("correct")) {
            // 加载正确答案的问题
            mQuestions = getCorrectQuestions();
        } else if (type.equals("incorrect")) {
            // 加载不正确答案的问题
            mQuestions = getIncorrectQuestions();
        }

        HistoryAdapter adapter = new HistoryAdapter(mQuestions, type, this);
        historyRecyclerView.setAdapter(adapter);
    }

    private List<Question> getAllQuestions() {
        DBHelper dbHelper = new DBHelper(HistoryActivity.this);
        return dbHelper.getUserALLContents(username);
    }

    private List<Question> getCorrectQuestions() {
        DBHelper dbHelper = new DBHelper(HistoryActivity.this);
        return dbHelper.getCorrectQuestionsByUserId(username);
    }

    private List<Question> getIncorrectQuestions() {
        DBHelper dbHelper = new DBHelper(HistoryActivity.this);
        return dbHelper.getIncorrectQuestionsByUserId(username);
    }
}