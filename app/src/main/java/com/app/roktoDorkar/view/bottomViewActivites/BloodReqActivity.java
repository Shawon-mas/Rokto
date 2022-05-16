package com.app.roktoDorkar.view.bottomViewActivites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.databinding.ActivityBloodReqBinding;
import com.app.roktoDorkar.databinding.ActivityRequestBinding;
import com.app.roktoDorkar.view.HomeActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class BloodReqActivity extends AppCompatActivity {
    private ActivityBloodReqBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBloodReqBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNav();
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