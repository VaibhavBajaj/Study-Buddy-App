package edu.illinois.finalproject.auth;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.UserSessionManager;
import edu.illinois.finalproject.home.HomeActivity;

/**
 * Activity launched from HomeActivity if user is not authenticated.
 * Contains methods needed for GoogleSignIn
 */
public class SignInActivity extends AppCompatActivity {

    private SignInButton mSignInButton;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    private static final int RC_SIGN_IN = 2;
    private static final String TAG = SignInActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // The google api client is used for sign in and sign out.
        mGoogleApiClient = UserSessionManager.getGoogleApiClient(this);

        mAuth = FirebaseAuth.getInstance();
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /**
     * If sign in was success, we will give it FirebaseAuth else the page stays at Sign In.
     * @param result    Result given by GoogleSignIn
     */
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
        } else {
            Log.w(TAG, "Authentication failed");
        }
    }

    /**
     * Completes initialization of FirebaseAuth with the unique Id from the Google Account
     * @param account   Google account recieved from GoogleSignIn
     */
    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                private Context context = SignInActivity.this;
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // FirebaseAuth has now been intialized.
                        UserSessionManager.initUser(context);
                        Intent launchHomePageIntent = new Intent(context, HomeActivity.class);
                        context.startActivity(launchHomePageIntent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "Sign In Failure", task.getException());
                        Toast.makeText(SignInActivity.this, "Sign in failed.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
    }
}
