package com.example.cloud.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cloud.databinding.ActivityLoginSelectBinding;
import com.example.cloud.eventbus.LoginEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginSelectActivity extends AppCompatActivity {
    ActivityLoginSelectBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.tvLogin.setOnClickListener(view ->{
            callLogin();
        });
        binding.tvSignup.setOnClickListener(view ->{
            callRegister();
        });
    }

    private void callRegister() {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    private void callLogin() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }
    @Subscribe
    public void onLogined(LoginEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
