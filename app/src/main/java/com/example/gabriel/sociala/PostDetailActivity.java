/*TODO: figure out how to get the specific post data when we click on a post grid*/

package com.example.gabriel.sociala;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabriel.sociala.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;


public class PostDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PostDetailActivity";

    private Button back;
    private Button viewFeedbacks;
    private Button provideFeedback;
    private ImageView ivProfilePic;
    private ImageView ivMainPhoto;
    private TextView tvUserName;
    private TextView tvCaption;
    private TextView tvReason;
    private TextView tvSchedulePost;

    private Post post;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        back = (Button) findViewById(R.id.post_detail_back_button);
        viewFeedbacks = (Button) findViewById(R.id.view_feedback_btn);
        provideFeedback = findViewById(R.id.provide_feedback_btn);
        ivProfilePic = findViewById(R.id.profile_pic);
        ivMainPhoto = findViewById(R.id.post_photo);
        tvCaption = findViewById(R.id.post_caption);
        tvReason = findViewById(R.id.post_requirement);
        tvUserName = findViewById(R.id.username);
        tvSchedulePost = findViewById(R.id.schedulePostBtn);

        Intent i = getIntent();
        final String objectID = i.getStringExtra("postID");
        if (objectID == null) {
            finish();
        }

        Post.Query query = new Post.Query();
        query = query.specificPost(objectID);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    post = objects.get(0);
                    if (!post.getACL().getReadAccess(ParseUser.getCurrentUser())) {
                        provideFeedback.setVisibility(View.INVISIBLE);
                    } else {
                        provideFeedback.setVisibility(View.VISIBLE);
                    }

                    String caption = post.getCaption();
                    String reason = post.getPurpose();

                    tvCaption.setText(caption);
                    tvReason.setText(reason);

                    new PostManager.DownloadImageTask(ivMainPhoto, 300, null)
                            .execute(post.getPhoto().getUrl());

                    ParseUser creator = post.getCreator();

                    try {
                        creator = creator.fetchIfNeeded();
                        if (creator == ParseUser.getCurrentUser()) {
                            tvSchedulePost.setVisibility(View.VISIBLE);
                        } else {
                            tvSchedulePost.setVisibility(View.GONE);
                        }
                        tvUserName.setText(creator.getUsername());
                        ParseFile pf = creator.getParseFile("profilePic");
                        if (pf != null) {
                            new PostManager.DownloadImageTask(ivProfilePic, 300, null).execute(pf.getUrl());
                        }
                    } catch (ParseException e1) {
                        e.printStackTrace();
                    }

                } else {
                    e.printStackTrace();
                    Log.e(TAG, "done: ", e);
                }
            }
        });
        tvSchedulePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Unfortunately this feature wasn't fully built yet. ", Snackbar.LENGTH_LONG).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewFeedbacks.setOnClickListener(this);
        provideFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostDetailActivity.this, EditFeedbackActivity.class);
                if (post != null) {
                    i.putExtra("postID", post.getID());
                    startActivity(i);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.post_detail_back_button) {
            finish();
        } else if (v.getId() == R.id.view_feedback_btn) {
            Intent feedbacksIntent = new Intent(PostDetailActivity.this, FeedbacksActivity.class);
            feedbacksIntent.putExtra("postID", post.getID());
            startActivity(feedbacksIntent);
        }
    }
}
