package com.example.cloud.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloud.R;
import com.example.cloud.adapter.TepAdapter;
import com.example.cloud.model.Tep;
import com.example.cloud.onclick.IOnClickItem;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private ArrayList<Tep> listTep;
    private RecyclerView recyclerView;
    private TepAdapter tepAdapter;

    private AppCompatImageView btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_activity, container, false);

        initRecycleView();

        btnBack=view.findViewById(R.id.imgClear);

        btnBack.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(SearchFragment.this);
            fragmentTransaction.commit();
        });

        return view;
    }

    private void initRecycleView() {
        recyclerView = binding.rcvDataFolder;

        listTep = new ArrayList<>();
        createTepList();
        tepAdapter = new TepAdapter(this.getContext(), listTep, R.layout.file_folder, new IOnClickItem() {
            @Override
            public void onClickItem(Tep tep) {
                taiFile(tep);
            }
        });
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
