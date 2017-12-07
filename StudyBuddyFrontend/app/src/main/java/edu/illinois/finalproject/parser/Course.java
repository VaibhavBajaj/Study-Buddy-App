package edu.illinois.finalproject.parser;

/**
 * An individual course's details
 */
public class Course {
    String id;
    String name;
    String deptName;

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return deptName + " " + id  + " - " + name;
    }
}
