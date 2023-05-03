package com.example.cloud.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.cloud.R;
import com.example.cloud.activity.MainActivity;
import com.example.cloud.adapter.TepAdapter;
import com.example.cloud.api.ApiCollection;
import com.example.cloud.api.ApiSumoner;
import com.example.cloud.databinding.FragmentFolderBinding;
import com.example.cloud.model.NguoiDung;
import com.example.cloud.model.Tep;
import com.example.cloud.onclick.IOnClickItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FolderFragment extends Fragment {
    FragmentFolderBinding binding;

    private List<Tep> listTep;
    private RecyclerView recyclerView;
    private TepAdapter tepAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFolderBinding.inflate(inflater, container, false);

        initRecycleView();

        return binding.getRoot();

    }

    private void initRecycleView() {
        recyclerView = binding.rcvDataFolder;

        listTep = new ArrayList<>();
        createTepList();
        tepAdapter = new TepAdapter(this.getContext(), listTep, R.layout.file_folder, new IOnClickItem() {
            @Override
            public void onClickItem(Tep tep) {
                if (tep.getLoai().equals("thu muc")) {
                    taiFolder(tep);
                } else {
                    taiFile(tep);
                }
            }
        });
        recyclerView.setAdapter(tepAdapter);
    }

    private void createTepList() {
//        listTep.add(new Tep(1, "abv", "anhson/abv.png", "image", "anhson"));
//        listTep.add(new Tep(1, "abv", "anhson/abv.png", "image", "anhson"));
//        listTep.add(new Tep(1, "abv", "anhson/abv.png", "image", "anhson"));
//        listTep.add(new Tep(1, "abv", "anhson/abv.png", "image", "anhson"));
//        listTep.add(new Tep(1, "abv", "anhson/abv.png", "image", "anhson"));

        ApiCollection api = ApiSumoner.callApi();

        Call<List<Tep>> call = api.downloadNameAll(MainActivity.folderPath);

        call.enqueue(new Callback<List<Tep>>() {
            @Override
            public void onResponse(Call<List<Tep>> call, Response<List<Tep>> response) {
                listTep = response.body();
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

    }

    void taiFolder(Tep tep) {

    }
}