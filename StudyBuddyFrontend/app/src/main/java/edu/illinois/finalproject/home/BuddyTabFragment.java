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

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.UserSessionManager;
import edu.illinois.finalproject.helper.AddBuddyActivity;
import edu.illinois.finalproject.home.adapter.BuddyAdapter;
import edu.illinois.finalproject.listener.Observer;
import edu.illinois.finalproject.listener.Subject;
import edu.illinois.finalproject.parser.User;

/**
 * Tab fragment displaying all data pertaining to user buddies.
 */
public class BuddyTabFragment extends Fragment implements Observer {

    private static final String TAG = BuddyTabFragment.class.getSimpleName();

    private BuddyAdapter mBuddyAdapter;
    private List<User> mBuddyList;

    private Button mAddBuddyButton;
    private Context mContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View returnView = inflater.inflate(R.layout.fragment_buddy_tab, container,
                false);

        mContext = returnView.getContext();
        mBuddyList = new ArrayList<>();
        update();

        RecyclerView buddyRecyclerView = (RecyclerView) returnView
                .findViewById(R.id.recycler_buddy_list);
        mBuddyAdapter = new BuddyAdapter(mBuddyList, BuddyAdapter.LAUNCH_REMOVE_BUDDY_PAGE);

        buddyRecyclerView.setAdapter(mBuddyAdapter);
        buddyRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        );

        mAddBuddyButton = (Button) returnView.findViewById(R.id.find_buddy_button);
        mAddBuddyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchAddBuddyIntent = new Intent(mContext, AddBuddyActivity.class);
                startActivity(launchAddBuddyIntent);
            }
        });

        Subject.addObserver(this);

        return returnView;
    }

    /**
     * Updates list of buddies to buddies stored on user's firebase and notifies adapter of changes.
     */
    @Override
    public void update() {
        User user = UserSessionManager.getUser(mContext);
        if (user == null) {
            return;
        }

        List<String> buddies = user.getBuddies();
        final FirebaseDatabase db = FirebaseDatabase.getInstance();

        for (String buddy : buddies) {
            final DatabaseReference dbRef = db.getReference("students").child(buddy);
            dbRef.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mBuddyList.add(dataSnapshot.getValue(User.class));
                    Log.d(TAG, "Buddy list item count: " + mBuddyList.size());
                    Log.d(TAG, "Buddy adapter item count: " + mBuddyAdapter.getItemCount());
                    mBuddyAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
