package org.secuso.privacyfriendlyludo.Map;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import org.secuso.privacyfriendlyludo.R;
import org.secuso.privacyfriendlyludo.logic.BoardModel;
import org.secuso.privacyfriendlyludo.logic.GameField;
import org.secuso.privacyfriendlyludo.logic.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Julchen on 31.05.2017.
 */

public class GameFieldPosition implements  Parcelable, Serializable{

    public ArrayList<GameField> getMyGamefield() {
        return myGamefield;
    }

    public void setMyGamefield(ArrayList<GameField> myGamefield) {
        this.myGamefield = myGamefield;
    }

    private ArrayList<GameField> myGamefield = new ArrayList<GameField>();

    //40 fields

    public GameFieldPosition(ArrayList<Player> players) {
        int main_color = R.color.white;
        int color1, color2, color3, color4;
        color1 = players.get(0).getColor();
        color2 = players.get(1).getColor();
        //color3 = players.get(2).getColor();
        //color4 = players.get(3).getColor();
        color3 = R.color.green;
        color4 = R.color.yellow;
        //start field p1
        myGamefield.add(new GameField(1 ,0,4,0,0, color1));

        myGamefield.add(new GameField(2, 1,4,0,0, main_color));
        myGamefield.add(new GameField(3, 2,4,0,0, main_color));
        myGamefield.add(new GameField(4, 3,4,0,0, main_color));
        myGamefield.add(new GameField(5, 4,4,0,0, main_color));
        myGamefield.add(new GameField(6, 4,3,0,0, main_color));

        myGamefield.add(new GameField(7, 4,2,0,0, main_color));
        myGamefield.add(new GameField(8, 4, 1,0,0, main_color));
        myGamefield.add(new GameField(9, 4,0,0,0, main_color));
        myGamefield.add(new GameField(10, 5,0,0,0, main_color));
        //startfield p2
        myGamefield.add(new GameField(11, 6,0,0,0, color2));

        myGamefield.add(new GameField(12, 6,1,0,0, main_color));
        myGamefield.add(new GameField(13, 6,2,0,0, main_color));
        myGamefield.add(new GameField(14, 6,3,0,0, main_color));
        myGamefield.add(new GameField(15, 6,4,0,0, main_color));
        myGamefield.add(new GameField(16, 7,4,0,0, main_color));
        myGamefield.add(new GameField(17, 8,4,0,0, main_color));
        myGamefield.add(new GameField(18, 9,4,0,0, main_color));
        myGamefield.add(new GameField(19, 10,4,0,0, main_color));
        myGamefield.add(new GameField(20, 10, 5,0,0, main_color));
        //startfield p3
        myGamefield.add(new GameField(21, 10,6,0,0, color3));

        myGamefield.add(new GameField(22, 9,6,0,0, main_color));
        myGamefield.add(new GameField(23, 8,6,0,0, main_color));
        myGamefield.add(new GameField(24, 7,6,0,0, main_color));
        myGamefield.add(new GameField(25, 6,6,0,0, main_color));
        myGamefield.add(new GameField(26, 6,7,0,0, main_color));
        myGamefield.add(new GameField(27, 6,8,0,0, main_color));
        myGamefield.add(new GameField(28, 6,9,0,0, main_color));
        myGamefield.add(new GameField(29, 6,10,0,0, main_color));
        myGamefield.add(new GameField(30, 5,10,0,0, main_color));
        //startfield p4
        myGamefield.add(new GameField(31, 4,10,0,0, color4));

        myGamefield.add(new GameField(32, 4,9,0,0, main_color));
        myGamefield.add(new GameField(33, 4,8,0,0, main_color));
        myGamefield.add(new GameField(34, 4,7,0,0, main_color));
        myGamefield.add(new GameField(35, 4,6,0,0, main_color));
        myGamefield.add(new GameField(36, 3,6,0,0, main_color));
        myGamefield.add(new GameField(37, 2,6,0,0, main_color));
        myGamefield.add(new GameField(38, 1,6,0,0, main_color));
        myGamefield.add(new GameField(39, 0,6,0,0, main_color));
        myGamefield.add(new GameField(40, 0,5,0,0, main_color));

        //final fields player 1
        myGamefield.add(new GameField(41, 1, 5, 0, 0, color1));
        myGamefield.add(new GameField(42, 2, 5, 0, 0, color1));
        myGamefield.add(new GameField(43, 3, 5, 0, 0, color1));
        myGamefield.add(new GameField(44, 4, 5, 0, 0, color1));
        //player2
        myGamefield.add(new GameField(45, 5, 1, 0, 0, color2));
        myGamefield.add(new GameField(46, 5, 2, 0, 0, color2));
        myGamefield.add(new GameField(47, 5, 3, 0, 0, color2));
        myGamefield.add(new GameField(48, 5, 4, 0, 0, color2));
        //player3
        myGamefield.add(new GameField(49, 9, 5, 0, 0, color3));
        myGamefield.add(new GameField(50, 8, 5, 0, 0, color3));
        myGamefield.add(new GameField(51, 7, 5, 0, 0, color3));
        myGamefield.add(new GameField(52, 6, 5, 0, 0, color3));
        //player 4
        myGamefield.add(new GameField(53, 5, 9, 0, 0, color4));
        myGamefield.add(new GameField(54, 5, 8, 0, 0, color4));
        myGamefield.add(new GameField(55, 5, 7, 0, 0, color4));
        myGamefield.add(new GameField(56, 5, 6, 0, 0, color4));
    }

    protected GameFieldPosition(Parcel in) {
        if (in.readByte() == 0x01) {
            myGamefield = new ArrayList<GameField>();
            in.readList(myGamefield, GameField.class.getClassLoader());
        } else {
            myGamefield = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (myGamefield == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(myGamefield);
        }
    }

    public static final Parcelable.Creator<GameFieldPosition> CREATOR = new Parcelable.Creator<GameFieldPosition>() {
        @Override
        public GameFieldPosition createFromParcel(Parcel in) {
            return new GameFieldPosition(in);
        }

        @Override
        public GameFieldPosition[] newArray(int size) {
            return new GameFieldPosition[size];
        }
    };

}
