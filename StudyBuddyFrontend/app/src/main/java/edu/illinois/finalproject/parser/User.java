package edu.illinois.finalproject.parser;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private List<String> courses;
    private List<String> buddies;
    private String location;

    public User() {}

    public User(String id, List<String> courses, List<String> buddies, String location) {
        this.id = id;
        this.courses = courses;
        this.buddies = buddies;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public List<String> getCourses() {
        if (courses == null) {
            return new ArrayList<>();
        }

        return courses;
    }

    public List<String> getBuddies() {
        if (buddies == null) {
            return new ArrayList<>();
        }

        return buddies;
    }

    public String getLocation() {
        return location;
    }
}
