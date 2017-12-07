package edu.illinois.finalproject.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.helper.AddBuddyActivity;

public class BuddyTabFragment extends Fragment {

    private Button mAddBuddyButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_buddy_tab, container,
                false);
        final Context context = returnView.getContext();

        mAddBuddyButton = (Button) returnView.findViewById(R.id.find_buddy_button);
        mAddBuddyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchAddBuddyIntent = new Intent(context, AddBuddyActivity.class);
                startActivity(launchAddBuddyIntent);
            }
        });
        return returnView;
    }
}
