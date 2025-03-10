package com.app.wegatyou;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishActivity extends AppCompatActivity {
    private CheckBox selectedOption;
    private Button btnSubmit;
    private ImageButton btnNext;

    private String userId;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

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

        selectedOption = null;

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setEnabled(false); // Initially disabled
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOption == null) {
                    Toast.makeText(DishActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                } else {
                    // Process the selected option
                    String selectedOptionText = selectedOption.getText().toString();
                    Toast.makeText(DishActivity.this, "Selected option: " + selectedOptionText, Toast.LENGTH_SHORT).show();

                    // Enable the "Next" ImageButton
                    btnNext.setEnabled(true);
                }
            }
        });

        btnNext = findViewById(R.id.next);
        btnNext.setEnabled(false); // Initially disabled
    }

    private void clickCross() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void clickNext() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (selectedOption != null) {
                String selectedOptionText = selectedOption.getText().toString();

                // Update the corresponding user entry with the selected option
                usersRef.child(userId).child("dish").setValue(selectedOptionText);

                // Proceed to the Membership Activity
                Intent membershipIntent = new Intent(DishActivity.this, OfferMembershipActivity.class);
                membershipIntent.putExtra("userId", userId);
                startActivity(membershipIntent);
            } else {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    public void onOptionClicked(View view) {
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()) {
            // Uncheck the previously selected option (if any)
            if (selectedOption != null && !selectedOption.equals(checkBox)) {
                selectedOption.setChecked(false);
            }
            selectedOption = checkBox;
        } else if (selectedOption != null && selectedOption.equals(checkBox)) {
            // The selected option is being unchecked
            selectedOption = null;
        }

        // Check if at least 1 option is selected to enable the submit button
        btnSubmit.setEnabled(selectedOption != null);
    }
}