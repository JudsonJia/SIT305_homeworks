package com.example.task61d;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyAccountActivity extends AppCompatActivity {

    private TextView correct_number;
    private TextView incorrect_number;
    private TextView username;
    private TextView email;
    private LinearLayout total, correct, incorrect, share;

    private Button upgrade;
    private ImageButton goBack;

    private String Username;
    private List<Question> list;

    private String Email;

    private ExecutorService executorService;
    private Future<?> future;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        executorService = Executors.newSingleThreadExecutor();

        TextView total_number = findViewById(R.id.total_number);
        correct_number = findViewById(R.id.correct_number);
        incorrect_number = findViewById(R.id.incorrect_number);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email_display);

        total = findViewById(R.id.total_history);
        correct = findViewById(R.id.correctly_history);
        incorrect = findViewById(R.id.incorrect_history);
        share = findViewById(R.id.share);

        upgrade = findViewById(R.id.upgrade);

        goBack = findViewById(R.id.goBack_MyAccount);

        Intent intent = getIntent();
        if (intent != null) {
            Username = intent.getStringExtra("username");
        }
        if (Username == null) {
            throw new IllegalArgumentException("Username is null");
        }
        username.setText(Username);

        future = executorService.submit(new LoadDataTask(this, Username));

        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "total";
                Intent intent = new Intent(MyAccountActivity.this, HistoryActivity.class);
                Bundle extras = new Bundle();
                extras.putString("username", Username);
                extras.putString("type", type);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "correct";
                Intent intent = new Intent(MyAccountActivity.this, HistoryActivity.class);
                Bundle extras = new Bundle();
                extras.putString("username", Username);
                extras.putString("type", type);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        incorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "incorrect";
                Intent intent = new Intent(MyAccountActivity.this, HistoryActivity.class);
                Bundle extras = new Bundle();
                extras.putString("username", Username);
                extras.putString("type", type);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String profileData = "Hi, my name is " + Username + ". This is an example to share something to you!";
                Bitmap qrCodeBitmap = generateQRCode(profileData);

                if (qrCodeBitmap != null) {
                    shareQRCode(MyAccountActivity.this, qrCodeBitmap);
                }
            }
        });

        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, UpgradeActivity.class);
                startActivity(intent);
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
        executorService.shutdown();
    }

    private static class LoadDataTask implements Runnable {
        private WeakReference<MyAccountActivity> activityReference;
        private String username;

        private int correctCount;
        private int incorrectCount;
        private int totalCount;
        private String userEmail;

        LoadDataTask(MyAccountActivity context, String username) {
            this.activityReference = new WeakReference<>(context);
            this.username = username;
        }

        @Override
        public void run() {
            MyAccountActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            DBHelper dbHelper = new DBHelper(activity);

            List<Question> list = dbHelper.getCorrectQuestionsByUserId(username);
            correctCount = list.size();

            list = dbHelper.getIncorrectQuestionsByUserId(username);
            incorrectCount = list.size();

            list = dbHelper.getUserALLContents(username);
            totalCount = list.size();

            userEmail = dbHelper.getUserEmailById(username);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (activityReference.get() != null && !activityReference.get().isFinishing()) {
                        activity.correct_number.setText(String.valueOf(correctCount));
                        activity.incorrect_number.setText(String.valueOf(incorrectCount));
                        TextView total_number = activity.findViewById(R.id.total_number);
                        total_number.setText(String.valueOf(totalCount));
                        activity.email.setText(userEmail);
                    }
                }
            });
        }
    }

    public Bitmap generateQRCode(String content) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = null;
        try {
            BitMatrix bitMatrix = barcodeEncoder.encode(content, BarcodeFormat.QR_CODE, 400, 400);
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Uri saveBitmapAndGetUri(Context context, Bitmap bitmap) {
        File imagePath = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "qr_code.png");
        try {
            FileOutputStream fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取FileProvider的URI
        return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", imagePath);
    }

    public void shareQRCode(Context context, Bitmap bitmap) {
        Uri uri = saveBitmapAndGetUri(context, bitmap);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my QRcode");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/png");
        // 授予临时访问权限
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "Share it to"));
    }
}