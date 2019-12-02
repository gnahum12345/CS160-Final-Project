package com.example.gabriel.sociala;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditFeedbackActivity extends AppCompatActivity {

    ImageView postImageView;
    Button backButton, nextButton;
    Button filterButton, tagButton, stickerButton, musicButton;
    ImageView profilePic;
    TextView userName;
    EditText caption, reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_feedback);

        postImageView = findViewById(R.id.image_view_photo);
        backButton = findViewById(R.id.back_button);
        nextButton = findViewById(R.id.next_button);
        filterButton = findViewById(R.id.filter_button);
        tagButton = findViewById(R.id.tag_button);
        stickerButton = findViewById(R.id.sticker_button);
        musicButton = findViewById(R.id.music_button);

        userName = findViewById(R.id.textView_username);
        profilePic = findViewById(R.id.profile_image);

        caption = findViewById(R.id.editText_caption);
        reason = findViewById(R.id.editText_reason);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: back to previous page.
                finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add image processing api.
                finish();
            }
        });

    }
}
