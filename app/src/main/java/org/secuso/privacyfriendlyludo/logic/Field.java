package org.secuso.privacyfriendlyludo.logic;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import org.secuso.privacyfriendlyludo.R;

/**
 * Created by Julchen on 15.05.2017.
 */

public class Field extends AppCompatImageView {
    boolean filled;
    int xIndex;
    int yIndex;
    Colors myColor;
    public int getyIndex() {
        return yIndex;
    }

    public int getxIndex() {
        return xIndex;
    }

    public Field(Context context, AttributeSet attrs, Colors myColor, boolean filled)
    {
        super(context, attrs);
        this.filled = filled;
        this.myColor = myColor;
        if(filled)
        {
            Drawable d = ContextCompat.getDrawable(context, R.drawable.field2);
            this.setBackground(d);
            this.setBackgroundTintList(ColorStateList.valueOf(myColor.getColor()));
            this.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        }
        else
        {
        }
    }

    // set color and figure of player to the field
    public void addPlayer(int playerColor, Context context)
    {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.figure);
       // this.setForeground(drawable);
        //this.setForegroundTintList(ColorStateList.valueOf(playerColor));
        //this.setForegroundTintList(ColorStateList.valueOf(Color.GREEN));
        //this.setForegroundTintMode(PorterDuff.Mode.MULTIPLY);

    }
}
