package org.secuso.privacyfriendlyludo.logic;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by Julchen on 31.05.2017.
 */

public class GameField {
    int x=0;
    int y=0;

    public int getFigure_id() {
        return figure_id;
    }

    public void setFigure_id(int figure_id) {
        this.figure_id = figure_id;
        Log.i("tag", "Index: " + this.getIndex() + " Figure_id: " + this.figure_id);
    }

    int figure_id;

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
        Log.i("tag", "Index: " + this.getIndex() + " Player_id: " + this.player_id);
    }

    int player_id = 0;
    int index;

    public int getMyColor() {
        return myColor;
    }

    public void setMyColor(int myColor) {
        this.myColor = myColor;
    }

    private int myColor;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public GameField(int index, int y, int x, int playerId, int figure_id, int myColor) {
        this.x = x;
        this.y = y;
        this.figure_id = figure_id;
        this.index = index;
        this.myColor = myColor;
        this.player_id = playerId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }



}
