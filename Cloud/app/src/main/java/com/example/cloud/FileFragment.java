package com.example.cloud;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cloud.adapter.TepAdapter;
import com.example.cloud.model.Tep;
import com.example.cloud.onclick.IOnClickItem;

import java.util.ArrayList;


public class FileFragment extends Fragment {

    private ArrayList<Tep> listTep;
    private RecyclerView recyclerView;
    private TepAdapter tepAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file, container, false);

        initRecycleView(view);

        return view;
    }

    private void initRecycleView( View view) {
        recyclerView = view.findViewById(R.id.rcvDataFile);

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