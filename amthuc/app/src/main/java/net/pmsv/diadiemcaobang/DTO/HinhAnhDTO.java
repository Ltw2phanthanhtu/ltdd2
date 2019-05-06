package net.pmsv.diadiemcaobang.DTO;

import java.io.Serializable;

/**
 * Created by USER on 6/17/2017.
 */

public class HinhAnhDTO implements Serializable {
    private int idHinhAnh;
    private int idGocChup;
    private String tenHinh;

    public HinhAnhDTO(int idHinhAnh, int idGocChup, String tenHinh) {
        this.idHinhAnh = idHinhAnh;
        this.idGocChup = idGocChup;
        this.tenHinh = tenHinh;
    }

    public HinhAnhDTO() {}

    public void setIdHinhAnh(int idHinhAnh) {

        this.idHinhAnh = idHinhAnh;
    }

    public void setIdGocChup(int idGocChup) {
        this.idGocChup = idGocChup;
    }

    public void setTenHinh(String tenHinh) {
        this.tenHinh = tenHinh;
    }

    public int getIdHinhAnh() {

        return idHinhAnh;
    }

    public int getIdGocChup() {
        return idGocChup;
    }

    public String getTenHinh() {
        return tenHinh;
    }
}
