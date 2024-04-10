package com.example.task41;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class TaskDetails extends AppCompatActivity {
    private static final int RESULT_UPDATED = 1;
    private static final int RESULT_DELETED = 2;
    // 实现任务详情界面的逻辑
    EditText etTitle, etDescription, etDueDate;
    Button btnUpdate, btnDelete;
    TaskManagerDB taskManagerDB;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        etTitle = findViewById(R.id.editTextTitle);
        etDescription = findViewById(R.id.editTextDescription);
        etDueDate = findViewById(R.id.editTextDueDate);
        btnUpdate = findViewById(R.id.btnUpdateTask);
        btnDelete = findViewById(R.id.btnDeleteTask);

        taskManagerDB = new TaskManagerDB(this);

        Intent intent = getIntent();
        String taskId = intent.getStringExtra("TASK_ID");
        task = taskManagerDB.getTask(taskId);

        etTitle.setText(task.getTitle());
        etDescription.setText(task.getDescription());
        etDueDate.setText(task.getDueDate());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                String dueDate = etDueDate.getText().toString();

                task.setTitle(title);
                task.setDescription(description);
                task.setDueDate(dueDate);

                taskManagerDB.updateTask(task);
                Toast.makeText(TaskDetails.this, "Task was updated!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(TaskDetails.this, TaskListActivity.class));
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskManagerDB.deleteTask(task);
                Toast.makeText(TaskDetails.this, "Task was Deleted", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(TaskDetails.this, TaskListActivity.class));
            }
        });
    }
}
