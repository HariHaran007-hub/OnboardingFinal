package com.example.onboarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.onboarding.authentication.LoginActivity;
import com.example.onboarding.model.User;
import com.example.onboarding.model.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class WorkingActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;

    private TextView name,phoneNumber;

    private Button logoutButton;

    private FirebaseAuth mAuth;

    private String uid;

    private User user;

    UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working);
        name = findViewById(R.id.name_edittext);
        phoneNumber = findViewById(R.id.phonenumber_edittext);

        logoutButton = findViewById(R.id.logout);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

//        name.setText(getIntent().getStringExtra("Name"));
//        phoneNumber.setText(getIntent().getStringExtra("Phonenumber"));


      mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        if(!uid.isEmpty()){
            getUserData();
        }




        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mAuth.signOut();
               startActivity(new Intent(WorkingActivity.this, LoginActivity.class));
               finish();
            }
        });



    }

    private void getUserData() {
        mDatabaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                assert user != null;
                if(user != null){
                    name.setText("Name: "+user.getName());
                    phoneNumber.setText("Phone Number: "+user.getPhoneNumber());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}