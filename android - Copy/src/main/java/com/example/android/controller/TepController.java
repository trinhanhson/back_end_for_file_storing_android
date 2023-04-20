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

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) throws IOException {

        String fileName = file.getOriginalFilename();

        File newFile = new File("D:/project java/android/user/" + path + "/" + fileName);

        String mimetype = Files.probeContentType(newFile.toPath());
        Tep tep = new Tep();
        tep.setTen(fileName);
        tep.setDuongDan("D:/project java/android/user/" + path + "/" + fileName);
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

        try {
            file.transferTo(newFile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok("Tep da tai len thanh cong.");
    }

    @GetMapping("/downloadFile")
    public ResponseEntity<?> downloadFile(@RequestParam("path") String path) {
        String fullPath = "D:/project java/android/user/" + path;

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

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/downloadFolder")
    public ResponseEntity<?> downloadFolder(@RequestParam("path") String path) {
        List<String> listFolder = FileMaker.getAllFolder(path);
        return ResponseEntity.ok(listFolder);
    }

    @GetMapping("/downloadFileOfType")
    public ResponseEntity<?> downloadImage(@RequestParam("username") String username, @RequestParam("loai") String loai) {
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
}
