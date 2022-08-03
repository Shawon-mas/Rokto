package com.app.roktoDorkar.view.bottomViewActivites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.databinding.ActivityDonorsBinding;

public class DonorsActivity extends AppCompatActivity {
    private ActivityDonorsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDonorsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomnvav();
    }

    private void bottomnvav() {
    }
}