package com.example.onboarding.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.onboarding.R;
import com.example.onboarding.model.User;
import com.example.onboarding.WorkingActivity;
import com.example.onboarding.model.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 120;
    private Button signUpButton, signInButton;
    
    private ImageButton imageButton;

    private TextInputEditText name,phoneNumber,email,password,confirmPassword;

    private FirebaseAuth mAuth;

    private UserViewModel userViewModel;

    private GoogleSignInClient mGoogleSignInClient;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        imageButton = findViewById(R.id.google_signup_button);

        signUpButton = findViewById(R.id.signUp_button);
        signInButton = findViewById(R.id.signup_signin);
        name = findViewById(R.id.signup_name);
        phoneNumber = findViewById(R.id.signup_phonenumber);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_confirmpassword);

        mAuth = FirebaseAuth.getInstance();

        userViewModel = new ViewModelProvider(this)
                        .get(UserViewModel.class);

        firebaseUser = mAuth.getCurrentUser();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpWithEmail();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });
        
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
                signIn();
            }
        });

    }

    private void signInWithGoogle() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("540041530557-nmk0588m43iimhudvf191ngioeloij9g.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            if(task.isSuccessful()){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d("Main", "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.d("Main", "Google sign in failed", e);
                }
            }else{
                Log.d("Main", "Google sign in failed", task.getException());
            }
        }
    }

    //here we should change the logic
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            final String userId = firebaseUser.getUid();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                            reference.child(userId)
                                    .get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        Intent intent = new Intent(SignUpActivity.this,WorkingActivity.class);

                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Intent googleUserIntent = new Intent(SignUpActivity.this, GoogleSignInUserData.class);
                                        startActivity(googleUserIntent);
                                        finish();
                                    }
                                }
                            });




                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Main", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Sign in failed",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }



    private void signUpWithEmail(){

        String nameText = name.getText().toString().trim();
        String phoneNumberText = phoneNumber.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String passwordText  = password.getText().toString();
        String confirmPasswordText = confirmPassword.getText().toString();

        if(nameText.isEmpty()){
           name.setError("Name required");
           name.requestFocus();
           return;
        }
        else if(phoneNumberText.isEmpty()){
            phoneNumber.setError("Phone number required");
            phoneNumber.requestFocus();
            return;
        }else if(emailText.isEmpty()){
            email.setError("Email required");
            email.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            email.setError("Enter correct email");
            email.requestFocus();
            return;
        }

        else if(passwordText.isEmpty()){
            password.setError("Password required");
            password.requestFocus();
            return;
        }else if(passwordText.length() < 6){
            password.setError("Password should have minimum 6 characters");
            password.requestFocus();
            return;
        } else if(confirmPasswordText.isEmpty()){
            confirmPassword.setError("Required");
            confirmPassword.requestFocus();
            return;
        }else if(!confirmPasswordText.equals(passwordText)){
            confirmPassword.setError("Password not matches");
            confirmPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                                User user = new User(nameText,phoneNumberText);

                                //Setting data to real time database

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            Toast.makeText(SignUpActivity.this,
                                                    "Registration successful", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(SignUpActivity.this,WorkingActivity.class));
                                            finish();

                                        }else{
                                            Toast.makeText(SignUpActivity.this,
                                                    "Registration failed in database", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                        }else{
                            Toast.makeText(SignUpActivity.this,
                                    "Registration failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
   }


}