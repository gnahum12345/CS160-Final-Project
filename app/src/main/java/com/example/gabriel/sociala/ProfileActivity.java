package com.example.gabriel.sociala;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.sociala.models.Post;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView rv_Profile;
    private Context context;
    ArrayList<Post> myPosts;
    TextView logout;
    private MyRecyclerAdapter myRecyclerAdapter;
    private static final String TAG = "ProfileActivity";

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;

        rv_Profile = (RecyclerView)findViewById(R.id.rvProfile);
        logout = (TextView) findViewById(R.id.logout);

        createProfileGrids(rv_Profile);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent profileIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                        startActivity(profileIntent);
                        PostManager.getInstance().getFriends(myRecyclerAdapter, myPosts);
                        break;
                    case R.id.nav_add:
                        Toast.makeText(context, "post", Toast.LENGTH_SHORT).show();
                        postPhoto();
                        break;
                    case R.id.nav_profile:
                        Toast.makeText(context, "profile: " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
                        PostManager.getInstance().getInfluencers(myRecyclerAdapter, myPosts);
                        Toast.makeText(context, "Refreshing page", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        logout.setOnClickListener(this);

    }

    private class MyRecyclerAdapter extends RecyclerView.Adapter<ProfileActivity.GridHolder>{

        Bitmap[] bitmaps;

        List<Post> posts;

        public MyRecyclerAdapter(Bitmap[] bitmaps) {
            this.bitmaps = bitmaps;
        }

        public MyRecyclerAdapter(ArrayList<Post> posts) {
            this.posts = posts;
        }

        @NonNull
        @Override
        public GridHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.home_grid_rv, viewGroup, false);
            return new GridHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GridHolder gridHolder, int i) {
            final Post p = posts.get(i);
            gridHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), p.getPhoto().getUrl(), Toast.LENGTH_SHORT).show();
                    ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                    ClipData myClip = ClipData.newPlainText("text", p.getPhoto().getUrl());

                    clipboardManager.setPrimaryClip(myClip);

                }
            });

            PostManager.DownloadImageTask dimv = new PostManager.DownloadImageTask(gridHolder.imageView);
            dimv.execute(p.getPhoto().getUrl());
            Toast.makeText(getApplicationContext(), p.getPhoto().getUrl(), Toast.LENGTH_SHORT).show();
            gridHolder.textView.setText(p.getCreator().getUsername() + ": " + p.getCaption());
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

    private void createProfileGrids(RecyclerView rv_profile) {
        myPosts = new ArrayList<>();
        myRecyclerAdapter = new MyRecyclerAdapter(myPosts);

        PostManager.getInstance().getInfluencers(myRecyclerAdapter, myPosts);
        rv_profile.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv_profile.setAdapter(myRecyclerAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logout) {
            ParseUser.logOut();
            Intent logoutIntent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(logoutIntent);
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
}
