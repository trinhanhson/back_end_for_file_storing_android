package com.example.cloud.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cloud.databinding.FragmentVideoShowBinding;

public class VideoShowFragment extends Fragment {

    private FragmentVideoShowBinding binding;

    private AppCompatImageView btnBack,btnDelete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentVideoShowBinding.inflate(inflater, container, false);

        btnBack=binding.imgExit;

        btnDelete=binding.trash;

        btnBack.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(VideoShowFragment.this);
            fragmentTransaction.commit();
        });

        return binding.getRoot();
    }
}
