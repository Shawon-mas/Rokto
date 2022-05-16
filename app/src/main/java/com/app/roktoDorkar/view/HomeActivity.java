package com.app.roktoDorkar.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.adapter.BloodItemAdapter;
import com.app.roktoDorkar.databinding.ActivityHomeBinding;
import com.app.roktoDorkar.databinding.ActivitySignInBinding;
import com.app.roktoDorkar.model.BloodItem;
import com.app.roktoDorkar.view.bottomViewActivites.AccountActivity;
import com.app.roktoDorkar.view.bottomViewActivites.BloodReqActivity;
import com.app.roktoDorkar.view.bottomViewActivites.HistoryActivity;
import com.app.roktoDorkar.view.bottomViewActivites.RequestActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private String[] bloodItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bloodItem=getResources().getStringArray(R.array.donate_blood);
        gridView();
        bottomNavHome();




    }

    private void gridView() {
        ArrayList<BloodItem> bloodItems=new ArrayList<BloodItem>();
        bloodItems.add(new BloodItem("A+"));
        bloodItems.add(new BloodItem("B+"));
        bloodItems.add(new BloodItem("C+"));
        bloodItems.add(new BloodItem("D+"));
        /*bloodItems.add(new BloodItem(bloodItem[2]));
        bloodItems.add(new BloodItem(bloodItem[3]));
        bloodItems.add(new BloodItem(bloodItem[4]));
        bloodItems.add(new BloodItem(bloodItem[5]));
        bloodItems.add(new BloodItem(bloodItem[6]));
        bloodItems.add(new BloodItem(bloodItem[7]));
        bloodItems.add(new BloodItem(bloodItem[8]));*/
        BloodItemAdapter adapter=new BloodItemAdapter(this,bloodItems);
        binding.bloodItemGrid.setAdapter(adapter);
        binding.bloodItemGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Item:",String.valueOf(position));
                adapter.getItem(position);
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void bottomNavHome() {
        binding.bottomNavHome.add(new MeowBottomNavigation.Model(1,R.drawable.search_donar));
        binding.bottomNavHome.add(new MeowBottomNavigation.Model(2,R.drawable.request));
        binding.bottomNavHome.add(new MeowBottomNavigation.Model(3,R.drawable.add));
        binding.bottomNavHome.add(new MeowBottomNavigation.Model(4,R.drawable.history));
        binding.bottomNavHome.add(new MeowBottomNavigation.Model(5,R.drawable.account));
        binding.bottomNavHome.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId())
                {
                    case 1:
                        overridePendingTransition(0, 0);
                        return;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),RequestActivity.class));
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
        binding.bottomNavHome.show(1,true);
        binding.bottomNavHome.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item)
            {
            }
        });
    }


}