package com.example.gabriel.sociala;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.example.gabriel.sociala.models.Photo;
import com.example.gabriel.sociala.models.Post;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PostManager {
    private static final PostManager ourInstance = new PostManager();

    public static PostManager getInstance() {
        return ourInstance;
    }

    private PostManager() {
    }

    public void getFriends(final RecyclerView.Adapter adapter, final List<Post> adapterObj) {
        Post.Query postQuery = new Post.Query();
        postQuery = postQuery.visibleUserPost();
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    adapterObj.clear();
                    adapterObj.addAll(objects);
                    adapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                    Log.e("Get Friends: ", "Failed to get friends: ");
                }
            }
        });
    }

    public void getInfluencers(final RecyclerView.Adapter adapter, final List<Post> adapterObj) {
        Post.Query postQuery = new Post.Query();
        postQuery = postQuery.areInfluencers().visibleUserPost();
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    adapterObj.clear();
                    adapterObj.addAll(objects);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("Get influencers", "Failed to get posts ");
                    e.printStackTrace();
                }
            }
        });
    }
    public void createPost(String caption, String purpose, ArrayList<File> files, ArrayList<String> users) {
        createPost(caption, purpose, files, users, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void createPost(String caption, String purpose, ArrayList<File> files, ArrayList<String> users, SaveCallback callback) {
        Post p = new Post();
        p.setCaption(caption);
        ArrayList<String> photos = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            ParseFile pf = new ParseFile(files.get(i));
            Photo photo = new Photo();
            photo.setPhoto(pf);
            photo.setName(p.getID() + "-" + i);
            photos.add(photo.getID());
            photo.saveInBackground();
        }
        p.setPhotos(photos);
        p.setInfluencer(false);
        p.setPurpose(purpose);
        if (!files.isEmpty()) {
            p.setPhoto(new ParseFile(files.get(0)));
        }
        ParseACL acl = new ParseACL();
        acl.setPublicWriteAccess(false);
        acl.setPublicReadAccess(false);
        for (int i = 0; i < users.size(); i++) {
            acl.setReadAccess(users.get(i), true);
        }
        p.setACL(acl);
        p.setCreator(ParseUser.getCurrentUser());
        p.saveInBackground(callback);
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            result = BitmapScalar.scaleToFitWidth(result, 300);
            bmImage.setImageBitmap(result);
        }
    }

}

