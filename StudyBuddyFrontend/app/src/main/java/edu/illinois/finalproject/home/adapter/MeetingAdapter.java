package edu.illinois.finalproject.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.helper.MeetingDetailedActivity;
import edu.illinois.finalproject.parser.Meeting;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    private static final String TAG = MeetingAdapter.class.getSimpleName();
    private List<Meeting> meetingList;

    public MeetingAdapter (List<Meeting> meetingList) {
        this.meetingList = meetingList;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.recycler_meeting_item;
    }

    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View courseItem = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new MeetingViewHolder(courseItem);
    }

    @Override
    public void onBindViewHolder(MeetingViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        final Meeting meeting = meetingList.get(position);

        holder.meetingName.setText(meeting.getName());
        holder.meetingLocation.setText(meeting.getLocation());
        holder.meetingTime.setText(meeting.getTime());
        holder.meetingBuddies.setText(meeting.extractBuddyNamesStr());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchDetailedMeetingIntent = new Intent(context,
                        MeetingDetailedActivity.class);
                launchDetailedMeetingIntent.putExtra(MeetingDetailedActivity.MEETING_KEY, meeting);
                context.startActivity(launchDetailedMeetingIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meetingList.size();
    }

    class MeetingViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView meetingName;
        TextView meetingLocation;
        TextView meetingTime;
        TextView meetingBuddies;

        MeetingViewHolder(View view) {
            super(view);

            itemView = view;
            meetingName = (TextView) itemView.findViewById(R.id.recycler_meeting_name);
            meetingLocation = (TextView) itemView.findViewById(R.id.recycler_meeting_location);
            meetingTime = (TextView) itemView.findViewById(R.id.recycler_meeting_time);
            meetingBuddies = (TextView) itemView.findViewById(R.id.recycler_meeting_buddy_names);
        }
    }
}
