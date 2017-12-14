package edu.illinois.finalproject.parser;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * An individual user's details.
 */
public class User implements Parcelable {

    private String id;
    private String name;
    private List<String> courses;
    private List<String> buddies;
    private List<String> meetings;
    private String location;
    private boolean isSelected;

    public User() {}

    public User(String id, String name, List<String> courses, List<String> buddies,
                List<String> meetings, String location) {
        this.id = id;
        this.name = name;
        this.courses = courses;
        this.buddies = buddies;
        this.meetings = meetings;
        this.location = location;
        isSelected = false;
    }

    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        courses = in.createStringArrayList();
        buddies = in.createStringArrayList();
        meetings = in.createStringArrayList();
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
        if (courses == null) {
            return new ArrayList<>();
        }

        return courses;
    }

    /**
     * Extract course dept id and course id from courseApi
     * @param courseApi    courseApi passed in
     * @return             Course display name. Eg - "CS 196", "MATH 241" etc.
     */
    private String extractCourseStr(String courseApi) {
        String[] courseParts = courseApi.split("/");
        return courseParts[0] + " " + courseParts[1];
    }

    /**
     * Method used to return courses as a comma-separated list
     * @return courses as a comma-separated list
     */
    public String extractCoursesStr() {
        if (courses == null || courses.size() == 0) {
            return "None registered";
        }

        StringBuilder coursesStr = new StringBuilder();
        for (int i = 0; i < courses.size() - 1; i++) {
            coursesStr.append(extractCourseStr(courses.get(i)));
            coursesStr.append(", ");
        }
        coursesStr.append(extractCourseStr(courses.get(courses.size() - 1)));
        return coursesStr.toString();
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

    public List<String> getMeetings() {
        if (meetings == null) {
            return new ArrayList<>();
        }

        return meetings;
    }

    public String getName() {
        return name;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeStringList(courses);
        parcel.writeStringList(buddies);
        parcel.writeStringList(meetings);
        parcel.writeString(location);
    }
}
