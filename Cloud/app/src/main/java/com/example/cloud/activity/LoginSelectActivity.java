package com.example.cloud.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cloud.databinding.ActivityLoginSelectBinding;

public class LoginSelectActivity extends AppCompatActivity {
    ActivityLoginSelectBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.txtUsername.setOnClickListener(view ->{
            callLogin();
        });
        binding.btnRegis.setOnClickListener(view ->{
            callRegister();
        });


    }

    private void callRegister() {
    }

    private void callLogin() {
    }
}
