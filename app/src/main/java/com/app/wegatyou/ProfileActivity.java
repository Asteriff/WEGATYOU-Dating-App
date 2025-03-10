package com.app.wegatyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileNameTextView;
    private TextView profileFoodTextView;
    private TextView profileGenderTextView;
    private TextView profilePreferenceTextView;
    private TextView profileLocationTextView;
    private ImageView ppImage;

    private String userId;

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileNameTextView = findViewById(R.id.profile_name);
        profileFoodTextView = findViewById(R.id.food);
        profileGenderTextView = findViewById(R.id.profile_gender);
        profilePreferenceTextView = findViewById(R.id.profile_preference);
        profileLocationTextView = findViewById(R.id.profile_location);
        ppImage = findViewById(R.id.pp_image);

        ImageButton crossButton = findViewById(R.id.cross);
        crossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCross();
            }
        });

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");

        if (userId != null) {
            // Reference to the user's data in the database
            usersRef = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference()
                    .child("users")
                    .child(userId);

            // Retrieve user data from the database
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve user data from the snapshot
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        ArrayList<String> selectedOptions = dataSnapshot.child("selectedOptions")
                                .getValue(new GenericTypeIndicator<ArrayList<String>>() {});
                        String gender = dataSnapshot.child("gender").getValue(String.class);
                        String sexuality = dataSnapshot.child("sexuality").getValue(String.class);
                        String location = dataSnapshot.child("location").getValue(String.class);
                        Long age = dataSnapshot.child("age").getValue(Long.class);
                        String profilePictureUrl = dataSnapshot.child("profilePictureUrl").getValue(String.class);

                        // Check if selectedOptions is not null and contains at least one option
                        if (selectedOptions != null && !selectedOptions.isEmpty()) {
                            // Extract the first option from the ArrayList
                            String firstOption = selectedOptions.get(0);

                            // Set the first option as a string in the "Food" TextView
                            profileFoodTextView.setText(firstOption);
                        }

                        // Set the retrieved data to the corresponding TextViews
                        profileNameTextView.setText(firstName + ", " + age);
                        profileGenderTextView.setText(gender);
                        profilePreferenceTextView.setText(sexuality);
                        profileLocationTextView.setText("Lives in " + location);

                        // Load profile picture using Glide
                        if (!isDestroyed()) {
                            Glide.with(ProfileActivity.this)
                                    .load(profilePictureUrl)
                                    .into(ppImage);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle any errors that occur during the database retrieval
                }
            });
        }
    }

    public void clickCross() {
        Intent intent = new Intent(this, SwipeActivity.class);
        intent.putExtra("missingUserId", true); // Pass the missingUserId flag
        intent.putExtra("userId", userId); // Pass the original userId if available
        startActivity(intent);
    }

}
