package com.example.serviciosdownloadmanger;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // URL with the file to download, small file size
    String url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3";
    // URL with the file to download, big file size.
    // Use this if you have fast internet connection and you want to see download progress
    //String url = "https://releases.ubuntu.com/20.04.1/ubuntu-20.04.1-desktop-amd64.iso?_ga=2.99653022.1611649230.1611573443-123579438.1611573443";
    // ID of our download
    private long downloadID;

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(MainActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Listening if the download is completed, receiver will trigger when downloads ends
        registerReceiver(onDownloadComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void clickDownload(View v){
        // Creating file name from URL
        String[] urlSplitted = url.split("/");
        String fileName = urlSplitted[urlSplitted.length-1];

        // Setting up download manager requests
        // Contains all the information to request a new download
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                // Visibility of the download notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                // Path of the file
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                // Title and description of the download notification
                .setTitle("Music file")
                .setDescription("Wait until download ends")
                // Set if charging is required to begin the download
                .setRequiresCharging(false)
                // Set if download is allowed on Mobile network
                .setAllowedOverMetered(false)
                // Set if download is allowed on roaming network
                .setAllowedOverRoaming(false);

        DownloadManager downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        // enqueue puts the download request in the queue.
        downloadID = downloadManager.enqueue(request);
    }


    // If app is closed unregister the download receiver created
    @Override
    public void onDestroy() {
        super.onDestroy();
        // using broadcast method
        unregisterReceiver(onDownloadComplete);
    }

    // Only for testing that download does not freezes our GUI thread
    public void sayHi(View v){
        Toast.makeText(this, "Hi!", Toast.LENGTH_SHORT).show();
    }

}