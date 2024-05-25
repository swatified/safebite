package com.example.safebite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class Welcome extends AppCompatActivity {
    public static final int GOOGLE_SIGN_IN_CODE = 10005;
    private ImageView loginButton, signupButton, googleButton;
    private String mode;
    private FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.welcome), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loginButton=findViewById(R.id.buttonLogin);
        signupButton=findViewById(R.id.buttonSignup);
        googleButton=findViewById(R.id.buttonGoogle);
        mAuth=FirebaseAuth.getInstance();

        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("680347858374-g47t29mr4l63bkqjteq3vfmmj35jk74u.apps.googleusercontent.com")
                .requestEmail()
                .build();

        signInClient= GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null || mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, Form.class));
        }
        googleButton.setOnClickListener(v -> {
            Intent sign = signInClient.getSignInIntent();
            startActivityForResult(sign, GOOGLE_SIGN_IN_CODE);
        });



        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(Welcome.this, Authentication.class);
            intent.putExtra("mode", "login");
            startActivity(intent);
        });
        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(Welcome.this, Authentication.class);
            intent.putExtra("mode", "signup");
            startActivity(intent);
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAcc.getIdToken(), null);
                mAuth.signInWithCredential(authCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getApplicationContext(), "Account successfully connected to our application", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Form.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Welcome.this, "Sorry, authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
}
