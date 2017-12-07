package edu.illinois.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.List;

import edu.illinois.finalproject.parser.Course;
import edu.illinois.finalproject.parser.Department;
import edu.illinois.finalproject.parser.Section;

public class AddCourseActivity extends AppCompatActivity {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Spinner mDeptSpinner;
    private Spinner mCourseSpinner;
    private Spinner mSectionSpinner;

    private List<Department> mDeptList = new ArrayList<>();
    private List<Course> mCourseList = new ArrayList<>();
    private List<Section> mSectionList = new ArrayList<>();

    private Button mAddCourseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        final Department selectedDept = null;
        final Course selectedCourse = null;
        final Section selectedSection = null;

        mDeptSpinner = (Spinner) findViewById(R.id.dept_spinner);
        final ArrayAdapter<Department> deptAdapter = new ArrayAdapter<>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                mDeptList
        );
        updateDepartmentList(deptAdapter);

        deptAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mDeptSpinner.setAdapter(deptAdapter);
    }

    private void updateDepartmentList(final ArrayAdapter<Department> deptAdapter) {
        final DatabaseReference deptRef = db.getReference("departments");
        deptRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    mDeptList.add(child.getValue(Department.class));
                }
                deptAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
