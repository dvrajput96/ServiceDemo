package com.example.pc.servicedemo;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("FileService", "Service Received");
            Toast.makeText(getApplicationContext(), "Service Received", Toast.LENGTH_LONG).show();

            showFileContents();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(DownloadService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.edt);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.NOTIFICATION);

        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void showFileContents() {
        StringBuilder stringBuilder;
        try {
            FileInputStream fileInputStream = this.openFileInput("myFile");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            stringBuilder = new StringBuilder();

            String line;

            while ((line = bufferedReader.readLine()) != null) {

                stringBuilder.append(line).append("\n");
            }

            editText.setText(stringBuilder.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onClick(View view) {

        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("URL", "https://en.wikipedia.org/wiki/Lemon");
        this.startService(intent);
    }

    public void onStop(View view) {
        Intent intent = new Intent(this, DownloadService.class);
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        stopService(intent);
    }
}
