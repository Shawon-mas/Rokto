package com.app.roktoDorkar.view.bottomViewActivites;

import static com.app.roktoDorkar.utilites.Constants.KEY_ABOUT;
import static com.app.roktoDorkar.utilites.Constants.KEY_BLOODTYPE;
import static com.app.roktoDorkar.utilites.Constants.KEY_COLLECTION_USERS;
import static com.app.roktoDorkar.utilites.Constants.KEY_DISTRICT;
import static com.app.roktoDorkar.utilites.Constants.KEY_DIVISION;
import static com.app.roktoDorkar.utilites.Constants.KEY_EMAIL;
import static com.app.roktoDorkar.utilites.Constants.KEY_FCM_TOKEN;
import static com.app.roktoDorkar.utilites.Constants.KEY_IMAGE_URI;
import static com.app.roktoDorkar.utilites.Constants.KEY_NAME;
import static com.app.roktoDorkar.utilites.Constants.KEY_NUMBER;
import static com.app.roktoDorkar.utilites.Constants.KEY_UPZILA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.databinding.ActivityAccountBinding;
import com.app.roktoDorkar.databinding.ActivityHistoryBinding;
import com.app.roktoDorkar.utilites.PreferenceManager;
import com.app.roktoDorkar.view.HomeActivity;
import com.app.roktoDorkar.view.SignInActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class AccountActivity extends AppCompatActivity {
      private ActivityAccountBinding binding;
    private FirebaseAuth mAuth;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        preferenceManager=new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        bottomNav();
        signOut();
        initViews();

    }

    private void initViews() {

        binding.nameProfile.setText(preferenceManager.getString(KEY_NAME));
        binding.bloodProfile.setText(preferenceManager.getString(KEY_BLOODTYPE));
        binding.aboutProfile.setText(preferenceManager.getString(KEY_ABOUT));
        binding.profileNumber.setText(preferenceManager.getString(KEY_NUMBER));
        binding.emailProfile.setText(preferenceManager.getString(KEY_EMAIL));
        binding.division.setText(preferenceManager.getString(KEY_DIVISION));
        binding.district.setText(preferenceManager.getString(KEY_DISTRICT));
        binding.upazila.setText(preferenceManager.getString(KEY_UPZILA));

        byte[] bytes= Base64.decode(preferenceManager.getString(KEY_IMAGE_URI),Base64.DEFAULT);
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }
    private void showToast(String message)
    {
        Toasty.success(getApplicationContext(),message,Toasty.LENGTH_SHORT,false).show();
    }
    private void errorToast(String message)
    {
        Toasty.error(getApplicationContext(),message,Toasty.LENGTH_SHORT,false).show();
    }
    private void signOut() {
        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Signing out...");
                FirebaseFirestore database=FirebaseFirestore.getInstance();
                DocumentReference documentReference=database.collection(KEY_COLLECTION_USERS).document(preferenceManager.getString(KEY_EMAIL));
                HashMap<String,Object> updates=new HashMap<>();
                updates.put(KEY_FCM_TOKEN, FieldValue.delete());
                documentReference.update(updates)
                        .addOnSuccessListener(unused -> {
                            mAuth.signOut();
                            preferenceManager.clear();
                            Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                            startActivity(intent);
                            finish();
                        }).addOnFailureListener(e -> errorToast("Unable to sign out"));


            }
        });

    }

    private void bottomNav() {
        binding.bottomNavAccount.add(new MeowBottomNavigation.Model(1,R.drawable.search_donar));
        binding.bottomNavAccount.add(new MeowBottomNavigation.Model(2,R.drawable.request));
        binding.bottomNavAccount.add(new MeowBottomNavigation.Model(3,R.drawable.add));
        binding.bottomNavAccount.add(new MeowBottomNavigation.Model(4, R.drawable.group));
        binding.bottomNavAccount.add(new MeowBottomNavigation.Model(5,R.drawable.account));
        binding.bottomNavAccount.setOnShowListener(new MeowBottomNavigation.ShowListener() {
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
                        startActivity(new Intent(getApplicationContext(), BloodReqActivity.class));
                        overridePendingTransition(0, 0);
                        return;
                    case 4:
                        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                        overridePendingTransition(0, 0);
                        return;
                    case 5:

                        overridePendingTransition(0, 0);
                        return;
                }


            }
        });
        binding.bottomNavAccount.show(5,true);
        binding.bottomNavAccount.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item)
            {
                return;
            }
        });
        binding.bottomNavAccount.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                return;
            }
        });
    }

    @Override
    public void onBackPressed() {
        binding.bottomNavAccount.show(1,true);
        super.onBackPressed();
        finish();
    }
}