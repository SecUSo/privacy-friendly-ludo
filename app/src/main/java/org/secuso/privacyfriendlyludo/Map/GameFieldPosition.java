package org.secuso.privacyfriendlyludo.Map;

import android.os.Parcel;
import android.os.Parcelable;
import org.secuso.privacyfriendlyludo.logic.GameField;
import org.secuso.privacyfriendlyludo.logic.GameType;

import java.io.Serializable;
import java.util.ArrayList;

/*
  @author: Julia Schneider

  This file is part of The Game Ludo.

 Ludo is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 You should have received a copy of the GNU General Public License
 along with Ludo.  If not, see <http://www.gnu.org/licenses/>.
 */

public class GameFieldPosition implements  Parcelable, Serializable{


    public ArrayList<GameField> getMyGamefield() {
        return myGamefield;
    }

    private ArrayList<GameField> myGamefield = new ArrayList<>();

    //40 fields

    public GameFieldPosition(ArrayList<Integer> colors, GameType game_type) {
        int main_color;
        switch (game_type) {
            case Four_players:
                main_color=colors.get(4);
                //start field p1
                myGamefield.add(new GameField(1 ,0,4,0,0, colors.get(0)));

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
                myGamefield.add(new GameField(11, 6,0,0,0, colors.get(1)));

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
                myGamefield.add(new GameField(21, 10,6,0,0, colors.get(2)));

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
                myGamefield.add(new GameField(31, 4,10,0,0, colors.get(3)));

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
                myGamefield.add(new GameField(41, 1, 5, 0, 0, colors.get(0)));
                myGamefield.add(new GameField(42, 2, 5, 0, 0, colors.get(0)));
                myGamefield.add(new GameField(43, 3, 5, 0, 0, colors.get(0)));
                myGamefield.add(new GameField(44, 4, 5, 0, 0, colors.get(0)));
                //player2
                myGamefield.add(new GameField(45, 5, 1, 0, 0, colors.get(1)));
                myGamefield.add(new GameField(46, 5, 2, 0, 0, colors.get(1)));
                myGamefield.add(new GameField(47, 5, 3, 0, 0, colors.get(1)));
                myGamefield.add(new GameField(48, 5, 4, 0, 0, colors.get(1)));
                //player3
                myGamefield.add(new GameField(49, 9, 5, 0, 0, colors.get(2)));
                myGamefield.add(new GameField(50, 8, 5, 0, 0, colors.get(2)));
                myGamefield.add(new GameField(51, 7, 5, 0, 0, colors.get(2)));
                myGamefield.add(new GameField(52, 6, 5, 0, 0, colors.get(2)));
                //player 4
                myGamefield.add(new GameField(53, 5, 9, 0, 0, colors.get(3)));
                myGamefield.add(new GameField(54, 5, 8, 0, 0, colors.get(3)));
                myGamefield.add(new GameField(55, 5, 7, 0, 0, colors.get(3)));
                myGamefield.add(new GameField(56, 5, 6, 0, 0, colors.get(3)));
                break;
            case Six_players:
               //start field p1
                main_color=colors.get(6);
                myGamefield.add(new GameField(1 , 4, 2,0,0, colors.get(0)));

                myGamefield.add(new GameField(2,  4, 3,0,0, main_color));
                myGamefield.add(new GameField(3,  4, 4,0,0, main_color));
                myGamefield.add(new GameField(4,  4, 5,0,0, main_color));
                myGamefield.add(new GameField(5,  4, 6,0,0, main_color));
                myGamefield.add(new GameField(6,  5, 6,0,0, main_color));
                myGamefield.add(new GameField(7,  6, 6,0,0, main_color));
                myGamefield.add(new GameField(8,  6, 5,0,0, main_color));
                myGamefield.add(new GameField(9,  6, 4,0,0, main_color));
                myGamefield.add(new GameField(10, 6, 3,0,0, main_color));
                myGamefield.add(new GameField(11, 6, 2,0,0, main_color));
                myGamefield.add(new GameField(12, 7, 2,0,0, main_color));

                //start field p2
                myGamefield.add(new GameField(13, 8, 2,0,0, colors.get(1)));

                myGamefield.add(new GameField(14, 8, 3,0,0, main_color));
                myGamefield.add(new GameField(15, 8, 4,0,0, main_color));
                myGamefield.add(new GameField(16, 8, 5,0,0, main_color));
                myGamefield.add(new GameField(17, 8, 6,0,0, main_color));
                myGamefield.add(new GameField(18, 9, 6,0,0, main_color));
                myGamefield.add(new GameField(19,10, 6,0,0, main_color));
                myGamefield.add(new GameField(20,10, 5,0,0, main_color));
                myGamefield.add(new GameField(21,10, 4,0,0, main_color));
                myGamefield.add(new GameField(22,10, 3,0,0, main_color));
                myGamefield.add(new GameField(23,10, 2,0,0, main_color));
                myGamefield.add(new GameField(24,11, 2,0,0, main_color));

                //start field p3
                myGamefield.add(new GameField(25,12, 2,0,0, colors.get(2)));

                myGamefield.add(new GameField(26,12, 3,0,0, main_color));
                myGamefield.add(new GameField(27,12, 4,0,0, main_color));
                myGamefield.add(new GameField(28,12, 5,0,0, main_color));
                myGamefield.add(new GameField(29,12, 6,0,0, main_color));
                myGamefield.add(new GameField(30,12, 7,0,0, main_color));
                myGamefield.add(new GameField(31,12, 8,0,0, main_color));
                myGamefield.add(new GameField(32,12, 9,0,0, main_color));
                myGamefield.add(new GameField(33,12,10,0,0, main_color));
                myGamefield.add(new GameField(34,12,11,0,0, main_color));
                myGamefield.add(new GameField(35,12,12,0,0, main_color));
                myGamefield.add(new GameField(36,11,12,0,0, main_color));

                //start field p4
                myGamefield.add(new GameField(37,10,12,0,0, colors.get(3)));

                myGamefield.add(new GameField(38,10,11,0,0, main_color));
                myGamefield.add(new GameField(39,10,10,0,0, main_color));
                myGamefield.add(new GameField(40,10, 9,0,0, main_color));
                myGamefield.add(new GameField(41,10, 8,0,0, main_color));
                myGamefield.add(new GameField(42, 9, 8,0,0, main_color));
                myGamefield.add(new GameField(43, 8, 8,0,0, main_color));
                myGamefield.add(new GameField(44, 8, 9,0,0, main_color));
                myGamefield.add(new GameField(45, 8,10,0,0, main_color));
                myGamefield.add(new GameField(46, 8,11,0,0, main_color));
                myGamefield.add(new GameField(47, 8,12,0,0, main_color));
                myGamefield.add(new GameField(48, 7,12,0,0, main_color));

                //start field p5
                myGamefield.add(new GameField(49, 6,12,0,0, colors.get(4)));

                myGamefield.add(new GameField(50, 6,11,0,0, main_color));
                myGamefield.add(new GameField(51, 6,10,0,0, main_color));
                myGamefield.add(new GameField(52, 6, 9,0,0, main_color));
                myGamefield.add(new GameField(53, 6, 8,0,0, main_color));
                myGamefield.add(new GameField(54, 5, 8,0,0, main_color));
                myGamefield.add(new GameField(55, 4, 8,0,0, main_color));
                myGamefield.add(new GameField(56, 4, 9,0,0, main_color));
                myGamefield.add(new GameField(57, 4,10,0,0, main_color));
                myGamefield.add(new GameField(58, 4,11,0,0, main_color));
                myGamefield.add(new GameField(59, 4,12,0,0, main_color));
                myGamefield.add(new GameField(60, 3,12,0,0, main_color));

                //start field p6
                myGamefield.add(new GameField(61,2,12,0,0, colors.get(5)));

                myGamefield.add(new GameField(62, 2,11,0,0, main_color));
                myGamefield.add(new GameField(63, 2,10,0,0, main_color));
                myGamefield.add(new GameField(64, 2, 9,0,0, main_color));
                myGamefield.add(new GameField(65, 2, 8,0,0, main_color));
                myGamefield.add(new GameField(66, 2, 7,0,0, main_color));
                myGamefield.add(new GameField(67, 2, 6,0,0, main_color));
                myGamefield.add(new GameField(68, 2, 5,0,0, main_color));
                myGamefield.add(new GameField(69, 2, 4,0,0, main_color));
                myGamefield.add(new GameField(70, 2, 3,0,0, main_color));
                myGamefield.add(new GameField(71, 2, 2,0,0, main_color));
                myGamefield.add(new GameField(72, 3, 2,0,0, main_color));

                //final fields player1
                myGamefield.add(new GameField(73, 3, 3, 0, 0, colors.get(0)));
                myGamefield.add(new GameField(74, 3, 4, 0, 0, colors.get(0)));
                myGamefield.add(new GameField(75, 3, 5, 0, 0, colors.get(0)));
                myGamefield.add(new GameField(76, 3, 6, 0, 0, colors.get(0)));
                //player2
                myGamefield.add(new GameField(77, 7, 3, 0, 0, colors.get(1)));
                myGamefield.add(new GameField(78, 7, 4, 0, 0, colors.get(1)));
                myGamefield.add(new GameField(79, 7, 5, 0, 0, colors.get(1)));
                myGamefield.add(new GameField(80, 7, 6, 0, 0, colors.get(1)));
                //player3
                myGamefield.add(new GameField(81,11, 3, 0, 0, colors.get(2)));
                myGamefield.add(new GameField(82,11, 4, 0, 0, colors.get(2)));
                myGamefield.add(new GameField(83,11, 5, 0, 0, colors.get(2)));
                myGamefield.add(new GameField(84,11, 6, 0, 0, colors.get(2)));
                //player4
                myGamefield.add(new GameField(85,11,11, 0, 0, colors.get(3)));
                myGamefield.add(new GameField(86,11,10, 0, 0, colors.get(3)));
                myGamefield.add(new GameField(87,11, 9, 0, 0, colors.get(3)));
                myGamefield.add(new GameField(88,11, 8, 0, 0, colors.get(3)));
                //player5
                myGamefield.add(new GameField(89, 7,11, 0, 0, colors.get(4)));
                myGamefield.add(new GameField(90, 7,10, 0, 0, colors.get(4)));
                myGamefield.add(new GameField(91, 7, 9, 0, 0, colors.get(4)));
                myGamefield.add(new GameField(92, 7, 8, 0, 0, colors.get(4)));
                //player6
                myGamefield.add(new GameField(93, 3,11, 0, 0, colors.get(5)));
                myGamefield.add(new GameField(94, 3,10, 0, 0, colors.get(5)));
                myGamefield.add(new GameField(95, 3, 9, 0, 0, colors.get(5)));
                myGamefield.add(new GameField(96, 3, 8, 0, 0, colors.get(5)));

                break;

        }

    }

    private GameFieldPosition(Parcel in) {
        if (in.readByte() == 0x01) {
            myGamefield = new ArrayList<>();
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
