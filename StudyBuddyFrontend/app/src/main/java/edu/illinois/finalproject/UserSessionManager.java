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
import edu.illinois.finalproject.parser.User;

public class UserSessionManager extends FragmentActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private static User mUser = null;
    private static final String TAG = UserSessionManager.class.getSimpleName();

    public UserSessionManager(Context context) {
        mContext = context;

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.google_web_client_id))
                .requestEmail()
                .build();

        GoogleApiClient.OnConnectionFailedListener connectionFailedListener =
                new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "onConnectionFailed: " + connectionResult);
                    }
                };

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(this, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleApiClient.connect();


    }

    public static void initUser(final Context context) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("students")
                .child(auth.getUid());
        Log.d(TAG, auth.getUid());
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

    public static User getUser(final Context context) {
        if (mUser == null) {
            initUser(context);
        }

        return mUser;
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Log.d(TAG, "Signed out.");
                    }
                });
        Intent authIntent = new Intent(mContext, SignInActivity.class);
        Log.d(TAG, "authIntent" + authIntent.toString());
        mContext.startActivity(authIntent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
