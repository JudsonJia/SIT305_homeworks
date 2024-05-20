package com.example.task61d;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class UpgradeActivity extends AppCompatActivity {

    private ImageButton goBack;
    private Button starterButton, intermediateButton, advancedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_purchasing);

        goBack = findViewById(R.id.goBack_Upgrade);
        starterButton = findViewById(R.id.starter_purchase);
        intermediateButton = findViewById(R.id.intermediate_purchase);
        advancedButton = findViewById(R.id.advanced_purchase);

        goBack.setOnClickListener(v -> finish());

        starterButton.setOnClickListener(v -> openStripeLink("https://buy.stripe.com/test_aEUdS23CD8I71hKeUU"));
        intermediateButton.setOnClickListener(v -> openStripeLink("https://buy.stripe.com/test_6oE5lw5KLe2r1hKeUV")); // 替换为中级链接
        advancedButton.setOnClickListener(v -> openStripeLink("https://buy.stripe.com/test_7sI8xI3CD4rRgcEdQT")); // 替换为高级链接
    }

    private void openStripeLink(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}