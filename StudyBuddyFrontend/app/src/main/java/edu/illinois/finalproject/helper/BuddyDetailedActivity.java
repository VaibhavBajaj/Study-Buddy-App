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
import edu.illinois.finalproject.home.adapter.BuddyAdapter;
import edu.illinois.finalproject.parser.User;

public class BuddyDetailedActivity extends AppCompatActivity {

    private TextView mBuddyName;
    private TextView mBuddyCourses;
    private TextView mBuddyLocation;
    private Button mRemoveBuddyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_buddy_activity);

        Intent intent = getIntent();
        final User user = intent.getParcelableExtra(BuddyAdapter.USER_KEY);

        mBuddyName = (TextView) findViewById(R.id.buddy_name);
        mBuddyName.setText(user.getName());

        mBuddyCourses = (TextView) findViewById(R.id.buddy_courses);
        mBuddyCourses.setText(user.extractCoursesStr());

        mBuddyLocation = (TextView) findViewById(R.id.buddy_location);
        mBuddyLocation.setText(user.getLocation());

        mRemoveBuddyButton = (Button) findViewById(R.id.remove_buddy_button);
        mRemoveBuddyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> buddyList = UserSessionManager
                        .getUser(BuddyDetailedActivity.this).getBuddies();
                buddyList.remove(user.getId());
                Map<String, Object> buddyMap = new HashMap<>();
                buddyMap.put("buddies", buddyList);

                final FirebaseDatabase db = FirebaseDatabase.getInstance();
                final FirebaseAuth auth = FirebaseAuth.getInstance();
                db.getReference("students")
                        .child(auth.getUid())
                        .updateChildren(buddyMap);

                Intent launchBuddyTabIntent = new Intent(BuddyDetailedActivity.this,
                        HomeActivity.class);
                launchBuddyTabIntent.putExtra(HomeActivity.TAB_KEY, HomeActivity.BUDDY_TAB);
                startActivity(launchBuddyTabIntent);
            }
        });
    }
}
