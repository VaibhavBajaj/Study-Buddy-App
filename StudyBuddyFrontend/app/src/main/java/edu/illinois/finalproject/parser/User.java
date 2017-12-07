package edu.illinois.finalproject.parser;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class User implements Parcelable {

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

    protected User(Parcel in) {
        id = in.readString();
        courses = in.createStringArrayList();
        buddies = in.createStringArrayList();
        location = in.readString();
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

    public String getId() {
        return id;
    }

    public List<String> getCourses() {
        return courses;
    }

    public List<String> getBuddies() {
        return buddies;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeStringList(courses);
        parcel.writeStringList(buddies);
        parcel.writeString(location);
    }
}
