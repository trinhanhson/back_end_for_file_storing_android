package com.example.cloud.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cloud.R;
import com.example.cloud.databinding.FragmentChoseUploadBinding;
import com.example.cloud.databinding.FragmentFileBinding;

public class ChoseUploadFragment extends Fragment {

    private FragmentChoseUploadBinding binding;

    private AppCompatImageView btnBack;

    private Button btnAddMedia, btnAddFile, btnAddFolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentChoseUploadBinding.inflate(inflater,container,false);

        btnBack=binding.imgExit;

        btnAddMedia=binding.addMedia;
        btnAddFile=binding.addFile;
        btnAddFolder=binding.addFolder;

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(ChoseUploadFragment.this);
                fragmentTransaction.commit();
            }
        });

        return binding.getRoot();
    }
}