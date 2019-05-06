package net.pmsv.diadiemcaobang.DAL;

import android.content.Context;
import android.database.Cursor;

import net.pmsv.diadiemcaobang.DTO.HinhAnhDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 6/17/2017.
 */

public class HinhAnhDAL {
    private SQLiteDataAccessHelper dataAccessHelper;
    private Context context;
    Cursor data;
    public HinhAnhDAL(Context context) {
        this.context = context;
    }

    public List<HinhAnhDTO> layDanhSachHinhAnh() {
        dataAccessHelper = new SQLiteDataAccessHelper(context);
        List<HinhAnhDTO> list = new ArrayList<>();
        Cursor data = dataAccessHelper.getData("select * from HinhAnh");
        while (data.moveToNext()) {
            HinhAnhDTO HA = new HinhAnhDTO();
            HA.setIdHinhAnh(data.getInt(0));
            HA.setIdGocChup(data.getInt(1));
            HA.setTenHinh(data.getString(2));
            list.add(HA);
        }
        return list;
    }

    public Cursor getHinhAnhByIDGoCChup(String idDiaDiem) {
        dataAccessHelper = new SQLiteDataAccessHelper(context);
        Cursor data = dataAccessHelper.getData("select * from HinhAnh, GocChup where GocChup.DiaDiem = '" + idDiaDiem +"' and GocChup.id = HinhAnh.GocChup");

        return data;
    }
}
