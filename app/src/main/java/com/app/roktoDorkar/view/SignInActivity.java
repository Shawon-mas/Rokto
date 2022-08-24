package com.app.roktoDorkar.view;

import static com.app.roktoDorkar.utilites.Constants.KEY_ABOUT;
import static com.app.roktoDorkar.utilites.Constants.KEY_AGE;
import static com.app.roktoDorkar.utilites.Constants.KEY_BLOODTYPE;
import static com.app.roktoDorkar.utilites.Constants.KEY_COLLECTION_USERS;
import static com.app.roktoDorkar.utilites.Constants.KEY_DISTRICT;
import static com.app.roktoDorkar.utilites.Constants.KEY_DIVISION;
import static com.app.roktoDorkar.utilites.Constants.KEY_DOB;
import static com.app.roktoDorkar.utilites.Constants.KEY_EMAIL;
import static com.app.roktoDorkar.utilites.Constants.KEY_IMAGE_URI;
import static com.app.roktoDorkar.utilites.Constants.KEY_NAME;
import static com.app.roktoDorkar.utilites.Constants.KEY_NUMBER;
import static com.app.roktoDorkar.utilites.Constants.KEY_UID;
import static com.app.roktoDorkar.utilites.Constants.KEY_UPZILA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.databinding.ActivitySignInBinding;
import com.app.roktoDorkar.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    private FirebaseAuth mAuth;
    private PreferenceManager preferenceManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean passwordShowing = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        preferenceManager=new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        setListeners();
    }
    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.signInButton.setVisibility(View.INVISIBLE);
            binding.signInProgressbar.setVisibility(View.VISIBLE);
        } else {
            binding.signInButton.setVisibility(View.VISIBLE);
            binding.signInProgressbar.setVisibility(View.INVISIBLE);
        }
    }

    private void setListeners() {
         binding.passIconSignIn.setOnClickListener(v -> {
             if (passwordShowing) {
                 passwordShowing = false;
                 binding.signIneditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                 binding.passIconSignIn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pass_show));
             } else {

                 passwordShowing = true;
                 binding.signIneditTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                 binding.passIconSignIn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pass_hide));
             }
             binding.signIneditTextPassword.setSelection(binding.signIneditTextPassword.length());
         });
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
                }else {
                    userSignIn(email, pass);
                    loading(true);
                }

            }
        });
    }

    private void userSignIn(String email, String pass) {
        db.collection(KEY_COLLECTION_USERS).whereEqualTo(KEY_EMAIL,binding.signIneditTextEmail.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() !=null
                            && task.getResult().getDocuments().size()>0 )
                    {
                        DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                        preferenceManager.putString(KEY_UID,documentSnapshot.getString(KEY_UID));
                        preferenceManager.putString(KEY_NAME,documentSnapshot.getString(KEY_NAME));
                        preferenceManager.putString(KEY_NUMBER,documentSnapshot.getString(KEY_NUMBER));
                        preferenceManager.putString(KEY_EMAIL,documentSnapshot.getString(KEY_EMAIL));
                        preferenceManager.putString(KEY_DOB,documentSnapshot.getString(KEY_DOB));
                        preferenceManager.putString(KEY_AGE,documentSnapshot.getString(KEY_AGE));
                        preferenceManager.putString(KEY_DIVISION,documentSnapshot.getString(KEY_DIVISION));
                        preferenceManager.putString(KEY_DISTRICT,documentSnapshot.getString(KEY_DISTRICT));
                        preferenceManager.putString(KEY_UPZILA,documentSnapshot.getString(KEY_UPZILA));
                        preferenceManager.putString(KEY_BLOODTYPE,documentSnapshot.getString(KEY_BLOODTYPE));
                        preferenceManager.putString(KEY_ABOUT,documentSnapshot.getString(KEY_ABOUT));
                        preferenceManager.putString(KEY_IMAGE_URI,documentSnapshot.getString(KEY_IMAGE_URI));
                        mAuth.signInWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            FirebaseUser user = mAuth.getCurrentUser();
                                            if (user.isEmailVerified())
                                            {

                                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                                            }else {
                                                loading(false);
                                                user.sendEmailVerification();
                                                Toast.makeText(SignInActivity.this, "Check your email/spam to verify your account",
                                                        Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                        else {
                                            loading(false);
                                            Log.w("message", "signInWithEmail:failure", task.getException());
                                            Toast.makeText(SignInActivity.this, "Wrong credentials",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loading(false);
                                        Toast.makeText(SignInActivity.this, "Wrong ",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });



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

        }


    }
}