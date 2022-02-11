package com.example.onboarding.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.onboarding.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private TextInputEditText email;

    private Button foregtpasswordButton;

    private String emailTxt;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.forgetpassword_email);
        foregtpasswordButton = findViewById(R.id.forgetpassword_button);
        
        foregtpasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
        emailTxt = email.getText().toString();

        if(emailTxt.isEmpty()){
            email.setError("Email address required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()){
            email.setError("Enter valid email address");
            email.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(emailTxt)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgetPasswordActivity.this,"Check your email",Toast.LENGTH_LONG);
                            startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
                            finish();
                        }else{
                            Toast.makeText(ForgetPasswordActivity.this,task.getException().toString(),Toast.LENGTH_LONG);
                        }
                    }
                });

    }
}