package com.example.pc.servicedemo;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pc on 1/8/18.
 */

public class DownloadService extends IntentService {

    public static final String NOTIFICATION = "com.example.pc.servicedemo.NOTIFICATION";

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d("FileService", "Service Started");
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        String passedURL = intent.getStringExtra("URL");
        downloadFile(passedURL);

        Log.d("FileService", "Service Stopped");
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();

        Intent i = new Intent(NOTIFICATION);
        DownloadService.this.sendBroadcast(i);


    }

    private void downloadFile(String passedURL) {


        String fileName = "myFile";

        try {
            FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);

            URL fileURL = new URL(passedURL);

            HttpURLConnection urlConnection = (HttpURLConnection) fileURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
