package com.app.roktoDorkar.view.bottomViewActivites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.databinding.ActivityAccountBinding;
import com.app.roktoDorkar.databinding.ActivityHistoryBinding;
import com.app.roktoDorkar.view.HomeActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class AccountActivity extends AppCompatActivity {
      private ActivityAccountBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNav();
    }

    private void bottomNav() {
        binding.bottomNavAccount.add(new MeowBottomNavigation.Model(1,R.drawable.search_donar));
        binding.bottomNavAccount.add(new MeowBottomNavigation.Model(2,R.drawable.request));
        binding.bottomNavAccount.add(new MeowBottomNavigation.Model(3,R.drawable.add));
        binding.bottomNavAccount.add(new MeowBottomNavigation.Model(4,R.drawable.history));
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
            }
        });
    }

    @Override
    public void onBackPressed() {
        binding.bottomNavAccount.show(1,true);
        super.onBackPressed();
    }
}