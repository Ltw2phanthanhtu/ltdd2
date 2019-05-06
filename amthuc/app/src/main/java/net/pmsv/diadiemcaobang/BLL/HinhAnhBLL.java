package net.pmsv.diadiemcaobang.BLL;

import android.content.Context;
import android.database.Cursor;

import net.pmsv.diadiemcaobang.DAL.HinhAnhDAL;
import net.pmsv.diadiemcaobang.DTO.HinhAnhDTO;

import java.util.List;

/**
 * Created by USER on 6/17/2017.
 */

public class HinhAnhBLL {

    private Context context;

    public HinhAnhBLL(Context context) {
        this.context = context;
    }

    public List<HinhAnhDTO> layDanhSachHinhAnh() {
        HinhAnhDAL hinhAnhDAL = new HinhAnhDAL(context);
        return hinhAnhDAL.layDanhSachHinhAnh();
    }

    public Cursor getHinhAnhByIDGoCChup(String idDiaDiem) {
        HinhAnhDAL hinhAnhDAL = new HinhAnhDAL(context);
        return hinhAnhDAL.getHinhAnhByIDGoCChup(idDiaDiem);
    }
}
