package edu.illinois.finalproject.helper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import edu.illinois.finalproject.home.adapter.BuddyAdapter;
import edu.illinois.finalproject.parser.Meeting;
import edu.illinois.finalproject.parser.User;

public class AddMeetingActivity extends AppCompatActivity {

    private static final int MIN_BUDDIES = 2;
    private static final String TAG = AddMeetingActivity.class.getSimpleName();
    private EditText mMeetingName;
    private EditText mMeetingLocation;
    private EditText mMeetingTime;

    private User mUser;
    private BuddyAdapter mBuddyAdapter;
    private List<User> mBuddyList;
    private Button mAddMeetingButton;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        mMeetingName = (EditText) findViewById(R.id.add_meeting_name_text);
        mMeetingLocation = (EditText) findViewById(R.id.add_meeting_location_text);
        mMeetingTime = (EditText) findViewById(R.id.add_meeting_time_text);
        mAddMeetingButton = (Button) findViewById(R.id.add_meeting_button);

        mUser = UserSessionManager.getUser(this);
        mBuddyList = new ArrayList<>();
        initBuddyList();

        mBuddyAdapter = new BuddyAdapter(mBuddyList, BuddyAdapter.ALLOW_SELECTION);
        final RecyclerView buddyRecyclerView = (RecyclerView) findViewById(R.id.add_meeting_buddy_list);
        buddyRecyclerView.setAdapter(mBuddyAdapter);
        buddyRecyclerView.setLayoutManager( new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false)
        );

        mAddMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mMeetingName.getText().toString();
                String location = mMeetingLocation.getText().toString();
                String time = mMeetingTime.getText().toString();

                List<User> buddiesChosen = new ArrayList<>();
                List<String> buddiesChosenIds = new ArrayList<>();
                List<String> buddiesChosenNames = new ArrayList<>();
                buddiesChosen.add(UserSessionManager.getUser(AddMeetingActivity.this));
                buddiesChosenIds.add(mAuth.getUid());
                buddiesChosenNames.add(mUser.getName());
                for (User buddy : mBuddyList) {
                    if (buddy.isSelected()) {
                        buddiesChosen.add(buddy);
                        buddiesChosenIds.add(buddy.getId());
                        buddiesChosenNames.add(buddy.getName());
                    }
                }

                if (checkInvalidInput(name, location, time, buddiesChosenIds)) {
                    return;
                }

                pushDataToFirebase(name, location, time, buddiesChosenIds,
                        buddiesChosenNames, buddiesChosen);
                Intent launchMeetingTabIntent = new Intent(AddMeetingActivity.this,
                        HomeActivity.class);
                launchMeetingTabIntent.putExtra(HomeActivity.TAB_KEY, HomeActivity.MEETING_TAB);
                startActivity(launchMeetingTabIntent);
            }
        });
    }

    private void pushDataToFirebase(String name, String location, String time,
                                    List<String> buddiesChosenIds, List<String> buddiesChosenNames,
                                    List<User> buddiesChosen) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("meetings").push();

        Meeting meetingAdded = new Meeting(dbRef.getKey(), name, location, time, buddiesChosenIds,
                buddiesChosenNames);
        dbRef.setValue(meetingAdded);

        for (User user : buddiesChosen) {
            Map<String, Object> meetingMap = new HashMap<>();
            List<String> userMeetings = user.getMeetings();
            userMeetings.add(dbRef.getKey());

            meetingMap.put(user.getId(), userMeetings);
            db.getReference("studentMeetings").updateChildren(meetingMap);
        }
    }

    private boolean checkInvalidInput(String name, String location, String time,
                                      List<String> buddiesChosenIds) {
        if (name.length() <= 0) {
            mMeetingName.setError("Name too small");
            return true;
        } else if (location.length() <= 0) {
            mMeetingLocation.setError("Location not entered");
            return true;
        } else if (time.length() <= 0) {
            mMeetingTime.setError("Invalid time");
            return true;
        } else if (buddiesChosenIds.size() < MIN_BUDDIES) {
            Toast.makeText(
                    AddMeetingActivity.this,
                    "You must select at least one buddy",
                    Toast.LENGTH_SHORT
            ).show();
            return true;
        }
        return false;
    }

    private void initBuddyList() {
        if (mUser == null) {
            return;
        }

        List<String> buddies = mUser.getBuddies();
        final FirebaseDatabase db = FirebaseDatabase.getInstance();

        for (String buddy : buddies) {
            final DatabaseReference dbRef = db.getReference("students").child(buddy);
            dbRef.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mBuddyList.add(dataSnapshot.getValue(User.class));
                    mBuddyAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
