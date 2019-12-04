package com.example.gabriel.sociala.models;

import com.example.gabriel.sociala.exceptions.FeedbackException;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Feedback")
public class Feedback extends ParseObject {

    private final static String UUID_KEY = "objectId";
    private final static String DESCRIPTION_KEY  = "description";
    private final static String CAPTION_KEY = "caption";
    private final static String POST_KEY = "post";
    private final static String REVIEWER_KEY = "reviewer";
    private final static String EDITED_PHOTO_KEY = "editedPhoto";
    private final static String LIKES_KEY = "likes";
    private static final String CREATED_DATE_KEY = "createdAt";
    private static final String VIDEO_PATH_KEY = "video";


    public int getLikes() {
        return getList(LIKES_KEY).size();
    }

    /**
     * Make a user like or dislike the feedback.
     * @param u
     */
    public int toggleLikeFeedback(ParseUser u) {
        List<ParseUser>  users = getList(LIKES_KEY);
        if (users.contains(u)) {
            users.remove(u);
        } else {
            users.add(u);
        }
        // update db.
        put(LIKES_KEY, users);
        return users.size();
    }

    /**
     * @param u: the user currently viewing the feedback.
     * @return true if user likes the feedback and false otherwise.
     */
    public boolean likedFeedback(ParseUser u) {
        return getList(LIKES_KEY).contains(u);
    }

    public String getCaption() {
        return getString(CAPTION_KEY);
    }

    public String getComment() {
        return getString(DESCRIPTION_KEY);
    }


    public String getID() {
        return getString(UUID_KEY);
    }

    public void setEditedPhoto(ParseFile file) {
        put(EDITED_PHOTO_KEY, file);
    }

    public void setCaption(String caption) {
        put(CAPTION_KEY, caption);
    }

    public void setComment(String comment) {
        put(DESCRIPTION_KEY, comment);
    }

    public void setPost(Post post) {
        put(POST_KEY, post);
    }

    public void setVideoPath(ParseFile videoPath) {
        put(VIDEO_PATH_KEY, videoPath);
    }

    public ParseFile getEditedPhoto() {
        return getParseFile(EDITED_PHOTO_KEY);
    }

    public ParseFile getVideo() {
        return getParseFile(VIDEO_PATH_KEY);
    }

    public ParseUser getReviewer() {
        return getParseUser(REVIEWER_KEY);
    }

    public void setReviewer(ParseUser u) {
        put(REVIEWER_KEY, u);
    }

    /**
     * When editing a post. This should be called once all the user reviewed the post.
     * @throws FeedbackException when missing piece of information.
     */
    public void postFeedback() throws FeedbackException {
        String missingData = this.checkData();
        if (!missingData.isEmpty()) {
            throw new FeedbackException(missingData);
        }
        //  upload to db.
        saveInBackground();
    }

    private String checkData() {
        StringBuilder sbMissingData = new StringBuilder();

        if (getComment() == null || getComment().isEmpty()) {
            sbMissingData.append("Comments on edit  is missing\n");
        }

        if (getVideo() == null) {
            sbMissingData.append("Could not find video file!\tVideoPath: ");

        } else if(getCaption() == null) {
            sbMissingData.append("Need to edit either photo or caption\n");
        }

        return sbMissingData.toString();
    }


    public static class Query extends ParseQuery<Feedback> {

        public Query() {
            super(Feedback.class);
        }

        public Query currentUserFeedback() {
            include("creator");
            whereEqualTo(REVIEWER_KEY, ParseUser.getCurrentUser());
            return this;
        }

        public Query withPost(Post p) {
            include("post");
            whereEqualTo(POST_KEY, p);
            return this;
        }
    }



}
