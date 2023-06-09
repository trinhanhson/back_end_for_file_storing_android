package com.example.cloud.model;

import com.google.gson.annotations.SerializedName;

public class Tep {
    @SerializedName("id")
    private int id;

    @SerializedName("ten")
    private String ten;

    @SerializedName("duongDan")
    private String duongDan;

    @SerializedName("duongDanCha")
    private String duongDanCha;

    @SerializedName("loai")
    private String loai;

    @SerializedName("nguoiDung")
    private String nguoiDung;

    public Tep() {
    }

    public Tep(int id, String ten, String duongDan, String duongDanCha, String loai, String nguoiDung) {
        this.id = id;
        this.ten = ten;
        this.duongDan = duongDan;
        this.loai = loai;
        this.nguoiDung = nguoiDung;
        this.duongDanCha=duongDanCha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getDuongDan() {
        return duongDan;
    }

    public void setDuongDan(String duongDan) {
        this.duongDan = duongDan;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getNguoiDung() {
        return nguoiDung;
    }

    public void setNguoiDung(String nguoiDung) {
        this.nguoiDung = nguoiDung;
    }
}
