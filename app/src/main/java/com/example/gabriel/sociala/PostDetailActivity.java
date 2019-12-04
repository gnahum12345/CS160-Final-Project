/*TODO: figure out how to get the specific post data when we click on a post grid*/

package com.example.gabriel.sociala;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class PostDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Button back;
        Button viewFeedbacks;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        back = (Button) findViewById(R.id.post_detail_back_button);
        viewFeedbacks = (Button) findViewById(R.id.view_feedback_btn);
        back.setOnClickListener(this);
        viewFeedbacks.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.post_detail_back_button) {
            Intent homeIntent = new Intent(PostDetailActivity.this, HomeActivity.class);
            startActivity(homeIntent);
        } else if (v.getId() == R.id.view_feedback_btn) {
            Intent feedbacksIntent = new Intent(PostDetailActivity.this, FeedbacksActivity.class);
            startActivity(feedbacksIntent);
        }
    }
}
