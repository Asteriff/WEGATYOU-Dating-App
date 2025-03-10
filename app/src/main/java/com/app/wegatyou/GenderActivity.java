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
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GenderActivity extends AppCompatActivity {
    private RadioGroup genderRadioGroup;
    private TextView selectedOptionTextView;

    private DatabaseReference usersRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);

        usersRef = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");

        ImageButton Cross = findViewById(R.id.cross);
        Cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCross();
            }
        });

        ImageButton Next = findViewById(R.id.next);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickNext();
            }
        });

        initializeGenderOptions();

        // Get reference to the selected options TextView
        selectedOptionTextView = findViewById(R.id.selectedOptionTextView);

        // Set up click listener for the gender options
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                String selectedOption = selectedRadioButton.getText().toString();

                selectedOptionTextView.setText(selectedOption); // Set the selected option

                if (selectedOption.equals("Other")) {
                    showCustomGenderDialog();
                }
            }
        });

        // Retrieve the userId from the previous activity
        userId = getIntent().getStringExtra("userId");
    }

    private void initializeGenderOptions() {
        // Get reference to the RadioGroup
        genderRadioGroup = findViewById(R.id.genderRadioGroup);

        // Clear any existing options
        genderRadioGroup.removeAllViews();

        // Add the initial gender options
        addGenderOption("Male");
        addGenderOption("Female");
        addGenderOption("Nonbinary");
        addGenderOption("Other");
    }

    private void showCustomGenderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Gender");

        final EditText inputEditText = new EditText(this);
        builder.setView(inputEditText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String customGender = inputEditText.getText().toString().trim();
                if (!customGender.isEmpty()) {
                    selectedOptionTextView.setText(customGender);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    private void addGenderOption(String option) {
        RadioButton radioButton = new RadioButton(this);
        radioButton.setText(option);
        radioButton.setId(View.generateViewId());
        genderRadioGroup.addView(radioButton);
    }

    private void clickCross() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void clickNext() {
        // Get the selected gender option
        String selectedGender = selectedOptionTextView.getText().toString().trim();
        Log.d(TAG, "gender selected: " + selectedGender);

        // Update the corresponding user entry with the selected gender
        DatabaseReference userRef = usersRef.child(userId);
        userRef.child("gender").setValue(selectedGender);

        // Proceed to the SexualityActivity
        Intent sexualityIntent = new Intent(GenderActivity.this, SexualityActivity.class);
        sexualityIntent.putExtra("userId", userId); // Pass the userId value
        startActivity(sexualityIntent);
    }

}
