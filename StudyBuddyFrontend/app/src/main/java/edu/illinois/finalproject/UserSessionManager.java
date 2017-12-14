package edu.illinois.finalproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.illinois.finalproject.auth.SignInActivity;
import edu.illinois.finalproject.auth.SignUpActivity;
import edu.illinois.finalproject.home.BuddyTabFragment;
import edu.illinois.finalproject.home.CoursesTabFragment;
import edu.illinois.finalproject.parser.User;

/**
 * Class that gets and holds user data, and performs all operations with regards to user session
 */
public class UserSessionManager implements GoogleApiClient.OnConnectionFailedListener {

    private static User mUser = null;
    private static final String TAG = UserSessionManager.class.getSimpleName();

    /**
     * Retrieves user information from Firebase. If none exists, redirects to SignUpActivity
     * @param context   Context needed for starting intents.
     */
    public static void initUser(final Context context) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("students")
                .child(auth.getUid());
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Intent launchSignUpIntent = new Intent(context, SignUpActivity.class);
                    context.startActivity(launchSignUpIntent);
                } else {
                    mUser = dataSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /**
     * Initializes user if not done yet. Returns user data retrieved from Firebase
     * @param context   Context needed to initialize user data.
     * @return          User object retrieved from Firebase
     */
    public static User getUser(final Context context) {
        if (mUser == null) {
            initUser(context);
        }

        return mUser;
    }

    /**
     * Returns a google api client for the given user.
     * @param context   Context needed to initialize client.
     * @return          GoogleApiClient initialized with provided context
     */
    public static GoogleApiClient getGoogleApiClient(final Context context) {

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.google_web_client_id))
                .requestEmail()
                .build();

        GoogleApiClient.OnConnectionFailedListener connectionFailedListener =
                new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "onConnectionFailed: " + connectionResult);
                    }
                };

        final FragmentActivity fragmentActivity = new FragmentActivity();
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(fragmentActivity, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleApiClient.connect();
        return googleApiClient;
    }

    /**
     * Performs user sign out using GoogleApiClient and returns to SignIn activity.
     * @param context           Context needed to sign out.
     * @param googleApiClient   GoogleApiClient needed to perform sign out.
     */
    public static void signOut(final Context context, GoogleApiClient googleApiClient) {

        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Log.d(TAG, "Signed out.");
                    }
                });
        Intent authIntent = new Intent(context, SignInActivity.class);
        Log.d(TAG, "authIntent" + authIntent.toString());
        context.startActivity(authIntent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
