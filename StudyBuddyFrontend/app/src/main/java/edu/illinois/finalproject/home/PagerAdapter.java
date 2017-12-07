package edu.illinois.finalproject.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNubOfTabs;

    public PagerAdapter(FragmentManager fragmentManager, int numOfTabs) {
        super(fragmentManager);
        mNubOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case HomeActivity.HOME_TAB:
                HomeTabFragment homeTabFragment = new HomeTabFragment();
                return homeTabFragment;
            case HomeActivity.COURSES_TAB:
                CoursesTabFragment coursesTabFragment = new CoursesTabFragment();
                return coursesTabFragment;
            case HomeActivity.BUDDY_TAB:
                BuddyTabFragment buddyTabFragment = new BuddyTabFragment();
                return buddyTabFragment;
            case HomeActivity.MEETING_TAB:
                MeetingTabFragment meetingTabFragment = new MeetingTabFragment();
                return meetingTabFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNubOfTabs;
    }
}
