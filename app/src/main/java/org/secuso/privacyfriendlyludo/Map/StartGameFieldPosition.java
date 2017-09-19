package org.secuso.privacyfriendlyludo.Map;


import android.os.Parcel;
import android.os.Parcelable;

import org.secuso.privacyfriendlyludo.logic.BoardModel;
import org.secuso.privacyfriendlyludo.logic.GameField;
import org.secuso.privacyfriendlyludo.logic.GameType;

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


public class StartGameFieldPosition implements Parcelable, Serializable{

    public ArrayList<GameField> getMyGamefield() {
        return myGamefield;
    }

    private ArrayList<GameField> myGamefield = new ArrayList<>();

    public StartGameFieldPosition(ArrayList<Integer> colors, GameType game_type) {

        switch (game_type) {
            case Four_players:
                // 1, 3, 4, 2
                //Home fields player 1 with figure 1 to 4
                myGamefield.add(new GameField(100, 0, 0, 0, 1, colors.get(0)));
                myGamefield.add(new GameField(101, 0, 1, 0, 2, colors.get(0)));
                myGamefield.add(new GameField(102, 1, 0, 0, 3, colors.get(0)));
                myGamefield.add(new GameField(103, 1, 1, 0, 4, colors.get(0)));
                //player2
                myGamefield.add(new GameField(104, 9, 0, 0, 1, colors.get(1)));
                myGamefield.add(new GameField(105, 10, 1, 0, 2, colors.get(1)));
                myGamefield.add(new GameField(106, 9, 1, 0, 3, colors.get(1)));
                myGamefield.add(new GameField(107, 10, 0, 0, 4, colors.get(1)));
                //player3
                myGamefield.add(new GameField(108, 9, 9, 0, 1, colors.get(2)));
                myGamefield.add(new GameField(109, 10, 9, 0, 2, colors.get(2)));
                myGamefield.add(new GameField(110, 9, 10, 0, 3, colors.get(2)));
                myGamefield.add(new GameField(111, 10, 10, 0, 4, colors.get(2)));
                //player 4
                myGamefield.add(new GameField(112, 0, 10, 0, 1, colors.get(3)));
                myGamefield.add(new GameField(113, 0, 9, 0, 2, colors.get(3)));
                myGamefield.add(new GameField(114, 1, 9, 0, 3, colors.get(3)));
                myGamefield.add(new GameField(115, 1, 10, 0, 4, colors.get(3)));
                break;
            case Six_players:
                //home fields player1 with figure 1 to 4
                // 1,3, 5, 2, 4, 6
                myGamefield.add(new GameField(100, 0, 3, 0, 1, colors.get(0)));
                myGamefield.add(new GameField(101, 0, 4, 0, 2, colors.get(0)));
                myGamefield.add(new GameField(102, 0, 5, 0, 3, colors.get(0)));
                myGamefield.add(new GameField(103, 0, 6, 0, 4, colors.get(0)));
                //player2
                myGamefield.add(new GameField(104, 5, 0, 0, 1, colors.get(1)));
                myGamefield.add(new GameField(105, 6, 0, 0, 2, colors.get(1)));
                myGamefield.add(new GameField(106, 7, 0, 0, 3, colors.get(1)));
                myGamefield.add(new GameField(107, 8, 0, 0, 4, colors.get(1)));
                //player3
                myGamefield.add(new GameField(108, 14, 3, 0, 1, colors.get(2)));
                myGamefield.add(new GameField(109, 14, 4, 0, 2, colors.get(2)));
                myGamefield.add(new GameField(110, 14, 5, 0, 3, colors.get(2)));
                myGamefield.add(new GameField(111, 14, 6, 0, 4, colors.get(2)));
                //player4
                myGamefield.add(new GameField(112, 14, 8, 0, 1, colors.get(3)));
                myGamefield.add(new GameField(113, 14, 9, 0, 2, colors.get(3)));
                myGamefield.add(new GameField(114, 14, 10, 0, 3, colors.get(3)));
                myGamefield.add(new GameField(115, 14, 11, 0, 4, colors.get(3)));
                //player5
                myGamefield.add(new GameField(116, 5, 14, 0, 1, colors.get(4)));
                myGamefield.add(new GameField(117, 6, 14, 0, 2, colors.get(4)));
                myGamefield.add(new GameField(118, 7, 14, 0, 3, colors.get(4)));
                myGamefield.add(new GameField(119, 8, 14, 0, 4, colors.get(4)));
                //player6
                myGamefield.add(new GameField(120, 0, 8, 0, 1, colors.get(5)));
                myGamefield.add(new GameField(121, 0, 9, 0, 2, colors.get(5)));
                myGamefield.add(new GameField(122, 0, 10, 0, 3, colors.get(5)));
                myGamefield.add(new GameField(123, 0, 11, 0, 4, colors.get(5)));

                break;
        }


    }
    public void fill_with_players(BoardModel model)
    {
        for (int i=0; i< model.getPlayers().size(); i++)
        {
            for (int j=0; j< model.getPlayers().get(i).getFigures().size(); j++)
            {
                    int index = model.getPlayers().get(i).getFigures().get(j).getField_position_index();
                    myGamefield.get((index) %100).setFigure_id(j+1);
                    myGamefield.get((index) % 100).setPlayer_id(i+1);
            }
        }
    }

    private StartGameFieldPosition(Parcel in) {
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
    public static final Parcelable.Creator<StartGameFieldPosition> CREATOR = new Parcelable.Creator<StartGameFieldPosition>() {
        @Override
        public StartGameFieldPosition createFromParcel(Parcel in) {
            return new StartGameFieldPosition(in);
        }

        @Override
        public StartGameFieldPosition[] newArray(int size) {
            return new StartGameFieldPosition[size];
        }
    };
}
