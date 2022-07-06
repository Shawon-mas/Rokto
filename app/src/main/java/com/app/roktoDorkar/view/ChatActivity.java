package com.app.roktoDorkar.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadUserDetails();
    }

    private void loadUserDetails() {
        String name=getIntent().getStringExtra("user_name");
        binding.textName.setText(name);
    }
}