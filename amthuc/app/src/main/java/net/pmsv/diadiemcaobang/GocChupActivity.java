package net.pmsv.diadiemcaobang;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import net.pmsv.diadiemcaobang.DAL.HinhAnhDAL;
import net.pmsv.diadiemcaobang.DTO.DiaDiemDTO;
import net.pmsv.diadiemcaobang.DTO.HinhAnhDTO;

import java.util.HashMap;
import java.util.List;

public class GocChupActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {
    TextView textViewGocChup, txtTitleGocChup;
    Toolbar toolbar;
    ImageView lineBelowText;

    DiaDiemDTO diaDiemDTO;
    Cursor cusorDataHinhAnh;
    SliderLayout sliderShow;
    HashMap<String,Integer> file_maps;
    HinhAnhDAL hinhAnhDAL;
    private List<HinhAnhDTO> hinhAnhList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_goc_chup);

        init();
        setDataSliderShow();
        getEventSliderShow();
        sliderShow.setCurrentPosition(file_maps.size() - 1);

    }

    private void init(){

        textViewGocChup = (TextView) findViewById(R.id.textViewGocChup);
        txtTitleGocChup = (TextView) findViewById(R.id.txtTitleGocChup);
        sliderShow = (SliderLayout) findViewById(R.id.sliderGocChup);
        lineBelowText = (ImageView) findViewById(R.id.line);

        lineBelowText.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels - 400;

//        set SliderShow
        sliderShow.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        sliderShow.setDuration(15000);

//        get diaDiemDTO and set toolbar
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        diaDiemDTO = (DiaDiemDTO) intent.getSerializableExtra("data");
        toolbar = (Toolbar) findViewById(R.id.toolbarGocChup);

        toolbar.setTitle(diaDiemDTO.getTen());
        setSupportActionBar(toolbar);
        toolbar.setPadding(20, 0, 0, 0);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GocChupActivity.super.onBackPressed();
            }
        });

        Toast.makeText(GocChupActivity.this, diaDiemDTO.getTen() + " " + diaDiemDTO.getId()
                ,Toast.LENGTH_SHORT).show();

//        get dataHinhAnh
        hinhAnhDAL = new HinhAnhDAL(this);
        cusorDataHinhAnh = hinhAnhDAL.getHinhAnhByIDGoCChup(diaDiemDTO.getId());
        file_maps = new HashMap<String, Integer>();

    }

//    get and set data SliderShow
    private void setDataSliderShow() {
        while (cusorDataHinhAnh.moveToNext())
        {
            Log.d("asd",cusorDataHinhAnh.getString(5) + " -- " + cusorDataHinhAnh.getString(2));
            int id = getResources().getIdentifier(cusorDataHinhAnh.getString(2), "drawable", getPackageName());
            file_maps.put(cusorDataHinhAnh.getString(5), id);
        }

        for(String name : file_maps.keySet()){
            DefaultSliderView defaultSliderView = new DefaultSliderView(this);

            // initialize a SliderLayout
            defaultSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            defaultSliderView.bundle(new Bundle());
            defaultSliderView.getBundle()
                    .putString("extra",name);

            sliderShow.addSlider(defaultSliderView);
        }
    }


    public void getEventSliderShow(){
        sliderShow.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textViewGocChup.setText(sliderShow.getCurrentSlider().getDescription().toString());
                txtTitleGocChup.setText("Góc Chụp Thứ " + (position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

//    envent of slidershow
    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }
    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

}
