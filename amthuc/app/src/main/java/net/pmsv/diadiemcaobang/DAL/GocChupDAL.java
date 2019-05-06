package net.pmsv.diadiemcaobang.DAL;

import android.content.Context;
import android.database.Cursor;

import net.pmsv.diadiemcaobang.DTO.GocChupDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 6/17/2017.
 */

public class GocChupDAL {
    private SQLiteDataAccessHelper dataAccessHelper;
    private Context context;

    public GocChupDAL(Context context) {
        this.context = context;
    }

    public List<GocChupDTO> layDanhSachGocChup() {
        dataAccessHelper = new SQLiteDataAccessHelper(context);
        List<GocChupDTO> list = new ArrayList<>();
        Cursor data = dataAccessHelper.getData("select * from GocChup");
        while (data.moveToNext()) {
            GocChupDTO GC = new GocChupDTO();
            GC.setIdGocChup(data.getInt(0));
            GC.setDiaDiem(data.getString(1));
            GC.setGocChup(data.getString(2));
            list.add(GC);
        }
        return list;
    }

    public List<GocChupDTO> getGocChupByID(String idDiaDiem) {
        dataAccessHelper = new SQLiteDataAccessHelper(context);
        List<GocChupDTO> list = new ArrayList<>();
        Cursor data = dataAccessHelper.getData("select * from GocChup where DiaDiem " + " = '" + idDiaDiem + "'");
        while (data.moveToNext()) {
            GocChupDTO GC = new GocChupDTO();
            GC.setIdGocChup(data.getInt(0));
            GC.setDiaDiem(data.getString(1));
            GC.setGocChup(data.getString(2));
            list.add(GC);
        }
        return list;
    }
}
