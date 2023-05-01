package com.example.feandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.feandroid.API.MyAPI;
import com.example.feandroid.entity.NguoiDung;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final int PICKFILE_REQUEST_CODE = 100;

    private static String beURL="http://192.168.205.250:8080/";

    SharedPreferences sharedPref;

    Button testDangNhapBtn, testDangKyBtn, testTaiFileBtn, testTaiFileVeBtn, testTaiCacTenBtn, testXoaFileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        testDangNhapBtn = (Button) findViewById(R.id.testDangNhap);
        testDangKyBtn = (Button) findViewById(R.id.testDangKy);
        testTaiFileBtn = (Button) findViewById(R.id.testTaiFile);
        testTaiFileVeBtn = (Button) findViewById(R.id.testTaiFileVe);
        testTaiCacTenBtn = (Button) findViewById(R.id.testTaiCacTen);
        testXoaFileBtn=(Button) findViewById(R.id.testXoaFile);

        testDangNhapBtn.setOnClickListener(view -> dangNhap());

        testDangKyBtn.setOnClickListener(view -> dangKy());

        testTaiFileBtn.setOnClickListener(view -> pickFileFromStorage());

        testTaiFileVeBtn.setOnClickListener(view -> taiFileVe());

        testTaiCacTenBtn.setOnClickListener(view -> taiCacTen());

        testXoaFileBtn.setOnClickListener(view -> xoaFile());
    }

    private void init() {
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

        File folder = new File(directory, "hdv");

        if (!folder.exists()) {
            folder.mkdirs();
        } else {
            File[] files = folder.listFiles();
            for (File file : files) {
                file.delete();
            }

        }

        sharedPref = getSharedPreferences("MySharedPref", MODE_PRIVATE);
    }

    private void dangNhap() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(beURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyAPI api = retrofit.create(MyAPI.class);

        Call<NguoiDung> call = api.login("anhson1", "anhson1");
        call.enqueue(new Callback<NguoiDung>() {
            @Override
            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {
                // Xử lý phản hồi từ server
                NguoiDung nguoiDung = response.body();
                Log.e("thanh cong", nguoiDung.getTenDangNhap());
            }

            @Override
            public void onFailure(Call<NguoiDung> call, Throwable t) {
                // Xử lý lỗi
                Log.e("that bai", t.getMessage());
            }
        });
    }

    private void dangKy() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(beURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyAPI api = retrofit.create(MyAPI.class);

        Call<NguoiDung> call = api.signup("anhson1", "anhson1");
        call.enqueue(new Callback<NguoiDung>() {
            @Override
            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {
                // Xử lý phản hồi từ server
                NguoiDung nguoiDung = response.body();
                Log.e("thanh cong", nguoiDung.getTenDangNhap());
            }

            @Override
            public void onFailure(Call<NguoiDung> call, Throwable t) {
                // Xử lý lỗi
                Log.e("that bai", t.getMessage());
            }
        });
    }

    private void pickFileFromStorage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String fileName = (returnCursor.getString(nameIndex));

        Log.e("fileName", fileName.split("\\.")[1]);

        InputStream inputStream = getContentResolver().openInputStream(uri);

        File tempFile = new File(getFilesDir(), fileName);

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

        String path = "anhson";
        RequestBody pathRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), path);

        Log.e("Name", "1");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(beURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyAPI api = retrofit.create(MyAPI.class);

        Call<ResponseBody> call = (Call<ResponseBody>) api.uploadFile(filePart, pathRequestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String rs = response.body().toString();

                    Log.e("tk", rs);

                    tempFile.delete();
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

    private void taiFileVe() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(beURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyAPI api = retrofit.create(MyAPI.class);

        Call<ResponseBody> call = api.downloadOneFile("1681572511299.jpg", "anhson");

        Log.e("Name", "1");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Lấy đường dẫn thư mục trên thiết bị để lưu file tải về
                    // Lưu file vào thư mục tải về.
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

                    File file = new File(directory + "/hdv", "1681572511299.jpg");
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
                    Log.e("ss", "2");
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

    public void taiCacTen() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(beURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyAPI api = retrofit.create(MyAPI.class);

        Call<List<String>> call = api.downloadNameAll("anhson");

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    List<String> listCacTen = response.body();

                    SharedPreferences.Editor editor = sharedPref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(listCacTen);
                    editor.putString("listTen", json);
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }

    private void xoaFile(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(beURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyAPI api = retrofit.create(MyAPI.class);

        Call<ResponseBody> call= api.deleteFile("1057eab64b921d7fee8a3ae956cf5ab6.jpg","anhson");

        Log.e("fall1", "3");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("fall",response.isSuccessful()+"");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("fall",t.getMessage());
            }
        });
    }
}