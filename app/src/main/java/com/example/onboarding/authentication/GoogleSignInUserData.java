package com.example.onboarding.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.onboarding.R;
import com.example.onboarding.WorkingActivity;
import com.example.onboarding.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GoogleSignInUserData extends AppCompatActivity {

    private Button updateButton;

    private TextInputEditText name,phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in_user_data);

        updateButton = findViewById(R.id.update_data);

        name = findViewById(R.id.user_name);
        phonenumber = findViewById(R.id.user_phonenumber);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

    }

    public void updateData(){

        User user = new User(name.getText().toString(), phonenumber.getText().toString());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Intent intent = new Intent(GoogleSignInUserData.this,WorkingActivity.class);
                    intent.putExtra("Name",user.getName());
                    intent.putExtra("Phonenumber",user.getPhoneNumber());
                    startActivity(intent);
                    finish();
                }


            }
        });

    }



    }
