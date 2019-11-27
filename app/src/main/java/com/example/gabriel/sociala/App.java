package com.example.gabriel.sociala;

import android.app.Application;

import com.example.gabriel.sociala.models.Feedback;
import com.example.gabriel.sociala.models.Photo;
import com.example.gabriel.sociala.models.Post;
import com.facebook.stetho.Stetho;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();



        ParseObject.registerSubclass(Feedback.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Photo.class);

        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        Stetho.initializeWithDefaults(this);

        // set applicationId and server based on the values in the Heroku settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("ucb-social-a") // should correspond to APP_ID env variable
                .clientKey("ucb-social-a-final-project")
                .server("https://ucb-social-a.herokuapp.com/parse/").build());

//        ParseUser.logOut();
    }
}
