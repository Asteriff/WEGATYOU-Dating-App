package com.app.wegatyou;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SexualityActivity extends AppCompatActivity {
    private RadioGroup sexualityRadioGroup;
    private TextView selectedOptionTextView;
    private DatabaseReference usersRef;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sexuality);

        ImageButton Cross = findViewById(R.id.cross);
        Cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickcross();
            }
        });

        ImageButton Next = findViewById(R.id.next);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicknext();
            }
        });

        initializeSexualityOptions();

        // Get reference to the selected options TextView
        selectedOptionTextView = findViewById(R.id.selectedOptionTextView);

        // Set up click listener for the gender options
        sexualityRadioGroup = findViewById(R.id.sexualityRadioGroup);
        sexualityRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                StringBuilder selectedOptions = new StringBuilder();

                for (int i = 0; i < sexualityRadioGroup.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) sexualityRadioGroup.getChildAt(i);

                    if (radioButton.isChecked()) {
                        String selectedOption = radioButton.getText().toString();
                        selectedOptions.append(selectedOption).append(", ");
                    }
                }

                String selectedOptionsString = selectedOptions.toString();

                if (selectedOptionsString.endsWith(", ")) {
                    selectedOptionsString = selectedOptionsString.substring(0, selectedOptionsString.length() - 2);
                }

                selectedOptionTextView.setText(selectedOptionsString);

                if (selectedOptionsString.contains("I use another term")) {
                    showCustomSexualityDialog();
                }
            }
        });

        usersRef = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
    }

    private void initializeSexualityOptions() {
        // Get reference to the RadioGroup
        sexualityRadioGroup = findViewById(R.id.sexualityRadioGroup);

        // Clear any existing options
        sexualityRadioGroup.removeAllViews();

        // Add the initial sexuality options
        addSexualityOption("Straight");
        addSexualityOption("Gay");
        addSexualityOption("Lesbian");
        addSexualityOption("Bisexual");
        addSexualityOption("Non-Binary");
        addSexualityOption("Prefer not to say");
        addSexualityOption("I use another term");
    }

    private void showCustomSexualityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Sexuality");

        final EditText inputEditText = new EditText(this);
        builder.setView(inputEditText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String customSexuality = inputEditText.getText().toString().trim();
                if (!customSexuality.isEmpty()) {
                    selectedOptionTextView.setText(customSexuality);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    private void addSexualityOption(String option) {
        RadioButton radioButton = new RadioButton(this);
        radioButton.setText(option);
        radioButton.setId(View.generateViewId());
        sexualityRadioGroup.addView(radioButton);
    }

    public void clickcross() {
        // Delete user data and authentication
        if (userId != null) {
            // Delete user data from the database
            DatabaseReference userRef = usersRef.child(userId);
            userRef.removeValue();

            // Delete user authentication (example using Firebase Authentication)
            FirebaseAuth.getInstance().getCurrentUser().delete();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clicknext() {
        // Get the selected sexuality option
        String selectedSexuality = selectedOptionTextView.getText().toString().trim();
        Log.d(TAG, "gender selected: " + selectedSexuality);

        // Update the corresponding user entry with the selected gender
        DatabaseReference userRef = usersRef.child(userId);
        userRef.child("sexuality").setValue(selectedSexuality);

        // Proceed to the TandCActivity
        Intent tancIntent = new Intent(SexualityActivity.this, TermsandConditionsActivity.class);
        tancIntent.putExtra("userId", userId); // Pass the userId value
        startActivity(tancIntent);
    }
}
