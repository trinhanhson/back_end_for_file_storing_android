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
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

public class VideoShowFragment extends Fragment {

    private FragmentVideoShowBinding binding;

    private AppCompatImageView btnBack;

    private ExoPlayer exoPlayer;
    private String videoUrl;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentVideoShowBinding.inflate(inflater, container, false);

        btnBack=binding.imgExit;
        btnBack.setOnClickListener(v -> {
            exoPlayer.pause();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(VideoShowFragment.this);
            fragmentTransaction.commit();
        });

        // Đường dẫn tới video

        String encodedVideoName="";
        try {
            encodedVideoName = URLEncoder.encode(MainActivity.tep.getDuongDan(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        videoUrl="http://192.168.0.183:8080/getFile?filePath=" + encodedVideoName;

        playVideo(videoUrl);

        return binding.getRoot();
    }

    private void playVideo(String videoUrl) {
        try{
            exoPlayer = new ExoPlayer.Builder(getContext()).build();
            binding.plvExoPlayer.setPlayer(exoPlayer);
            MediaItem mediaItem = MediaItem.fromUri(videoUrl);
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(true);
        }catch(Exception e){
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        exoPlayer.pause();
    }

    @Override
    public void onPause() {
        super.onPause();
        exoPlayer.pause();
    }
}
