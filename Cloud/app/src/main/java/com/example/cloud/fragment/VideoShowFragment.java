package com.example.cloud.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cloud.activity.MainActivity;
import com.example.cloud.databinding.FragmentVideoShowBinding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class VideoShowFragment extends Fragment {

    private FragmentVideoShowBinding binding;

    private AppCompatImageView btnBack,btnDelete;

    private VideoView videoView;

    private MediaController mMediaController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentVideoShowBinding.inflate(inflater, container, false);

        btnBack=binding.imgExit;

        btnDelete=binding.trash;

        videoView=binding.videoView;

        btnBack.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(VideoShowFragment.this);
            fragmentTransaction.commit();
        });

        mMediaController = new MediaController(this.getContext());

        // Đường dẫn tới video

        String encodedVideoName="";
        try {
            encodedVideoName = URLEncoder.encode(MainActivity.tep.getDuongDan(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String videoUrl="http://192.168.0.183:8080/getFile?filePath=" + encodedVideoName;

        // Thiết lập MediaController cho VideoView
        mMediaController.setAnchorView(videoView);
        videoView.setMediaController(mMediaController);

        // Đưa đường dẫn tới video vào VideoView để phát
        videoView.setVideoPath(videoUrl);

        // Bắt đầu phát video
        videoView.start();

        return binding.getRoot();
    }
}
