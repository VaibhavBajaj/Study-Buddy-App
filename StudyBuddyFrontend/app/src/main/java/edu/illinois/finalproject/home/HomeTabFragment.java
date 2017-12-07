package edu.illinois.finalproject.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.parser.User;
import edu.illinois.finalproject.UserSessionManager;
import edu.illinois.finalproject.auth.SignInActivity;
import edu.illinois.finalproject.auth.SignUpActivity;

public class HomeTabFragment extends Fragment {

    private static final String TAG = HomeTabFragment.class.getSimpleName();

    private TextView mWelcomeText;
    private ImageView mProfileImage;
    private Button mSignOutButton;

    private UserSessionManager mUserSessionManager;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private User mUser = null;

    private boolean mOtherActivityProcessing = false;

    private Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View returnView = inflater.inflate(R.layout.fragment_home_tab, container,
                false);
        context = returnView.getContext();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        getFirebaseData();
        if (mOtherActivityProcessing) {
            return returnView;
        }

        String welcomeText = String.format(getString(R.string.welcome_text),
                mFirebaseUser.getDisplayName());
        mWelcomeText = (TextView) returnView.findViewById(R.id.welcome_text);
        mWelcomeText.setText(welcomeText);

        mProfileImage = (ImageView) returnView.findViewById(R.id.profile_image);
        Picasso.with(context).load(mFirebaseUser.getPhotoUrl()).into(mProfileImage);
        mProfileImage.setVisibility(ImageView.VISIBLE);

        mUserSessionManager = new UserSessionManager(context);
        mSignOutButton = (Button) returnView.findViewById(R.id.sign_out_button);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserSessionManager.signOut();
            }
        });

        // Inflate the layout for this fragment
        return returnView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mOtherActivityProcessing = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        mOtherActivityProcessing = false;
    }

    private void getFirebaseData() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("students")
                .child(mAuth.getUid());
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null) {
                    Intent launchSignUpIntent = new Intent(context, SignUpActivity.class);
                    startActivity(launchSignUpIntent);
                } else {
                    mUser = dataSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
