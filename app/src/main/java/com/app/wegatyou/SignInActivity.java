package com.app.wegatyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app");

        emailEditText = findViewById(R.id.email_type);
        passwordEditText = findViewById(R.id.password_type);

        ImageButton signInButton = findViewById(R.id.next);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignInActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // User is signed in, retrieve the user ID from the database
                                DatabaseReference userRef = mDatabase.getReference("users");
                                userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String userId = null;
                                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                            String userEmail = userSnapshot.child("email").getValue(String.class);
                                            if (userEmail != null && userEmail.equals(email)) {
                                                userId = userSnapshot.getKey();
                                                break;
                                            }
                                        }
                                        Log.d("SignInActivity", "Retrieved user ID: " + userId); // Add this line
                                        if (userId != null) {
                                            // User ID is retrieved, proceed to SwipeActivity with user data
                                            Intent intent = new Intent(SignInActivity.this, SwipeActivity.class);
                                            intent.putExtra("userId", userId);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // User ID is null, handle accordingly (optional)
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Error occurred while retrieving user ID from the database
                                    }
                                });
                            } else {
                                // User is null, handle accordingly (optional)
                            }
                        } else {
                            // Sign in failed, display a message to the user
                            Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
