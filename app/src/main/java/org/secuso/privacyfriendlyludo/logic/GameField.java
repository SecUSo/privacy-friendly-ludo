package org.secuso.privacyfriendlyludo.logic;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Julchen on 31.05.2017.
 */

public class GameField implements Parcelable, Serializable{
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

    private int myColor;

    public int getIndex() {
        return index;
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

    public int getY() {
        return y;
    }

    protected GameField(Parcel in) {
        x = in.readInt();
        y = in.readInt();
        figure_id = in.readInt();
        player_id = in.readInt();
        index = in.readInt();
        myColor = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeInt(figure_id);
        dest.writeInt(player_id);
        dest.writeInt(index);
        dest.writeInt(myColor);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GameField> CREATOR = new Parcelable.Creator<GameField>() {
        @Override
        public GameField createFromParcel(Parcel in) {
            return new GameField(in);
        }

        @Override
        public GameField[] newArray(int size) {
            return new GameField[size];
        }
    };
}
