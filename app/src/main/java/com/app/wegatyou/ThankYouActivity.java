package com.app.wegatyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ThankYouActivity extends AppCompatActivity {

    private DatabaseReference usersRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

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

        usersRef = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
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
        // Proceed to the ProfilePicActivity
        Intent ppIntent = new Intent(ThankYouActivity.this, ProfilePicActivity.class);
        ppIntent.putExtra("userId", userId); // Pass the userId value
        startActivity(ppIntent);
    }
}
