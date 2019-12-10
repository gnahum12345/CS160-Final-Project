package com.example.gabriel.sociala;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

public class PlayVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        Intent i = getIntent();

        String fileURL = i.getStringExtra("file");

        Uri uri=Uri.parse(fileURL);

        VideoView video= findViewById(R.id.videoView);
        video.setVideoURI(uri);
        video.start();
    }
}
