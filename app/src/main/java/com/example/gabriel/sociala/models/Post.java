package com.example.gabriel.sociala.models;


import com.parse.ParseACL;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {

    private final static String UUID_KEY = "objectId";
    private final static String PURPOSE_KEY  = "purpose";
    private final static String CAPTION_KEY = "caption";
    private final static String IMAGE_KEY = "image";
    private final static String CREATOR_KEY = "creator";
    private final static String INFLUENCER_KEY = "influencer";
    private final static String IMAGE_ARR_KEY = "imageArr";
    private static final String CREATED_DATE_KEY = "createdAt";



    public boolean isInfluencer() {
        return getBoolean(INFLUENCER_KEY); 
    }

    public ParseUser getCreator() {
        return getParseUser(CREATOR_KEY);
    }


    public String getPurpose() {
        return getString(PURPOSE_KEY);
    }

    public String getCaption() {
        return getString(CAPTION_KEY);
    }

    public ParseFile getPhoto() {
       return getParseFile(IMAGE_KEY);
    }

    public List<Photo> getPhotos() {
        List<Photo> arr = getList(IMAGE_ARR_KEY);
        return arr;

    }


    public Date getDate() {
        return getDate(CREATED_DATE_KEY);
    }

    public void setCreator(ParseUser user) {
        put(CREATOR_KEY, user);
    }
    public void setPhotos(ArrayList<String> photos) {
        List<String> obj = getList(IMAGE_ARR_KEY);
        if (obj == null) {
            obj = new ArrayList<>();
        }
        obj.addAll(photos);

        put(IMAGE_ARR_KEY, obj);
    }
    public void setPhoto(ParseFile f) {
        put(IMAGE_KEY, f);
    }

    public void setCaption(String caption) {
        put(CAPTION_KEY, caption);
    }

    public void setPurpose(String purpose) {
        put(PURPOSE_KEY, purpose);
    }

    public void setInfluencer(boolean isInfluencer) {
        put(INFLUENCER_KEY, isInfluencer); 
    }
    /**
     * If the creator wants to add a user as a "can see" option.
     */
    public void grantAccess(ParseUser u) {
       ParseACL acl = getACL();
       acl.setWriteAccess(u, true);
       acl.setReadAccess(u, true);
       setACL(acl);
    }

    /**
     * If the creator wants to remove a user as a "can see" option.
     * (i.e. User u will not be able to see this post).
     */
    public void removeAccess(ParseUser u) {
        ParseACL acl = getACL();
        acl.setWriteAccess(u, false);
        setACL(acl);
    }

    /**
     * Returns true if the user has access to see the post.
     */
    public boolean hasAccess(ParseUser u) {
        ParseACL acl = getACL();
        return acl.getWriteAccess(u);
    }


    /**
     * Throws PostException if failed. otherwise its good.
     * and make user's postCount++;
     * and false otherwise.
     * */
    public void uploadToDB() {
        String missingData = this.checkData();
        if (!missingData.isEmpty()) {
        }

        //upload to DB;
        saveInBackground();
    }

    private String checkData() {
        StringBuilder sbMissingData = new StringBuilder();
        if (getCaption() == null || getCaption().isEmpty()) {
            sbMissingData.append("Caption is missing\n");
        }

        if (getPurpose() == null || getPurpose().isEmpty()) {
            sbMissingData.append("Purpose is missing\n");
        }

        if (getPhoto() != null) {
            sbMissingData.append("No photos were added");
        }

        return sbMissingData.toString();

    }


    public String getID() {
        return getObjectId();
    }


    public static class Query extends ParseQuery<Post> {

        public Query() {
            super(Post.class);
        }

        public Query currentUserPost() {
            include("creator");
            whereEqualTo(CREATOR_KEY, ParseUser.getCurrentUser());
            return this;
        }

        public Query visibleUserPost() {
            include("creator");
            whereNotEqualTo(CREATOR_KEY, ParseUser.getCurrentUser());
            return this;
        }

        public Query areInfluencers() {
            whereEqualTo(INFLUENCER_KEY, true);
            return this;
        }
        public Query specificPost(String id) {
            whereEqualTo(UUID_KEY, id);
            return this;
        }
    }
}
