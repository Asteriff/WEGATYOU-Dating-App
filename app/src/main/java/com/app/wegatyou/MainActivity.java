package com.app.wegatyou;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1001;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> signInLauncher;

    private boolean isLoginExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //This needs to be in MainActivity in the onCreate method...
        FirebaseApp.initializeApp(this);

        // Write a message to the database
        FirebaseDatabase europeDatabase = FirebaseDatabase.getInstance("https://wegatyou-386819-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = europeDatabase.getReference("message");

        myRef.setValue("Hello, World!");

        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        handleSignInResult(resultCode, data);
                    }
                }
        );

        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        SignInButton googleSignInButton = findViewById(R.id.google_sign_in_button);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                signInLauncher.launch(signInIntent);
            }
        });

        Button createAccount = findViewById(R.id.create_account);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateAccount();
            }
        });

        Button logInWithEmail = findViewById(R.id.log_in_with_email);
        logInWithEmail.setEnabled(true);
        logInWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEmail();
            }
        });

        VideoView videoView = findViewById(R.id.home_screen);
        ImageView darkFilter = findViewById(R.id.dark_filter);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.homescreen);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
                darkFilter.setVisibility(View.INVISIBLE);
            }
        });

        Button logIn = findViewById(R.id.log_in);
        Button backButton = findViewById(R.id.back_button);
        LinearLayout moreButtonsLayout = findViewById(R.id.more_buttons_layout);
        moreButtonsLayout.setVisibility(View.INVISIBLE);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moreButtonsLayout.getVisibility() == View.INVISIBLE) {
                    logIn.animate().alpha(0f).setDuration(300).start();
                    createAccount.animate().alpha(0f).setDuration(300).start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            moreButtonsLayout.setVisibility(View.VISIBLE);
                            moreButtonsLayout.animate().alpha(1f).setDuration(300).start();
                        }
                    }, 300);
                    isLoginExpanded = true;
                    createAccount.setEnabled(false);
                } else {
                    moreButtonsLayout.animate().alpha(0f).setDuration(300).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            moreButtonsLayout.setVisibility(View.INVISIBLE);
                            logIn.animate().alpha(1f).setDuration(300).start();
                            createAccount.animate().alpha(1f).setDuration(300).start();
                        }
                    }).start();
                    isLoginExpanded = false;
                    createAccount.setEnabled(true);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreButtonsLayout.animate().alpha(0f).setDuration(300).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        moreButtonsLayout.setVisibility(View.INVISIBLE);
                        logIn.animate().alpha(1f).setDuration(300).start();
                        createAccount.animate().alpha(1f).setDuration(300).start();
                    }
                }).start();
                isLoginExpanded = false;
                if (!isLoginExpanded) {
                    createAccount.setEnabled(true);
                }
            }
        });
    }

    private void handleSignInResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Signed in successfully, proceed to the next activity.
                proceedToNextActivity(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        } else {
            // Sign-in was canceled or failed, handle accordingly
        }
    }

    private void proceedToNextActivity(GoogleSignInAccount account) {
        Intent intent = new Intent(this, SwipeActivity.class);
        startActivity(intent);
        finish();
    }

    public void openCreateAccount() {
        Intent intent = new Intent(this, EmailActivity.class);
        startActivity(intent);
    }

    public void openEmail() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            proceedToNextActivity(account);
        }
    }
}
