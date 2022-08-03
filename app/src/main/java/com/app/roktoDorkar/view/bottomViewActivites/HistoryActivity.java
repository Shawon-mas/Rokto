package com.app.roktoDorkar.view.bottomViewActivites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.databinding.ActivityBloodReqBinding;
import com.app.roktoDorkar.databinding.ActivityHistoryBinding;
import com.app.roktoDorkar.view.HomeActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class HistoryActivity extends AppCompatActivity {
private ActivityHistoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNav();
    }

    private void bottomNav() {
        binding.bottomNavHistory.add(new MeowBottomNavigation.Model(1,R.drawable.search_donar));
        binding.bottomNavHistory.add(new MeowBottomNavigation.Model(2,R.drawable.request));
        binding.bottomNavHistory.add(new MeowBottomNavigation.Model(3,R.drawable.add));
        binding.bottomNavHistory.add(new MeowBottomNavigation.Model(4,R.drawable.history));
        binding.bottomNavHistory.add(new MeowBottomNavigation.Model(5,R.drawable.account));
        binding.bottomNavHistory.setOnShowListener(new MeowBottomNavigation.ShowListener() {
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

                        overridePendingTransition(0, 0);
                        return;
                    case 5:
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0, 0);
                        return;
                }
            }
        });
        binding.bottomNavHistory.show(4,true);
        binding.bottomNavHistory.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item)
            {
            }
        });
        binding.bottomNavHistory.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                return;
            }
        });
    }
    @Override
    public void onBackPressed() {
        binding.bottomNavHistory.show(1,true);
        super.onBackPressed();
    }
}