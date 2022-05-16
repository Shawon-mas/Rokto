
package com.app.roktoDorkar.view.bottomViewActivites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.databinding.ActivityHomeBinding;
import com.app.roktoDorkar.databinding.ActivityRequestBinding;
import com.app.roktoDorkar.view.HomeActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationBarView;

public class RequestActivity extends AppCompatActivity {
private ActivityRequestBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNav();
    }

    private void bottomNav()
    {
        binding.bottomNavRequest.add(new MeowBottomNavigation.Model(1,R.drawable.search_donar));
        binding.bottomNavRequest.add(new MeowBottomNavigation.Model(2,R.drawable.request));
        binding.bottomNavRequest.add(new MeowBottomNavigation.Model(3,R.drawable.add));
        binding.bottomNavRequest.add(new MeowBottomNavigation.Model(4,R.drawable.history));
        binding.bottomNavRequest.add(new MeowBottomNavigation.Model(5,R.drawable.account));
        binding.bottomNavRequest.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId())
                {
                    case 1:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return;
                    case 2:

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
        binding.bottomNavRequest.show(2,true);
        binding.bottomNavRequest.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item)
            {
            }
        });
    }
    @Override
    public void onBackPressed() {
        binding.bottomNavRequest.show(1,true);
        super.onBackPressed();
    }
}