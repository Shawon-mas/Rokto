package com.app.roktoDorkar.view;


import static com.app.roktoDorkar.utilites.Constants.KEY_BLOODTYPE;
import static com.app.roktoDorkar.utilites.Constants.KEY_COLLECTION_USERS;
import static com.app.roktoDorkar.utilites.Constants.KEY_EMAIL;
import static com.app.roktoDorkar.utilites.Constants.KEY_FCM_TOKEN;
import static com.app.roktoDorkar.utilites.Constants.KEY_SERVER_KEY;
import static com.app.roktoDorkar.utilites.Constants.KEY_UPZILA;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.app.roktoDorkar.api.upazilaApi.ThanaAdapter;
import com.app.roktoDorkar.api.upazilaApi.ThanaClick;
import com.app.roktoDorkar.api.upazilaApi.ThanaModel;
import com.app.roktoDorkar.databinding.ActivityHomeBinding;
import com.app.roktoDorkar.utilites.PreferenceManager;
import com.app.roktoDorkar.view.bottomViewActivites.AccountActivity;
import com.app.roktoDorkar.view.bottomViewActivites.BloodReqActivity;
import com.app.roktoDorkar.view.bottomViewActivites.HistoryActivity;
import com.app.roktoDorkar.view.bottomViewActivites.RequestActivity;
import com.deeplabstudio.fcmsend.FCMSend;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.ArrayList;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity implements DivisionClick, DistrictClick, ThanaClick {
    private ActivityHomeBinding binding;
    private String[] bloodItem;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static Boolean viewType = false;
    private PreferenceManager preferenceManager;

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

    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        preferenceManager=new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        bloodItem = getResources().getStringArray(R.array.donate_blood);
        bottomNavHome();
        getToken();
        initViews();
        getBloodType();
        FCMSend.SetServerKey(KEY_SERVER_KEY);

    }

    private void getBloodType() {
        String[] bloodGroup=new String[] {"A+","A-","B+","B-","O+","O-","AB+","AB-"};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(
                this,
                R.layout.drorp_down_item,
                bloodGroup
        );
        binding.homeBloodGroup.setAdapter(arrayAdapter);
        binding.homeBloodGroup.setOnItemClickListener((parent, view, position, id) -> {
            type=binding.homeBloodGroup.getText().toString();
            //Toast.makeText(getApplicationContext(),binding.filledExposed.getText().toString(),Toast.LENGTH_SHORT).show();
        });
    }
    private void showErrorToast(String message)
    {
        Toasty.error(getApplicationContext(),message,Toasty.LENGTH_LONG).show();
    }

    private void initViews() {

             binding.includeLocation.editTextDiv.setOnClickListener(v -> {
                 getDivision(true);
             });
        binding.includeLocation.editTextDis.setOnClickListener(v -> {
            if (division_id==null)
            {
                showErrorToast("Select Division");
                return;
            }else {
                setDistrict(true,division_id);
            }
        });
        binding.includeLocation.editTextUp.setOnClickListener(v -> {
            if (district_id==null)
            {
                showErrorToast("Select District");
                return;
            }else {
                setThana(true,district_id);
            }
        });


        binding.button.setOnClickListener(v -> {
            if (checkValidator())
            {
                findDonar();
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
                        thanaAdapter.setOnItemClckListener(HomeActivity.this);
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
                        districtAdapter.setOnItemClckListener(HomeActivity.this);
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
                        adapter.setOnItemClckListener(HomeActivity.this);
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





    private void getToken(){

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
   private void updateToken(String token){
        preferenceManager.putString(KEY_FCM_TOKEN,token);
       FirebaseFirestore database=FirebaseFirestore.getInstance();
       DocumentReference documentReference=database.collection(KEY_COLLECTION_USERS).document(preferenceManager.getString(KEY_EMAIL));
       documentReference.update(KEY_FCM_TOKEN,token)
               .addOnFailureListener(e -> errorToast("Unable to update token"));



   }
    private void errorToast(String message)
    {
        Toasty.error(getApplicationContext(),message,Toasty.LENGTH_SHORT,false).show();
    }
    private void showToast(String message)
    {
        Toasty.success(getApplicationContext(),message,Toasty.LENGTH_SHORT,false).show();
    }

    private Boolean checkValidator()
    {
        if (binding.homeBloodGroup.getText().toString().isEmpty())
        {
            errorToast("Select blood group");
            return false;
        }else if (binding.includeLocation.editTextDiv.getText().toString().isEmpty())
        {
            errorToast("Select your division");
            return false;
        }else if (binding.includeLocation.editTextDis.getText().toString().isEmpty())
        {
            errorToast("Select your district");
            return false;
        }else if (binding.includeLocation.editTextUp.getText().toString().isEmpty())
        {
            errorToast("Select your upazila/thana");
            return false;
        }


        else {
            return true;
        }
    }

    private void findDonar() {

                binding.homeIndicator.setVisibility(View.VISIBLE);
                db.collection(KEY_COLLECTION_USERS).whereEqualTo(KEY_BLOODTYPE, type)
                        .whereEqualTo(KEY_UPZILA, binding.includeLocation.editTextUp.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    binding.homeIndicator.setVisibility(View.GONE);
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                        Intent intent = new Intent(getApplicationContext(), DonarsListActivity.class);
                                        intent.putExtra("type", type);
                                        intent.putExtra("location", binding.includeLocation.editTextUp.getText().toString());
                                        startActivity(intent);
                                    }
                                }
                                if (task.getResult().size() == 0) {
                                    binding.homeIndicator.setVisibility(View.GONE);
                                    Toast.makeText(HomeActivity.this, "There is no donor right now your location", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


    }

    private void bottomNavHome() {
        binding.bottomNavHome.add(new MeowBottomNavigation.Model(1, R.drawable.search_donar));
        binding.bottomNavHome.add(new MeowBottomNavigation.Model(2, R.drawable.request));
        binding.bottomNavHome.add(new MeowBottomNavigation.Model(3, R.drawable.add));
        binding.bottomNavHome.add(new MeowBottomNavigation.Model(4, R.drawable.history));
        binding.bottomNavHome.add(new MeowBottomNavigation.Model(5, R.drawable.account));
        binding.bottomNavHome.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 1:
                        overridePendingTransition(0, 0);
                        return;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), RequestActivity.class));
                        overridePendingTransition(0, 0);
                        return;
                    case 3:
                        startActivity(new Intent(getApplicationContext(), BloodReqActivity.class));
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
        binding.bottomNavHome.show(1, true);
        binding.bottomNavHome.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
            }
        });
        binding.bottomNavHome.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                return;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
        alertDialog.setTitle("Wait !");
        alertDialog.setMessage("Do you want to close the application?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                (dialog, which) -> {
                    dialog.dismiss();
                    finishAndRemoveTask();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }


    @Override
    public void onDivisionItemClick(int position) {
        binding.includeLocation.editTextDiv.setText(divisionModelArrayList.get(position).getName());
        division_id=divisionModelArrayList.get(position).getId();
        dialog.cancel();
    }

    @Override
    public void onDistrictItemClick(int position) {
        binding.includeLocation.editTextDis.setText(districtsModelArrayList.get(position).getName());
        district_id=districtsModelArrayList.get(position).getId();
        dialog_dis.cancel();
    }

    @Override
    public void onThanaItemClick(int position) {
        binding.includeLocation.editTextUp.setText(upThanaArrayList.get(position).getName());
        dialog_thana.cancel();
    }
}