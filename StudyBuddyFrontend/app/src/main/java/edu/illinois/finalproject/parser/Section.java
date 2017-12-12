package edu.illinois.finalproject.parser;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * An individual section's details.
 */
public class Section implements Parcelable {

    private String course_id;
    private String course_name;
    private String crn;
    private String dept_id;
    private String dept_name;
    private String end_date;
    private String start_date;
    private String section;
    private String semester;
    private List<String> instructors;

    public Section() {}

    protected Section(Parcel in) {
        course_id = in.readString();
        course_name = in.readString();
        crn = in.readString();
        dept_id = in.readString();
        dept_name = in.readString();
        end_date = in.readString();
        start_date = in.readString();
        section = in.readString();
        semester = in.readString();
        instructors = in.createStringArrayList();
    }

    public static final Creator<Section> CREATOR = new Creator<Section>() {
        @Override
        public Section createFromParcel(Parcel in) {
            return new Section(in);
        }

        @Override
        public Section[] newArray(int size) {
            return new Section[size];
        }
    };

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return parseDate(end_date);
        }
        return end_date;
    }

    public String getStart_date() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return parseDate(start_date);
        }
        return start_date;
    }

    public String getSection() {
        return section;
    }

    public List<String> getInstructors() {
        return instructors;
    }

    public String getInstructorsString() {
        StringBuilder instructorStr = new StringBuilder();
        for (int i = 0; i < instructors.size() - 1; i++) {
            instructorStr.append(instructors.get(i));
            instructorStr.append(", ");
        }
        instructorStr.append(instructors.get(instructors.size() - 1));
        return instructorStr.toString();
    }

    public String getSemester() {
        return semester;
    }

    public String getCourseApi() {
        return dept_id + "/" + course_id + "/" + crn;
    }

    @Override
    public String toString() {
        return crn + " - " + section;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(course_id);
        parcel.writeString(course_name);
        parcel.writeString(crn);
        parcel.writeString(dept_id);
        parcel.writeString(dept_name);
        parcel.writeString(end_date);
        parcel.writeString(start_date);
        parcel.writeString(section);
        parcel.writeString(semester);
        parcel.writeStringList(instructors);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String parseDate (String date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        LocalDateTime localDateTime = LocalDateTime.parse(date, inputFormatter);

        return localDateTime.format(outputFormatter);
    }
}
