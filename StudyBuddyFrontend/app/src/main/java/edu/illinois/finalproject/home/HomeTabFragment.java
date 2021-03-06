package edu.illinois.finalproject.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.listener.Observer;
import edu.illinois.finalproject.listener.Subject;
import edu.illinois.finalproject.parser.User;
import edu.illinois.finalproject.UserSessionManager;

/**
 * Base tab fragment launched when app launches.
 * It is the Welcome page containing basic data about user.
 */
public class HomeTabFragment extends Fragment implements Observer {

    private static final String TAG = HomeTabFragment.class.getSimpleName();

    private TextView mWelcomeText;
    private ImageView mProfileImage;
    private Button mSignOutButton;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleApiClient mGoogleApiClient;

    private Context mContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View returnView = inflater.inflate(R.layout.fragment_home_tab, container,
                false);
        mContext = returnView.getContext();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mGoogleApiClient = UserSessionManager.getGoogleApiClient(mContext);

        String welcomeText = String.format(getString(R.string.welcome_text),
                mFirebaseUser.getDisplayName());
        mWelcomeText = (TextView) returnView.findViewById(R.id.welcome_text);
        mWelcomeText.setText(welcomeText);

        // Parse and display user's google account profile image.
        mProfileImage = (ImageView) returnView.findViewById(R.id.profile_image);
        Picasso.with(mContext).load(mFirebaseUser.getPhotoUrl()).into(mProfileImage);
        mProfileImage.setVisibility(ImageView.VISIBLE);

        mSignOutButton = (Button) returnView.findViewById(R.id.sign_out_button);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSessionManager.signOut(mContext, mGoogleApiClient);
            }
        });

        Subject.addObserver(this);

        // Inflate the layout for this fragment
        return returnView;
    }

    @Override
    public void update() {
        User user = UserSessionManager.getUser(mContext);
        if (user == null) {
            return;
        }
    }
}
