package com.example.cloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cloud.api.ApiCollection;
import com.example.cloud.api.ApiSumoner;
import com.example.cloud.databinding.ActivityRegisterBinding;
import com.example.cloud.model.NguoiDung;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    private String username, pass, reTimePass;
    public static NguoiDung user;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnRegis.setEnabled(false);
        binding.txtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {checkRegisterIsValid();
            }
        });
        binding.txtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {checkRegisterIsValid();
            }
        });
        binding.txtPass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(checkReTimePass()){
                    checkRegisterIsValid();
                }else {
                    binding.txtPass2.setError("Pass conflicted ");
                }

            }
        });
        binding.imgBack.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.btnRegis.setOnClickListener(view -> {
            NguoiDung user = new NguoiDung();
            user.setTenDangNhap(username);
            user.setMatKhau(pass);
            saveUse(user);
            intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void saveUse(NguoiDung user) {

        ApiCollection api= ApiSumoner.callApi();

        Call<NguoiDung> call=api.signup(user);

        call.enqueue(new Callback<NguoiDung>() {
            @Override
            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {

            }

            @Override
            public void onFailure(Call<NguoiDung> call, Throwable t) {

            }
        });
    }

    private boolean checkReTimePass() {
        pass = binding.txtPass.getText()+"";
        reTimePass = binding.txtPass2.getText()+"";
        if( !pass.equals(reTimePass) ) return false;
        return true;
    }

    private void checkRegisterIsValid() {
        username = binding.txtUsername.getText()+"";
        pass = binding.txtPass.getText()+"";
        reTimePass = binding.txtPass2.getText()+"";


        binding.btnRegis.setEnabled(!username.isEmpty() && !pass.isEmpty() && !reTimePass.isEmpty());
    }
}
