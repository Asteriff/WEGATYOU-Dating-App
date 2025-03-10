package com.app.wegatyou;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfilePicActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private ImageView profilePicture;

    private DatabaseReference usersRef;
    private StorageReference profilePicturesRef;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pic);

        ImageButton Cross = findViewById(R.id.cross);
        Cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCross();
            }
        });

        ImageButton Next = findViewById(R.id.next);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickNext();
            }
        });

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        usersRef = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");
        profilePicturesRef = FirebaseStorage.getInstance().getReference("profile_pictures");

        // Initialize the image picker launcher
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri imageUri = data.getData();
                        if (imageUri != null) {
                            // Upload the image to Firebase Storage
                            uploadProfilePicture(imageUri);
                        }
                    }
                }
            }
        });

        profilePicture = findViewById(R.id.profile_picture);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchImagePicker();
            }
        });
    }

    private void launchImagePicker() {
        // Launch image picker intent
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void uploadProfilePicture(Uri imageUri) {
        // Create a reference to the profile picture file in Firebase Storage
        StorageReference pictureRef = profilePicturesRef.child(userId + ".jpg");

        // Upload the file to Firebase Storage
        UploadTask uploadTask = pictureRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Retrieve the download URL of the uploaded file
            pictureRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                String profilePictureUrl = downloadUri.toString();

                // Update the corresponding user entry with the profile picture URL
                DatabaseReference userRef = usersRef.child(userId);
                userRef.child("profilePictureUrl").setValue(profilePictureUrl);

                // Proceed to the Dish Activity
                Intent dishIntent = new Intent(ProfilePicActivity.this, DishActivity.class);
                dishIntent.putExtra("userId", userId); // Pass the userId value
                startActivity(dishIntent);
            });
        });
    }

    private void clickCross() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void clickNext() {
        // Proceed to the Dish Activity without selecting a profile picture
        Intent dishIntent = new Intent(ProfilePicActivity.this, DishActivity.class);
        dishIntent.putExtra("userId", userId); // Pass the userId value
        startActivity(dishIntent);
    }
}
