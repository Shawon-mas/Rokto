package com.app.roktoDorkar.view.bottomViewActivites;

import static com.app.roktoDorkar.global.SharedPref.USER_NAME;
import static com.app.roktoDorkar.view.DonarsListActivity.type;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.databinding.ActivityBloodReqBinding;
import com.app.roktoDorkar.databinding.ActivityRequestBinding;
import com.app.roktoDorkar.model.DateValidatorWeekdays;
import com.app.roktoDorkar.view.DonarsListActivity;
import com.app.roktoDorkar.view.HomeActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import es.dmoral.toasty.Toasty;

public class BloodReqActivity extends AppCompatActivity {
    private ActivityBloodReqBinding binding;
    private MaterialDatePicker materialDatePicker;
    private MaterialTimePicker materialTimePicker;
    int mHour,mMinute;
    private String type,requiredBlood,patientGender,patientType;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @TimeFormat private int clockFormat;
    private String request_status="not_accept";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBloodReqBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        bottomNav();
        clickListener();
    }

    private void initViews() {
        clockFormat = TimeFormat.CLOCK_12H;
        binding.requestMakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chckeValidator();
            }
        });


    }

    private void chckeValidator() {
        if (binding.reqDate.getText().toString().isEmpty())
        {
            binding.reqDate.setError("Select Date");
            binding.reqDate.requestFocus();
        }
        if (binding.reqTime.getText().toString().isEmpty())
        {
            binding.reqTime.setError("Select time");
            binding.reqTime.requestFocus();
        }
        if (binding.reqUpazila.getText().toString().isEmpty())
        {
            binding.reqUpazila.setError("Enter Upazila");
            binding.reqUpazila.requestFocus();
        }
        if (binding.reqNumber.getText().toString().isEmpty())
        {
            binding.reqNumber.setError("Enter Your Number");
            binding.reqNumber.requestFocus();
        }
        if (binding.reqLocation.getText().toString().isEmpty())
        {
            binding.reqLocation.setError("Enter Your Current Location");
            binding.reqLocation.requestFocus();
        }


        if (binding.description.getText().toString().isEmpty())
        {
            binding.description.setError("Write Description");
            binding.description.requestFocus();
        }
        submitRequest();
    }

    private void submitRequest()
    {
        type = binding.reqBloodType.getSelectedItem().toString();
        requiredBlood = binding.reqBloodBag.getSelectedItem().toString();
        patientGender = binding.bloodneedsGender.getSelectedItem().toString();
        patientType = binding.patientType.getSelectedItem().toString();
        db.collection("UserProfile").whereEqualTo("bloodType",type )
                .whereEqualTo("upzilla", binding.reqUpazila.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                          makeRequest();
                        }
                        if (task.getResult().size() == 0) {

                            Toast.makeText(BloodReqActivity.this, "There is no donor right now your location", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });




    }

    private void makeRequest() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_APP", MODE_PRIVATE);
        String senderName = preferences.getString(USER_NAME, null);
        String di=db.collection("BloodRequest").document().getId();
        Map<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("senderName",senderName);
        requestInfo.put("senderEmail",FirebaseAuth.getInstance().getCurrentUser().getEmail());
        requestInfo.put("senderUid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        requestInfo.put("senderRequiredBlood",type);
        requestInfo.put("senderRequiredQuantity",requiredBlood);
        requestInfo.put("senderPatientGender",patientGender);
        requestInfo.put("senderRequestForDate",binding.reqDate.getText().toString());
        requestInfo.put("senderRequestForTime",binding.reqTime.getText().toString());
        requestInfo.put("senderRequestUpazila",binding.reqUpazila.getText().toString());
        requestInfo.put("senderPhoneNumber",binding.reqNumber.getText().toString());
        requestInfo.put("senderRequestLocation",binding.reqLocation.getText().toString());
        requestInfo.put("senderGiftAmount",binding.gift.getText().toString());
        requestInfo.put("senderRequestDetails",binding.description.getText().toString());
        requestInfo.put("requestStatus",request_status);
        requestInfo.put("requestReceiverUid",null);
        requestInfo.put("requestReceiverName",null);


        db.collection("BloodRequest").
                document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(requestInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused)
                    {

                        Map<String, Object> requestMainInfo = new HashMap<>();
                        requestMainInfo.put("senderUid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        db.collection("BloodRequest").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("SenderUid")
                                .add(requestMainInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Map<String, Object> myRequestInfo = new HashMap<>();
                                        myRequestInfo.put("senderName",senderName);
                                        myRequestInfo.put("senderUid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        myRequestInfo.put("senderRequiredBlood",type);
                                        myRequestInfo.put("senderRequiredQuantity",requiredBlood);
                                        myRequestInfo.put("senderPatientGender",patientGender);
                                        myRequestInfo.put("senderRequestForDate",binding.reqDate.getText().toString());
                                        myRequestInfo.put("senderRequestForTime",binding.reqTime.getText().toString());
                                        myRequestInfo.put("senderRequestUpazila",binding.reqUpazila.getText().toString());
                                        myRequestInfo.put("senderPhoneNumber",binding.reqNumber.getText().toString());
                                        myRequestInfo.put("senderRequestLocation",binding.reqLocation.getText().toString());
                                        myRequestInfo.put("senderGiftAmount",binding.gift.getText().toString());
                                        myRequestInfo.put("senderRequestDetails",binding.description.getText().toString());
                                        myRequestInfo.put("requestStatus",request_status);
                                        myRequestInfo.put("requestReceiverUid",null);
                                        myRequestInfo.put("requestReceiverName",null);

                                        db.collection("UserProfile").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                                .collection("BloodRequestPortal").document("Request_Type")
                                                .collection("Sent_Request").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .set(myRequestInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused)
                                                    {
                                                        Toasty.success(getApplicationContext(),"Request Sent",Toasty.LENGTH_SHORT,false).show();

                                                    }
                                                });
                                    }
                                });
                    }
                });







    }

    private void clickListener() {
        selectDate();
        binding.reqTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime();
            }
        });


    }

    private void selectTime() {
        /*Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);*/
        TimePickerDialog timePickerDialog=new TimePickerDialog(
                BloodReqActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                mHour=hourOfDay;
                mMinute=minute;
                calendar.set(0,0,0,mHour,mMinute);

                binding.reqTime.setText(android.text.format.DateFormat.format("hh:mm aa",calendar));

            }
        },12,0,false
        );
        timePickerDialog.updateTime(mHour,mMinute);
        timePickerDialog.setTitle("Select Required date");
        timePickerDialog.show();

    }

    private void selectDate() {
        Calendar calendar =Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(new DateValidatorWeekdays());
        long today=MaterialDatePicker.todayInUtcMilliseconds();

        MaterialDatePicker.Builder builder=MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        builder.setTheme(R.style.MaterialCalendarTheme);
        builder.setSelection(today);
        materialDatePicker=builder.build();

        binding.reqDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(),"Date_Picke");

            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                binding.reqDate.setText(materialDatePicker.getHeaderText());
            }
        });
    }

    private void bottomNav() {
        binding.bottomNavBloodReq.add(new MeowBottomNavigation.Model(1,R.drawable.search_donar));
        binding.bottomNavBloodReq.add(new MeowBottomNavigation.Model(2,R.drawable.request));
        binding.bottomNavBloodReq.add(new MeowBottomNavigation.Model(3,R.drawable.add));
        binding.bottomNavBloodReq.add(new MeowBottomNavigation.Model(4,R.drawable.history));
        binding.bottomNavBloodReq.add(new MeowBottomNavigation.Model(5,R.drawable.account));
        binding.bottomNavBloodReq.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId())
                {
                    case 1:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), RequestActivity.class));
                        overridePendingTransition(0, 0);
                        return;
                    case 3:

                        overridePendingTransition(0, 0);
                        return;
                    case 4:
                        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                        overridePendingTransition(0, 0);
                        return;
                    case 5:
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0, 0);
                        return;
                }


            }
        });
        binding.bottomNavBloodReq.show(3,true);
        binding.bottomNavBloodReq.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item)
            {
            }
        });
    }
    @Override
    public void onBackPressed() {
        binding.bottomNavBloodReq.show(1,true);
        super.onBackPressed();
    }
}