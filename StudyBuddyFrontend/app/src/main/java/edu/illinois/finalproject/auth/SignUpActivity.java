package edu.illinois.finalproject.auth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.parser.User;
import edu.illinois.finalproject.home.HomeActivity;

/**
 * If we don't have any information about the user on Firebase, this activity is launched
 * from UserSessionManager to get User information and update Firebase.
 */
public class SignUpActivity extends AppCompatActivity {

    private Spinner mLocationSpinner;
    private Button mSignUpButton;
    private FirebaseAuth mAuth;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mLocationSpinner = (Spinner) findViewById(R.id.location_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.location_array,
                R.layout.support_simple_spinner_dropdown_item
        );
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mLocationSpinner.setAdapter(adapter);

        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mAuth = FirebaseAuth.getInstance();

        Toast informUser = Toast.makeText(this, R.string.inform_user, Toast.LENGTH_LONG);
        informUser.show();

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLocationSpinner.getSelectedItem() == null) {
                    Toast toast = Toast.makeText(context, R.string.error_no_location,
                            Toast.LENGTH_SHORT);
                    toast.show();
                    mLocationSpinner.requestFocus();
                    return;
                }

                // Create user and push to firebase.
                String userLocation = mLocationSpinner.getSelectedItem().toString();
                User currentUser = new User(
                        mAuth.getUid(),                             // Id
                        mAuth.getCurrentUser().getDisplayName(),    // Name
                        new ArrayList<String>(),                    // Courses
                        new ArrayList<String>(),                    // Buddies
                        new ArrayList<String>(),                    // Meetings
                        userLocation                                // Location
                );

                final FirebaseDatabase db = FirebaseDatabase.getInstance();
                final DatabaseReference studentRef = db.getReference("students");

                Map<String, Object> userMap = new HashMap<>();
                userMap.put(mAuth.getUid(), currentUser);
                studentRef.updateChildren(userMap);

                Intent launchHomeIntent = new Intent(context, HomeActivity.class);
                context.startActivity(launchHomeIntent);
            }
        });
    }

    /**
     * Prevent user from return to Home Page without signing up.
     */
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please complete signing up first", Toast.LENGTH_LONG)
                .show();
    }
}
