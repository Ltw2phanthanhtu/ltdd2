package net.pmsv.diadiemcaobang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

import su.levenetc.android.textsurface.TextSurface;

/**
 * Created by USER on 6/26/2017.
 */

public class
FlashScreenActivity extends AppCompatActivity {
    TextSurface textSurface;
    private String typeLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        hide title, full screen, hide action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
        typeLogin = pre.getString("typelogin", "none");

        setContentView(R.layout.activity_flash_screen);
        textSurface = (TextSurface) findViewById(R.id.text_surface);
        textSurface.postDelayed(new Runnable() {
            @Override public void run() {
                show();
            }
        }, 100);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(typeLogin.equals("none"))
                {
                    startActivity(new Intent(FlashScreenActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(FlashScreenActivity.this, HomeActivity.class));
                    finish();
                }

            }
        }, 9000);


    }

    private void show() {
        textSurface.reset();
        TravelThumper.play(textSurface, getAssets());
    }
}
