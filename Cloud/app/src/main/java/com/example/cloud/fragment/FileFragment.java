package com.example.cloud.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cloud.R;
import com.example.cloud.adapter.TepAdapter;
import com.example.cloud.databinding.FragmentFileBinding;
import com.example.cloud.databinding.FragmentFolderBinding;
import com.example.cloud.model.Tep;
import com.example.cloud.onclick.IOnClickItem;

import java.util.ArrayList;
import java.util.List;


public class FileFragment extends Fragment {
    FragmentFileBinding binding;

    private List<Tep> listTep;
    private RecyclerView recyclerView;
    private TepAdapter tepAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFileBinding.inflate(inflater,container,false);


        initRecycleView();

        return binding.getRoot();
    }

    private void initRecycleView() {
        recyclerView = binding.rcvDataFile;

        listTep = new ArrayList<>();
        createTepList();
        tepAdapter = new TepAdapter(this.getContext(),listTep,R.layout.file,new IOnClickItem() {
            @Override
            public void onClickItem(Tep tep) {
                taiFile(tep);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),4));
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

    private void taiFile(Tep tep) {
    }
}