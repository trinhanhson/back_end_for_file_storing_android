package com.example.cloud.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cloud.R;
import com.example.cloud.activity.MainActivity;
import com.example.cloud.activity.RegisterActivity;
import com.example.cloud.adapter.TepAdapter;
import com.example.cloud.api.ApiCollection;
import com.example.cloud.api.ApiSumoner;
import com.example.cloud.databinding.FragmentVideoBinding;
import com.example.cloud.model.Tep;
import com.example.cloud.onclick.IOnClickItem;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VideoFragment extends Fragment {
    FragmentVideoBinding binding;
    private List<Tep> listTep;
    private RecyclerView recyclerView;
    private TepAdapter tepAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVideoBinding.inflate(inflater, container, false);
        MainActivity.folderPath = RegisterActivity.user.getTenDangNhap();

        initRecycleView();

        return binding.getRoot();
    }

    private void initRecycleView() {

        recyclerView = binding.rcvDataVideo;

        listTep = new ArrayList<>();
        createTepList();
    }

    private void createTepList() {

        ApiCollection api = ApiSumoner.callApi();

        Call<List<Tep>> call = api.downloadNameFileOfType(RegisterActivity.user.getTenDangNhap(), "video");

        call.enqueue(new Callback<List<Tep>>() {
            @Override
            public void onResponse(Call<List<Tep>> call, Response<List<Tep>> response) {
                listTep = response.body();
                Log.e("t", listTep.size() + "");
                tepAdapter = new TepAdapter(VideoFragment.this.getContext(), listTep, R.layout.file, new IOnClickItem() {
                    @Override
                    public void onClickItem(Tep tep) {
                        taiFile(tep);
                    }
                });
                recyclerView.setAdapter(tepAdapter);
            }

            @Override
            public void onFailure(Call<List<Tep>> call, Throwable t) {
                Log.e("1", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tepAdapter != null) {
            tepAdapter.release();
        }
    }

    void taiFile(Tep tep) {
        MainActivity.tep = tep;
        replaceFragmentOverlay(new VideoShowFragment());
    }

    private void replaceFragmentOverlay(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.replace(R.id.frame_layout_1, fragment);
        fragmentTransaction.commit();
    }
}