package edu.illinois.finalproject.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.UserSessionManager;
import edu.illinois.finalproject.helper.AddMeetingActivity;
import edu.illinois.finalproject.home.adapter.BuddyAdapter;
import edu.illinois.finalproject.home.adapter.MeetingAdapter;
import edu.illinois.finalproject.parser.Meeting;
import edu.illinois.finalproject.parser.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingTabFragment extends Fragment {

    private User mUser;
    private List<Meeting> mMeetingList;
    private MeetingAdapter mMeetingAdapter;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Context mContext;
    private Button mAddMeetingButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View returnView = inflater.inflate(R.layout.fragment_meeting_tab, container,
                false);
        mContext = returnView.getContext();


        mUser = UserSessionManager.getUser(mContext);
        mMeetingList = new ArrayList<>();
        initMeetingList();

        RecyclerView buddyRecyclerView = (RecyclerView) returnView
                .findViewById(R.id.recycler_meeting_list);
        mMeetingAdapter = new MeetingAdapter(mMeetingList);

        buddyRecyclerView.setAdapter(mMeetingAdapter);
        buddyRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        );

        mAddMeetingButton = (Button) returnView.findViewById(R.id.set_meeting_button);
        mAddMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchAddMeetingIntent = new Intent(mContext, AddMeetingActivity.class);
                startActivity(launchAddMeetingIntent);
            }
        });
        return returnView;
    }

    private void initMeetingList() {
        final DatabaseReference dbRef = db.getReference("studentMeetings").child(mAuth.getUid());

        dbRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> meetingIdList = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    meetingIdList.add(child.getValue(String.class));
                }
                initMeetingIdInfo(meetingIdList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initMeetingIdInfo(final List<String> meetingIdList) {

        for (String meetingId : meetingIdList) {
            final DatabaseReference dbRef = db.getReference("meetings").child(meetingId);
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mMeetingList.add(dataSnapshot.getValue(Meeting.class));
                    mMeetingAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
