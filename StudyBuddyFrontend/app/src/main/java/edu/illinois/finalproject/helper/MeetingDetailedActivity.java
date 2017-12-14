package edu.illinois.finalproject.helper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.UserSessionManager;
import edu.illinois.finalproject.home.HomeActivity;
import edu.illinois.finalproject.parser.Meeting;
import edu.illinois.finalproject.parser.User;

public class MeetingDetailedActivity extends AppCompatActivity {

    public static final String MEETING_KEY = "Meeting";

    private User mUser;

    private TextView mMeetingName;
    private TextView mMeetingLocation;
    private TextView mMeetingTime;
    private TextView mMeetingBuddies;
    private Button mCancelMeetingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_meeting_activity);

        Intent intent = getIntent();
        final Meeting meeting = intent.getParcelableExtra(MEETING_KEY);

        mUser = UserSessionManager.getUser(this);

        mMeetingName = (TextView) findViewById(R.id.meeting_name);
        mMeetingName.setText(meeting.getName());

        mMeetingLocation = (TextView) findViewById(R.id.meeting_location);
        mMeetingLocation.setText(meeting.getLocation());

        mMeetingTime = (TextView) findViewById(R.id.meeting_time);
        mMeetingTime.setText(meeting.getTime());

        mMeetingBuddies = (TextView) findViewById(R.id.meeting_buddy_names);
        mMeetingBuddies.setText(meeting.extractBuddyNamesStr());

        mCancelMeetingButton = (Button) findViewById(R.id.cancel_meeting_button);
        mCancelMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                FirebaseAuth auth = FirebaseAuth.getInstance();

                List<String> userMeetingIds = mUser.getMeetings();
                userMeetingIds.remove(meeting.getId());
                Map<String, Object> meetingMap = new HashMap<>();
                meetingMap.put(auth.getUid(), userMeetingIds);

                db.getReference("studentMeetings").updateChildren(meetingMap);
                Intent launchMeetingTabIntent =
                        new Intent(MeetingDetailedActivity.this, HomeActivity.class);
                launchMeetingTabIntent.putExtra(HomeActivity.TAB_KEY, HomeActivity.MEETING_TAB);
                startActivity(launchMeetingTabIntent);
            }
        });
    }
}
