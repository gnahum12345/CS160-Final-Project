package com.example.gabriel.sociala;

public class PostManager {
    private static final PostManager ourInstance = new PostManager();

    public static PostManager getInstance() {
        return ourInstance;
    }

    private PostManager() {
    }
}
