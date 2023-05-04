package com.example.cloud.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cloud.R;
import com.example.cloud.activity.LoginSelectActivity;
import com.example.cloud.activity.RegisterActivity;
import com.example.cloud.api.ApiCollection;
import com.example.cloud.api.ApiSumoner;
import com.example.cloud.databinding.AccountActivityBinding;
import com.example.cloud.databinding.FragmentFolderBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    private AccountActivityBinding binding;

    private AppCompatImageView btnBack;

    private TextView btnLogout,btnDeleteAcc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = AccountActivityBinding.inflate(inflater, container, false);

        btnBack=binding.imgExit;

        btnLogout=binding.tvLogout;
        btnDeleteAcc=binding.tvDeleteAcc;

        btnBack.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(AccountFragment.this);
            fragmentTransaction.commit();
        });

        btnLogout.setOnClickListener(v ->{
            Intent intent = new Intent(getActivity(), LoginSelectActivity.class);
            startActivity(intent);
        });

        btnDeleteAcc.setOnClickListener(v ->{
            ApiCollection api = ApiSumoner.callApi();

            Call<ResponseBody> call = api.deleteUser(RegisterActivity.user.getTenDangNhap());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Intent intent = new Intent(getActivity(), LoginSelectActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        });

        return binding.getRoot();
    }
}
