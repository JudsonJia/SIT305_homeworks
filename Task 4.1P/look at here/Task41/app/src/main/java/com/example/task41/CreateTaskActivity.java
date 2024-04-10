package com.example.task41;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateTaskActivity extends AppCompatActivity {
    // 实现任务创建的逻辑
    EditText etTitle, etDescription, etDueDate;
    Button btnAddTask;
    TaskManagerDB taskManagerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etTitle = findViewById(R.id.TextTitle);
        etDescription = findViewById(R.id.TextDescription);
        etDueDate = findViewById(R.id.TextDueDate);
        btnAddTask = findViewById(R.id.SaveTask);

        taskManagerDB = new TaskManagerDB(this);

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                String dueDate = etDueDate.getText().toString();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(dueDate)) {
                    Task task = new Task(title + dueDate ,title, description, dueDate);
                    taskManagerDB.addTask(task);
                    Toast.makeText(CreateTaskActivity.this, "Already added the task!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateTaskActivity.this, "Please input all items!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
