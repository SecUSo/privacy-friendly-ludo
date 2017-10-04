package org.secuso.privacyfriendlyludo.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;


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
        // field border
        layers[0] = ContextCompat.getDrawable(context, R.drawable.field_color_border);
        // field content
        layers[1] = ContextCompat.getDrawable(context, R.drawable.field);
        layers[2] = ContextCompat.getDrawable(context, R.drawable.figure4);
        layersDrawable = new LayerDrawable(layers);

        layersDrawable.mutate();
        layersDrawable.setLayerInset(0, 0, 0, 0, 0);
        //layersDrawable.setLayerInset(1, 0, 0, 0, 0);
        //layersDrawable.setLayerInset(1, 15, 15, 15, 15);
        layersDrawable.setLayerInset(1, 50, 50, 50, 50);
        layersDrawable.setLayerInset(2, 200, 115, 200, 115);
        //layersDrawable.setLayerInset(2, 10, 10, 10, 10);
        //layersDrawable.setLayerInset(2, 400, 130, 400, 670);
        this.setImageDrawable(layersDrawable);

            if (d == null) {
                // this.setBackground(d);
                layersDrawable.getDrawable(0).setAlpha(0);
                layersDrawable.getDrawable(1).setAlpha(0);
                layersDrawable.getDrawable(2).setAlpha(0);
            }
            if (myColor != 0) {
                if(myColor==Color.WHITE)
                {
                    layersDrawable.getDrawable(0).setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
                }
                else
                {
                    layersDrawable.getDrawable(0).setColorFilter(myColor, PorterDuff.Mode.MULTIPLY);
                }

                layersDrawable.getDrawable(1).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                layersDrawable.getDrawable(2).setAlpha(0);
            }
            this.add_player = add_player;
            if (add_player)
            {
                layersDrawable.getDrawable(2).setAlpha(255);
            }

    }

    public void markPossibleFigure(int color) {

        this.myColor = color;
       layersDrawable.getDrawable(1).setColorFilter(color, PorterDuff.Mode. SRC_IN );
        layersDrawable.getDrawable(2).setColorFilter(color, PorterDuff.Mode.DST_ATOP);
        //layersDrawable.getDrawable(2).setAlpha(255);
    }

    public void hidePossibleFigure(int color) {
        layersDrawable.getDrawable(1).setColorFilter(ContextCompat.getColor(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
        layersDrawable.getDrawable(2).clearColorFilter();
        layersDrawable.getDrawable(2).setColorFilter(color, PorterDuff.Mode.MULTIPLY);


    }


}