package net.pmsv.diadiemcaobang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import net.pmsv.diadiemcaobang.Adapter.AdapterDiaDiem;

import java.util.ArrayList;

public class DiaDiemActivity extends AppCompatActivity {
    GridView gvDiaDiem;
    //ArrayList<DiaDiem> arrayDiaDiem;
    AdapterDiaDiem adapterDiaDiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_diem);
        //init();
        /*adapterDiaDiem = new AdapterDiaDiem(this, R.layout.list_diadiem, arrayDiaDiem);
        gvDiaDiem.setAdapter(adapterDiaDiem);*/

        gvDiaDiem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(DiaDiemActivity.this, DiaDiemDetailActivity.class);
                startActivity(i);
            }
        });
    }

    private void init() {
        gvDiaDiem = (GridView) findViewById(R.id.gridviewDiaDiem);
        /*arrayDiaDiem = new ArrayList<>();
        arrayDiaDiem.add(new DiaDiem("CB01", "Thác Bản Giốc",R.drawable.android_logo));
        arrayDiaDiem.add(new DiaDiem("CB02", "Động Ngườm Ngao", R.drawable.android_logo));
        arrayDiaDiem.add(new DiaDiem("CB03", "Cọn nước sông Quây Sơn", R.drawable.android_logo));
        arrayDiaDiem.add(new DiaDiem("CB04", "Di tích chiến thắng chiến dịch biên giới 1950", R.drawable.android_logo));
        arrayDiaDiem.add(new DiaDiem("CB05", "Hồ Thang Hen", R.drawable.android_logo));
        arrayDiaDiem.add(new DiaDiem("CB06", "Khu di tích Quốc gia đặc biệt Pác Bó", R.drawable.android_logo));
        arrayDiaDiem.add(new DiaDiem("CB07", "Khu di tích Quốc gia đặc biệt rừng Trần Hưng Đạo", R.drawable.android_logo));
        arrayDiaDiem.add(new DiaDiem("CB08", "Núi Thủng", R.drawable.android_logo));
        arrayDiaDiem.add(new DiaDiem("CB09", "Suối Lê-Nin", R.drawable.android_logo));*/


    }
}
