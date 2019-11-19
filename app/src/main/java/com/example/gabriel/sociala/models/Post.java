package com.example.gabriel.sociala.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.gabriel.sociala.exceptions.PostException;

import java.util.ArrayList;
import java.util.Date;

public class Post implements Comparable, Parcelable {

    private String uuid;
    private User creator;
    private Date date;
    private Date schedulePost;
    private ArrayList<Feedback> feedbackList;
    private ArrayList<Bitmap> photos;
    private String caption;
    private String purpose;
    private ArrayList<User> grantedUsers;

    public Post(User creator) {
        this.creator = creator;
        this.grantedUsers = new ArrayList<>();
        feedbackList = new ArrayList<>();
        this.date = new Date();
        this.uuid = getUUID();
    }

    protected Post(Parcel in) {
        uuid = in.readString();
        photos = in.createTypedArrayList(Bitmap.CREATOR);
        caption = in.readString();
        purpose = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public void setSchedulePost(Date d) {
        this.schedulePost = d;
    }

    public User getCreator() {
        return this.creator;
    }


    public String getPurpose() {
        return purpose;
    }

    public String getCaption() {
        return caption;
    }

    public ArrayList<Bitmap> getPhotos() {
        return photos;
    }

    public ArrayList<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public void addFeedback(Feedback f) {
        if (!feedbackList.contains(f)) {
            feedbackList.add(f);
        }
    }
    public Date getSchedulePost() {
        return schedulePost;
    }

    public Date getDate() {
        return date;
    }

    public void setPhotos(ArrayList<Bitmap> photos) {
        this.photos = photos;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     * If the creator wants to add a user as a "can see" option.
     */
    public void grantAccess(User u) {
        if (!this.grantedUsers.contains(u)) {
            this.grantedUsers.add(u);
        }
    }

    /**
     * If the creator wants to remove a user as a "can see" option.
     * (i.e. User u will not be able to see this post).
     */
    public boolean removeAccess(User u) {
        return this.grantedUsers.remove(u);
    }

    /**
     * Returns true if the user has access to see the post.
     */
    public boolean hasAccess(User u) {
        return this.grantedUsers.contains(u);
    }

    public int getFeedbackSize() {
        return feedbackList.size();
    }

    /**
     * Throws PostException if failed. otherwise its good.
     * and make user's postCount++;
     * and false otherwise.
     * */
    public void uploadToDB() throws PostException {
        String missingData = this.checkData();
        if (!missingData.isEmpty()) {
            throw new PostException(missingData);
        }

        //upload to DB;
        this.creator.addPost(this);
    }

    private String checkData() {
        StringBuilder sbMissingData = new StringBuilder();
        if (this.caption == null || this.caption.isEmpty()) {
            sbMissingData.append("Caption is missing\n");
        }

        if (this.purpose == null || this.purpose.isEmpty()) {
            sbMissingData.append("Purpose is missing\n");
        }

        if (this.grantedUsers.isEmpty()) {
            sbMissingData.append("No users were granted access\n");
        }

        if (photos.isEmpty()) {
            sbMissingData.append("No photos were added");
        }

        return sbMissingData.toString();

    }

    /**
     * Gets the user uuid and total posts of user and adds 1 to that.
     * i.e. username = "GeniusBob", postCount = 4,
     * this function will return "GeniusBob-4"
     */
    private String getUUID() {
        String userName = this.creator.getUserName();
        Integer totalPosts = this.creator.totalPost();
        return userName + "-" + totalPosts;
    }

    public String getID() {
        return uuid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(this.creator.getUserName());
        dest.writeTypedList(photos);
        dest.writeString(caption);
        dest.writeString(purpose);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof Post) {
            Post op = (Post) o;
            return op.uuid.compareTo(this.uuid);
        } else {
            return 1;
        }
    }
}
