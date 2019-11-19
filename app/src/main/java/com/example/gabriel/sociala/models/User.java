package com.example.gabriel.sociala.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.gabriel.sociala.exceptions.CreateUserException;

import java.util.ArrayList;

public class User implements Comparable, Parcelable {

    private String username;
    private String password;
    private ArrayList<Feedback> feedbackList;
    private ArrayList<Post> posts;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.feedbackList = new ArrayList<>();
        this.posts = new ArrayList<>();

    }

    protected User(Parcel in) {
        username = in.readString();
        password = in.readString();
        posts = in.createTypedArrayList(Post.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     * Authenticate a user signing in.
     * @param username: the username the user says they have
     * @param password: the password corresponding to the user.
     * @return User if they are able to retrieved from DB.
     */
    public static User authenticate(String username, String password) {
        // check db
        // if db contains username and password
        // pull info from db.
        return null;
    }


    /**
     * Create user is from signup page where each username is unique.
     * @param username: username the users wants and it has to be unique.
     * @param password: password they want to use.
     * @return User if username is unique with corresponding password.
     * @throws CreateUserException if username is not unique.
     */
    public static User createUser(String username, String password) throws CreateUserException {
        // check db if username exists.
        // if yes -> throw createUserException("username is already taken")
        // if no -> createUser
        User newUser = new User(username, password);
        // add to db newUser.
        return newUser;
    }

    public String getUserName() {
        return username;
    }

    public Integer totalPost() {
        return posts.size();
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void addPost(Post p) {
        this.posts.add(p);
    }

    public ArrayList<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public boolean canProvideFeedback(Post p) {
        return p.hasAccess(this);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeTypedList(posts);
        dest.writeTypedList(feedbackList);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof User) {
            User op = (User) o;
            return op.username.compareTo(this.username);
        } else {
            return 1;
        }
    }

    public void addFeedback(Feedback feedback) {
        feedbackList.add(feedback);
    }
}
