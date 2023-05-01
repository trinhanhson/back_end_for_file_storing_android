/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.android.controller;

import com.example.android.Repository.TepRepo;
import com.example.android.model.Tep;
import com.example.android.utility.FileMaker;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
@Controller
public class TepController {

    @Autowired
    private TepRepo tepRepo;
    
    private String parentPath = "D:/back_end_for_file_storing_android/android - Copy/user/";

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) throws IOException {

        String fileName = file.getOriginalFilename();

//        if (tepRepo.existsByTenAndNguoiDung(path.split("/")[0], fileName)) {
//            fileName += "1";
//        }

        File newFile = new File(parentPath + path + "/" + fileName);

        try {
            file.transferTo(newFile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        String mimetype = Files.probeContentType(newFile.toPath());
        Tep tep = new Tep();
        tep.setTen(fileName);
        tep.setDuongDan( path + "/" + fileName);
        switch (mimetype.split("/")[0]) {
            case "image" ->
                tep.setLoai("image");
            case "video" ->
                tep.setLoai("video");
            default ->
                tep.setLoai("khac");
        }
        tep.setNguoiDung(path.split("/")[0]);

        tepRepo.save(tep);

        return ResponseEntity.ok("Tep da tai len thanh cong.");
    }

    @GetMapping("/downloadFile")
    public ResponseEntity<?> downloadFile(@RequestParam("path") String path) {
        String fullPath = parentPath + path;

        File directory = new File(fullPath);

        // Lấy danh sách các file trong thư mục
        File[] files = directory.listFiles();

        List<File> fileNames = Arrays.asList(files);

        // Tạo đối tượng MultiValueMap để lưu trữ danh sách các đối tượng Resource
        MultiValueMap<String, Resource> multiValueMap = new LinkedMultiValueMap<>();

        // Tạo các đối tượng Resource đại diện cho các file cần download và thêm vào multiValueMap
        for (File fileName : fileNames) {
            if (!fileName.isFile()) {
                continue;
            }
            Resource resource = new FileSystemResource(fileName.getAbsolutePath());
            multiValueMap.add(path, resource);
        }

        // Tạo đối tượng Resource đại diện cho file zip
        Resource zipFile = FileMaker.createZipFile(multiValueMap);

        // Trả về file zip cho client
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipFile.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipFile);
    }

    @PostMapping("/uploadFolder")
    public ResponseEntity<?> uploadFolder(@RequestParam("folder") String folder, @RequestParam("path") String path) {
        if (FileMaker.MakeFolder(path, folder)) {
            return ResponseEntity.ok("Thu muc da duoc tao.");
        }
        
        Tep tep = new Tep();
        tep.setTen(folder);
        tep.setDuongDan(path + "/" + folder);
        tep.setLoai("thu muc");
        tep.setNguoiDung(path.split("/")[0]);

        tepRepo.save(tep);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

//    @GetMapping("/downloadFolder")
//    public ResponseEntity<?> downloadFolder(@RequestParam("path") String path) {
//        List<String> listFolder = FileMaker.getAllFolder(path);
//        return ResponseEntity.ok(listFolder);
//    }

    @GetMapping("/downloadFileOfType")
    public ResponseEntity<?> downloadFileOfType(@RequestParam("username") String username, @RequestParam("loai") String loai) {
        List<Tep> listTep = tepRepo.findByNguoiDungAndLoai(username, loai);

        // Tạo đối tượng MultiValueMap để lưu trữ danh sách các đối tượng Resource
        MultiValueMap<String, Resource> multiValueMap = new LinkedMultiValueMap<>();

        // Tạo các đối tượng Resource đại diện cho các file cần download và thêm vào multiValueMap
        for (Tep file : listTep) {
            Resource resource = new FileSystemResource(file.getDuongDan());
            multiValueMap.add(username, resource);
        }

        Resource zipFile = FileMaker.createZipFile(multiValueMap);

        // Trả về file zip cho client
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipFile.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipFile);
    }

    //Newcode
    @GetMapping("/downloadNameAll")
    public ResponseEntity<?> downloadNameAll(@RequestParam("path") String path) {
        List<Tep> listNameTep = tepRepo.findByLikeDuongDan(path);
        return ResponseEntity.ok(listNameTep);
    }

    @GetMapping("/downloadOneFile")
    public ResponseEntity<?> downloadOneFile(@RequestParam("fileName") String fileName, @RequestParam("username") String username) {

        Tep tep = tepRepo.findByTenAndNguoiDung(fileName, username);

        File file = new File(tep.getDuongDan());

        Resource resource = new FileSystemResource(file);

        // Trả về đối tượng ResponseEntity chứa tệp tin và các đầu mục HTTP cần thiết
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @PostMapping("/deleteFile")
    public ResponseEntity<?> deleteFile(@RequestParam("fileName") String fileName, @RequestParam("username") String username) throws IOException {
        Tep tep = tepRepo.findByTenAndNguoiDung(fileName, username);

        if(FileMaker.DeleteFile(tep.getDuongDan()))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        tepRepo.deleteByTenAndNguoiDung(fileName, username);

        return ResponseEntity.ok("Xoa file thanh cong");
    }

    @GetMapping("/downloadNameFileOfType")
    public ResponseEntity<?> downloadNameFileOfType(@RequestParam("username") String username, @RequestParam("loai") String loai) {
        List<Tep> listTep = tepRepo.findByNguoiDungAndLoai(username, loai);

        return ResponseEntity.ok(listTep);
    }
}
