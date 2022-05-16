package com.app.roktoDorkar.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    ActivitySignUpBinding binding;
    String DonateBloodType;
    String[] donateBlood,bloodType;
    public static String val;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mRef = db.collection("UserProfile");
  //  private DocumentReference userRef = db.collection("UserProfile").document(mAuth.getCurrentUser().getEmail());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        donateBlood=getResources().getStringArray(R.array.donate_blood);
        bloodType=getResources().getStringArray(R.array.donate_type);
        birthDate();
        clickListeners();
        bloodSpinner();
        bloodTypeSpinner();

    }

    private void bloodTypeSpinner() {
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.signUpIndicator.setVisibility(View.VISIBLE);
                String name=binding.editTextName.getText().toString();
                String email=binding.editTextEmail.getText().toString();
                String number=binding.editTextNumber.getText().toString();
                String dob=binding.birthDate.getText().toString();
                String pass=binding.editTextPassword.getText().toString();
                String div=binding.editTextDiv.getText().toString();
                String dis=binding.editTextDis.getText().toString();
                String upz=binding.editTextUp.getText().toString();

                if (name.isEmpty())
                {
                    binding.editTextName.setError("Enter your name");
                    binding.editTextName.requestFocus();
                    return;
                }
                if (email.isEmpty())
                {
                    binding.editTextEmail.setError("Enter your email");
                    binding.editTextEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.editTextEmail.setError("Invalid Email Address");
                    binding.editTextEmail.requestFocus();
                    return;
                }

                if (number.isEmpty())
                {
                    binding.editTextNumber.setError("Enter your mobile number");
                    binding.editTextNumber.requestFocus();
                    return;
                }
                if (dob.isEmpty())
                {
                    binding.birthDate.setError("Enter your Date of Birth");
                    binding.birthDate.requestFocus();
                    return;
                }
                if (div.isEmpty())
                {
                    binding.editTextDiv.setError("Enter your Division");
                    binding.editTextDiv.requestFocus();
                    return;
                }
                if (dis.isEmpty())
                {
                    binding.editTextDis.setError("Enter your District");
                    binding.editTextDis.requestFocus();
                    return;
                }
                if (upz.isEmpty())
                {
                    binding.editTextUp.setError("Enter your Upzilla");
                    binding.editTextUp.requestFocus();
                    return;
                }

                if (pass.isEmpty())
                {
                    binding.editTextPassword.setError("Enter your password");
                    binding.editTextPassword.requestFocus();
                    return;
                }
                if (pass.length()<6)
                {
                    binding.editTextPassword.setError("Password should be 6 digit");
                    binding.editTextPassword.requestFocus();
                    return;
                }
                createAccount(email,pass);


               // checkFieldValid(name,email,number);

              //  Log.d("Type:",type);
                Log.d("Age:",val);
            }
        });
        binding.signInBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createAccount(String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.signUpIndicator.setVisibility(View.GONE);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Report", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user,email);
                        } else {
                            binding.signUpIndicator.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            Log.w("Report", "createUserWithEmail:failure", task.getException());
                            /*Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();*/
                           // updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user, String email) {

       /* //String email=binding.editTextEmail.getText().toString();
       // String number=binding.editTextNumber.getText().toString();
        String dob=binding.birthDate.getText().toString();
        String pass=binding.editTextPassword.getText().toString();
        String div=binding.editTextDiv.getText().toString();
        String dis=binding.editTextDis.getText().toString();
        String upz=binding.editTextUp.getText().toString();*/
        String bloodType= binding.bloodtypeSpinner.getSelectedItem().toString();
        Map<String, Object> usersInfo = new HashMap<>();
        usersInfo.put("userName",binding.editTextName.getText().toString());
        usersInfo.put("userEmail", email);
        usersInfo.put("userDob", binding.birthDate.getText().toString());
        usersInfo.put("userAge", binding.age.getText().toString());
        if (bloodType.isEmpty())
        {
            usersInfo.put("bloodType","Not Selected");
        }else {
            usersInfo.put("bloodType",bloodType);
        }
        usersInfo.put("uId", user.getUid());
        usersInfo.put("division", binding.editTextDiv.getText().toString());
        usersInfo.put("district", binding.editTextDis.getText().toString());
        usersInfo.put("upzilla", binding.editTextUp.getText().toString());
        db.collection("UserProfile").document(email).set(usersInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void unused)
           {
               Toast.makeText(getApplicationContext(),"Data Saved",Toast.LENGTH_SHORT).show();
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

           }
       });


    }

    private void bloodSpinner() {
        ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(),R.layout.spinner_view,R.id.spiner_textview,donateBlood);
        binding.spinnerdonateBlood.setAdapter(adapter);
        binding.spinnerdonateBlood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //Toast.makeText(getApplicationContext(),"Type: "+donateBlood[0],Toast.LENGTH_SHORT).show();

                        binding.bloodtypeSpinner.setVisibility(View.GONE);
                        break;
                    case 1:
                        // Toast.makeText(getApplicationContext(),"Type: "+donateBlood[1],Toast.LENGTH_SHORT).show();

                        binding.bloodtypeSpinner.setVisibility(View.VISIBLE);

                        break;
                    case 2:
                        //Toast.makeText(getApplicationContext(),"Type: "+donateBlood[2],Toast.LENGTH_SHORT).show();

                        binding.bloodtypeSpinner.setVisibility(View.GONE);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"Please Select",Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void birthDate() {
        binding.birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String currentYear= String.valueOf(year);
                        String date = day + "/" + month + "/" + year;
                        binding.birthDate.setText(date);

                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                        String formattedDate = sdf.format(d);
                        int currentY=Integer.parseInt(formattedDate)-Integer.parseInt(currentYear);
                         val= String.valueOf(currentY);



                        if (Integer.valueOf(val)>= 18)
                        {
                            binding.age.setText(val);
                            binding.spinnerdonateBlood.setVisibility(View.VISIBLE);
                        }else
                        {
                            binding.age.setText(val);
                            binding.spinnerdonateBlood.setVisibility(View.GONE);
                            binding.bloodtypeSpinner.setVisibility(View.GONE);

                        }


                    }
                }, year, month, day);
                datePickerDialog.show();
                //   binding.age.setText(currentYear);
            }
        });
    }

    private void clickListeners() {

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
           // reload();
        }
    }
}