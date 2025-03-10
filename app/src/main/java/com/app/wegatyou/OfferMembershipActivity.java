package com.app.wegatyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OfferMembershipActivity extends AppCompatActivity {

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_membership);

        Button haveALook = findViewById(R.id.have_a_look);
        haveALook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickHaveALook();
            }
        });

        Button anotherTime = findViewById(R.id.another_time);
        anotherTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickAnotherTime();
            }
        });

        usersRef = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");
    }

    public void ClickHaveALook(){
        Intent intent = new Intent(this, MemberPlanActivity.class);
        startActivity(intent);
    }

    public void ClickAnotherTime(){
        // Retrieve the userId from the previous activity
        String userId = getIntent().getStringExtra("userId");

        // Proceed to the Swipe Activity
        Intent swipeIntent = new Intent(OfferMembershipActivity.this, SwipeActivity.class);
        swipeIntent.putExtra("userId", userId);
        startActivity(swipeIntent);
    }
}