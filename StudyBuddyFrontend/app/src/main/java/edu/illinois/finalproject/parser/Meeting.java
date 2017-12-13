package edu.illinois.finalproject.parser;

import java.util.List;

public class Meeting {
    private String name;
    private String location;
    private String time;
    private List<String> buddies;

    public Meeting() {}

    public Meeting(String name, String location, String time, List<String> buddies) {
        this.name = name;
        this.location = location;
        this.time = time;
        this.buddies = buddies;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public List<String> getBuddies() {
        return buddies;
    }
}
