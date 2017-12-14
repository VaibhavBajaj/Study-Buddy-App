package edu.illinois.finalproject.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.helper.CourseDetailedActivity;
import edu.illinois.finalproject.parser.Section;

/**
 * Adapter used to parse course list to form recycler view
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private static final String TAG = CourseAdapter.class.getSimpleName();
    private List<Section> courseList;

    public CourseAdapter (List<Section> courseList) {
        this.courseList = courseList;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.recycler_course_item;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View courseItem = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new CourseViewHolder(courseItem);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        final Section section = courseList.get(position);

        String courseDisplayName = section.getDept_id() + " " + section.getCourse_id();
        holder.courseId.setText(courseDisplayName);
        holder.courseName.setText(section.getCourse_name());
        holder.courseSection.setText(section.getSection());
        String courseDates = section.getStart_date() + " - " + section.getEnd_date();
        holder.courseDates.setText(courseDates);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchDetailedCourseIntent = new Intent(context,
                        CourseDetailedActivity.class);
                launchDetailedCourseIntent.putExtra(CourseDetailedActivity.COURSE_KEY, section);
                context.startActivity(launchDetailedCourseIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {

        View itemView;

        TextView courseId;
        TextView courseName;
        TextView courseDates;
        TextView courseSection;

        CourseViewHolder(View view) {
            super(view);
            itemView = view;
            courseId = itemView.findViewById(R.id.recycler_course_id);
            courseName = itemView.findViewById(R.id.recycler_course_name);
            courseDates = itemView.findViewById(R.id.recycler_course_dates);
            courseSection = itemView.findViewById(R.id.recycler_course_section);
        }
    }
}
