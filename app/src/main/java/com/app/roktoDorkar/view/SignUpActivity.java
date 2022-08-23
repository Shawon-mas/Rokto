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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.api.upazilaApi.ApiInstance;
import com.app.roktoDorkar.api.upazilaApi.DistrictAdapter;
import com.app.roktoDorkar.api.upazilaApi.DistrictClick;
import com.app.roktoDorkar.api.upazilaApi.DistrictModel;
import com.app.roktoDorkar.api.upazilaApi.DivisionClick;
import com.app.roktoDorkar.api.upazilaApi.DivisionAdapter;
import com.app.roktoDorkar.api.upazilaApi.DivisionModel;
import com.app.roktoDorkar.databinding.ActivitySignUpBinding;
import com.app.roktoDorkar.utilites.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements DivisionClick, DistrictClick {
    private DatePickerDialog datePickerDialog;
    private PreferenceManager preferenceManager;
    ActivitySignUpBinding binding;
    String DonateBloodType;
    private String encodedImage;
    String[] donateBlood,bloodType;
    public static String val;
    private FirebaseAuth mAuth;
    Integer division_id,district_id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Dialog dialog,dialog_dis;
    private ArrayList<DivisionModel.Division> divisionModelArrayList;
    private ArrayList<DivisionModel.Division> filterDivisionList;

    private ArrayList<DistrictModel.District> districtsModelArrayList;
    private ArrayList<DistrictModel.District> filterDistrictList;
    private DivisionAdapter adapter;
    private DistrictAdapter districtAdapter;
    private boolean passwordShowing = false;
    private boolean isDivision=false;
    private boolean isDistrict=false;
    private boolean isUpazila=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        preferenceManager=new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        donateBlood=getResources().getStringArray(R.array.donate_blood);
        bloodType=getResources().getStringArray(R.array.donate_type);
        birthDate();
        clickListeners();
        bloodSpinner();
        bloodTypeSpinner();

    }
   private void showErrorToast(String message)
   {
       Toasty.error(getApplicationContext(),message,Toasty.LENGTH_LONG).show();
   }
    private void showSuccessToast(String message)
    {
        Toasty.success(getApplicationContext(),message,Toasty.LENGTH_SHORT).show();
    }
    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.signUpButton.setVisibility(View.INVISIBLE);
            binding.signUpProgressbar.setVisibility(View.VISIBLE);
        } else {
            binding.signUpButton.setVisibility(View.VISIBLE);
            binding.signUpProgressbar.setVisibility(View.INVISIBLE);
        }
    }
    private void bloodTypeSpinner() {
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                String name=binding.editTextName.getText().toString();
                String email=binding.editTextEmail.getText().toString();
                String number=binding.editTextNumber.getText().toString();
                String dob=binding.birthDate.getText().toString();
                String pass=binding.editTextPassword.getText().toString();
                String div=binding.editTextDiv.getText().toString();
                String dis=binding.editTextDis.getText().toString();
                String upz=binding.editTextUp.getText().toString();
                String about=binding.editTextAbout.getText().toString();

                if (encodedImage==null)
                {
                    showErrorToast("Select profile image");
                    return;
                }

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
                if (about.isEmpty())
                {
                    binding.editTextAbout.setError("Write your self in short");
                    binding.editTextAbout.requestFocus();
                    return;
                }else {
                    createAccount(email,pass);
                }



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
        loading(true);
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Message", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user,email);
                        } else {

                            loading(false);
                            showErrorToast(task.getException().toString());
                            //Log.w("Report", "createUserWithEmail:failure", task.getException());

                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user, String email) {

        String bloodType= binding.bloodtypeSpinner.getSelectedItem().toString();
        Map<String, Object> usersInfo = new HashMap<>();
        usersInfo.put(KEY_IMAGE_URI, encodedImage);
        usersInfo.put(KEY_NAME,binding.editTextName.getText().toString());
        usersInfo.put(KEY_EMAIL, email);
        usersInfo.put(KEY_NUMBER,binding.editTextNumber.getText().toString());
        usersInfo.put(KEY_DOB, binding.birthDate.getText().toString());
        usersInfo.put(KEY_AGE, binding.age.getText().toString());
        if (bloodType.isEmpty())
        {
            usersInfo.put("bloodType","Not Selected");
        }else {
            usersInfo.put(KEY_BLOODTYPE,bloodType);
        }
        usersInfo.put(KEY_UID, user.getUid());
        usersInfo.put(KEY_DIVISION, binding.editTextDiv.getText().toString());
        usersInfo.put( KEY_DISTRICT, binding.editTextDis.getText().toString());
        usersInfo.put(KEY_UPZILA, binding.editTextUp.getText().toString());
        usersInfo.put(KEY_ABOUT,binding.editTextAbout.getText().toString());
        db.collection(KEY_COLLECTION_USERS).document(email).set(usersInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void unused)
           {

               showSuccessToast("Registration Completed");
               Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
               startActivity(intent);
               finish();
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
                        binding.bloodtypeSpinner.setVisibility(View.GONE);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"Please Select Text",Toast.LENGTH_SHORT).show();

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
    private String encodeImage(Bitmap bitmap)
    {
        int previewWidth=150;
        int previewHeight=bitmap.getHeight()*previewWidth/bitmap.getWidth();
        Bitmap previewBitmap=Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);

    }
    private final ActivityResultLauncher<Intent> pickImage=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode()==RESULT_OK)
                {
                    if (result.getData()!=null)
                    {
                        Uri imageUri=result.getData().getData();
                        try {
                            InputStream inputStream=getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                            binding.imageSignUp.setImageBitmap(bitmap);
                            binding.addImageText.setVisibility(View.GONE);
                            encodedImage=encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
    );

    private void clickListeners() {
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
        binding.editTextDiv.setOnClickListener(v -> {
            getDivision(true);
        });
        binding.editTextDis.setOnClickListener(v -> {
            if (division_id==null)
            {
                showErrorToast("Select Division");
                return;
            }else {
                setDistrict(true,division_id);
            }

        });
        binding.passIcon.setOnClickListener(v -> {
            if (passwordShowing) {
                passwordShowing = false;
                binding.editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binding.passIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pass_show));
            } else {

                passwordShowing = true;
                binding.editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                binding.passIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pass_hide));
            }
            binding.editTextPassword.setSelection(binding.editTextPassword.length());
        });

    }

    private void getDivision(Boolean isDivision) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.upzila_list);
        dialog.getWindow().setLayout(800, 1500);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        RecyclerView recyclerView = dialog.findViewById(R.id.upzila_listview);
        ProgressBar progressBar= dialog.findViewById(R.id.progressbar_upzilla);
        TextView textView=dialog.findViewById(R.id.setName);
        if (isDivision){
            textView.setText("Select Division");
        }
        /*EditText editText=dialog.findViewById(R.id.up_search);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              //  filter(s.toString());

            }
        });*/
        progressBar.setVisibility(View.VISIBLE);
        ImageView imageView=dialog.findViewById(R.id.up_close);
        imageView.setOnClickListener(v -> {
            dialog.cancel();
        });
        Call<DivisionModel> call= ApiInstance.getDivisionApiEndpoint().getDivision();
        call.enqueue(new Callback<DivisionModel>() {
            @Override
            public void onResponse(Call<DivisionModel> call, Response<DivisionModel> response) {
                if (response.isSuccessful()){
                    divisionModelArrayList=new ArrayList<>();
                    divisionModelArrayList=response.body().getDivision();
                    filterDivisionList=divisionModelArrayList;
                    for (DivisionModel.Division upazila:divisionModelArrayList)
                    {
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter=new DivisionAdapter(getApplicationContext(),divisionModelArrayList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        adapter.setOnItemClckListener(SignUpActivity.this);
                        progressBar.setVisibility(View.GONE);


                    }
                }
            }

            @Override
            public void onFailure(Call<DivisionModel> call, Throwable t) {
                showErrorToast("Something Went Wrong");
                dialog.cancel();

            }
        });

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

    @Override
    public void onDivisionItemClick(int position) {
         binding.editTextDiv.setText(divisionModelArrayList.get(position).getName());
        division_id=divisionModelArrayList.get(position).getId();
         dialog.cancel();
         showSuccessToast(division_id.toString());

    }

    private void setDistrict(Boolean isDistrict,Integer division_id) {
        dialog_dis = new Dialog(this);
        dialog_dis.setContentView(R.layout.upzila_list);
        dialog_dis.getWindow().setLayout(800, 1500);
        dialog_dis.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_dis.show();
        RecyclerView recyclerView = dialog_dis.findViewById(R.id.upzila_listview);
        ProgressBar progressBar= dialog_dis.findViewById(R.id.progressbar_upzilla);
        TextView textView=dialog_dis.findViewById(R.id.setName);
        if (isDistrict){
            textView.setText("Select District");
        }
        progressBar.setVisibility(View.VISIBLE);
        ImageView imageView=dialog_dis.findViewById(R.id.up_close);
        imageView.setOnClickListener(v -> {
            dialog_dis.cancel();
        });

        Call<DistrictModel> call=ApiInstance.getDivisionApiEndpoint().getDistrict(division_id);
        call.enqueue(new Callback<DistrictModel>() {
            @Override
            public void onResponse(Call<DistrictModel> call, Response<DistrictModel> response) {
                if (response.isSuccessful())
                {
                    districtsModelArrayList=new ArrayList<>();
                    districtsModelArrayList=response.body().getDistrict();

                    for (DistrictModel.District data:districtsModelArrayList)
                    {
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        districtAdapter=new DistrictAdapter(getApplicationContext(),districtsModelArrayList);
                        recyclerView.setAdapter(districtAdapter);
                        districtAdapter.notifyDataSetChanged();
                        districtAdapter.setOnItemClckListener(SignUpActivity.this);
                        progressBar.setVisibility(View.GONE);


                    }

                }
            }

            @Override
            public void onFailure(Call<DistrictModel> call, Throwable t)
            {
                showErrorToast("Something Went Wrong");

            }
        });

    }


    @Override
    public void onDistrictItemClick(int position) {
        binding.editTextDis.setText(districtsModelArrayList.get(position).getName());
        district_id=districtsModelArrayList.get(position).getId();
        dialog_dis.cancel();
    }
}