package com.app.wegatyou;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SwipeActivity extends AppCompatActivity {
    private DatabaseReference usersRef;
    private String userId;

    private List<User> userList;
    private int currentUserIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        Button dislikeButton = findViewById(R.id.dislike);
        Button likeButton = findViewById(R.id.like);

        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextUser();
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUserIndex < userList.size()) {
                    User currentUser = userList.get(currentUserIndex);
                    List<String> likedUserIds = currentUser.getLikedUserIds();

                    if (likedUserIds != null) {
                        storeUserFeedback(likedUserIds);
                    } else {
                        Log.d(TAG, "likedUserIds is null");
                    }
                }
                showNextUser();
            }
        });

        fetchUserDataFromDatabase();

        userId = getIntent().getStringExtra("userId");
        if (userId != null) {
            usersRef = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                    .child("users")
                    .child(userId);
        }

        ImageButton bellButton = findViewById(R.id.profile_bell);
        bellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotificationSettingsDialog();
            }
        });

        ImageButton profileButton = findViewById(R.id.swipe_profile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileActivity();
            }
        });

        ImageButton mapButton = findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDateSpotsActivity();
            }
        });

        ImageButton messageButton = findViewById(R.id.message_button);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMessageActivity();
            }
        });

        ImageButton settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsActivity();
            }
        });

        ImageButton homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do nothing (already on the home screen)
            }
        });
    }

    private void fetchUserDataFromDatabase() {
        usersRef = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users");
        Log.d(TAG, "fetchUserDataFromDatabase: UserRef" + usersRef);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList = new ArrayList<>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    List<String> likedUserIds = new ArrayList<>();
                    likedUserIds.add(userSnapshot.getKey());
                    user.setLikedUserIds(likedUserIds);
                    userList.add(user);
                }

                showNextUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any database error
            }
        });
    }

    private void showNotificationSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notification Settings");

        SwitchCompat switchCompat = new SwitchCompat(this);
        switchCompat.setChecked(isNotificationEnabled());
        switchCompat.setText("Enable Notifications");

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setNotificationEnabled(isChecked);
            }
        });

        builder.setView(switchCompat);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean isNotificationEnabled() {
        // Implement your logic here to check the notification status
        // Return true if notifications are enabled, false otherwise
        return true;
    }

    private void setNotificationEnabled(boolean enabled) {
        // Implement your logic here to update the notification status based on the 'enabled' parameter
    }

    private void openDateSpotsActivity() {
        Intent intent = new Intent(this, DateSpotsActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void openMessageActivity() {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void showNextUser() {
        if (isDestroyed()) {
            return; // Activity is destroyed, do nothing
        }
        if (userList == null || userList.isEmpty()) {
            Log.d(TAG, "showNextUser: userList Empty");
            return;
        }

        if (currentUserIndex < userList.size() - 1) {
            currentUserIndex++;
        } else {
            Log.d(TAG, "showNextUser: No More Users!");
            return;
        }

        User nextUser = userList.get(currentUserIndex);

        TextView nameAgeTextView = findViewById(R.id.name_age);
        nameAgeTextView.setText(nextUser.getFirstName() + ", " + nextUser.getAge());

        TextView dishTextView = findViewById(R.id.dish);
        dishTextView.setText(nextUser.getDish());

        TextView locationTextView = findViewById(R.id.location);
        locationTextView.setText(nextUser.getLocation());

        ImageView userPictureImageView = findViewById(R.id.user_picture);
        String profilePictureUrl = nextUser.getProfilePictureUrl();

        Glide.with(this)
                .load(profilePictureUrl)
                .placeholder(R.drawable.clouds)
                .error(R.drawable.clouds)
                .into(userPictureImageView);
    }


    private void storeUserFeedback(List<String> likedUserIds) {
        if (userId != null && likedUserIds != null) {
            DatabaseReference currentUserRef = usersRef.child(userId);
            currentUserRef.child("likedUserIds").setValue(likedUserIds)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "storeUserFeedback: Storing User!" + userId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "storeUserFeedback: Failed to store user feedback!", e);
                        }
                    });
        }
    }

}
