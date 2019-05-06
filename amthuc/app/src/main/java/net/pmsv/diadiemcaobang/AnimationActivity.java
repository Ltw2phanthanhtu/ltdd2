package net.pmsv.diadiemcaobang;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by May1 on 6/2/2017.
 */

public class AnimationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        Button btn = (Button) findViewById(R.id.button7);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator moveAnim = ObjectAnimator.ofFloat(v, "Y", 1000);
                moveAnim.setDuration(2000);
                moveAnim.setInterpolator(new BounceInterpolator());
                moveAnim.start();
            }
        });

    }

    public void clockwise(View view) {
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.clockwise);
        image.startAnimation(animation);
    }

    public void zoom(View view) {
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom);
        image.startAnimation(animation1);
    }

    public void fade(View view) {
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.sequential);
        image.startAnimation(animation1);
    }

    public void blink(View view) {
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
        image.startAnimation(animation1);
    }

    public void move(View view) {
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        image.startAnimation(animation1);
    }

    public void slide(View view) {
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
        image.startAnimation(animation1);
    }
}
