package com.example.safebite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.resources.TypefaceUtils;
import java.lang.reflect.Field;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Form extends AppCompatActivity {
    private final String[] allergens = {
            "milk products", "eggs", "nuts", "soy", "shellfish", "wheat",
            "mustard", "kiwi", "sesame", "mango", "eggplant", "cocoa",
            "avocado", "corn", "onion", "fish", "garlic", "banana", "celery", "tomato"
    };
    private static final String TAG = "MainActivity";


    private ImageView[] imageViews = new ImageView[20];
    private boolean[] selectedAllergens = new boolean[20]; // to keep track of selected allergens
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /* int[] imageViewIds = {
                R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4,
                R.id.imageView5, R.id.imageView6, R.id.imageView7, R.id.imageView8,
                R.id.imageView9, R.id.imageView10, R.id.imageView11, R.id.imageView12,
                R.id.imageView13, R.id.imageView14, R.id.imageView15, R.id.imageView16,
                R.id.imageView17, R.id.imageView18b
        };

        for (int id : imageViewIds) {
            ImageView imageView = findViewById(id);
            setImageViewOnClickListener(imageView);
        }
    }

    private void setImageViewOnClickListener(final ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setAlpha(0.4f); // Decrease opacity to 50%
            }
        }); */





        // Initialize Firestore
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();


        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in, navigate to the next activity
            startActivity(new Intent(Form.this, Main.class));
            finish(); // Close the Authentication activity
            return;
        }


        // Find and set click listeners on image views
        for (int i = 0; i < imageViews.length; i++) {
            String imageViewID = "imageView" + (i + 1);
            int resID = getResources().getIdentifier(imageViewID, "id", getPackageName());
            imageViews[i] = findViewById(resID);

            final int index = i;
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedAllergens[index] = !selectedAllergens[index]; // toggle selection
                    v.setAlpha(0.4f); // change background to indicate selection
                }
            });
        }

        // Handle submit button click
        ImageView submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selectedAllergenList = new ArrayList<>();
                for (int i = 0; i < selectedAllergens.length; i++) {
                    if (selectedAllergens[i]) {
                        selectedAllergenList.add(allergens[i]);
                    }
                }

                // Ensure user is authenticated before saving
                if (user != null) {
                    String userName = user.getDisplayName() != null ? user.getDisplayName() : "Anonymous";
                    String userEmail = user.getEmail() != null ? user.getEmail() : "noemail@example.com";

                    // Save to Firestore
                    Map<String, Object> data = new HashMap<>();
                    data.put("name", userName);
                    data.put("email", userEmail);
                    data.put("selectedAllergens", selectedAllergenList);

                    db.collection("users").document(user.getUid()) // use the authenticated user's UID
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Form.this, "Allergens saved successfully!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Form.this, Main.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Error saving allergens", e);
                                    Toast.makeText(Form.this, "Error saving allergens: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    Toast.makeText(Form.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}