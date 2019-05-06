package net.pmsv.diadiemcaobang.DTO;

import java.io.Serializable;

/**
 * Created by USER on 6/17/2017.
 */

public class GocChupDTO implements Serializable {
    private int idGocChup;
    private String idDiaDiem;
    private String gocChup;

    public int getIdGocChup() {
        return idGocChup;
    }

    public String getDiaDiem() {
        return idDiaDiem;
    }

    public String getGocChup() {
        return gocChup;
    }

    public void setIdGocChup(int idGocChup) {
        this.idGocChup = idGocChup;
    }

    public void setDiaDiem(String idDiaDiem) {
        this.idDiaDiem = idDiaDiem;
    }

    public void setGocChup(String gocChup) {
        this.gocChup = gocChup;
    }

    public GocChupDTO(int idGocChup, String idDiaDiem, String gocChup) {
        this.idGocChup = idGocChup;
        this.idDiaDiem = idDiaDiem;
        this.gocChup = gocChup;
    }
    public GocChupDTO() {}

}
