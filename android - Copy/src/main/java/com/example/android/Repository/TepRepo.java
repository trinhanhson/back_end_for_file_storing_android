/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.android.Repository;

import com.example.android.model.Tep;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ADMIN
 */
public interface TepRepo extends JpaRepository<Tep, Integer>{
    
    List<Tep> findByNguoiDungAndLoai(String nguoiDung,String loai);
}
