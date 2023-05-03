package com.example.cloud.activity;

import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cloud.R;
import com.example.cloud.databinding.ActivityMainBinding;
import com.example.cloud.fragment.AccountFragment;
import com.example.cloud.fragment.ChoseUploadFragment;
import com.example.cloud.fragment.FileFragment;
import com.example.cloud.fragment.FolderFragment;
import com.example.cloud.fragment.ImageFragment;
import com.example.cloud.fragment.SearchFragment;
import com.example.cloud.fragment.VideoFragment;
import com.example.cloud.model.Tep;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private AppCompatImageView btnAccount;

    private AppCompatImageView btnSearch;

    private FloatingActionButton btnAdd;

    public static String folderPath;

    public static Tep tep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FolderFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
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
            return true;
        });

        btnAccount = findViewById(R.id.btnAccount);

        btnAccount.setOnClickListener(v -> replaceFragmentOverlay(new AccountFragment()));

        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(v -> replaceFragmentOverlay(new SearchFragment()));

        btnAdd=findViewById(R.id.add);

        btnAdd.setOnClickListener(v -> replaceFragmentOverlay(new ChoseUploadFragment()));

        folderPath=RegisterActivity.user.getTenDangNhap();

        init();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void replaceFragmentOverlay(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.replace(R.id.frame_layout_1, fragment);
        fragmentTransaction.commit();
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

    }
}
