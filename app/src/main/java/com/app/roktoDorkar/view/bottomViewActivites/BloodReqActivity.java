package com.app.roktoDorkar.view.bottomViewActivites;

import static com.app.roktoDorkar.utilites.Constants.KEY_BLOODTYPE;
import static com.app.roktoDorkar.utilites.Constants.KEY_COLLECTION_USERS;
import static com.app.roktoDorkar.utilites.Constants.KEY_FCM_TOKEN;
import static com.app.roktoDorkar.utilites.Constants.KEY_IMAGE_URI;
import static com.app.roktoDorkar.utilites.Constants.KEY_NAME;
import static com.app.roktoDorkar.utilites.Constants.KEY_NUMBER;
import static com.app.roktoDorkar.utilites.Constants.KEY_REQBLOOD;
import static com.app.roktoDorkar.utilites.Constants.KEY_REQUPTHANA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.api.upazilaApi.ApiInstance;
import com.app.roktoDorkar.api.upazilaApi.DistrictAdapter;
import com.app.roktoDorkar.api.upazilaApi.DistrictClick;
import com.app.roktoDorkar.api.upazilaApi.DistrictModel;
import com.app.roktoDorkar.api.upazilaApi.DivisionClick;
import com.app.roktoDorkar.api.upazilaApi.DivisionAdapter;
import com.app.roktoDorkar.api.upazilaApi.DivisionModel;
import com.app.roktoDorkar.api.upazilaApi.ThanaAdapter;
import com.app.roktoDorkar.api.upazilaApi.ThanaClick;
import com.app.roktoDorkar.api.upazilaApi.ThanaModel;
import com.app.roktoDorkar.databinding.ActivityBloodReqBinding;
import com.app.roktoDorkar.model.DateValidatorWeekdays;
import com.app.roktoDorkar.utilites.PreferenceManager;
import com.app.roktoDorkar.view.HomeActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BloodReqActivity extends AppCompatActivity implements DivisionClick, DistrictClick, ThanaClick {
    private PreferenceManager preferenceManager;
    private ActivityBloodReqBinding binding;
    private MaterialDatePicker materialDatePicker;
    private MaterialTimePicker materialTimePicker;
    int mHour,mMinute;
    private String type,requiredBlood,patientGender,patientType;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @TimeFormat private int clockFormat;
    private String request_status="not_accept";
    DocumentReference ref = db.collection("BloodRequest").document();
    Integer division_id,district_id;
    private Dialog dialog,dialog_dis,dialog_thana;
    private ArrayList<DivisionModel.Division> divisionModelArrayList;
    private ArrayList<DistrictModel.District> districtsModelArrayList;
    private ArrayList<ThanaModel.UpThana> upThanaArrayList;
    private DivisionAdapter adapter;
    private DistrictAdapter districtAdapter;
    private ThanaAdapter thanaAdapter;
    private boolean passwordShowing = false;
    private final boolean isDivision=false;
    private final boolean isDistrict=false;
    private final boolean isUpazila=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBloodReqBinding.inflate(getLayoutInflater());
        preferenceManager=new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        selectDate();
        getBloodType();
        getBloodAmount();
        getPatientGender();
        getPatientType();
        initViews();
        bottomNav();
        clickListener();
    }
    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.requestMakeButton.setVisibility(View.INVISIBLE);
            binding.reqProgressbar.setVisibility(View.VISIBLE);
        } else {
            binding.requestMakeButton.setVisibility(View.VISIBLE);
            binding.reqProgressbar.setVisibility(View.INVISIBLE);
        }
    }

    private void initViews() {


        clockFormat = TimeFormat.CLOCK_12H;
        binding.requestMakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (checkValidator())
               {
                   submitRequest();
                   loading(true);
               }
            }
        });
         binding.reqNumber.setText(preferenceManager.getString(KEY_NUMBER));

    }
    private void showErrorToast(String message)
    {
        Toasty.error(getApplicationContext(),message,Toasty.LENGTH_LONG).show();
    }

    private Boolean checkValidator() {
        if (binding.reqBloodGroup.getText().toString().isEmpty())
        {
            showErrorToast("Select blood group");
            return false;
        }else if (binding.reqBloodAmount.getText().toString().isEmpty()){
            showErrorToast("Select  blood amount");
            return false;
        }else if (binding.reqDate.getText().toString().isEmpty()) {
            showErrorToast("Select  Date");
            return false;
        }else if (binding.reqTime.getText().toString().isEmpty()) {
            showErrorToast("Select  Time");
            return false;
        }else if (binding.includeBloodReq.editTextDiv.getText().toString().isEmpty()) {
            showErrorToast("Select  division");
            return false;
        }else if (binding.includeBloodReq.editTextDis.getText().toString().isEmpty()) {
            showErrorToast("Select  district");
            return false;
        }else if (binding.includeBloodReq.editTextUp.getText().toString().isEmpty()) {
            showErrorToast("Select  upazila/thana");
            return false;
        }

        else if (binding.reqNumber.getText().toString().isEmpty()) {
            showErrorToast("Enter phone number");
            return false;
        }else if (binding.reqLocation.getText().toString().isEmpty()) {
            showErrorToast("Enter hospital name");
            return false;
        }else if (binding.reqBloodGender.getText().toString().isEmpty()) {
            showErrorToast("Select patient gender");
            return false;
        }else if (binding.reqBloodPatientType.getText().toString().isEmpty()) {
            showErrorToast("Select patient type");
            return false;
        }else if (Integer.parseInt(binding.gift.getText().toString())<1000) {
            showErrorToast("Gift Amount will be below 1000");
            return false;
        }
        else if (binding.description.getText().toString().isEmpty()) {
            showErrorToast("Write some details");
            return false;
        }else {
            return true;
        }




    }

    private void submitRequest()
    {


        db.collection(KEY_COLLECTION_USERS)
                .whereEqualTo("bloodType",type )
                .whereEqualTo("upazila", binding.includeBloodReq.editTextUp.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            if (task.getResult().size() == 0) {
                                loading(false);

                                Toast.makeText(BloodReqActivity.this, "There is no donor right now your location", Toast.LENGTH_SHORT).show();
                            }else {
                                makeRequest();
                            }

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showErrorToast("Something went wrong");
                        loading(false);
                    }
                });




    }

    private void makeRequest() {


        String dI=ref.getId();
        Map<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("senderName",preferenceManager.getString(KEY_NAME));
        requestInfo.put("senderEmail",FirebaseAuth.getInstance().getCurrentUser().getEmail());
        requestInfo.put("senderUid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        requestInfo.put("senderToken", preferenceManager.getString(KEY_FCM_TOKEN));
        requestInfo.put("senderRequiredBlood",type);
        requestInfo.put("senderRequiredQuantity",requiredBlood);
        requestInfo.put("senderPatientGender",patientGender);
        requestInfo.put("senderPatientType",patientType);
        requestInfo.put("senderRequestForDate",binding.reqDate.getText().toString());
        requestInfo.put("senderRequestForTime",binding.reqTime.getText().toString());
        requestInfo.put("senderRequestUpazila",binding.includeBloodReq.editTextUp.getText().toString());
        requestInfo.put("senderRequestDivision",binding.includeBloodReq.editTextDiv.getText().toString());
        requestInfo.put("senderRequestDistrict",binding.includeBloodReq.editTextDis.getText().toString());
        requestInfo.put("senderPhoneNumber",binding.reqNumber.getText().toString());
        requestInfo.put("senderRequestLocation",binding.reqLocation.getText().toString());
        requestInfo.put("senderGiftAmount",binding.gift.getText().toString());
        requestInfo.put("senderRequestDetails",binding.description.getText().toString());
        requestInfo.put("requestStatus",request_status);
        requestInfo.put("requestReceiverUid",null);
        requestInfo.put("requestReceiverName",null);
        requestInfo.put("documentId",dI);
        requestInfo.put("reqtime", FieldValue.serverTimestamp());
        requestInfo.put("zSenderImage",preferenceManager.getString(KEY_IMAGE_URI));


       ref.set(requestInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void unused)
           {
               saveToProfile();
           }
       });
    }

    private void saveToProfile() {
        String dI1=ref.getId();
        Map<String, Object> myRequestInfo = new HashMap<>();

        myRequestInfo.put("senderRequiredBlood",type);
        myRequestInfo.put("senderRequiredQuantity",requiredBlood);
        myRequestInfo.put("senderPatientGender",patientGender);
        myRequestInfo.put("senderPatientType",patientType);
        myRequestInfo.put("senderRequestForDate",binding.reqDate.getText().toString());
        myRequestInfo.put("senderRequestForTime",binding.reqTime.getText().toString());
        myRequestInfo.put("senderRequestUpazila",binding.includeBloodReq.editTextUp.getText().toString());
        myRequestInfo.put("senderPhoneNumber",binding.reqNumber.getText().toString());
        myRequestInfo.put("senderRequestLocation",binding.reqLocation.getText().toString());
        myRequestInfo.put("senderGiftAmount",binding.gift.getText().toString());
        myRequestInfo.put("senderRequestDetails",binding.description.getText().toString());
        myRequestInfo.put("requestStatus",request_status);
        myRequestInfo.put("requestReceiverUid",null);
        myRequestInfo.put("requestReceiverName",null);
        myRequestInfo.put("documentId",dI1);
        myRequestInfo.put("reqtime", FieldValue.serverTimestamp());

        DocumentReference ref2 = db.collection(KEY_COLLECTION_USERS).document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("MyBloodRequest").document(dI1);
        ref2.set(myRequestInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused)
            {
                preferenceManager.putString(KEY_REQBLOOD,binding.reqBloodGroup.getText().toString());
                preferenceManager.putString(KEY_REQUPTHANA,binding.includeBloodReq.editTextUp.getText().toString());
              showSuccessToast("Request Sent");
              binding.bottomNavBloodReq.show(2,true);

            }
        });


    }

    private void clickListener() {

        binding.includeBloodReq.editTextDiv.setOnClickListener(v -> {
            getDivision(true);
        });
        binding.includeBloodReq.editTextDis.setOnClickListener(v -> {
            if (division_id==null)
            {
                showErrorToast("Select Division");
                return;
            }else {
                setDistrict(true,division_id);
            }
        });
        binding.includeBloodReq.editTextUp.setOnClickListener(v -> {
            if (district_id==null)
            {
                showErrorToast("Select District");
                return;
            }else {
                setThana(true,district_id);
            }
        });

        binding.reqTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime();
            }
        });



    }

    private void setThana(boolean isUpazila, Integer district_id) {
        dialog_thana = new Dialog(this);
        dialog_thana.setContentView(R.layout.upzila_list);
        dialog_thana.getWindow().setLayout(800, 1500);
        dialog_thana.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_thana.show();
        RecyclerView recyclerView = dialog_thana.findViewById(R.id.upzila_listview);
        ProgressBar progressBar= dialog_thana.findViewById(R.id.progressbar_upzilla);
        TextView textView=dialog_thana.findViewById(R.id.setName);
        if (isUpazila){
            textView.setText("Select Upazila/Thana");
        }
        progressBar.setVisibility(View.VISIBLE);
        ImageView imageView=dialog_thana.findViewById(R.id.up_close);
        imageView.setOnClickListener(v -> {
            dialog_thana.cancel();
        });
        Call<ThanaModel> call=ApiInstance.getDivisionApiEndpoint().getThana(division_id,district_id);
        call.enqueue(new Callback<ThanaModel>() {
            @Override
            public void onResponse(Call<ThanaModel> call, Response<ThanaModel> response) {
                if (response.isSuccessful()){
                    upThanaArrayList=new ArrayList<>();
                    upThanaArrayList=response.body().getUpThana();
                    for (ThanaModel.UpThana upThana:upThanaArrayList)
                    {
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        thanaAdapter=new ThanaAdapter(getApplicationContext(),upThanaArrayList);
                        recyclerView.setAdapter(thanaAdapter);
                        thanaAdapter.notifyDataSetChanged();
                        thanaAdapter.setOnItemClckListener(BloodReqActivity.this);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ThanaModel> call, Throwable t) {

            }
        });

    }

    private void setDistrict(boolean isDistrict, Integer division_id) {
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
                        districtAdapter.setOnItemClckListener(BloodReqActivity.this);
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

    private void getDivision(boolean isDivision) {
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
                    for (DivisionModel.Division upazila:divisionModelArrayList)
                    {
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter=new DivisionAdapter(getApplicationContext(),divisionModelArrayList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        adapter.setOnItemClckListener(BloodReqActivity.this);
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


    private void getBloodType()
    {
        String[] bloodGroup=new String[] {"A+","A-","B+","B-","O+","O-","AB+","AB-"};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(
                this,
                R.layout.drorp_down_item,
                bloodGroup
        );
        binding.reqBloodGroup.setAdapter(arrayAdapter);
        binding.reqBloodGroup.setOnItemClickListener((parent, view, position, id) -> {
            type=binding.reqBloodGroup.getText().toString();
            //Toast.makeText(getApplicationContext(),binding.filledExposed.getText().toString(),Toast.LENGTH_SHORT).show();
        });
    }
    private void getBloodAmount()
    {
        String[] bloodAmount=new String[]
                {"1 bag","2 bag","3 bag","4 bag","5 bag","6 bag","7 bag","8 bag","9 bag","10 bag"};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(
                this,
                R.layout.drorp_down_item,
                bloodAmount
        );
        binding.reqBloodAmount.setAdapter(arrayAdapter);
        binding.reqBloodAmount.setOnItemClickListener((parent, view, position, id) -> {
            requiredBlood=binding.reqBloodAmount.getText().toString();
            //Toast.makeText(getApplicationContext(),binding.filledExposed.getText().toString(),Toast.LENGTH_SHORT).show();
        });
    }
    private void getPatientGender()
    {
        String[] gender=new String[]
                {"Kids","Male","Female","Others",};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(
                this,
                R.layout.drorp_down_item,
                gender
        );
        binding.reqBloodGender.setAdapter(arrayAdapter);
        binding.reqBloodGender.setOnItemClickListener((parent, view, position, id) -> {
            patientGender=binding.reqBloodGender.getText().toString();
            //Toast.makeText(getApplicationContext(),binding.filledExposed.getText().toString(),Toast.LENGTH_SHORT).show();
        });
    }
    private void getPatientType()
    {
        String[] pType=new String[]
                {"Normal","Accident","Baby Delivery","Emergency","Others"};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(
                this,
                R.layout.drorp_down_item,
                pType
        );
        binding.reqBloodPatientType.setAdapter(arrayAdapter);
        binding.reqBloodPatientType.setOnItemClickListener((parent, view, position, id) -> {
            patientType=binding.reqBloodPatientType.getText().toString();
            //Toast.makeText(getApplicationContext(),binding.filledExposed.getText().toString(),Toast.LENGTH_SHORT).show();
        });
    }
    private void showSuccessToast(String Message){
        Toasty.success(getApplicationContext(),Message,Toasty.LENGTH_SHORT,false).show();
    }

    private void selectTime() {

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
        calendar.add(Calendar.DAY_OF_MONTH,6);
        Date res=calendar.getTime();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        MaterialDatePicker.Builder<Long> builder=MaterialDatePicker.Builder.datePicker();
        long today=MaterialDatePicker.todayInUtcMilliseconds();
        CalendarConstraints.DateValidator pastDateValidator = DateValidatorPointForward.from(today);
        CalendarConstraints.DateValidator futureDateValidator = DateValidatorPointBackward.before(res.getTime());
        ArrayList<CalendarConstraints.DateValidator> listValidators =
                new ArrayList<CalendarConstraints.DateValidator>();
        listValidators.add(pastDateValidator);
        listValidators.add(futureDateValidator);

        CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
        constraintsBuilder.setValidator(validators);

        builder.setCalendarConstraints(constraintsBuilder.build());


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
        binding.bottomNavBloodReq.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                return;
            }
        });
    }
    @Override
    public void onBackPressed() {
        binding.bottomNavBloodReq.show(1,true);
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDivisionItemClick(int position) {
        binding.includeBloodReq.editTextDiv.setText(divisionModelArrayList.get(position).getName());
        division_id=divisionModelArrayList.get(position).getId();
        dialog.cancel();
    }

    @Override
    public void onDistrictItemClick(int position) {
        binding.includeBloodReq.editTextDis.setText(districtsModelArrayList.get(position).getName());
        district_id=districtsModelArrayList.get(position).getId();
        dialog_dis.cancel();
    }

    @Override
    public void onThanaItemClick(int position) {
        binding.includeBloodReq.editTextUp.setText(upThanaArrayList.get(position).getName());
        dialog_thana.cancel();
    }

}
