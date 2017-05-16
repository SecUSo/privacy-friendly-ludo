package org.secuso.privacyfriendlyludo.logic;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import org.secuso.privacyfriendlyludo.R;

/**
 * Created by Julchen on 15.05.2017.
 */

public class BoardImageView extends AppCompatImageView {
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

    public BoardImageView(Context context, AttributeSet attrs, Colors myColor, boolean filled)
    {
        super(context, attrs);
        this.filled = filled;
        this.myColor = myColor;
        if(filled)
        {
            Drawable d = getResources().getDrawable(R.drawable.field2);
            this.setBackground(d);
            this.setBackgroundTintList(ColorStateList.valueOf(myColor.getColor()));
            this.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        }
        else
        {
        }
    }

    // set color and figure of player to the field
    public void addPlayer(int playerColor)
    {
        Drawable drawable = getResources().getDrawable(R.drawable.figure);
        this.setForeground(drawable);
        //this.setForegroundTintList(ColorStateList.valueOf(playerColor));
        this.setForegroundTintList(ColorStateList.valueOf(Color.GREEN));
        this.setForegroundTintMode(PorterDuff.Mode.MULTIPLY);

    }
}
