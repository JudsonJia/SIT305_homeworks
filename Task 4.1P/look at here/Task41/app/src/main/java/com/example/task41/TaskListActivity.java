package com.example.task41;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TaskManagerDB taskManagerDB;
    List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_layout);

        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskManagerDB = new TaskManagerDB(this);
        taskList = taskManagerDB.getAllTasks();

        TaskListAdapter adapter = new TaskListAdapter(this, taskList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TaskListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Task task = taskList.get(position);
                Intent intent = new Intent(TaskListActivity.this, TaskDetails.class);
                intent.putExtra("TASK_ID", task.getId());
                startActivity(intent);
                finish();
            }
        });
    }

}
