package com.example.cloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cloud.databinding.ActivityLoginBinding;
import com.example.cloud.eventbus.LoginEvent;
import com.example.cloud.model.NguoiDung;

import org.greenrobot.eventbus.EventBus;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private String userName, passWord;
    private NguoiDung user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user = RegisterActivity.user;
        if(user!= null){
            binding.txtUsername.setText(user.getTenDangNhap());
            binding.txtPass.setText(user.getMatKhau());
        }
        binding.btnLogin.setOnClickListener(view -> {
            userName = binding.txtUsername.getText()+"";
            passWord = binding.txtPass.getText()+"";
            if(!userName.isEmpty() && !passWord.isEmpty()){
                //callapi
                if(user!=null){
                    callHome();
                }else{
                    Toast.makeText(this, "SAI Ten DN hoac Pass", Toast.LENGTH_SHORT).show();

                }
            }else{
                if(userName.isEmpty()) binding.txtUsername.setError("Nhap Username");
                if(passWord.isEmpty()) binding.txtPass.setError("Nhap password");
            }
        });
        binding.imgBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void callHome() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        EventBus.getDefault().post(new LoginEvent());
        finish();
    }
}
