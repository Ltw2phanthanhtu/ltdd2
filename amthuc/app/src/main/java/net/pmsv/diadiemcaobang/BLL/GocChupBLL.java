package net.pmsv.diadiemcaobang.BLL;

import android.content.Context;

import net.pmsv.diadiemcaobang.DAL.GocChupDAL;
import net.pmsv.diadiemcaobang.DTO.GocChupDTO;

import java.util.List;

/**
 * Created by USER on 6/17/2017.
 */

public class GocChupBLL {
    private Context context;

    public GocChupBLL(Context context) {
        this.context = context;
    }

    public List<GocChupDTO> layDanhSachGocChup() {
        GocChupDAL gocChupDAL = new GocChupDAL(context);
        return gocChupDAL.layDanhSachGocChup();
    }

    public List<GocChupDTO> getGocChupByID(String idDiaDiem) {
        GocChupDAL gocChupDAL = new GocChupDAL(context);
        return gocChupDAL.getGocChupByID(idDiaDiem);
    }
}
