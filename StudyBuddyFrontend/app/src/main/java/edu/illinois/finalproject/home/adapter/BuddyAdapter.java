package edu.illinois.finalproject.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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

    public static final int ALLOW_SELECTION = 1;
    public static final int LAUNCH_ADD_BUDDY_PAGE = 2;
    public static final int LAUNCH_REMOVE_BUDDY_PAGE = 3;

    public static final String USER_KEY = "User";
    private List<User> buddyList;
    private int selectedOption;


    public BuddyAdapter(List<User> buddyList, int option) {
        this.buddyList = buddyList;
        this.selectedOption = option;
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
    public void onBindViewHolder(final BuddyViewHolder holder, final int position) {
        final Context context = holder.itemView.getContext();
        final User user = buddyList.get(position);

        holder.buddyName.setText(user.getName());
        holder.buddyCourses.setText(user.extractCoursesStr());
        holder.buddyLocation.setText(user.getLocation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (selectedOption) {
                    case ALLOW_SELECTION:
                        user.setSelected(!user.isSelected());

                        holder.itemView.setBackgroundColor(
                                user.isSelected() ? Color.rgb(248, 255, 204) : Color.WHITE
                        );
                        break;
                    case LAUNCH_ADD_BUDDY_PAGE:
                        Intent launchDetailedAddBuddyIntent = new Intent(context,
                                AddBuddyDetailedActivity.class);
                        launchDetailedAddBuddyIntent.putExtra(USER_KEY, user);
                        context.startActivity(launchDetailedAddBuddyIntent);
                        break;
                    case LAUNCH_REMOVE_BUDDY_PAGE:
                        Intent launchDetailedBuddyIntent = new Intent(context,
                            BuddyDetailedActivity.class);
                        launchDetailedBuddyIntent.putExtra(USER_KEY, user);
                        context.startActivity(launchDetailedBuddyIntent);
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal argument sent to BuddyAdapter");
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