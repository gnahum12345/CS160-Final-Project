package com.example.gabriel.sociala;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.gabriel.sociala.R;

public class PostPhotoActivity extends AppCompatActivity {

    ImageView selectedImageView;
    ImageButton backButton;
    Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        selectedImageView = findViewById(R.id.image_view);

        Intent intent = getIntent();
        String image_path = intent.getStringExtra("imagePath");
        Uri fileUri = Uri.parse(image_path);
        selectedImageView.setImageURI(fileUri);

        backButton = findViewById(R.id.imageButtonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        postButton = findViewById(R.id.button_post);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
