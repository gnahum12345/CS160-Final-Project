package com.example.gabriel.sociala;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.gabriel.sociala.models.Post;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewFlipper viewFlipper;
    RecyclerView rv_friends;
    RecyclerView rv_explore;
    TextView btn_friends;
    TextView btn_explore;
    private Context context;
    private float initialX;
    private MyRecyclerAdapter myRecyclerAdapter;
    ArrayList<Post> myPosts;
    private static final String TAG = "HomeActivity";

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_home);
        context = this;
        viewFlipper = findViewById(R.id.view_flipper);
        rv_friends = (RecyclerView)findViewById(R.id.rvFriends);
        rv_explore = (RecyclerView)findViewById(R.id.rvExplore);
        createHomeGrids(rv_friends);
        createHomeGrids(rv_explore);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Toast.makeText(context, "Refreshing page", Toast.LENGTH_SHORT).show();
                        PostManager.getInstance().getFriends(myRecyclerAdapter, myPosts);
                        break;
                    case R.id.nav_add:
                        Toast.makeText(context, "post", Toast.LENGTH_SHORT).show();
                        postPhoto();
                        break;
                    case R.id.nav_profile:
                        Toast.makeText(context, "profile: " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
                        Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);

                        startActivity(profileIntent);
                        finish();
                        break;
                }
                return true;
            }
        });



        btn_explore = (TextView) findViewById(R.id.btn_explore);
        btn_friends = (TextView) findViewById(R.id.btn_friends);
        btn_friends.setOnClickListener(this);
        btn_explore.setOnClickListener(this);

        selectFriend();
    }




    private void createHomeGrids(RecyclerView v) {
        myPosts = new ArrayList<>();
        myRecyclerAdapter = new MyRecyclerAdapter(myPosts);

        if (v == (RecyclerView)findViewById(R.id.rvFriends)) {
            PostManager.getInstance().getFriends(myRecyclerAdapter, myPosts);
        } else {
            PostManager.getInstance().getInfluencers(myRecyclerAdapter, myPosts);
        }
        v.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        v.setAdapter(myRecyclerAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_explore) {
            if (viewFlipper.getDisplayedChild() != 0) {
                selectExplore();
            }
        } else {
            if (viewFlipper.getDisplayedChild() != 1) {
                selectFriend();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = touchevent.getX();
                break;

            case MotionEvent.ACTION_UP:
                float finalX = touchevent.getX();
                if (initialX > finalX) {
                    if (viewFlipper.getDisplayedChild() == 1)
                        break;

                    selectFriend();
                } else {
                    if (viewFlipper.getDisplayedChild() == 0)
                        break;

                    selectExplore();
                }
                break;
        }
        return true;
    }

    private void selectFriend() {
        viewFlipper.setDisplayedChild(1);
        btn_friends.setTextColor(getApplication().getColor(R.color.colorPrimary));
        btn_friends.setTextSize(25);
        btn_explore.setTextColor(getApplication().getColor(R.color.colorLightGrey));
        btn_explore.setTextSize(15);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_left));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_right));
    }

    private void selectExplore() {
        viewFlipper.setDisplayedChild(0);
        btn_explore.setTextColor(getApplication().getColor(R.color.colorPrimary));
        btn_explore.setTextSize(25);
        btn_friends.setTextColor(getApplication().getColor(R.color.colorLightGrey));
        btn_friends.setTextSize(15);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_right));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_left));
    }

    private class MyRecyclerAdapter extends RecyclerView.Adapter<GridHolder>{

        List<Post> posts;

        private MyRecyclerAdapter(ArrayList<Post> posts) {
            this.posts = posts;
        }

        @NonNull
        @Override
        public GridHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.home_grid_rv, viewGroup, false);
            return new GridHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final GridHolder gridHolder, int i) {
            final Post p = posts.get(i);
            gridHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                    ClipData myClip = ClipData.newPlainText("text", p.getPhoto().getUrl());
                    clipboardManager.setPrimaryClip(myClip);
                    Intent i = new Intent(HomeActivity.this, PostDetailActivity.class);
                    i.putExtra("postID", p.getID());
                    startActivity(i);
                }
            });

            PostManager.DownloadImageTask dimv = new PostManager.DownloadImageTask(gridHolder.imageView, 300, null);
            dimv.execute(p.getPhoto().getUrl());
            String username = "";
            try {
                ParseUser creator = p.getCreator();
                username = creator.fetchIfNeeded().getUsername();
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            gridHolder.textView.setText(username + ": " + p.getCaption());
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }
    }

    private class GridHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        private GridHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivImage);
            textView = itemView.findViewById(R.id.tvCaption);
        }
    }


    private void postPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                //permission not granted, request it.
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                //show popup for runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else {
                //permission already granted
                pickImageFromGallery();
            }
        }
        else {
            //system os is less then marshmallow
            pickImageFromGallery();
        }

    }


    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }


    //handle result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){

            Uri selectedImage = data.getData();
            final String[] columns = { MediaStore.MediaColumns.DATA };
            final Cursor cursor = this.getContentResolver().query(selectedImage, columns, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            String filePath = cursor.getString(columnIndex);

            Intent intent = new Intent(this, PostPhotoActivity.class);
            intent.putExtra("imagePath", selectedImage.toString());
            intent.putExtra("filePath", filePath);
            startActivity(intent);

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}
