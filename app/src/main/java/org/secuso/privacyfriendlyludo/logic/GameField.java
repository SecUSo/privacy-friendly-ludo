package org.secuso.privacyfriendlyludo.logic;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

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


public class GameField implements Parcelable, Serializable{
    private int x=0;
    private int y=0;

    public int getFigure_id() {
        return figure_id;
    }

    public void setFigure_id(int figure_id) {
        this.figure_id = figure_id;
    }

    private int figure_id;

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    private int player_id;
    private int index;

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

    private GameField(Parcel in) {
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
