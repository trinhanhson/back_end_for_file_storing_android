package com.example.cloud.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cloud.R;
import com.example.cloud.databinding.AccountActivityBinding;
import com.example.cloud.databinding.FragmentFolderBinding;

public class AccountFragment extends Fragment {

    private AccountActivityBinding binding;

    private AppCompatImageView btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = AccountActivityBinding.inflate(inflater, container, false);

        btnBack=binding.imgExit;

        btnBack.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(AccountFragment.this);
            fragmentTransaction.commit();
        });

        return binding.getRoot();
    }
}
