package com.example.cloud;

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

import com.example.cloud.adapter.TepAdapter;
import com.example.cloud.databinding.FragmentFolderBinding;
import com.example.cloud.model.Tep;
import com.example.cloud.onclick.IOnClickItem;

import java.util.ArrayList;


public class FolderFragment extends Fragment {
    FragmentFolderBinding binding;

    private ArrayList<Tep> listTep;
    private RecyclerView recyclerView;
    private TepAdapter tepAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFolderBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        initRecycleView(inflater, container, view);

        return binding.getRoot();

    }

    private void initRecycleView(LayoutInflater inflater, ViewGroup container, View view) {
        recyclerView = view.findViewById(R.id.rcvDataFolder);

        listTep = new ArrayList<>();
        createTepList();
        tepAdapter = new TepAdapter(this.getContext(), listTep, R.layout.file_folder, new IOnClickItem() {
            @Override
            public void onClickItem(Tep tep) {
                if(tep.getLoai().equals("thu muc")){
                    taiFolder(tep);
                }
                else{
                    taiFile(tep);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(tepAdapter);
    }

    private void createTepList() {
        listTep.add(new Tep(1, "abv", "anhson/abv.png", "image", "anhson"));
        listTep.add(new Tep(1, "abv", "anhson/abv.png", "image", "anhson"));
        listTep.add(new Tep(1, "abv", "anhson/abv.png", "image", "anhson"));
        listTep.add(new Tep(1, "abv", "anhson/abv.png", "image", "anhson"));
        listTep.add(new Tep(1, "abv", "anhson/abv.png", "image", "anhson"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(tepAdapter!=null){
            tepAdapter.release();
        }
    }

    void taiFile(Tep tep){

    }

    void taiFolder(Tep tep){

    }
}