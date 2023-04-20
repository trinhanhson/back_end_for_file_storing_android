/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.android.model;

import jakarta.persistence.*;
import lombok.*;
/**
 *
 * @author ADMIN
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tep")
public class Tep {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ten")
    private String ten;

    @Column(name = "duong_dan")
    private String duongDan;
    
    @Column(name = "loai")
    private String loai;
    
    @Column(name = "nguoi_dung")
    private String nguoiDung;
}
