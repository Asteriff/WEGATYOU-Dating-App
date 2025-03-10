package com.app.wegatyou;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PasswordActivity extends AppCompatActivity {

    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;

    private String userId;
    private String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");

        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
            userEmail = intent.getStringExtra("email"); // Retrieve the email from the intent
        }

        // Find views and set click listener for password submission button
        ImageButton next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered password
                TextInputEditText passwordEditText = findViewById(R.id.password_type);
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(PasswordActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                } else {
                    // Create user in Firebase Authentication with the email and password
                    mAuth.createUserWithEmailAndPassword(userEmail, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // User successfully created in Firebase Authentication
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            // Store the user ID in the Realtime Database under the unique ID
                                            usersRef.child(userId).child("uid").setValue(user.getUid());
                                        }

                                        // Continue to the next activity or perform any other necessary tasks
                                        // e.g., start a new activity
                                        Intent nextIntent = new Intent(PasswordActivity.this, NameActivity.class);
                                        nextIntent.putExtra("userId", userId);
                                        startActivity(nextIntent);
                                    } else {
                                        // Failed to create user in Firebase Authentication
                                        Toast.makeText(PasswordActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

}




