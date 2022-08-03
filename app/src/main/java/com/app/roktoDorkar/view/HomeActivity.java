package com.app.roktoDorkar.view;

import static com.app.roktoDorkar.global.SharedPref.USER_BLOODTYPE;
import static com.app.roktoDorkar.global.SharedPref.USER_NAME;
import static com.app.roktoDorkar.global.SharedPref.USER_UPAZILA;
import static com.app.roktoDorkar.utilites.Constants.KEY_COLLECTION_USERS;
import static com.app.roktoDorkar.utilites.Constants.KEY_EMAIL;
import static com.app.roktoDorkar.utilites.Constants.KEY_FCM_TOKEN;
import static com.app.roktoDorkar.utilites.Constants.KEY_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.databinding.ActivityHomeBinding;
import com.app.roktoDorkar.utilites.PreferenceManager;
import com.app.roktoDorkar.view.bottomViewActivites.AccountActivity;
import com.app.roktoDorkar.view.bottomViewActivites.BloodReqActivity;
import com.app.roktoDorkar.view.bottomViewActivites.HistoryActivity;
import com.app.roktoDorkar.view.bottomViewActivites.RequestActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private String[] bloodItem;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static Boolean viewType = false;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        preferenceManager=new PreferenceManager(getApplicationContext());

        setContentView(binding.getRoot());
        bloodItem = getResources().getStringArray(R.array.donate_blood);
        gridView();
       // Picasso.get().load("https://i.ibb.co/C1xfSLF/b110a1631ac9ae054007f19bd98295c0.png").into(binding.image);
        bottomNavHome();
        getToken();



    }
    private void getToken(){

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
   private void updateToken(String token){
       FirebaseFirestore database=FirebaseFirestore.getInstance();
       DocumentReference documentReference=database.collection(KEY_COLLECTION_USERS).document(preferenceManager.getString(KEY_EMAIL));
       documentReference.update(KEY_FCM_TOKEN,token)
               .addOnSuccessListener(unused -> showToast("Token Updated"))
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
    private void gridView() {
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeIndicator.setVisibility(View.VISIBLE);
                String type = binding.spinnerdonateBlood.getSelectedItem().toString();
                //   Toast.makeText(HomeActivity.this, type, Toast.LENGTH_SHORT).show();
                db.collection("UserProfile").whereEqualTo("bloodType", type)
                        .whereEqualTo("upzilla", binding.location.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    binding.homeIndicator.setVisibility(View.GONE);
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        String name = documentSnapshot.getString("userName");
                                        String bllod = documentSnapshot.getString("bloodType");

                                        Log.d("User Name:", name);
                                        Log.d("User Blood group:", bllod);
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
}