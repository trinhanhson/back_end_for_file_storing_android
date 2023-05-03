package com.example.cloud.fragment;

import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cloud.R;
import com.example.cloud.activity.MainActivity;
import com.example.cloud.api.ApiCollection;
import com.example.cloud.api.ApiSumoner;
import com.example.cloud.databinding.FragmentVideoShowBinding;
import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoShowFragment extends Fragment {

    private FragmentVideoShowBinding binding;

    private AppCompatImageView btnBack;

    private ExoPlayer exoPlayer;
    private String videoUrl;

    private ImageView btnDelete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentVideoShowBinding.inflate(inflater, container, false);

        btnBack=binding.imgExit;
        btnBack.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(VideoShowFragment.this);
            fragmentTransaction.commit();
        });

        btnDelete=binding.trash;

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiCollection api = ApiSumoner.callApi();

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

        // Đường dẫn tới video

        String encodedVideoName="";
        try {
            encodedVideoName = URLEncoder.encode(MainActivity.tep.getDuongDan(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        videoUrl="http://192.168.55.107:8080/getFile?filePath=" + encodedVideoName;

        playVideo(videoUrl);

        return binding.getRoot();
    }

    private void playVideo(String videoUrl) {
        try{

            exoPlayer = new ExoPlayer.Builder(requireActivity()).build();
            binding.plvExoPlayer.setPlayer(exoPlayer);
            MediaItem mediaItem = MediaItem.fromUri(videoUrl);
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(
                    new DefaultDataSourceFactory(getContext(), "MyApp")
            ).createMediaSource(mediaItem);
            exoPlayer.prepare(mediaSource);
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

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.remove(VideoShowFragment.this);
        fragmentTransaction.commit();
    }
}
