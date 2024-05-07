package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListItemsActivity extends AppCompatActivity {

    private ListView lvItems;
    private List<Item> itemList;
    private ArrayAdapter<Item> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        lvItems = findViewById(R.id.lvItems);
        itemList = new ArrayList<>();

        adapter = new ListViewAdapter(this, R.layout.item_layout, itemList);
        lvItems.setAdapter(adapter);


        fetchItemList();

        adapter.notifyDataSetChanged();
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = itemList.get(position);
                Intent intent = new Intent(ListItemsActivity.this, RemoveItemActivity.class);
                intent.putExtra("item", (Serializable) item);
                startActivity(intent);
            }
        });
    }

    private void fetchItemList() {
        ItemDbHelper dbHelper = new ItemDbHelper(this);
        itemList.clear();
        dbHelper.fetchItemList(itemList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchItemList();
    }
}