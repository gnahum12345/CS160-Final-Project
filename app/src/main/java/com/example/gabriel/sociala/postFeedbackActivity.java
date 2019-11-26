package com.example.gabriel.sociala;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class postFeedbackActivity extends AppCompatActivity {

    ImageView postImageView;
    Button backButton;
    ImageView profilePic;
    TextView userName;
    EditText caption, reason;
    Button addVideoButton, postFeedbackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_feedback);

        postImageView = findViewById(R.id.image_view_photo);
        backButton = findViewById(R.id.back_button);

        userName = findViewById(R.id.textView_username);
        profilePic = findViewById(R.id.profile_image);

        caption = findViewById(R.id.editText_caption);
        reason = findViewById(R.id.editText_reason);

        addVideoButton = findViewById(R.id.add_video_button);
        postFeedbackButton = findViewById(R.id.post_feedback_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToEdit();
            }
        });

        postFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postFeedback();
            }
        });

        addVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVideo();
            }
        });

    }

    public void backToEdit() {
        Intent intent = new Intent(this, editFeedbackActivity.class);
        startActivity(intent);
    }

    public void postFeedback() {
        // TODO: add to database and navigate to the post.
    }

    public void addVideo() {
        // TODO: add the video to database and display it on post page.
    }
}
