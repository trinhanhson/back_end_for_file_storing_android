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
import com.example.cloud.R;
import com.example.cloud.activity.MainActivity;
import com.example.cloud.api.ApiCollection;
import com.example.cloud.api.ApiSumoner;
import com.example.cloud.databinding.FragmentImageShowBinding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        imageView=binding.imageView;

        btnBack.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (MainActivity.isSearch) {
                fragmentTransaction.replace(R.id.frame_layout_1,new SearchFragment());
            } else {
                fragmentTransaction.remove(ImageShowFragment.this);
            }
            fragmentTransaction.commit();
        });

        btnDelete=binding.trash;

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiCollection api = ApiSumoner.callApi();

                MainActivity.deletedFilePath=MainActivity.tep.getDuongDan();

                Call<ResponseBody> call = api.deleteFile(MainActivity.tep.getDuongDan());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch (MainActivity.tab) {
                            case R.id.folder:
                                replaceFragment(new FolderFragment());
                                break;
                            case R.id.image:
                                replaceFragment(new ImageFragment());
                                break;
                            case R.id.video:
                                replaceFragment(new VideoFragment());
                                break;
                            case R.id.file:
                                replaceFragment(new FileFragment());
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        String encodedImageName = "";
        try {
            encodedImageName = URLEncoder.encode(MainActivity.tep.getDuongDan(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Glide.with(this.getContext()).load("http://192.168.55.107:8080/getFile?filePath=" + encodedImageName).into(imageView);

        return binding.getRoot();
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.remove(ImageShowFragment.this);
        fragmentTransaction.commit();
    }
}
