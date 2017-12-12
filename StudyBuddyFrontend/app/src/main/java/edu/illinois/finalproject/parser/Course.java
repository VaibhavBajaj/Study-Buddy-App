package edu.illinois.finalproject.parser;

/**
 * An individual course's details
 */
public class Course {
    String id;
    String name;

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id  + " - " + name;
    }
}
