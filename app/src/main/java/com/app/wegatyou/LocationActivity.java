package com.app.wegatyou;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocationActivity extends AppCompatActivity {
    private TextView mDisplayLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

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

        mDisplayLocation = findViewById(R.id.location_answer);
        mDisplayLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and configure the dialog box
                AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
                builder.setTitle("Select a city")
                        .setItems(R.array.uk_cities, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Retrieve the selected city
                                String[] cities = getResources().getStringArray(R.array.uk_cities);
                                String selectedCity = cities[which];
                                // Update the text view with the selected city
                                mDisplayLocation.setText(selectedCity);
                            }
                        });

                // Show the dialog box
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void clickcross() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clicknext() {
        String locationText = mDisplayLocation.getText().toString().trim();

        if (!locationText.isEmpty()) {
            Intent intent = getIntent();
            String userId = intent.getStringExtra("userId");

            if (userId != null) {
                DatabaseReference usersRef = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");
                DatabaseReference userRef = usersRef.child(userId);
                userRef.child("location").setValue(locationText);

                // Proceed to the GenderActivity
                Intent genderIntent = new Intent(LocationActivity.this, GenderActivity.class);
                genderIntent.putExtra("userId", userId); // Pass the userId value
                startActivity(genderIntent);
            } else {
                // Handle the case when userId is null
                // ...
            }
        } else {
            // Handle the case when the location field is empty
            // ...
        }
    }
}
