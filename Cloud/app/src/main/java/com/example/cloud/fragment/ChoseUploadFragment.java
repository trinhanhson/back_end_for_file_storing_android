package com.example.cloud.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cloud.R;
import com.example.cloud.activity.MainActivity;
import com.example.cloud.api.ApiCollection;
import com.example.cloud.api.ApiSumoner;
import com.example.cloud.databinding.FragmentChoseUploadBinding;
import com.example.cloud.databinding.FragmentFileBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoseUploadFragment extends Fragment {

    private static final int PICKFILE_REQUEST_CODE = 100;

    private FragmentChoseUploadBinding binding;

    private ImageView btnBack;

    private Button btnAddMedia, btnAddFile, btnAddFolder, btnTao, btnHuy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChoseUploadBinding.inflate(inflater, container, false);

        btnBack = binding.imgExit;

        btnAddMedia = binding.addMedia;
        btnAddFile = binding.addFile;
        btnAddFolder = binding.addFolder;
        btnTao = binding.tao;
        btnHuy = binding.huy;

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.editFolderName.getWindowToken(), 0);

                switch (MainActivity.tab) {
                    case R.id.folder:
                        replaceFragment(new FolderFragment());
                        break;
                    case R.id.image:
                        replaceFragment(new ImageFragment());
                        break;
                    case R.id.video:
                        replaceFragment(new VideoFragment());
                        break;
                    case R.id.file:
                        replaceFragment(new FileFragment());
                        break;
                }
            }
        });

        btnAddMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/* video/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }
        });

        btnAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                binding.addThuMuc.setVisibility(View.INVISIBLE);

                binding.editFolderName.setText("");

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.editFolderName.getWindowToken(), 0);
            }
        });

        btnAddFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.addThuMuc.setVisibility(View.VISIBLE);
            }
        });

        btnTao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editFolderName.getText().toString().equals(""))
                    return;

                binding.addThuMuc.setVisibility(View.INVISIBLE);

                ApiCollection api = ApiSumoner.callApi();

                Call<ResponseBody> call = api.uploadFolder(binding.editFolderName.getText().toString(), MainActivity.folderPath);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

                binding.editFolderName.setText("");

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.editFolderName.getWindowToken(), 0);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();

            Log.e("uri", fileUri.toString() + "\n" + fileUri.getEncodedPath());

            // Tải tệp tin lên server từ đường dẫn fileUri
            // ...
            try {
                taiFile(fileUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void taiFile(Uri uri) throws IOException {
        Cursor returnCursor =
                getActivity().
                        getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String fileName = (returnCursor.getString(nameIndex));

        Log.e("fileName", fileName.split("\\.")[1]);

        InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);

        File tempFile = new File(getActivity().getFilesDir(), fileName);

        Log.e("Name", tempFile.getName());

        FileOutputStream outputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[4 * 1024]; // 4KB buffer
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();

        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), tempFile);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", tempFile.getName(), fileRequestBody);

        String path = MainActivity.folderPath;
        RequestBody pathRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), path);

        Log.e("Name", MainActivity.folderPath);

        ApiCollection api = ApiSumoner.callApi();
        Call<ResponseBody> call = api.uploadFile(filePart, pathRequestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String rs = response.body().toString();

                    Log.e("tk", rs);

                    tempFile.delete();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new FolderFragment());
                    fragmentTransaction.commit();
                    // Xử lý phản hồi từ server khi upload file và chuỗi string thành công
                } else {
                    // Xử lý phản hồi từ server khi upload file và chuỗi string thất bại
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Xử lý lỗi khi gửi yêu cầu API upload file và chuỗi string
                Log.e("that bai", t.getMessage());
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.remove(ChoseUploadFragment.this);
        fragmentTransaction.commit();
    }
}