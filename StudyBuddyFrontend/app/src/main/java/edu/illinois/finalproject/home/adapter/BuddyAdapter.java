package edu.illinois.finalproject.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.helper.AddBuddyDetailedActivity;
import edu.illinois.finalproject.helper.BuddyDetailedActivity;
import edu.illinois.finalproject.parser.User;

public class BuddyAdapter extends RecyclerView.Adapter<BuddyAdapter.BuddyViewHolder> {

    private static final String TAG = BuddyAdapter.class.getSimpleName();

    public static final String USER_KEY = "User";
    private List<User> buddyList;
    private boolean addBuddyOnClick;

    public BuddyAdapter(List<User> buddyList, boolean addBuddyOnClick) {
        this.buddyList = buddyList;
        this.addBuddyOnClick = addBuddyOnClick;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.recycler_buddy_item;
    }

    @Override
    public BuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View buddyItem = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new BuddyViewHolder(buddyItem);
    }

    @Override
    public void onBindViewHolder(BuddyViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        final User user = buddyList.get(position);

        holder.buddyName.setText(user.getName());
        holder.buddyCourses.setText(user.getCoursesStr());
        holder.buddyLocation.setText(user.getLocation());

        Log.d(TAG, String.valueOf(addBuddyOnClick));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addBuddyOnClick) {
                    Intent launchDetailedAddBuddyIntent = new Intent(context,
                            AddBuddyDetailedActivity.class);
                    launchDetailedAddBuddyIntent.putExtra(USER_KEY, user);
                    context.startActivity(launchDetailedAddBuddyIntent);
                } else {
                    Intent launchDetailedBuddyIntent = new Intent(context,
                            BuddyDetailedActivity.class);
                    launchDetailedBuddyIntent.putExtra(USER_KEY, user);
                    context.startActivity(launchDetailedBuddyIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return buddyList.size();
    }

    class BuddyViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView buddyName;
        TextView buddyCourses;
        TextView buddyLocation;

        BuddyViewHolder(View view) {
            super(view);
            itemView = view;
            buddyName = itemView.findViewById(R.id.recycler_buddy_name);
            buddyCourses = itemView.findViewById(R.id.recycler_buddy_courses);
            buddyLocation = itemView.findViewById(R.id.recycler_buddy_location);
        }
    }
}