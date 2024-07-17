package com.example.safebite;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class Main extends AppCompatActivity {
    ImageView feed,mood,blogs,logoutButton;
    TextView feed1,mood1,blogs1,logout1;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "MainActivity"; // Define the TAG for logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        feed=findViewById(R.id.buttonFeed);
        mood=findViewById(R.id.buttonMood);
        blogs=findViewById(R.id.buttonBlog);
        logoutButton=findViewById(R.id.logoutButton);

        feed1=findViewById(R.id.feedText);
        mood1=findViewById(R.id.moodText);
        blogs1=findViewById(R.id.blogText);
        logout1=findViewById(R.id.logoutText);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        feed.setOnClickListener(v -> {
            Intent intent = new Intent(Main.this, anim1.class);
            startActivity(intent);
        });

        feed1.setOnClickListener(v -> {
            Intent intent = new Intent(Main.this, anim1.class);
            startActivity(intent);
        });

        mood.setOnClickListener(v -> {
            Intent intent = new Intent(Main.this, anim2.class);
            startActivity(intent);
        });

        mood1.setOnClickListener(v -> {
            Intent intent = new Intent(Main.this, anim2.class);
            startActivity(intent);
        });

        blogs.setOnClickListener(v -> {
            Toast.makeText(Main.this, "sorry, this feature is yet to come", Toast.LENGTH_SHORT).show();
        });

        blogs1.setOnClickListener(v -> {
            Toast.makeText(Main.this, "sorry, this feature is yet to come", Toast.LENGTH_SHORT).show();
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out from Firebase
                mAuth.signOut();
                Log.d(TAG, "Firebase user signed out");

                // Sign out from Google
                mGoogleSignInClient.signOut().addOnCompleteListener(Main.this, task -> {
                    Log.d(TAG, "Google user signed out");

                    // Redirect to Authentication activity
                    Intent intent = new Intent(Main.this, Welcome.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
            }
        });

        logout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out from Firebase
                mAuth.signOut();
                Log.d(TAG, "Firebase user signed out");

                // Sign out from Google
                mGoogleSignInClient.signOut().addOnCompleteListener(Main.this, task -> {
                    Log.d(TAG, "Google user signed out");

                    // Redirect to Authentication activity
                    Intent intent = new Intent(Main.this, Welcome.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
            }
        });



    }

}