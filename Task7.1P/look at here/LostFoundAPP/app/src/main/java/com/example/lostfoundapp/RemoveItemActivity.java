package com.example.lostfoundapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class RemoveItemActivity extends AppCompatActivity {

    private TextView tvItemType, tvItemName, tvItemDescription, tvItemDate, tvItemLocation;
    private Button btnRemove;

    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);

        tvItemType = findViewById(R.id.tvItemType);
        tvItemName = findViewById(R.id.tvItemName);
        tvItemDescription = findViewById(R.id.tvItemDescription);
        tvItemDate = findViewById(R.id.tvItemDate);
        tvItemLocation = findViewById(R.id.tvItemLocation);
        btnRemove = findViewById(R.id.btnRemove);

        // Retrieve the item from the intent
        item = (Item) getIntent().getSerializableExtra("item");

        // Display the item details
        tvItemType.setText(String.format("Type: %s", item.getType()));
        tvItemName.setText(String.format("Name: %s", item.getName()));
        tvItemDescription.setText(String.format("Description: %s", item.getDescription()));
        tvItemDate.setText(String.format("Date: %s", item.getDate()));
        tvItemLocation.setText(String.format("Location: %s", item.getLocation()));

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(item.getName(), item.getLocation());
                Intent intent = new Intent(RemoveItemActivity.this, RemoveItemActivity.class);
                startActivity(intent);
            }
        });

    }

    private void removeItem(String itemName, String itemLocation) {
        ItemDbHelper dbHelper = new ItemDbHelper(this);
        if(dbHelper.remove(itemName, itemLocation)){
            Toast.makeText(RemoveItemActivity.this, "Item removed successfully!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(RemoveItemActivity.this, "Mission Failed.", Toast.LENGTH_SHORT).show();
        }
        // Finish the activity and return to the previous screen
        finish();
    }
}
