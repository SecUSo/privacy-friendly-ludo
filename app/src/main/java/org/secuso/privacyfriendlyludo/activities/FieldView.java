package org.secuso.privacyfriendlyludo.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;


import org.secuso.privacyfriendlyludo.R;

/*
  @author: Julia Schneider

  This file is part of the Game Ludo.

 Ludo is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 You should have received a copy of the GNU General Public License
 along with Ludo.  If not, see <http://www.gnu.org/licenses/>.
 */


public class FieldView extends android.support.v7.widget.AppCompatImageView {
    int myColor;
    boolean add_player;
    LayerDrawable layersDrawable;


    public LayerDrawable getLayersDrawable() {
        return layersDrawable;
    }


    public FieldView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FieldView(Context context, AttributeSet attrs, int myColor, Drawable d, boolean add_player) {
        super(context, attrs);
        this.myColor = myColor;
        Drawable[] layers = new Drawable[3];
        layers[0] = ContextCompat.getDrawable(context, R.drawable.field2);
        layers[1] = ContextCompat.getDrawable(context, R.drawable.figure);
        layers[2] = ContextCompat.getDrawable(context, R.drawable.field2);
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
            if (add_player) {
            layersDrawable.getDrawable(1).setAlpha(255);
            layersDrawable.getDrawable(2).setAlpha(0);
           // layersDrawable.getDrawable(1).setColorFilter(myColor, PorterDuff.Mode.MULTIPLY);
            }

    }

    public void markPossibleFigure(int color) {

        this.myColor = color;
        int[] androidColors = getResources().getIntArray(R.array.playerColors);
       // int[] highlightColors = getResources().getIntArray(R.array.highlightColors);
       // int neonColor=0;
       /* for(int i=0; i<androidColors.length; i++)
        {
            if ( color == androidColors[i])
            {
                 neonColor = highlightColors[i];
            }
        }
        layersDrawable.getDrawable(1).setColorFilter(neonColor, PorterDuff.Mode.MULTIPLY); */
        //layersDrawable.getDrawable(2).setAlpha(255);
    }

    public void hidePossibleFigure(int color) {
        layersDrawable.getDrawable(1).setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }


}