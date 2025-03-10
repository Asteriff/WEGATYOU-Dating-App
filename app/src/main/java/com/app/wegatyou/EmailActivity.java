package com.app.wegatyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmailActivity extends AppCompatActivity {

    private DatabaseReference usersRef;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        ImageButton next = findViewById(R.id.next);

        TextInputEditText emailType = findViewById(R.id.email_type);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailType.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(EmailActivity.this, "Please enter an email", Toast.LENGTH_SHORT).show();
                } else {
                    // Store email in the Firebase Realtime Database with a unique user ID
                    DatabaseReference newUserRef = usersRef.push();
                    String userId = newUserRef.getKey(); // Generate a unique user ID
                    newUserRef.child("email").setValue(email);

                    Intent intent = new Intent(EmailActivity.this, PasswordActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("email", email); // Pass the email as an extra
                    startActivity(intent);
                }
            }
        });



        usersRef = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");
    }

    private void clickCross() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
