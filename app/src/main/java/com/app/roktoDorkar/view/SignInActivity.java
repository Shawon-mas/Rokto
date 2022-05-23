package com.app.roktoDorkar.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.app.roktoDorkar.MainActivity;
import com.app.roktoDorkar.R;
import com.app.roktoDorkar.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        setListeners();
    }

    private void setListeners() {

        binding.createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
        binding.signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                binding.signInIndicator.setVisibility(View.VISIBLE);
                String email = binding.signIneditTextEmail.getText().toString().trim();
                String pass = binding.signIneditTextPassword.getText().toString().trim();
                if (email.isEmpty()) {
                    binding.signIneditTextEmail.setError("Enter your email");
                    binding.signIneditTextEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.signIneditTextEmail.setError("Invalid Email Address");
                    binding.signIneditTextEmail.requestFocus();
                    return;
                }
                if (pass.isEmpty()) {
                    binding.signIneditTextPassword.setError("Enter your email");
                    binding.signIneditTextPassword.requestFocus();
                    return;
                }
                userSignIn(email, pass);
            }
        });
    }

    private void userSignIn(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("message", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user.isEmailVerified())
                            {
                                binding.signInIndicator.setVisibility(View.GONE);
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            }else {
                                binding.signInIndicator.setVisibility(View.GONE);
                                user.sendEmailVerification();
                                Toast.makeText(SignInActivity.this, "Check your email/spam to verify your account",
                                        Toast.LENGTH_SHORT).show();
                            }

                            //  updateUI(user);
                        } else {
                            binding.signInIndicator.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            Log.w("message", "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Your account does not exist",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("message", e.toString());
                        Toast.makeText(SignInActivity.this, e.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null)
        {
            if (user.isEmailVerified())
            {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            }else {
                Log.d("Message:","User not verified");
            }
        } else {

           /* user.sendEmailVerification();
            Toast.makeText(SignInActivity.this, "Check your email/spam to verify your account",
                    Toast.LENGTH_SHORT).show();*/
        }


    }
}