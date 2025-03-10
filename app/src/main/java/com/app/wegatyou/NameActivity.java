package com.app.wegatyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NameActivity extends AppCompatActivity {

    private DatabaseReference usersRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        usersRef = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users");
        userId = getIntent().getStringExtra("userId");


        ImageButton nextButton = findViewById(R.id.next);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText firstNameEditText = findViewById(R.id.name_first);
                TextInputEditText lastNameEditText = findViewById(R.id.name_last);

                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();

                if (TextUtils.isEmpty(firstName)) {
                    Toast.makeText(NameActivity.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userId)) {
                    Toast.makeText(NameActivity.this, "User ID not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Store the first name in the Firebase Realtime Database
                usersRef.child(userId).child("firstName").setValue(firstName);

                if (!TextUtils.isEmpty(lastName)) {
                    // Store the last name in the Firebase Realtime Database if provided
                    usersRef.child(userId).child("lastName").setValue(lastName);
                }

                // Proceed to the next activity
                Intent nextIntent = new Intent(NameActivity.this, AgeActivity.class);
                nextIntent.putExtra("userId", userId);
                startActivity(nextIntent);
            }
        });

    }

}


