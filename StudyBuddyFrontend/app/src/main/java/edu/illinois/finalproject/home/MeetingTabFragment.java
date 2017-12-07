package edu.illinois.finalproject.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.helper.AddMeetingActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingTabFragment extends Fragment {

    private Button mAddMeetingButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View returnView = inflater.inflate(R.layout.fragment_meeting_tab, container,
                false);
        final Context context = returnView.getContext();

        mAddMeetingButton = (Button) returnView.findViewById(R.id.set_meeting_button);
        mAddMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchAddMeetingIntent = new Intent(context, AddMeetingActivity.class);
                startActivity(launchAddMeetingIntent);
            }
        });
        return returnView;
    }

}
