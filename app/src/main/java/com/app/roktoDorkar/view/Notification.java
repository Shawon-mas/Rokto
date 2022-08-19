package com.app.roktoDorkar.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.databinding.ActivityNotificationBinding;
import com.deeplabstudio.fcmsend.FCMSend;

public class Notification extends AppCompatActivity {
    private ActivityNotificationBinding binding;
    String token="fgVMFut8Qd-QRd5vz4bEbA:APA91bFldjOYb5tW92sChFYorIeVEZGFBc94cGVM0fa_cJEC4jj-fV94d2-RbVWEkrhIVxR5GpwwwXrcCDeE2upcaQoYSHbDwivigajhdFLFzGYZDkd-2-NweUQVUmNMq4S29tICihkx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.sendButton.setOnClickListener(v -> {
            FCMSend.Builder builder=new FCMSend.Builder("fgVMFut8Qd-QRd5vz4bEbA:APA91bFldjOYb5tW92sChFYorIeVEZGFBc94cGVM0fa_cJEC4jj-fV94d2-RbVWEkrhIVxR5GpwwwXrcCDeE2upcaQoYSHbDwivigajhdFLFzGYZDkd-2-NweUQVUmNMq4S29tICihkx")
                    .setTitle(binding.notTitle.getText().toString())
                    .setBody(binding.notBody.getText().toString());
            builder.send();
        });
    }
}