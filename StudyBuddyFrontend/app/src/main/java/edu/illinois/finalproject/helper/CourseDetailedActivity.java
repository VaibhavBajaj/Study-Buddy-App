package edu.illinois.finalproject.helper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.UserSessionManager;
import edu.illinois.finalproject.home.HomeActivity;
import edu.illinois.finalproject.parser.Section;

/**
 * Activity launched when user clicks on a course in Course Tab.
 * Gives details of course and the option to remove the course
 */
public class CourseDetailedActivity extends AppCompatActivity {

    public static final String COURSE_KEY = "Course";

    private TextView mCourseIdText;
    private TextView mCourseNameText;
    private TextView mCourseCrnText;
    private TextView mCourseSectionText;
    private TextView mCourseDatesText;
    private TextView mCourseInstructorsText;
    private Button mRemoveCourseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_course_activity);

        Intent intent = getIntent();
        final Section section = intent.getParcelableExtra(COURSE_KEY);

        String courseDisplayName = section.getDept_id() + " " + section.getCourse_id();
        mCourseIdText = (TextView) findViewById(R.id.course_id_text);
        mCourseIdText.setText(courseDisplayName);

        mCourseNameText = (TextView) findViewById(R.id.course_name_text);
        mCourseNameText.setText(section.getCourse_name());

        mCourseCrnText = (TextView) findViewById(R.id.course_crn_text);
        mCourseCrnText.setText(section.getCrn());

        mCourseSectionText = (TextView) findViewById(R.id.course_section_text);
        mCourseSectionText.setText(section.getSection());

        String courseDates = section.getStart_date() + " - " + section.getEnd_date();
        mCourseDatesText = (TextView) findViewById(R.id.course_dates_text);
        mCourseDatesText.setText(courseDates);

        mCourseInstructorsText = (TextView) findViewById(R.id.course_instructors_text);
        mCourseInstructorsText.setText(section.getInstructorsString());

        mRemoveCourseButton = (Button) findViewById(R.id.remove_course_button);
        mRemoveCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> courseList = UserSessionManager
                        .getUser(CourseDetailedActivity.this).getCourses();
                courseList.remove(section.getCourseApi());
                Map<String, Object> courseMap = new HashMap<>();
                courseMap.put("courses", courseList);

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                db.getReference("students")
                        .child(auth.getUid())
                        .updateChildren(courseMap);

                Intent launchCoursesTabIntent = new Intent(CourseDetailedActivity.this,
                        HomeActivity.class);
                launchCoursesTabIntent.putExtra(HomeActivity.TAB_KEY, HomeActivity.COURSES_TAB);
                startActivity(launchCoursesTabIntent);
            }
        });
    }
}
