package net.pmsv.diadiemcaobang;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import net.pmsv.diadiemcaobang.Adapter.AdaperChiTiet;
import net.pmsv.diadiemcaobang.BLL.GocChupBLL;
import net.pmsv.diadiemcaobang.DAL.HinhAnhDAL;
import net.pmsv.diadiemcaobang.DTO.DiaDiemDTO;
import net.pmsv.diadiemcaobang.DTO.GocChupDTO;
import net.pmsv.diadiemcaobang.DTO.HinhAnhDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiaDiemDetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    AdaperChiTiet adaperChiTiet;
    SliderLayout sliderShow;
    ExpandableListView elvDiaDiem;
    Toolbar toolbar;

    DiaDiemDTO diaDiemDTO;
    List<GocChupDTO> gocChupDTO;
    GocChupBLL gocChupBLL;
    HinhAnhDAL hinhAnhDAL;

    Cursor cusorDataHinhAnh;
    HashMap<String,Integer> file_maps;
    private List<HinhAnhDTO> hinhAnhList;

    HashMap<String, ArrayList<String>> hmdiaDiemItem;
    ArrayList<String> arrLvHeaderDiaDiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dia_diem_detail);


        init();
        getEventSliderShow();
        setDataOfExListview();
        sliderShow.setCurrentPosition(file_maps.size() - 1);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        adaperChiTiet =  new AdaperChiTiet(this, arrLvHeaderDiaDiem, hmdiaDiemItem);

        // Chỉ định Adapter cho ExpandableListView
        elvDiaDiem.setIndicatorBounds(width - GetPixelFromDips(20), width - GetPixelFromDips(40));
        elvDiaDiem.setAdapter(adaperChiTiet);
        elvDiaDiem.expandGroup(0);
        elvDiaDiem.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(groupPosition == 3) {// neu click vao item cua goc chup
                    sliderShow.stopAutoCycle();
                    sliderShow.setCurrentPosition(childPosition + 1);
                }
                return false;
            }
        });
        elvDiaDiem.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                if(groupPosition == 3)
                {
                    sliderShow.startAutoCycle();
                }
            }
        });
    }

    private void init() {

        sliderShow = (SliderLayout) findViewById(R.id.sliderDiaDiemDetail);
        toolbar = (Toolbar) findViewById(R.id.toolbarChiTiet);
        elvDiaDiem = (ExpandableListView) findViewById(R.id.elvDiaDiemDetail);

        Intent intent = getIntent();
        diaDiemDTO = (DiaDiemDTO) intent.getSerializableExtra("dataDiaDiem");

        //        set toolbar
        toolbar.setTitle(diaDiemDTO.getTen());
        setSupportActionBar(toolbar);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaDiemDetailActivity.super.onBackPressed();
            }
        });

        //        get dataHinhAnh, dataGocChup
        hinhAnhDAL = new HinhAnhDAL(this);
        cusorDataHinhAnh = hinhAnhDAL.getHinhAnhByIDGoCChup(diaDiemDTO.getId());
        file_maps = new HashMap<String, Integer>();

        //        set SliderShow
        sliderShow.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        sliderShow.setDuration(15000);
        setDataSliderShow();

        //        get goc chup by idDiaDiem
        gocChupBLL = new GocChupBLL(this);
        gocChupDTO = new ArrayList<>();
        gocChupDTO = gocChupBLL.getGocChupByID(diaDiemDTO.getId());
    }

    @Override
    public void onBackPressed() {
        boolean haveGroupExpand = false;
        for (int i = 0; i < elvDiaDiem.getCount(); i++)
        {
            if (elvDiaDiem.isGroupExpanded(i) == true)
            {
                haveGroupExpand = true;
                elvDiaDiem.collapseGroup(i);
            }
        }
        if (haveGroupExpand == false)
        {
            super.onBackPressed();
        }
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public void setDataOfExListview()
    {
        //        test
        arrLvHeaderDiaDiem = new ArrayList<>();
        arrLvHeaderDiaDiem.add("Thông Tin");
        arrLvHeaderDiaDiem.add("Thời Điểm");
        arrLvHeaderDiaDiem.add("Đường Đi");
        arrLvHeaderDiaDiem.add("Góc Chụp");
        arrLvHeaderDiaDiem.add("Mách Nhỏ");

        hmdiaDiemItem = new HashMap<>();

        ArrayList<String> thongTin = new ArrayList<>();
        thongTin.add(diaDiemDTO.getThongtin());
        ArrayList<String> thoiDiem = new ArrayList<>();
        thoiDiem.add(diaDiemDTO.getThoidiem());
        ArrayList<String> duongDi = new ArrayList<>();
        duongDi.add(diaDiemDTO.getDuongdi());
        ArrayList<String> gocChup = new ArrayList<>();
        for(int i = 0; i < gocChupDTO.size(); i++)
        {
            gocChup.add("Góc chụp thứ " + (i + 1) + ": " + gocChupDTO.get(i).getGocChup());
        }
        ArrayList<String> machNho = new ArrayList<>();
        machNho.add(diaDiemDTO.getMachnho());

        hmdiaDiemItem.put(arrLvHeaderDiaDiem.get(0), thongTin);
        hmdiaDiemItem.put(arrLvHeaderDiaDiem.get(1), thoiDiem);
        hmdiaDiemItem.put(arrLvHeaderDiaDiem.get(2), duongDi);
        hmdiaDiemItem.put(arrLvHeaderDiaDiem.get(3), gocChup);
        hmdiaDiemItem.put(arrLvHeaderDiaDiem.get(4), machNho);






    }

    private void setDataSliderShow() {
        int id;
        id = getResources().getIdentifier(diaDiemDTO.getHinhdaidien(), "drawable", getPackageName());
        file_maps.put(diaDiemDTO.getTen(), id);
        while (cusorDataHinhAnh.moveToNext())
        {
            Log.d("asd",cusorDataHinhAnh.getString(5) + " -- " + cusorDataHinhAnh.getString(2));
            id = getResources().getIdentifier(cusorDataHinhAnh.getString(2), "drawable", getPackageName());
            file_maps.put(cusorDataHinhAnh.getString(5), id);
        }


        DefaultSliderView defaultSliderView = new DefaultSliderView(this);

        for(String name : file_maps.keySet()){
            // initialize a SliderLayout
            defaultSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            sliderShow.addSlider(defaultSliderView);
            defaultSliderView = new DefaultSliderView(this);
        }
    }

    public void getEventSliderShow(){
        sliderShow.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("asd when scroll", sliderShow.getCurrentSlider().getDescription().toString());
//                textViewGocChup.setText(sliderShow.getCurrentSlider().getDescription().toString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
