package org.secuso.privacyfriendlyludo.Map;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;

import org.secuso.privacyfriendlyludo.R;
import org.secuso.privacyfriendlyludo.logic.BoardModel;
import org.secuso.privacyfriendlyludo.logic.GameField;
import org.secuso.privacyfriendlyludo.logic.GameType;
import org.secuso.privacyfriendlyludo.logic.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Julchen on 01.06.2017.
 */

public class StartGameFieldPosition implements Parcelable, Serializable{

    public ArrayList<GameField> getMyGamefield() {
        return myGamefield;
    }

    public void setMyGamefield(ArrayList<GameField> myGamefield) {
        this.myGamefield = myGamefield;
    }

    private ArrayList<GameField> myGamefield = new ArrayList<GameField>();

    public StartGameFieldPosition(ArrayList<Integer> colors, GameType game_type) {

        switch (game_type) {
            case Four_players:
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

    protected StartGameFieldPosition(Parcel in) {
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
