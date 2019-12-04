package com.example.gabriel.sociala;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabriel.sociala.models.Post;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FeedbacksActivity extends AppCompatActivity implements View.OnClickListener {

    Button back;
    private MyRecyclerAdapter myRecyclerAdapter;
    RecyclerView rv_feedbacks;
    ArrayList<Post> myPosts;


    /*TODO, jump to a feedback detail page when click on a feedback grid*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedbacks);

        back = (Button) findViewById(R.id.feedbacks_back_button);
        rv_feedbacks = (RecyclerView) findViewById(R.id.rvFeedbacks);
        createFeedbackGrids(rv_feedbacks);
        back.setOnClickListener(this);

    }

    /* TODO: figure out how to switch back to the Post detail page of those feedback */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.feedbacks_back_button) {
            Intent postIntent = new Intent(FeedbacksActivity.this, PostDetailActivity.class);
            startActivity(postIntent);
        }
    }

    private class MyRecyclerAdapter extends RecyclerView.Adapter<GridHolder>{

        Bitmap[] bitmaps;

        List<Post> posts;

        public MyRecyclerAdapter(Bitmap[] bitmaps) {
            this.bitmaps = bitmaps;
        }

        private MyRecyclerAdapter(ArrayList<Post> posts) {
            this.posts = posts;
        }

        @NonNull
        @Override
        public GridHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(FeedbacksActivity.this).inflate(R.layout.feedback_grid_rv, viewGroup, false);
            return new GridHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final GridHolder gridHolder, int i) {
//            gridHolder.imageView.setImageBitmap(bitmaps[i]);
            final Post p = posts.get(i);
            gridHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(), p.getPhoto().getUrl(), Toast.LENGTH_SHORT).show();
                    ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                    ClipData myClip = ClipData.newPlainText("text", p.getPhoto().getUrl());

                    clipboardManager.setPrimaryClip(myClip);

                }
            });

            PostManager.DownloadImageTask dimv = new PostManager.DownloadImageTask(gridHolder.imageView);
            dimv.execute(p.getPhoto().getUrl());
//            Toast.makeText(getApplicationContext(), p.getPhoto().getUrl(), Toast.LENGTH_SHORT).show();
            String username = "";
            try {
                ParseUser creator = p.getCreator();
                username = creator.fetchIfNeeded().getUsername();
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            gridHolder.username.setText(username);
            gridHolder.userComment.setText(p.getCaption());
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }
    }

    private void createFeedbackGrids(RecyclerView v) {
        Bitmap[] bitmaps;
        myPosts = new ArrayList<>();
        myRecyclerAdapter = new MyRecyclerAdapter(myPosts);

        PostManager.getInstance().getFriends(myRecyclerAdapter, myPosts);

        v.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        v.setAdapter(myRecyclerAdapter);
    }

    private class GridHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView username;
        TextView userComment;

        private GridHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_feedback_Image);
            username = itemView.findViewById(R.id.iv_feedback_username);
            userComment = itemView.findViewById(R.id.iv_feedback_comment);
        }
    }
}
