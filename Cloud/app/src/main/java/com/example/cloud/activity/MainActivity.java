package com.example.cloud.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cloud.R;
import com.example.cloud.databinding.ActivityMainBinding;
import com.example.cloud.fragment.FileFragment;
import com.example.cloud.fragment.FolderFragment;
import com.example.cloud.fragment.ImageFragment;
import com.example.cloud.fragment.VideoFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FolderFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
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
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}
