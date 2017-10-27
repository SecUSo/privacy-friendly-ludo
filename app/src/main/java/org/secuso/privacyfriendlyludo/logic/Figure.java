package org.secuso.privacyfriendlyludo.logic;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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


public class Figure implements Parcelable, Serializable{

    private int field_position_index;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public int getCount_steps() {
        return count_steps;
    }

    void setCount_steps(int count_steps) {
        this.count_steps = count_steps;
    }

    private int count_steps;

    public String getState() {
        return state;
    }

    void setState(int field_position_index, BoardModel model) {
        if (field_position_index <=model.getLast_field_index() && field_position_index >0)
        {
            this.state="inGame";
        }
        else if (field_position_index >=100)
        {
            this.state="start";
        }
        else if (field_position_index>model.getLast_field_index() && field_position_index<=(field_position_index+ model.getMax_players()*4))
        {
            this.state="end";
        }
        else
        {
            Log.i("tag", "error");
        }


    }

    private String state;

    boolean isFinished() {
        return finished;
    }

    void setFinished(boolean finished) {
        this.finished = finished;
    }

    private boolean finished;

    Figure(int id, int field_position_index) {
        this.field_position_index = field_position_index;
        this.state = "start";
        this.count_steps = 0;
        this.id = id;
        this.finished = false;
    }

    public int getField_position_index() {
        return field_position_index ;
    }

    void setField_position_index(int field_position_index) {
        this.field_position_index = field_position_index;
    }

    private Figure(Parcel in) {
        field_position_index = in.readInt();
        id = in.readInt();
        count_steps = in.readInt();
        state = in.readString();
        finished = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(field_position_index);
        dest.writeInt(id);
        dest.writeInt(count_steps);
        dest.writeString(state);
        dest.writeByte((byte) (finished ? 0x01 : 0x00));
    }

    public static final Parcelable.Creator<Figure> CREATOR = new Parcelable.Creator<Figure>() {
        @Override
        public Figure createFromParcel(Parcel in) {
            return new Figure(in);
        }

        @Override
        public Figure[] newArray(int size) {
            return new Figure[size];
        }
    };

}
