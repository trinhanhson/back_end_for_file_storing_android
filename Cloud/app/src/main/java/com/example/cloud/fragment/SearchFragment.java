package com.example.cloud.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

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
import com.example.cloud.databinding.FragmentSearchBinding;
import com.example.cloud.model.Tep;
import com.example.cloud.onclick.IOnClickItem;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    private List<Tep> listTep = new ArrayList<>();
    private List<Tep> filteredListTep = new ArrayList<>();

    private RecyclerView recyclerView;
    private TepAdapter tepAdapter;

    private AppCompatImageView btnBack;

    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= FragmentSearchBinding.inflate(inflater,container,false);

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
            public boolean onQueryTextSubmit(String query) {return false;}

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredTep(newText);
                return false;
            }
        });

        return binding.getRoot();
    }

    private void filteredTep(String newText) {
        filteredListTep.clear();
        if(newText.isEmpty()){
            filteredListTep = tepAdapter.getData();
        }else{
            for( Tep tep : listTep){
                if(tep.getTen().toLowerCase().contains(newText.toLowerCase())){
                    filteredListTep.add(tep);
                }
            }
        }
        updateRecycleView(filteredListTep);
    }

    private void updateRecycleView(List<Tep> filteredListTep) {
        binding.rcvData.setHasFixedSize(true);
        listTep.clear();
        listTep.addAll(filteredListTep);
        tepAdapter.notifyDataSetChanged();
    }

    private void initRecycleView() {
        recyclerView = binding.rcvData;
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
        listTep = FolderFragment.tepAdapter.getData();
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
    public void hideKeyBoard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        binding.srvSearch.clearFocus();
    }
}
