package org.secuso.privacyfriendlyludo.logic;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
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

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BoardImageView(Context context, AttributeSet attrs, Colors myColor, boolean filled)
    {
        super(context, attrs);
        this.filled = filled;
        this.myColor = myColor;
        if(filled)
        {
            Drawable d = getResources().getDrawable(R.drawable.field);
            //this.setBackgroundColor(Color.BLACK);
            this.setImageResource(R.drawable.field2);
            //this.setBackgroundResource(R.drawable.field);
          //  this.setColorFilter(ContextCompat.getColor(getContext(), R.color.green));
            this.setColorFilter(myColor.getColor(), PorterDuff.Mode.SRC_IN);
            this.setForeground(d);
        }
        else
        {
           // this.setBackgroundColor(Color.middlegrey);
        }
    }

    public void addPlayer()
    {
        // set color and figure of player to the field

    }
}
