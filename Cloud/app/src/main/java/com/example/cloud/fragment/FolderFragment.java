package com.example.cloud.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.cloud.R;
import com.example.cloud.activity.MainActivity;
import com.example.cloud.activity.RegisterActivity;
import com.example.cloud.adapter.TepAdapter;
import com.example.cloud.api.ApiCollection;
import com.example.cloud.api.ApiSumoner;
import com.example.cloud.databinding.FragmentFolderBinding;
import com.example.cloud.model.NguoiDung;
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


public class FolderFragment extends Fragment {
    FragmentFolderBinding binding;

    private List<Tep> listTep;
    private RecyclerView recyclerView;
    public TepAdapter tepAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFolderBinding.inflate(inflater, container, false);

        initRecycleView();

        return binding.getRoot();

    }

    public void initRecycleView() {
        recyclerView = binding.rcvDataFolder;

        listTep = new ArrayList<>();
        createTepList();
    }

    private void createTepList() {

        ApiCollection api = ApiSumoner.callApi();

        Call<List<Tep>> call = api.downloadNameAll(MainActivity.folderPath);

        call.enqueue(new Callback<List<Tep>>() {
            @Override
            public void onResponse(Call<List<Tep>> call, Response<List<Tep>> response) {
                listTep = response.body();
                Log.e("t", listTep.size()+"");
                tepAdapter = new TepAdapter(FolderFragment.this.getContext(), listTep, R.layout.file_folder, new IOnClickItem() {
                    @Override
                    public void onClickItem(Tep tep) {
                        if (tep.getLoai().equals("thu muc")) {
                            taiFolder(tep);
                        } else {
                            taiTep(tep);
                        }
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

    void taiFolder(Tep tep) {
        MainActivity.folderPath+="/"+tep.getTen();
        replaceFragment();
    }

    private void replaceFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.replace(R.id.frame_layout, new FolderFragment());
        fragmentTransaction.commit();
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