package com.cs407.lab4_milestone1;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView downloadProgressText;

// Inside onCreate():
    private static final String TAG = "MainActivity";
    private Button startButton;
    private volatile boolean stopThread = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = findViewById(R.id.startButton);
        downloadProgressText = findViewById(R.id.downloadProgressText);

    }

    class ExampleRunnable implements Runnable{
        public void run(){
            mockFileDownloader();
        }
    }
    public void mockFileDownloader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Downloading...");
                downloadProgressText.setText("Download Progress: 0%"); // Initialize the text view at the beginning
            }
        });

        for (int downloadProgressValue = 0; downloadProgressValue <= 100; downloadProgressValue = downloadProgressValue + 10) {
            if (stopThread) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setText("Start");
                        downloadProgressText.setText("Download Stopped"); // Reset text view if stopped
                    }
                });
                return;
            }

            Log.d(TAG, "Download Progress: " + downloadProgressValue + "%");
            final int finalProgress = downloadProgressValue;

            // Update the TextView on the UI thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    downloadProgressText.setText("Download Progress: " + finalProgress + "%");
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Start");
            }
        });
    }

    public void startDownload(View view){
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
    }

    public void stopDownload(View view){
        stopThread = true;
    }
}