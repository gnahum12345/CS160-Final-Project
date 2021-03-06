package com.example.gabriel.sociala;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.example.gabriel.sociala.models.Feedback;
import com.example.gabriel.sociala.models.Photo;
import com.example.gabriel.sociala.models.Post;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PostManager {
    private static final PostManager ourInstance = new PostManager();
    private static final String TAG = "PostManager";

    public static PostManager getInstance() {
        return ourInstance;
    }

    private PostManager() {
    }


    public interface PostManagerListener {
        void usersAreReady(List<ParseUser> objects);
    }

    public void getAllUsers(final PostManagerListener listener) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                   listener.usersAreReady(objects);
                } else {
                    e.printStackTrace();
                    Log.e(TAG, "GET FEEDBACKS with USER: ", e);
                }
            }
        });
    }

  
    public void getFeedbacks(final RecyclerView.Adapter adapter, final List<Feedback> obj) {
        Feedback.Query feedbackQuery = new Feedback.Query();
        feedbackQuery.currentUserFeedback().findInBackground(new FindCallback<Feedback>() {
            @Override
            public void done(List<Feedback> objects, ParseException e) {
                if (e == null) {
                    obj.clear();
                    obj.addAll(objects);
                    adapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                    Log.e(TAG, "GET FEEDBACKS with USER: ", e);
                }
            }
        });
    }

    public void getFeedbacks(Post p, final RecyclerView.Adapter adapter, final List<Feedback> obj) {
        Feedback.Query feedbackQuery = new Feedback.Query();
        feedbackQuery.withPost(p);
        feedbackQuery.findInBackground(new FindCallback<Feedback>() {
            @Override
            public void done(List<Feedback> objects, ParseException e) {
                if (e == null) {
                    obj.clear();
                    obj.addAll(objects);
                    adapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                    Log.e(TAG, "GET FEEDBACKS: ", e);
                }
            }
        });
    }

    public void getMyPosts(final RecyclerView.Adapter adapter, final List<Post> adapterObj) {
        Post.Query postQuery = new Post.Query();
        postQuery = postQuery.currentUserPost();
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    adapterObj.clear();
                    adapterObj.addAll(objects);
                    adapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                    Log.e("Get My Posts", "Failed to get my own posts.... :'(", e);
                }
            }
        });
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
                    Log.e(TAG, "Failed to get friends: ");
                }
            }
        });
    }

    protected void getInfluencers(final RecyclerView.Adapter adapter, final List<Post> adapterObj) {
        Post.Query postQuery = new Post.Query();
        postQuery = postQuery.areInfluencers();
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    adapterObj.clear();
                    adapterObj.addAll(objects);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Failed to get posts ");
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
        acl.setReadAccess(ParseUser.getCurrentUser(), true); // current user can always see their own posts.
        acl.setWriteAccess(ParseUser.getCurrentUser(), true);
        p.setACL(acl);
        p.setCreator(ParseUser.getCurrentUser());
        p.saveInBackground(callback);
    }


    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        Integer width, height;
        private final String TAG = "DownloadImageTask";
        Button btn;
        Resources r;
        public DownloadImageTask(Button b, Integer w, Integer h, Resources r) {
            this.btn = b;
            this.width = w;
            this.height = h;
            this.r = r;
        }

        public DownloadImageTask(ImageView bmImage, Integer width, Integer height) {
            this.bmImage = bmImage;
            this.width = width;
            this.height = height;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (this.height != null && this.width != null) {
                result = BitmapScalar.scaleToFill(result, this.width, this.height);
            } else if (this.height != null) {
                result = BitmapScalar.scaleToFitHeight(result, this.height);
            } else if (this.width != null) {
                result = BitmapScalar.scaleToFitWidth(result, this.width);
            }

            if (btn != null) {
                Drawable d = new BitmapDrawable(this.r, result);
                btn.setBackground(d);
            } else {
                bmImage.setImageBitmap(result);
            }
        }
    }

}

