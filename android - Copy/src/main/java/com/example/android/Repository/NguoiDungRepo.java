/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.android.Repository;

import com.example.android.model.NguoiDung;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ADMIN
 */
@Repository
public interface NguoiDungRepo extends JpaRepository<NguoiDung, Integer> {

    @Query("SELECT nd FROM NguoiDung nd WHERE nd.tenDangNhap = :tenDangNhap AND nd.matKhau = :matKhau")
    NguoiDung findByTenDangNhapVaMatKhau(@Param("tenDangNhap") String tenDangNhap, @Param("matKhau") String matKhau);

    boolean existsByTenDangNhap(String tenDangNhap);
    
    @Transactional
    Integer deleteByTenDangNhap(String tenDangNhap);
}
