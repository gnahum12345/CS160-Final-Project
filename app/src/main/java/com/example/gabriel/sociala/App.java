package com.example.gabriel.sociala;

import android.app.Application;

import com.example.gabriel.sociala.models.Feedback;
import com.example.gabriel.sociala.models.Photo;
import com.example.gabriel.sociala.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Feedback.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Photo.class);

        Parse.Configuration config = new Parse.Configuration.Builder(this)
                                    .applicationId("ucb-social-a")
                                    .clientKey("ucb-social-a-final-project")
                                    .server("http://ucb-social-a.herokuapp.com/parse").build();
        Parse.initialize(config);

    }
}
