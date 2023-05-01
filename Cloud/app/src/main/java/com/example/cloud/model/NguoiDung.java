package com.example.cloud.model;

import com.google.gson.annotations.SerializedName;

public class NguoiDung {
    @SerializedName("id")
    private int id;
    @SerializedName("tenDangNhap")
    private String tenDangNhap;
    @SerializedName("matKhau")
    private String matKhau;

    public NguoiDung() {
    }

    public NguoiDung(int id, String tenDangNhap, String matKhau) {
        this.id = id;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
}
