package net.pmsv.diadiemcaobang.DTO;

/**
 * Created by May1 on 5/31/2017.
 */

public class ChiTiet {
    private String ten;
    private int hinh, id;

    public ChiTiet(String ten, int hinh, int id) {
        this.ten = ten;
        this.hinh = hinh;
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getHinh() {
        return hinh;
    }

    public void setHinh(int hinh) {
        this.hinh = hinh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
