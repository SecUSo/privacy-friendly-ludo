package org.secuso.privacyfriendlyludo.logic;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Julchen on 28.05.2017.
 */

public class Player implements Parcelable, Serializable{

    ArrayList <Figure> figures = new ArrayList<Figure>();

    int id;
    int color;
    String name;

    public Player()
    {

    }

    public Player(int id, int playercolor, String name)
    {
        this.name = name;
        this.id = id;
        this.color = playercolor;
        //create 4 figures
        for (int i=0; i<4; i++)
        {
            int index = 100 + i + (4*(id-1));
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

    public void setFigures(ArrayList<Figure> figures) {
        this.figures = figures;
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
        figures = in.createTypedArrayList(Figure.CREATOR);
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
        dest.writeTypedList(figures);
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
