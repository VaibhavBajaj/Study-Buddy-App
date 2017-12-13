package edu.illinois.finalproject.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import edu.illinois.finalproject.parser.Section;
import edu.illinois.finalproject.parser.User;

public class BuddyTabFragment extends Fragment {

    private static final String TAG = BuddyTabFragment.class.getSimpleName();

    private BuddyAdapter mBuddyAdapter;
    private List<User> mBuddyList;
    private Button mAddBuddyButton;

    private Context mContext;
    private User mUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_buddy_tab, container,
                false);
        mContext = returnView.getContext();

        mUser = UserSessionManager.getUser(mContext);
        mBuddyList = new ArrayList<>();
        initBuddyList();

        RecyclerView buddyRecyclerView = (RecyclerView) returnView
                .findViewById(R.id.recycler_buddy_list);
        mBuddyAdapter = new BuddyAdapter(mBuddyList, false);

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
        return returnView;
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
