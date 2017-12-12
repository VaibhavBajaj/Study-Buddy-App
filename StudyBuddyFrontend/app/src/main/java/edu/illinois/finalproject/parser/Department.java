package edu.illinois.finalproject.parser;

import android.support.annotation.NonNull;


/**
 * An individual department's details
 */
public class Department {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
