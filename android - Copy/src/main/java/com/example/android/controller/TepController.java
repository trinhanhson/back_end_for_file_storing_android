/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.android.controller;

import com.example.android.Repository.TepRepo;
import com.example.android.model.Tep;
import com.example.android.utility.FileMaker;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import org.apache.tomcat.util.http.fileupload.IOUtils;
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
import org.springframework.web.bind.annotation.PathVariable;
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

    private FileInputStream inputStream;

    private boolean isDelete = false;

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
        tep.setDuongDan(path + "/" + fileName);
        tep.setDuongDanCha(path);
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
            Tep tep = new Tep();
            tep.setTen(folder);
            tep.setDuongDan(path + "/" + folder);
            tep.setDuongDanCha(path);
            tep.setLoai("thu muc");
            tep.setNguoiDung(path.split("/")[0]);

            tepRepo.save(tep);
            return ResponseEntity.ok("Thu muc da duoc tao.");
        }

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
        List<Tep> listNameTep = tepRepo.findByDuongDanCha(path);
        return ResponseEntity.ok(listNameTep);
    }

    @GetMapping("/downloadOneFile")
    public ResponseEntity<?> downloadOneFile(@RequestParam("path") String path) throws UnsupportedEncodingException {
        File file = new File(parentPath + path);

        Resource resource = new FileSystemResource(file);
        String aString=URLEncoder.encode(file.getName(), "UTF-8");

        // Trả về đối tượng ResponseEntity chứa tệp tin và các đầu mục HTTP cần thiết
            return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +aString + "\"")
            .contentLength(file.length())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }

    @PostMapping("/deleteFile")
    public ResponseEntity<?> deleteFile(@RequestParam("path") String path) throws IOException {

        if (tepRepo.findByDuongDan(path) == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (!FileMaker.DeleteFile(parentPath + path)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        tepRepo.deleteByDuongDan(path);

        isDelete = true;

        return ResponseEntity.ok("Xoa file thanh cong");
    }

    @GetMapping("/downloadNameFileOfType")
    public ResponseEntity<?> downloadNameFileOfType(@RequestParam("username") String username, @RequestParam("loai") String loai) {
        List<Tep> listTep = tepRepo.findByNguoiDungAndLoai(username, loai);

        return ResponseEntity.ok(listTep);
    }

    @GetMapping("/image/{imageName}")
    public void getImage(HttpServletResponse response, @PathVariable String imageName) throws IOException {
        File file = new File("C:/Users/ADMIN/Pictures/" + imageName);
        FileInputStream inputStream = new FileInputStream(file);
        response.setContentType(Files.probeContentType(file.toPath()));
        IOUtils.copy(inputStream, response.getOutputStream());
    }

    @GetMapping("/video/{videoName}")
    public void getVideo(HttpServletResponse response, @PathVariable String videoName) throws IOException {
        File file = new File("D:/t1/" + videoName);
        FileInputStream inputStream = new FileInputStream(file);
        response.setContentType(Files.probeContentType(file.toPath()));
        IOUtils.copy(inputStream, response.getOutputStream());
    }

//    @GetMapping("/getFile/{filePath}")
//    public void getFile(HttpServletResponse response, @PathVariable String filePath) throws IOException {
//        File file = new File(parentPath + filePath);
//        FileInputStream inputStream = new FileInputStream(file);
//        response.setContentType(Files.probeContentType(file.toPath()));
//        IOUtils.copy(inputStream, response.getOutputStream());
//    }
    @GetMapping("/getFile")
    public void getFile(HttpServletResponse response, @RequestParam("filePath") String filePath) throws IOException {

        isDelete = false;
        File file = new File(parentPath + filePath);
        InputStream inputStream = new FileInputStream(file);
        response.setContentType(Files.probeContentType(file.toPath()));

        ServletOutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                outputStream.flush();
                if (isDelete) {
                    inputStream.close();
                    return;
                }
            }
        } finally {
            outputStream.close();
            inputStream.close();
        }
    }

    @GetMapping("/getAllFile")
    public ResponseEntity<?> getAllFile(@RequestParam("username") String username) {
        List<Tep> listNameTep = tepRepo.findByNguoiDungOnLyFile(username);
        return ResponseEntity.ok(listNameTep);

    }
}
