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

/**
 * When user chooses to search for buddies, this activity is launched.
 * This activity shows a list of optional buddies to User should he wish to add them.
 */
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

    /**
     * From all users on firebase, initializes mBuddyOptionsList to list of users having
     * common courses with current user.
     */
    private void initBuddyList() {

        final List<User> allUsers = new ArrayList<>();
        final List<String> currentBuddyIds = mUser.getBuddies();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    allUsers.add(child.getValue(User.class));
                }
                for (User user : allUsers) {
                    if (!user.getId().equals(mUser.getId())            // We don't want current user
                            && !currentBuddyIds.contains(user.getId())  // Not already a buddy
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

    /**
     * Checks if users have common courses
     * @param currentUser   Current user
     * @param buddyOption   Possible buddy
     * @return  Whether or not they have common courses
     */
    private boolean hasCommonCourses(User currentUser, User buddyOption) {

        for (String buddyCourse : buddyOption.getCourses()) {
            for (String userCourse : currentUser.getCourses()) {
                if (buddyCourse.equals(userCourse)) {
                    return true;
                }
            }
        }

        return false;
    }
}
