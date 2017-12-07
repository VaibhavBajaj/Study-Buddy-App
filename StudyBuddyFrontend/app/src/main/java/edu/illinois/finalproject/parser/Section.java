package edu.illinois.finalproject.parser;

import java.util.List;


/**
 * An individual section's details.
 */
public class Section {

    private String course_id;
    private String course_name;
    private String crn;
    private String dept_id;
    private String dept_name;
    private String end_date;
    private String start_date;
    private String section;
    private List<String> instructors;

    public String getCourse_id() {
        return course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getCrn() {
        return crn;
    }

    public String getDept_id() {
        return dept_id;
    }

    public String getDept_name() {
        return dept_name;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getSection() {
        return section;
    }

    public List<String> getInstructors() {
        return instructors;
    }
}
