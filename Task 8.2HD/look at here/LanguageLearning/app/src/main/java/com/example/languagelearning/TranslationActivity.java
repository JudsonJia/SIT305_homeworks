package com.example.languagelearning;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class TranslationActivity extends AppCompatActivity {

    private EditText inputText;
    private Spinner sourceLanguageSpinner, targetLanguageSpinner;
    private Button translateButton, goBack;
    private TextView translationText;

    private String[] languages = {"en", "zh", "fr", "es", "de", "it", "ja", "ko", "ru"};
    //"en": English "zh": Chinese (Mandarin) "fr": French "es": Spanish "de": German "it": Italian "ja": Japanese "ko": Korean "ru": Russian

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);

        inputText = findViewById(R.id.input_text);
        sourceLanguageSpinner = findViewById(R.id.source_language_spinner);
        targetLanguageSpinner = findViewById(R.id.target_language_spinner);
        translateButton = findViewById(R.id.translate_button);
        translationText = findViewById(R.id.translation_text);
        goBack = findViewById(R.id.goBack);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceLanguageSpinner.setAdapter(adapter);
        targetLanguageSpinner.setAdapter(adapter);

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = inputText.getText().toString().trim();
                String sourceLanguage = languages[sourceLanguageSpinner.getSelectedItemPosition()];
                String targetLanguage = languages[targetLanguageSpinner.getSelectedItemPosition()];

                if (!text.isEmpty() && !sourceLanguage.equals(targetLanguage)) {
                    requestTranslation(text, sourceLanguage, targetLanguage);
                } else {
                    Toast.makeText(TranslationActivity.this, "Please enter text and select different languages", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void requestTranslation(String text, String sourceLanguage, String targetLanguage) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, TimeUnit.MINUTES).build())
                .build();

        translationApiService Service = retrofit.create(translationApiService.class);

        // Create request object
        Request request = new Request(text, sourceLanguage, targetLanguage);

        Call<Result> call = Service.sendEssay(request);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    Result translationResponse = response.body();
                    String result = translationResponse.getTranslation();
                    translationText.setText(result);
                } else {
                    Toast.makeText(TranslationActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                // Handle request failure
                Toast.makeText(TranslationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Define translation API service interface
    public static interface translationApiService {
        @POST("/translate")
        Call<Result> sendEssay(@Body Request request);
    }

    // Define translation result class
    public static class Result {
        private String translation;

        public String getTranslation() {
            return translation;
        }

        public void setTranslation(String translation) {
            this.translation = translation;
        }
    }

    // Define translation request class
    public static class Request {
        private String text;
        private String source_language;
        private String target_language;

        public Request(String text, String source_language, String target_language) {
            this.text = text;
            this.source_language = source_language;
            this.target_language = target_language;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getSource_language() {
            return source_language;
        }

        public void setSource_language(String source_language) {
            this.source_language = source_language;
        }

        public String getTarget_language() {
            return target_language;
        }

        public void setTarget_language(String target_language) {
            this.target_language = target_language;
        }
    }

}
