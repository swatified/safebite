package com.example.safebite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.text.Normalizer;
import java.util.concurrent.TimeUnit;

public class Authentication extends AppCompatActivity {
    EditText countryCode, phoneNumber, enterOTPField;
    ImageView sendOTP, verifyOTP, resendOTP;
    String userPhoneNumber, verificationId;
    FirebaseAuth auth;
    PhoneAuthProvider.ForceResendingToken token;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private ConstraintLayout otplayout;
    private String mode;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        otplayout=findViewById(R.id.signup);
        mode = getIntent().getStringExtra("mode");

        if ("login".equals(mode)) {
            otplayout.setBackgroundResource(R.drawable.a1);
        } else if ("signup".equals(mode)) {
            otplayout.setBackgroundResource(R.drawable.picsart_24_05_25_02_11_10_019);
        }


        countryCode = findViewById(R.id.editTextNumberSigned);
        phoneNumber = findViewById(R.id.editTextPhone);
        sendOTP = findViewById(R.id.buttonSendOTP);
        enterOTPField = findViewById(R.id.editTextNumberPassword);
        resendOTP = findViewById(R.id.buttonResendOTP);
        verifyOTP = findViewById(R.id.buttonVerifyOTP);
        auth=FirebaseAuth.getInstance();
        //googleSignIn = findViewById(R.id.buttonGoogleSignIn);

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countryCode.getText().toString().isEmpty()) {
                    countryCode.setError("country code is a required field");
                    return;
                }
                if (phoneNumber.getText().toString().isEmpty()) {
                    phoneNumber.setError("please enter a phone number");
                    return;
                }
                userPhoneNumber = "+" + countryCode.getText().toString() + phoneNumber.getText().toString();
                verifyPhone(userPhoneNumber);
                Toast.makeText(Authentication.this,userPhoneNumber,Toast.LENGTH_SHORT).show();
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                verifyPhone(userPhoneNumber);
            }
        });

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enterOTPField.getText().toString().isEmpty()){
                    enterOTPField.setError("no otp recieved");
                    return;
                }
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, enterOTPField.getText().toString());
                authenticateUser(credential);
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                authenticateUser(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                token = forceResendingToken;

                sendOTP.setVisibility(View.GONE);
                //googleSignIn.setVisibility(View.GONE);
                enterOTPField.setVisibility(View.VISIBLE);
                resendOTP.setVisibility(View.VISIBLE);
                verifyOTP.setVisibility(View.VISIBLE);
                resendOTP.setEnabled(false);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                resendOTP.setEnabled(true);
            }
        };
    }

    public void verifyPhone(String phoneNum) {
        //very very crucial
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setActivity(this)
                .setPhoneNumber(phoneNum)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void authenticateUser(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(Authentication.this, "successfully signed you in", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Form.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Authentication.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
