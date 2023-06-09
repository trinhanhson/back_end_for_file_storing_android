package com.example.cloud.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloud.R;
import com.example.cloud.activity.MainActivity;
import com.example.cloud.activity.RegisterActivity;
import com.example.cloud.adapter.TepAdapter;
import com.example.cloud.api.ApiCollection;
import com.example.cloud.api.ApiSumoner;
import com.example.cloud.databinding.FragmentSearchBinding;
import com.example.cloud.model.Tep;
import com.example.cloud.onclick.IOnClickItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.srvSearch.getWindowToken(), 0);

            MainActivity.isSearch=false;

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
                filter(newText);
                return false;
            }
        });

        return binding.getRoot();
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Tep> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Tep item : listTep) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getTen().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        tepAdapter.filterList(filteredlist);
    }

    private void initRecycleView() {
        recyclerView = binding.rcvData;
        createTepList();
        tepAdapter = new TepAdapter(this.getContext(), listTep, R.layout.file_folder, new IOnClickItem() {
            @Override
            public void onClickItem(Tep tep) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.srvSearch.getWindowToken(), 0);

                MainActivity.isSearch=true;

                taiFile(tep);
            }
        });
        recyclerView.setAdapter(tepAdapter);
    }

    private void createTepList() {
        ApiCollection api = ApiSumoner.callApi();

        Call<List<Tep>> call = api.getAllFile(RegisterActivity.user.getTenDangNhap());

        call.enqueue(new Callback<List<Tep>>() {
            @Override
            public void onResponse(Call<List<Tep>> call, Response<List<Tep>> response) {
                listTep = response.body();
                Log.e("t", listTep.size() + "");
                tepAdapter = new TepAdapter(SearchFragment.this.getContext(), listTep, R.layout.file_folder, new IOnClickItem() {
                    @Override
                    public void onClickItem(Tep tep) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(binding.srvSearch.getWindowToken(), 0);

                        taiTep(tep);
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
        if(tepAdapter!=null){
            tepAdapter.release();
        }
    }

    void taiTep(Tep tep) {
        MainActivity.tep=tep;

        if(tep.getLoai().equals("video")){
            replaceFragmentOverlay(new VideoShowFragment());
        }
        else if(tep.getLoai().equals("image")){
            replaceFragmentOverlay(new ImageShowFragment());
        }
        else{
            taiFile(tep);
        }
    }

    private void replaceFragmentOverlay(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.replace(R.id.frame_layout_1, fragment);
        fragmentTransaction.commit();
    }

    void taiFile(Tep tep) {
        ApiCollection api = ApiSumoner.callApi();

        Call<ResponseBody> call = api.downloadOneFile(tep.getDuongDan());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Lấy đường dẫn thư mục trên thiết bị để lưu file tải về
                    // Lưu file vào thư mục tải về.
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

                    File file = new File(directory + "/hdv", tep.getTen());
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    try {
                        byte[] fileReader = new byte[4096];
                        inputStream = response.body().byteStream();
                        outputStream = new FileOutputStream(file);
                        while (true) {
                            int read = inputStream.read(fileReader);
                            if (read == -1) {
                                break;
                            }
                            outputStream.write(fileReader, 0, read);
                        }
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Log.e("ss", file.getAbsolutePath());

                    moFile(file);


                } else {
                    // Xử lý tải file không thành công.
                    Log.e("fall1", "3");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Xử lý lỗi tải file.
                Log.e("fall", t.getMessage());
            }
        });

    }

    private void moFile(File file) {
        Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider",file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String mime = getActivity().getContentResolver().getType(uri);
        intent.setDataAndType(uri, mime);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }
}
