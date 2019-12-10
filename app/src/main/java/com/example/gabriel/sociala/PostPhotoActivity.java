package com.example.gabriel.sociala;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostPhotoActivity extends AppCompatActivity  implements PostManager.PostManagerListener {


    ImageView selectedImageView;
    ImageButton backButton;
    Button postButton;
  
    TextView userName;
    CircleImageView profilePic;
    EditText rv_caption;
    EditText rv_purpose;
    final ArrayList<File> files = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        selectedImageView = findViewById(R.id.image_view_photo);
        userName = findViewById(R.id.textView_username);
        profilePic = findViewById(R.id.profile_image);
        rv_caption = findViewById(R.id.editText_caption);
        rv_purpose = findViewById(R.id.editText_purpose);
        try {
            ParseFile pf = ParseUser.getCurrentUser().getParseFile("profilePic");
            if (pf != null) {
                new PostManager.DownloadImageTask(profilePic, null,280)
                        .execute(pf.getUrl());
            }
        } catch (IllegalStateException e) {
            // current user doesn't have a profile pic.
        }
        userName.setText(ParseUser.getCurrentUser().getUsername());
        Intent intent = getIntent();
        final String filePath = intent.getStringExtra("filePath");

        final String image_path = intent.getStringExtra("imagePath");
        final Uri fileUri = Uri.parse(image_path);

        selectedImageView.setImageURI(fileUri);

        backButton = findViewById(R.id.imageButtonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostPhotoActivity.this, HomeActivity.class));
                finish();
            }
        });

        postButton = findViewById(R.id.button_post);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File f = new File(filePath);
                files.add(f); // add all files.
                String caption = rv_caption.getText().toString();
                String purpose = rv_purpose.getText().toString();
                if (!f.exists()) {
                    Snackbar.make(view, "oh oh this isn't right" + f.getAbsolutePath() + " " + f.exists(),Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (purpose.isEmpty()) {
                    Snackbar.make(view, "Please fill out the purpose of posting", Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            files.clear();
                            finish();
                        }
                    }).show();
                }
                PostManager.getInstance().getAllUsers(PostPhotoActivity.this);

            }
        });
    }

    @Override
    public void usersAreReady(final List<ParseUser> objects) {
        if (files.size() == 0) {
            return;
        }
        final String[] userNames = new String[objects.size()];
        for (int i = 0; i < userNames.length; i++) {
            userNames[i] = objects.get(i).getUsername();
        }
        final ArrayList<String> permittedUsers = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(PostPhotoActivity.this);
        builder.setTitle("Choose to Share with Users");

        final boolean[] checkedItems = new boolean[userNames.length]; //this will checked the items when user open the dialog
        builder.setMultiChoiceItems(userNames, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedItems[which] = isChecked;
            }
        });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < userNames.length; i++) {
                    if (checkedItems[i]) {
                        permittedUsers.add(objects.get(i).getObjectId());
                    }
                }
                createPost(permittedUsers);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createPost(ArrayList<String> permittedUsers) {
        String caption = rv_caption.getText().toString();
        String purpose = rv_purpose.getText().toString();

        PostManager.getInstance().createPost(caption, purpose, files, permittedUsers, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                startActivity(new Intent(PostPhotoActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
}
