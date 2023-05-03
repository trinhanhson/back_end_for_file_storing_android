package com.example.cloud.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.cloud.activity.MainActivity;
import com.example.cloud.databinding.FragmentImageShowBinding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ImageShowFragment extends Fragment {

    private FragmentImageShowBinding binding;

    private AppCompatImageView btnBack, btnDelete;

    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentImageShowBinding.inflate(inflater, container, false);

        btnBack = binding.imgExit;

        btnDelete = binding.trash;

        btnBack.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(ImageShowFragment.this);
            fragmentTransaction.commit();
        });

        String encodedImageName = "";
        try {
            encodedImageName = URLEncoder.encode(MainActivity.tep.getDuongDan(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Glide.with(this.getContext()).load("http://192.168.0.183:8080/getFile?filePath=" + encodedImageName).into(imageView);

        return binding.getRoot();
    }
}
