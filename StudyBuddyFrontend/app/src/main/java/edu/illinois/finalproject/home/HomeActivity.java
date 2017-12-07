package edu.illinois.finalproject.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.auth.SignInActivity;

public class HomeActivity extends AppCompatActivity {

    public static final String TAB_KEY = "CurrentTab";

    public static final int HOME_TAB = 0;
    public static final int COURSES_TAB = 1;
    public static final int BUDDY_TAB = 2;
    public static final int MEETING_TAB = 3;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            Intent authIntent = new Intent(this, SignInActivity.class);
            startActivity(authIntent);
            return;
        }

        mTabLayout = (TabLayout) findViewById(R.id.home_tabs);

        mTabLayout.addTab(mTabLayout.newTab().setText("Home"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Courses"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Buddies"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Meetings"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.home_tab_pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                mTabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        Intent intent = getIntent();
        viewPager.setCurrentItem(intent.getIntExtra(TAB_KEY, HOME_TAB));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
