
package com.app.roktoDorkar.view.bottomViewActivites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.databinding.ActivityRequestBinding;
import com.app.roktoDorkar.view.HomeActivity;
import com.app.roktoDorkar.view.RequestFrag.adapter.MyFragmentAdapter;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class RequestActivity extends AppCompatActivity {
private ActivityRequestBinding binding;
    private MyFragmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNav();
        implementTabLayout();
    }

    private void implementTabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Received Requests"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("My Requests"));
        FragmentManager fragmentManager=getSupportFragmentManager();
        adapter=new MyFragmentAdapter(fragmentManager,getLifecycle());
        binding.viewPager2.setAdapter(adapter);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });


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
        binding.bottomNavRequest.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                return;
            }
        });
    }
    @Override
    public void onBackPressed() {
        binding.bottomNavRequest.show(1,true);
        super.onBackPressed();
    }
}