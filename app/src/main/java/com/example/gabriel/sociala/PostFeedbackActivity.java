package com.example.gabriel.sociala;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.sociala.models.Feedback;
import com.example.gabriel.sociala.models.Photo;
import com.example.gabriel.sociala.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class PostFeedbackActivity extends AppCompatActivity {

    ImageView postImageView;
    Button backButton;
    ImageView profilePic;
    TextView userName;
    EditText caption, reason;
    Button addVideoButton, postFeedbackButton;
    File imgFile = null;
    Post post;
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
        final String postID = i.getStringExtra("postID");
        if (postID == null || postID.isEmpty()) {
            Toast.makeText(this, "Oh oh, we lost the reference to which post we were looking at. ", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Post.Query query = new Post.Query();
            query.specificPost(postID).findInBackground(new FindCallback<Post>() {
                @Override
                public void done(List<Post> objects, ParseException e) {
                    if (e == null) {
                        post = objects.get(0);

                        try {
                            ParseUser parseUser = post.getCreator().fetchIfNeeded();
                            userName.setText(parseUser.getUsername());

                            ParseFile pf = parseUser.getParseFile("");
                            if (pf != null) {
                                new PostManager.DownloadImageTask(profilePic, 300, 0).execute(pf.getUrl());
                            }
                        } catch (ParseException e1) {
                            Log.e("POST FEEDBACK", "done: Failed in getting user. ", e1);
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (!captionString.isEmpty()) {
            caption.setText(captionString);
        }
        reason.setText(reasonString);

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
                addVideo(); // currently doesn't do anything.
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
        if (imgFile != null) {
            final ParseFile pf = new ParseFile(imgFile);
            Photo p = new Photo();
            p.setName(post.getID());
            p.setPhoto(pf);
            p.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Feedback feedback = new Feedback();
                    feedback.setCaption(caption.getText().toString());
                    feedback.setComment(reason.getText().toString());
                    feedback.setEditedPhoto(pf);
                    feedback.setReviewer(ParseUser.getCurrentUser());
                    feedback.setPost(post);
//                    feedback.setVideoPath(null);
                    feedback.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // clean up first
                                cleanUp();
                                // switch activities.
                                startActivity(new Intent(PostFeedbackActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }

    private void cleanUp() {
        if (imgFile != null) {
            File parentDir = imgFile.getParentFile();
            for (File f : parentDir.listFiles()) {
                if (!f.delete()) {
                    Toast.makeText(this, "didn't delete "+ f.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            if (!parentDir.delete()) {
                Toast.makeText(this, "Didn't delete directory " + parentDir.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void addVideo() {
        // TODO: add the video to database and display it on post page.

    }
}
