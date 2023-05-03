package com.example.cloud.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloud.R;
import com.example.cloud.adapter.TepAdapter;
import com.example.cloud.databinding.SearchActivityBinding;
import com.example.cloud.model.Tep;
import com.example.cloud.onclick.IOnClickItem;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private SearchActivityBinding binding;

    private ArrayList<Tep> listTep;
    private RecyclerView recyclerView;
    private TepAdapter tepAdapter;

    private AppCompatImageView btnBack;

    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= SearchActivityBinding.inflate(inflater,container,false);

        initRecycleView();

        btnBack=binding.imgClear;

        btnBack.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(SearchFragment.this);
            fragmentTransaction.commit();
        });

        searchView=binding.srvSearch;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tepAdapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                tepAdapter.getFilter().filter(query);

                return false;
            }
        });

        return binding.getRoot();
    }

    private void initRecycleView() {
        recyclerView = binding.rcvData;

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
