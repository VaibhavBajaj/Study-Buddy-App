package edu.illinois.finalproject.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.illinois.finalproject.UserSessionManager;
import edu.illinois.finalproject.helper.AddCourseActivity;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.home.adapter.CourseAdapter;
import edu.illinois.finalproject.parser.Section;
import edu.illinois.finalproject.parser.User;


public class CoursesTabFragment extends Fragment {

    private static final String TAG = CoursesTabFragment.class.getSimpleName();
    private Button mAddCourseButton;

    private CourseAdapter mCourseAdapter;
    private List<Section> mCourseList;

    private Context mContext;
    private User mUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View returnView =  inflater.inflate(R.layout.fragment_courses_tab, container,
                false);
        mContext = returnView.getContext();
        mUser = UserSessionManager.getUser(mContext);
        mCourseList = new ArrayList<>();

        RecyclerView courseRecyclerView = (RecyclerView) returnView
                .findViewById(R.id.recycler_course_list);
        mCourseAdapter = new CourseAdapter(mCourseList);
        initCourseList();

        courseRecyclerView.setAdapter(mCourseAdapter);
        courseRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        );


        // Once UserSessionManager says User's data from Firebase is retrieved, we call initCourses
        // This updates the recycler view

        mAddCourseButton = (Button) returnView.findViewById(R.id.find_course_button);
        mAddCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchAddCourseIntent = new Intent(mContext, AddCourseActivity.class);
                startActivity(launchAddCourseIntent);
            }
        });

        return returnView;
    }

    /**
     * This function gets data about each section user is registered in from the
     * lists of course apis by querying Firebase with each course api.
     *
     * Once completed, it notifies the Course Recycler View
     */
    private void initCourseList() {

        if (mUser == null) {
            return;
        }

        List<String> courseApis = mUser.getCourses();
        final FirebaseDatabase db = FirebaseDatabase.getInstance();

        for (String courseApi : courseApis) {
            final DatabaseReference dbRef = db.getReference("sections").child(courseApi);
            dbRef.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mCourseList.add(dataSnapshot.getValue(Section.class));
                    mCourseAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
