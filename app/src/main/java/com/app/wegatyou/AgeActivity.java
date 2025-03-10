package com.app.wegatyou;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AgeActivity extends AppCompatActivity {
    private static final String TAG = "AgeActivity";
    private TextView mDisplayDate;
    private TextView mDisplayAge; // Added TextView for displaying age
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatabaseReference usersRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

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
                Log.d(TAG, "Next button clicked");
                clicknext();
            }
        });

        mDisplayDate = findViewById(R.id.age_date);
        mDisplayAge = findViewById(R.id.age_number); // Initialize TextView for displaying age

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(
                        AgeActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Log.d(TAG, "onDateSet: mm/dd/yy: " + month + "/" + day + "/" + year);

                String date = day + "/" + (month + 1) + "/" + year;
                mDisplayDate.setText(date);

                // Calculate age and display it
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                try {
                    Date birthDate = dateFormat.parse(date);
                    int age = calculateAge(birthDate);
                    mDisplayAge.setText(String.valueOf(age));

                    // Add age to the database
                    usersRef.child(userId).child("age").setValue(age);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        usersRef = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");
    }

    public void clickcross() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clicknext() {
        String ageText = mDisplayDate.getText().toString().trim();

        if (!ageText.isEmpty()) {
            Intent locationIntent = new Intent(AgeActivity.this, LocationActivity.class);
            locationIntent.putExtra("userId", userId); // Pass the userId value
            startActivity(locationIntent);
        } else {
            // Handle the case when the age field is empty
            // ...
        }
    }

    private int calculateAge(Date birthDate) {
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthDate);
        Calendar currentCalendar = Calendar.getInstance();

        int years = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
        int months = currentCalendar.get(Calendar.MONTH) - birthCalendar.get(Calendar.MONTH);
        int days = currentCalendar.get(Calendar.DAY_OF_MONTH) - birthCalendar.get(Calendar.DAY_OF_MONTH);

        if (months < 0 || (months == 0 && days < 0)) {
            years--;
        }

        return years;
    }
}
