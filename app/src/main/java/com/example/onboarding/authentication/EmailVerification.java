package com.example.onboarding.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.onboarding.R;
import com.example.onboarding.WorkingActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerification extends AppCompatActivity {

    Button verificationNotSend;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        verificationNotSend = findViewById(R.id.verify_now_button);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(!user.isEmailVerified()){
            verificationNotSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EmailVerification.this,"Verification Email Sent",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Main","onFailure: Email not sent"+e.getMessage());
                        }
                    });
                }
            });
        }

    }

    @Override
    protected void onStart() {
        if(user.isEmailVerified()){
            startActivity(new Intent(EmailVerification.this, WorkingActivity.class));
            finish();
        }
        super.onStart();
    }
}