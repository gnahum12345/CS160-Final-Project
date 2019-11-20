package com.example.gabriel.sociala.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.gabriel.sociala.exceptions.FeedbackException;

import java.io.File;
import java.util.ArrayList;

public class Feedback implements Comparable, Parcelable {

    private String uuid;
    private User reviewer;
    private String caption;
    private String comment;
    private Bitmap editedPhoto;
    private ArrayList<String> tags;
    private String song; // this would be a youtube link.
    private Post post;
    private String videoPath;
    private ArrayList<User> likes;

    /**
     * Feedback constructor only needs Users and Post in order to
     * get a uuid and create the object. Note that the same UUID will be created
     * for the user until the Feedback is "posted".
     * @param reviewer: user giving feedback
     * @param p: the post that the reviewer is discussing.
     * @Return Feedback object.
     */
    public Feedback(User reviewer, Post p) {
        this.reviewer = reviewer;
        this.post = p;
        this.uuid = getUUID();
        this.likes = new ArrayList<>();
    }


    /**
     * getLikes will return all the users that liked the given feedback.
     * @return the list of users to display to the user.
     */
    public ArrayList<User> getLikes() {
        return likes;
    }

    /**
     * Make a user like or dislike the feedback.
     * @param u
     */
    public void toggleLikeFeedback(User u) {
        if (!this.likes.contains(u)) {
            this.likes.add(u);
        } else {
            this.likes.remove(u);
        }
        // update db.
    }

    /**
     * @param u: the user currently viewing the feedback.
     * @return true if user likes the feedback and false otherwise.
     */
    public boolean likedFeedback(User u) {
        return this.likes.contains(u);
    }

    public String getCaption() {
        return caption;
    }

    public String getComment() {
        return comment;
    }

    public String getSong() {
        return song;
    }

    public String getID() {
        return uuid;
    }

    public void setEditedPhoto(Bitmap bitmap) {
        this.editedPhoto = bitmap;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
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
        this.post.addFeedback(this);
        this.reviewer.addFeedback(this);

        //  upload to db.
    }

    private String checkData() {
        StringBuilder sbMissingData = new StringBuilder();

        if (this.comment == null || this.comment.isEmpty()) {
            sbMissingData.append("Comments on edit  is missing\n");
        }

        if (!videoPath.isEmpty()) {
            File f = new File(videoPath);
            if (!f.exists()) {
                sbMissingData.append("Could not find video file!\tVideoPath: " + videoPath);
            }

            if (this.editedPhoto == null) {
                sbMissingData.append("Could not find edited photo!");
            }

        } else if(this.caption == null || this.caption.isEmpty()) {
            sbMissingData.append("Need to edit either photo or caption\n");
        }

        return sbMissingData.toString();

    }

    protected Feedback(Parcel in) {
        caption = in.readString();
        comment = in.readString();
        tags = in.createStringArrayList();
        song = in.readString();
        videoPath = in.readString();
    }

    public static final Creator<Feedback> CREATOR = new Creator<Feedback>() {
        @Override
        public Feedback createFromParcel(Parcel in) {
            return new Feedback(in);
        }

        @Override
        public Feedback[] newArray(int size) {
            return new Feedback[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(comment);
        dest.writeStringList(tags);
        dest.writeString(song);
        dest.writeString(videoPath);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof Feedback) {
            Feedback op = (Feedback) o;
            return op.uuid.compareTo(this.uuid);
        } else {
            return 1;
        }
    }

    private String getUUID() {
        String postID = this.post.getID();
        String username = this.reviewer.getUserName();
        return postID + "-" +  username + "-" + this.reviewer.getFeedbackList().size();
    }
}
