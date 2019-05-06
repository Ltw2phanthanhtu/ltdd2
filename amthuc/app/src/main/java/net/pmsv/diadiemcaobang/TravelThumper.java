package net.pmsv.diadiemcaobang;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.Circle;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Parallel;
import su.levenetc.android.textsurface.animations.Rotate3D;
import su.levenetc.android.textsurface.animations.Sequential;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.animations.TransSurface;
import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.contants.Direction;
import su.levenetc.android.textsurface.contants.Pivot;
import su.levenetc.android.textsurface.contants.Side;

/**
 * Created by USER on 6/26/2017.
 */

public class TravelThumper {
    public static void play(TextSurface textSurface, AssetManager assetManager) {

        final Typeface robotoBlack = Typeface.DEFAULT;
        Paint paint = new Paint();
        paint.setTypeface(robotoBlack);
        paint.setAntiAlias(true);

        final Typeface robotoBlack2 = Typeface.createFromAsset(assetManager, "fonts/harabara.ttf");
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setTypeface(robotoBlack2);

        Text textBDTRAVEL = TextBuilder
                .create("Ẩm Thực 3M")
                .setPaint(paint2)
                .setSize(72)
                .setAlpha(0)
                .setColor(Color.parseColor("#F50057"))
                .setPosition(Align.SURFACE_CENTER).build();

        Text textDuLichBinhDinh = TextBuilder
                .create("Mọi Miền Tổ Quốc")
                .setPaint(paint2)
                .setSize(20)
                .setAlpha(5)
                .setColor(Color.parseColor("#795a3b"))
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textBDTRAVEL).build();

        Text textDaCoToi = TextBuilder
                .create("\n")
                .setPaint(paint2)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textDuLichBinhDinh).build();

        Text textTatCa = TextBuilder
                .create("ăn 1 lần")
                .setPaint(paint2)
                .setSize(25)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textDaCoToi).build();

        Text textMoiThuODay = TextBuilder
                .create("nhớ cả đời")
                .setPaint(paint2)
                .setSize(30)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textTatCa).build();

        textSurface.play(
                new Sequential(
                        ShapeReveal.create(textBDTRAVEL, 750, SideCut.show(Side.LEFT), false),
                        new Parallel(ShapeReveal.create(textBDTRAVEL, 600, SideCut.hide(Side.LEFT), false), new Sequential(Delay.duration(300), ShapeReveal.create(textBDTRAVEL, 600, SideCut.show(Side.LEFT), false))),
                        new Parallel(TransSurface.toCenter(textDuLichBinhDinh, 500), Rotate3D.showFromSide(textDuLichBinhDinh, 750, Pivot.TOP)),
                        Delay.duration(500),
                        new Parallel(
                                new TransSurface(1500, textMoiThuODay, Pivot.CENTER),
                                new Sequential(
                                        new Sequential(ShapeReveal.create(textTatCa, 1000, Circle.show(Side.CENTER, Direction.OUT), false)),
                                        new Parallel(TransSurface.toCenter(textMoiThuODay, 1000), Rotate3D.showFromSide(textMoiThuODay, 500, Pivot.TOP))
                                )
                        ),
                        Delay.duration(1000),
                        new Parallel(ShapeReveal.create(textBDTRAVEL, 600, SideCut.hide(Side.LEFT), false), new Sequential(Delay.duration(300), ShapeReveal.create(textBDTRAVEL, 600, SideCut.show(Side.RIGHT), false)))

                )
        );
    }
}