package org.secuso.privacyfriendlyludo.logic;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

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

public class Player implements Parcelable, Serializable{

    public boolean isFinished() {
        return isFinished;
    }

    void setFinished(boolean finished) {
        isFinished = finished;
    }

    private boolean isFinished;
    private ArrayList <Figure> figures = new ArrayList<>();
    private int id;
    private int color;
    private String name;
    private int order;
    private int[] statistics = new int[6];

    void setStatistics(int dice_result) {

        statistics[dice_result-1] = statistics[dice_result-1] + 1;
    }

    public boolean isAI() {
        return isAI;
    }

    boolean isAI;

    public int[] getStatistics() {
        return statistics;
    }

    Player()

    {

    }

    public void setAI(boolean AI) {
        isAI = AI;
    }

    public int getOrder() {
        return order;
    }

    public Player(int id, int playercolor, String name, boolean isAI, int order)
    {
        this.order = order;
        this.name = name;
        this.id = id;
        this.color = playercolor;
        this.isAI = isAI;
        this.isFinished = false;
        //create 4 figures
        for (int i=0; i<4; i++)
        {

          //  int index = 100 + i + (4*(id-1));
            int index = 100 + i + (4*(order-1));
         //   int index = i + 4*(id-1);
            figures.add(new Figure(i+1, index));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public ArrayList<Figure> getFigures() {
        return figures;
    }

    public void setColor(int color) {
        this.color = color;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    protected Player(Parcel in) {
        id = in.readInt();
        color = in.readInt();
        name = in.readString();
        isAI = in.readByte() != 0;
        isFinished = in.readByte() != 0;
        figures = in.createTypedArrayList(Figure.CREATOR);
        statistics = new int[in.readInt()];
        in.readIntArray(statistics);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(color);
        dest.writeString(name);
        dest.writeByte((byte) (isAI ? 1 : 0));
        dest.writeByte((byte) (isFinished ? 1 : 0));
        dest.writeTypedList(figures);
        dest.writeInt(statistics.length);        // First write array length
        dest.writeIntArray(statistics);       // Then array content
    }

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
