/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.android.controller;

import com.example.android.Repository.NguoiDungRepo;
import com.example.android.Repository.TepRepo;
import com.example.android.model.NguoiDung;
import com.example.android.utility.FileMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADMIN
 */
@Controller
public class NguoiDungController {

    @Autowired
    private NguoiDungRepo nguoiDungRepo;
    
    @Autowired
    private TepRepo tepRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("username") String username,
            @RequestParam("password") String password) {

        NguoiDung nguoiDung = nguoiDungRepo.findByTenDangNhapVaMatKhau(username, password);

        if (nguoiDung == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(nguoiDung);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestParam("username") String username,
            @RequestParam("password") String password) {

        // Check if user with given username already exists in the database
        if (nguoiDungRepo.existsByTenDangNhap(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ten dang nhap da ton tai.");
        }

        NguoiDung nguoiDung = new NguoiDung();

        nguoiDung.setTenDangNhap(username);
        nguoiDung.setMatKhau(password);

        // Save user to the database
        nguoiDungRepo.save(nguoiDung);

        if (!FileMaker.MakeFolder("", username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Loi tao folder.");
        }

        return ResponseEntity.ok(nguoiDung);
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam("username") String username){
        
        if (!nguoiDungRepo.existsByTenDangNhap(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ten dang nhap khong ton tai.");
        }
        
        tepRepo.deleteByNguoiDung(username);
        
        nguoiDungRepo.deleteByTenDangNhap(username);
        
        FileMaker.DeleteFileAndFolder(username);
        
        return ResponseEntity.ok("Xoa thanh cong");
    }
}
