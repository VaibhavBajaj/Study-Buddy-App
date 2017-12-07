package edu.illinois.finalproject.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.illinois.finalproject.helper.AddCourseActivity;
import edu.illinois.finalproject.R;


public class CoursesTabFragment extends Fragment {

    private Button mAddCourseButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View returnView =  inflater.inflate(R.layout.fragment_courses_tab, container,
                false);
        final Context context = returnView.getContext();

        mAddCourseButton = (Button) returnView.findViewById(R.id.find_course_button);
        mAddCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchAddCourseIntent = new Intent(context, AddCourseActivity.class);
                startActivity(launchAddCourseIntent);
            }
        });

        return returnView;

    }
}
