package com.example.gabriel.sociala;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class PostFeedbackActivity extends AppCompatActivity {

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
        Intent i = getIntent();

        String filePath = i.getStringExtra("filePath");
        String captionString = i.getStringExtra("caption");
        String reasonString = i.getStringExtra("reason");
        caption.setText(captionString);
        reason.setText(reasonString);

        File imgFile = null;
        if (filePath != null && !filePath.isEmpty())  {
            File f = new File(filePath);
            imgFile = f.listFiles()[0];
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            postImageView.setImageBitmap(myBitmap);
        } else {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();
        }





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
        finish();
//        Intent intent = new Intent(this, EditFeedbackActivity.class);
//        startActivity(intent);
    }

    public void postFeedback() {
        // TODO: add to database and navigate to the post.

    }

    public void addVideo() {
        // TODO: add the video to database and display it on post page.
    }
}
