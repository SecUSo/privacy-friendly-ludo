package org.secuso.privacyfriendlyludo.activities;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import org.secuso.privacyfriendlyludo.R;

/**
 * Created by Julchen on 15.05.2017.
 */

public class FieldView extends android.support.v7.widget.AppCompatImageView {
    int myColor;
    boolean add_player;
    LayerDrawable layersDrawable;


    public LayerDrawable getLayersDrawable() {
        return layersDrawable;
    }

    public void setLayersDrawable(LayerDrawable layersDrawable) {
        this.layersDrawable = layersDrawable;
    }

    public FieldView(Context context, AttributeSet attrs, int myColor, Drawable d, boolean add_player) {
        super(context, attrs);
        this.myColor = myColor;
        Resources r = getResources();
        Drawable[] layers = new Drawable[3];
        layers[0] = r.getDrawable(R.drawable.field2);
        layers[1] = r.getDrawable(R.drawable.figure);
        layers[2] = r.getDrawable(R.drawable.field2);
        layersDrawable = new LayerDrawable(layers);

        layersDrawable.mutate();
        layersDrawable.setLayerInset(0, 0, 0, 0, 0);
        layersDrawable.setLayerInset(1, 150, 200, 150, 100);
        layersDrawable.setLayerInset(2, 400, 130, 400, 670);
        this.setImageDrawable(layersDrawable);

            if (d == null) {
                // this.setBackground(d);
                layersDrawable.getDrawable(0).setAlpha(0);
                layersDrawable.getDrawable(1).setAlpha(0);
                layersDrawable.getDrawable(2).setAlpha(0);
            }
            if (myColor != 0) {
                layersDrawable.getDrawable(0).setColorFilter(myColor, PorterDuff.Mode.MULTIPLY);
                layersDrawable.getDrawable(1).setAlpha(0);
                layersDrawable.getDrawable(2).setAlpha(0);
                //calculate opposite color
                float[] myhsv = new float[3];
                int new_color;
                new_color = myColor;
                Color.colorToHSV(new_color, myhsv);
                float hue = myhsv[0];
                if (myhsv[2] == 1)
                {
                    myhsv[2] = 0;
                }
                else
                {
                    hue = (hue + 180) % 360;
                    myhsv[0] = hue;
                }
                new_color = Color.HSVToColor(myhsv);
                // new_color = getResources().getColor(new_color);
                layersDrawable.getDrawable(2).setColorFilter(new_color, PorterDuff.Mode.MULTIPLY);
            }
            this.add_player = add_player;
            if (add_player == true) {
            layersDrawable.getDrawable(1).setAlpha(255);
            layersDrawable.getDrawable(2).setAlpha(0);
           // layersDrawable.getDrawable(1).setColorFilter(myColor, PorterDuff.Mode.MULTIPLY);
            }

    }

    public void markPossibleFigures(int color) {

       // this.myColor = color;
        layersDrawable.getDrawable(2).setAlpha(255);
    }

    public void hidePossibleFigure() {
        layersDrawable.getDrawable(2).setAlpha(0);
    }


}