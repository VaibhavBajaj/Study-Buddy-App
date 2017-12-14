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

/**
 * If user chooses to add a course, this activity is launched.
 * Allows user to choose department, course and section of course for addition to user course list.
 */
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

        // Do not allow user to change course or section if department is not chosen.
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
                String sectionDept =
                        deptList.get(mDeptSpinner.getSelectedItemPosition()).getId();
                String sectionCourse =
                        courseList.get(mCourseSpinner.getSelectedItemPosition()).getId();
                String sectionCrn =
                        sectionList.get(mSectionSpinner.getSelectedItemPosition()).getCrn();
                String addedCourseApi = sectionDept + "/" + sectionCourse + "/" + sectionCrn;

                List<String> courses = mUser.getCourses();
                if (!courses.contains(addedCourseApi)) {
                    courses.add(addedCourseApi);
                }

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

    /**
     * Generic method of creating array adapter for spinner.
     * @param initList      List to be displayed in spinner
     * @return              Returns an appropriate ArrayAdapter
     */
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

    /**
     * Updates department list by listeners from database and notifies adapter.
     * Note: Do not use Generics. Slows speed drastically.
     * @param adapter       ArrayAdapter to be notified of changes
     * @param deptList      List of departments to be altered.
     */
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

    /**
     * Updates course list by listeners from database and notifies adapter.
     * Note: Do not use Generics. Slows speed drastically.
     * @param adapter       ArrayAdapter to be notified of changes
     * @param courseList    List of courses to be altered.
     * @param queryApi      Specific qeury api for retrieving course list from database.
     */
    private void updateCourseListFromDb(final ArrayAdapter<Course> adapter,
                                      final List<Course> courseList, String queryApi) {

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

    /**
     * Updates section list by listeners from database and notifies adapter.
     * Note: Do not use Generics. Slows speed drastically.
     * @param adapter       ArrayAdapter to be notified of changes
     * @param sectionList   List of sections to be altered
     * @param queryApi      Specific qeury api for retrieving section list from database.
     */
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
