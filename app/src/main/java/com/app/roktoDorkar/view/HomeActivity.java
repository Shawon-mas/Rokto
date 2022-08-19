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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.app.roktoDorkar.R;
import com.app.roktoDorkar.api.upazilaApi.ApiInstance;
import com.app.roktoDorkar.api.upazilaApi.UpItemClick;
import com.app.roktoDorkar.api.upazilaApi.UpazilaAdapter;
import com.app.roktoDorkar.api.upazilaApi.UpzilaModel;
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

public class HomeActivity extends BaseActivity implements UpItemClick {
    private ActivityHomeBinding binding;
    private String[] bloodItem;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static Boolean viewType = false;
    private PreferenceManager preferenceManager;
    Dialog dialog;
    private ArrayList<UpzilaModel.Upazila> upzilaModelArrayList;
    private ArrayList<UpzilaModel.Upazila> filterUpList;
    private UpazilaAdapter adapter;
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

    private void initViews() {

        binding.location.setOnClickListener(v -> {
            getUpazila();
        });
        binding.button.setOnClickListener(v -> {
            if (checkValidator())
            {
                findDonar();
            }
        });
    }

    private void getUpazila() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.upzila_list);
        dialog.getWindow().setLayout(800, 1500);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        RecyclerView recyclerView = dialog.findViewById(R.id.upzila_listview);
        ProgressBar progressBar= dialog.findViewById(R.id.progressbar_upzilla);
        EditText editText=dialog.findViewById(R.id.up_search);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });
        progressBar.setVisibility(View.VISIBLE);
        ImageView imageView=dialog.findViewById(R.id.up_close);
        imageView.setOnClickListener(v -> {
            dialog.cancel();
        });
        Call<UpzilaModel> call= ApiInstance.getUpazilaApiEndpoint().getUpazila();
        call.enqueue(new Callback<UpzilaModel>() {
            @Override
            public void onResponse(Call<UpzilaModel> call, Response<UpzilaModel> response) {
                if (response.isSuccessful()){
                    upzilaModelArrayList=new ArrayList<>();
                    upzilaModelArrayList=response.body().getUpazila();
                    filterUpList=upzilaModelArrayList;
                    for (UpzilaModel.Upazila upazila:upzilaModelArrayList)
                    {
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter=new UpazilaAdapter(getApplicationContext(),upzilaModelArrayList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        adapter.setOnItemClckListener(HomeActivity.this);
                        progressBar.setVisibility(View.GONE);


                    }
                }
            }

            @Override
            public void onFailure(Call<UpzilaModel> call, Throwable t) {

                dialog.cancel();

            }
        });


    }

    private void filter(String toString) {
        filterUpList=new ArrayList<>();
        for (UpzilaModel.Upazila upazila:upzilaModelArrayList)
        {
            String search=upazila.getUpazila();
            if (search.toLowerCase().contains(toString.toLowerCase()))
            {
                filterUpList.add(upazila);
            }
        }
        adapter.filterListUp(filterUpList);
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
        }else if (binding.location.getText().toString().isEmpty())
        {
            errorToast("Select your upazila");
            return false;
        }else {
            return true;
        }
    }

    private void findDonar() {

                binding.homeIndicator.setVisibility(View.VISIBLE);
                db.collection(KEY_COLLECTION_USERS).whereEqualTo(KEY_BLOODTYPE, type)
                        .whereEqualTo(KEY_UPZILA, binding.location.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    binding.homeIndicator.setVisibility(View.GONE);
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                        Intent intent = new Intent(getApplicationContext(), DonarsListActivity.class);
                                        intent.putExtra("type", type);
                                        intent.putExtra("location", binding.location.getText().toString());
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
    public void onUPItemClick(int position) {
        binding.location.setText(filterUpList.get(position).getUpazila());
        dialog.dismiss();
    }
}