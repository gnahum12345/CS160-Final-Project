package com.example.gabriel.sociala;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostPhotoActivity extends AppCompatActivity {

    ImageView selectedImageView;
    ImageButton backButton;
    Button postButton;
    EditText etCaption;
    EditText etPurpose;
    CircleImageView ivProfile;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        selectedImageView = findViewById(R.id.image_view);
        etCaption = findViewById(R.id.etCaption);
        etPurpose = findViewById(R.id.etPurpose);
        ivProfile = findViewById(R.id.profile_image);
        tvName = findViewById(R.id.tvName);

        ParseFile pf = ParseUser.getCurrentUser().getParseFile("profilePic");
        if (pf != null) {
            new PostManager.DownloadImageTask(ivProfile)
                    .execute(pf.getUrl());
        }
        tvName.setText(ParseUser.getCurrentUser().getUsername());
        Intent intent = getIntent();
        final String filePath = intent.getStringExtra("filePath");

        final String image_path = intent.getStringExtra("imagePath");
        final Uri fileUri = Uri.parse(image_path);

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
                File f = new File(filePath);
                String caption = etCaption.getText().toString();
                String purpose = etPurpose.getText().toString();
                if (!f.exists()) {
                    Snackbar.make(view, "oh oh this isn't right" + f.getAbsolutePath() + " " + f.exists(),Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (purpose.isEmpty()) {
                    Snackbar.make(view, "Please fill out the purpose of posting", Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).show();
                }
                ArrayList<File> files = new ArrayList<>();
                files.add(f);
                ArrayList<String> users = new ArrayList<>();
                users.add("4sVi0zisCr");
                users.add("AMWRaP6MhM");
                users.add("izGnOFiIGK");
                PostManager.getInstance().createPost(caption, purpose, files, users, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        startActivity(new Intent(PostPhotoActivity.this, HomeActivity.class));
                        finish();
                    }
                });

            }
        });
    }
}
