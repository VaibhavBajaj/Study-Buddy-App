package edu.illinois.finalproject.helper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.UserSessionManager;
import edu.illinois.finalproject.home.HomeActivity;
import edu.illinois.finalproject.parser.Course;
import edu.illinois.finalproject.parser.Department;
import edu.illinois.finalproject.parser.Section;
import edu.illinois.finalproject.parser.User;

public class AddCourseActivity extends AppCompatActivity {

    private static final String TAG = AddCourseActivity.class.getSimpleName();
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private User mUser;

    private Spinner mDeptSpinner;
    private Spinner mCourseSpinner;
    private Spinner mSectionSpinner;

    private Button mAddCourseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        mUser = UserSessionManager.getUser(this);
        final List<Department> deptList = new ArrayList<>();
        final List<Course> courseList = new ArrayList<>();
        final List<Section> sectionList = new ArrayList<>();

        mDeptSpinner = (Spinner) findViewById(R.id.dept_spinner);
        final ArrayAdapter<Department> deptAdapter = getAdapter(deptList);
        mDeptSpinner.setAdapter(deptAdapter);

        mCourseSpinner = (Spinner) findViewById(R.id.course_spinner);
        final ArrayAdapter<Course> courseAdapter = getAdapter(courseList);
        mCourseSpinner.setAdapter(courseAdapter);

        mSectionSpinner = (Spinner) findViewById(R.id.section_spinner);
        final ArrayAdapter<Section> sectionAdapter = getAdapter(sectionList);
        mSectionSpinner.setAdapter(sectionAdapter);

        mCourseSpinner.setEnabled(false);
        mSectionSpinner.setEnabled(false);

        updateDeptListFromDb(deptAdapter, deptList);
        mDeptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateCourseListFromDb(courseAdapter, courseList, deptList.get(i).getId());
                Log.d(TAG, "It was hit.");
                mCourseSpinner.setEnabled(true);
                mSectionSpinner.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mCourseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedDeptId = deptList.get(mDeptSpinner.getSelectedItemPosition())
                        .getId();
                String queryApi = selectedDeptId + "/" + courseList.get(i).getId();
                updateSectionListFromDb(sectionAdapter, sectionList, queryApi);
                mSectionSpinner.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mAddCourseButton = (Button) findViewById(R.id.add_course_button);
        mAddCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseSectionAdded =
                        sectionList.get(mSectionSpinner.getSelectedItemPosition()).getCrn();
                List<String> courses = mUser.getCourses();
                courses.add(courseSectionAdded);
                Map<String, Object> courseUpdateMap = new HashMap<>();
                courseUpdateMap.put("courses", courses);
                db.getReference("students").child(mAuth.getUid()).updateChildren(courseUpdateMap);

                Intent launchCoursesIntent = new Intent(AddCourseActivity.this,
                        HomeActivity.class);
                launchCoursesIntent.putExtra(HomeActivity.TAB_KEY, HomeActivity.COURSES_TAB);
                startActivity(launchCoursesIntent);
            }
        });
    }

    @NonNull
    private <T> ArrayAdapter<T> getAdapter(List<T> initList) {
        final ArrayAdapter<T> adapter = new ArrayAdapter<>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                initList
        );
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        return adapter;
    }

    private void updateDeptListFromDb(final ArrayAdapter<Department> adapter,
                                      final List<Department> deptList) {
        final DatabaseReference deptRef = db.getReference("departments");
        deptRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    deptList.add(child.getValue(Department.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateCourseListFromDb(final ArrayAdapter<Course> adapter,
                                      final List<Course> courseList, String queryApi) {
        Log.d(TAG, "Query launched with queryApi: " + queryApi);
        final DatabaseReference courseRef = db.getReference("courses").child(queryApi);
        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                courseList.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    courseList.add(child.getValue(Course.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateSectionListFromDb(final ArrayAdapter<Section> adapter,
                                      final List<Section> sectionList, String queryApi) {
        final DatabaseReference sectionRef = db.getReference("sections").child(queryApi);
        sectionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sectionList.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    sectionList.add(child.getValue(Section.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
