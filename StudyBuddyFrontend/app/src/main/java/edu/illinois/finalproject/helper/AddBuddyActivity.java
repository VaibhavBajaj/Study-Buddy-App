package edu.illinois.finalproject.helper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.UserSessionManager;
import edu.illinois.finalproject.home.adapter.BuddyAdapter;
import edu.illinois.finalproject.parser.User;

public class AddBuddyActivity extends AppCompatActivity {

    private static final String TAG = AddBuddyActivity.class.getSimpleName();
    private User mUser;
    private List<User> mBuddyOptionsList;
    private BuddyAdapter mBuddyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_buddy);
        mBuddyOptionsList = new ArrayList<>();
        mUser = UserSessionManager.getUser(this);
        initBuddyList();

        mBuddyOptionsList = new ArrayList<>();
        mBuddyAdapter = new BuddyAdapter(
                mBuddyOptionsList,
                BuddyAdapter.LAUNCH_ADD_BUDDY_PAGE
        );

        RecyclerView buddyOptionsRecyclerView =
                (RecyclerView) findViewById(R.id.recycler_add_buddy_list);
        buddyOptionsRecyclerView.setAdapter(mBuddyAdapter);
        buddyOptionsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                        false)
        );
    }

    private void initBuddyList() {

        final List<User> allUsers = new ArrayList<>();
        final List<String> currentUserIds = mUser.getBuddies();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    allUsers.add(child.getValue(User.class));
                }
                for (User user : allUsers) {
                    if (!user.getId().equals(mUser.getId())
                            && !currentUserIds.contains(user.getId())
                            && hasCommonCourses(user, mUser)) {
                        mBuddyOptionsList.add(user);
                    }
                }
                mBuddyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean hasCommonCourses(User user, User mUser) {

        for (String otherCourse : user.getCourses()) {
            for (String userCourse : mUser.getCourses()) {
                if (otherCourse.equals(userCourse)) {
                    return true;
                }
            }
        }

        return false;
    }
}
