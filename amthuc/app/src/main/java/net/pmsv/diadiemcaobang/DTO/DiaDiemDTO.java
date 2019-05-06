package net.pmsv.diadiemcaobang.DTO;

import java.io.Serializable;

/**
 * Created by may38 on 5/29/2017.
 */

public class DiaDiemDTO implements Serializable {
    private int tinh;
    private String id;
    private String ten;
    private String thongtin;
    private int sogocchup;
    private String duongdi;
    private String hinhdaidien;
    private String machnho;
    private String thoidiem;

    public String getThoidiem() {
        return thoidiem;
    }

    public void setThoidiem(String thoidiem) {
        this.thoidiem = thoidiem;
    }

    public String getMachnho() {
        return machnho;
    }

    public void setMachnho(String machnho) {
        this.machnho = machnho;
    }

    public String getHinhdaidien() {
        return hinhdaidien;
    }

    public void setHinhdaidien(String hinhdaidien) {
        this.hinhdaidien = hinhdaidien;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTinh(int tinh) {
        this.tinh = tinh;
    }

    public int getTinh() {
        return tinh;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getTen() {
        return ten;
    }

    public int getSogocchup() {
        return sogocchup;
    }

    public void setSogocchup(int sogocchup) {
        this.sogocchup = sogocchup;
    }

    public String getDuongdi() {
        return duongdi;
    }

    public void setDuongdi(String duongdi) {
        this.duongdi = duongdi;
    }

    public String getThongtin() {
        return thongtin;
    }

    public void setThongtin(String thongtin) {
        this.thongtin = thongtin;
    }

    public DiaDiemDTO(String id, int tinh, String ten, String thongtin, String thoidiem, String duongdi, String machnho, int sogocchup, String hinhdaidien) {
        this.id = id;
        this.tinh = tinh;
        this.ten = ten;
        this.thongtin = thongtin;
        this.duongdi = duongdi;
        this.thoidiem = thoidiem;
        this.machnho = machnho;
        this.sogocchup = sogocchup;
        this.hinhdaidien = hinhdaidien;
    }

    public DiaDiemDTO() {

    }

}
