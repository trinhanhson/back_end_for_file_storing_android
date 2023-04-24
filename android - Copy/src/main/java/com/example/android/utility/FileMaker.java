/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.android.utility;

import com.example.android.Repository.TepRepo;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.apache.tomcat.util.http.fileupload.FileUtils;

/**
 *
 * @author ADMIN
 */
public class FileMaker {

    @Autowired
    private TepRepo tepRepo;

    public static boolean MakeFolder(String path, String name) {
        File folder = new File("D:/back_end_for_file_storing_android/android - Copy/user/" + path + "/" + name);
        if (folder.exists()) {
            return false;
        }
        folder.mkdir();
        return true;
    }

    public static boolean MakeFile(String path, String name) throws IOException {
        File file = new File("D:/back_end_for_file_storing_android/android - Copy/user/" + path + "/" + name);
        return file.createNewFile();
    }

    public static boolean DeleteFolder(String path) {
        File file = new File("D:/back_end_for_file_storing_android/android - Copy/user/" + path);

        if (!file.exists()) {
            return false;
        }

        FileSystemUtils.deleteRecursively(file);

        return true;
    }

    public static boolean DeleteFile(String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            return false;
        }

        FileUtils.forceDelete(file);

        return true;
    }

    public static Resource createZipFile(MultiValueMap<String, Resource> multiValueMap) {
        try {
            // Tạo ByteArrayOutputStream để lưu trữ file zip
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(baos);

            // Duyệt qua danh sách các đối tượng Resource và nén chúng thành file zip
            for (Map.Entry<String, List<Resource>> entry : multiValueMap.entrySet()) {
                for (Resource resource : entry.getValue()) {
                    ZipEntry zipEntry = new ZipEntry(resource.getFilename()); // đặt tên entry bằng tên file
                    zipOut.putNextEntry(zipEntry);
                    StreamUtils.copy(resource.getInputStream(), zipOut);
                    zipOut.closeEntry();
                }
            }

            // Đóng các stream và trả về đối tượng Resource đại diện cho file zip
            zipOut.finish();
            zipOut.close();
            byte[] bytes = baos.toByteArray();
            return new ByteArrayResource(bytes);
        } catch (IOException e) {
            return null;
        }
    }

    public static List<String> getAllFolder(String path) {
        String fullPath = "D:/back_end_for_file_storing_android/android - Copy/user/" + path;

        File directory = new File(fullPath);

        // Lấy danh sách các file trong thư mục
        File[] files = directory.listFiles();
        List<String> listOfFolders = new ArrayList<>();

        for (File file : files) {
            if (file.isDirectory()) {
                listOfFolders.add(file.getName());
            }
        }

        return listOfFolders;
    }

    public static List<String> getAll(String path) {
        String fullPath = "D:/back_end_for_file_storing_android/android - Copy/user/" + path;

        File directory = new File(fullPath);

        // Lấy danh sách các file trong thư mục
        File[] files = directory.listFiles();
        List<String> listOfAll = new ArrayList<>();

        for (File file : files) {
            listOfAll.add(file.getName());
        }

        return listOfAll;
    }

}
