/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.android.Repository;

import com.example.android.model.Tep;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author ADMIN
 */
public interface TepRepo extends JpaRepository<Tep, Integer>{
    
    List<Tep> findByNguoiDungAndLoai(String nguoiDung,String loai);
    
    @Transactional
    Integer deleteByNguoiDung(String nguoiDung);
    
    @Transactional
    Integer deleteByDuongDan(String duongDan);
    
    boolean existsByTenAndNguoiDung(String ten,String nguoiDung);
    
    @Query("SELECT tep FROM Tep tep WHERE tep.duongDanCha = :duongDan")
    List<Tep> findByDuongDanCha(@Param("duongDan") String duongDan);
    
    @Query("SELECT tep FROM Tep tep WHERE tep.duongDanCha = :duongDan AND tep.loai <> 'thu muc'")
    List<Tep> findByNguoiDungOnLyFile(@Param("duongDan") String duongDan);

    public Tep findByDuongDan(String path);
}
