package com.app.wegatyou;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TermsandConditionsActivity extends AppCompatActivity implements TermsandConditionsFragment.TermsAndConditionsDialogListener, SecurityFragment.SecurityDialogListener {
    private boolean isTermsAccepted = false;
    private boolean isSecurityAccepted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsand_conditions);

        ImageButton nextButton = findViewById(R.id.next);
        nextButton.setEnabled(false);

        AppCompatButton tancButton = findViewById(R.id.tanc_button);
        tancButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTermsAndConditionsDialog();
            }
        });

        AppCompatButton securityButton = findViewById(R.id.security_button);
        securityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSecurityDialog();
            }
        });
    }

    private void showTermsAndConditionsDialog() {
        TermsandConditionsFragment dialogFragment = new TermsandConditionsFragment(new TermsandConditionsFragment.TermsAndConditionsDialogListener() {
            @Override
            public void onTermsAndConditionsAccepted(boolean isAccepted) {
                isTermsAccepted = isAccepted;
                checkIfBothDialogsAccepted();
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "dialog_terms_conditions");
    }

    private void showSecurityDialog() {
        SecurityFragment dialogFragment = new SecurityFragment();
        dialogFragment.setSecurityDialogListener(this);
        dialogFragment.show(getSupportFragmentManager(), "dialog_security");
    }

    @Override
    public void onTermsAndConditionsAccepted(boolean isAccepted) {
        isTermsAccepted = isAccepted;
        checkIfBothDialogsAccepted();
    }

    @Override
    public void onSecurityTermsAccepted(boolean isAccepted) {
        isSecurityAccepted = isAccepted;
        checkIfBothDialogsAccepted();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Delete user data and authentication
        String userId = getIntent().getStringExtra("userId");

        // Delete user data from the database
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").child(userId);
        userRef.removeValue();

        // Delete user authentication (example using Firebase Authentication)
        FirebaseAuth.getInstance().getCurrentUser().delete();
    }


    private void checkIfBothDialogsAccepted() {
        if (isTermsAccepted && isSecurityAccepted) {
            String userId = getIntent().getStringExtra("userId");
            ImageButton nextButton = findViewById(R.id.next);
            nextButton.setEnabled(true);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Proceed to the ThankYouActivity
                    Intent thankIntent = new Intent(TermsandConditionsActivity.this, ThankYouActivity.class);
                    thankIntent.putExtra("userId", userId); // Pass the userId value
                    startActivity(thankIntent);
                }
            });
        }
    }
}
