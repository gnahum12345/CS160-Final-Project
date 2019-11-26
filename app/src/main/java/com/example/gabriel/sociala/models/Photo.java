package com.example.gabriel.sociala.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


@ParseClassName("Photo")
public class Photo extends ParseObject {

    private static final String IMG_KEY = "image";
    private static final String NAME_KEY = "name";
    private static final String UUID_KEY = "objectId";

    public ParseFile getPhoto() {
        return getParseFile(IMG_KEY);
    }

    public void setPhoto(ParseFile f) {
        put(IMG_KEY, f);
    }

    public void setName(String name) {
        put(NAME_KEY, name);
    }

    public String getName() {
        return getString(NAME_KEY);
    }

    public String getID() {
        return getString(UUID_KEY);
    }

    public static class Query extends ParseQuery<Photo> {
        public Query() {
            super(Photo.class);
        }

        public Query getTop() {
            orderByDescending("createdAt");
            setLimit(20);
            return this;
        }

        public Query withID(String objID) {
            whereEqualTo(UUID_KEY, objID);
            return this;
        }
        public Query withAllIds(List<String> ids) {
            whereContainedIn(UUID_KEY, ids);
            return this;
        }
    }


}
