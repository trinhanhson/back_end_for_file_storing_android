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
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<?> login(@RequestBody NguoiDung _nguoiDung) {

        NguoiDung nguoiDung = nguoiDungRepo.findByTenDangNhapVaMatKhau(_nguoiDung.getTenDangNhap(), _nguoiDung.getMatKhau());

        if (nguoiDung == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(nguoiDung);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody NguoiDung _nguoiDung) {

        // Check if user with given username already exists in the database
        if (nguoiDungRepo.existsByTenDangNhap(_nguoiDung.getTenDangNhap())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ten dang nhap da ton tai.");
        }

        // Save user to the database
        nguoiDungRepo.save(_nguoiDung);

        if (!FileMaker.MakeFolder("", _nguoiDung.getTenDangNhap())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Loi tao folder.");
        }
        
        _nguoiDung=nguoiDungRepo.findByTenDangNhapVaMatKhau(_nguoiDung.getTenDangNhap(), _nguoiDung.getTenDangNhap());

        return ResponseEntity.ok(_nguoiDung);
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam("username") String username){
        
        if (!nguoiDungRepo.existsByTenDangNhap(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ten dang nhap khong ton tai.");
        }
        
        tepRepo.deleteByNguoiDung(username);
        
        nguoiDungRepo.deleteByTenDangNhap(username);
        
        FileMaker.DeleteFolder(username);
        
        return ResponseEntity.ok("Xoa thanh cong");
    }
}
