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
 * Created by Julchen on 01.06.2017.
 */

public class StartGameFieldPosition implements Parcelable, Serializable {

    public ArrayList<GameField> getMyGamefield() {
        return myGamefield;
    }

    public void setMyGamefield(ArrayList<GameField> myGamefield) {
        this.myGamefield = myGamefield;
    }

    private ArrayList<GameField> myGamefield = new ArrayList<GameField>();

    public StartGameFieldPosition(ArrayList <Player> players) {
        int color1, color2, color3, color4;
        color1 = players.get(0).getColor();
        color2 = players.get(1).getColor();
   //     color3 = players.get(2).getColor();
   //     color4 = players.get(3).getColor();
        color3 = R.color.green;
        color4 = R.color.yellow;

        //final fields player 1 with figure 1 to 4
        myGamefield.add(new GameField(100, 0, 0, 0, 1,  color1));
        myGamefield.add(new GameField(101, 0, 1, 0, 2, color1));
        myGamefield.add(new GameField(102, 1, 0, 0, 3, color1));
        myGamefield.add(new GameField(103, 1, 1, 0, 4, color1));
        //player2
        myGamefield.add(new GameField(104, 9, 0, 0, 1, color2));
        myGamefield.add(new GameField(105, 10, 1, 0, 2, color2));
        myGamefield.add(new GameField(106, 9, 1, 0, 3, color2));
        myGamefield.add(new GameField(107, 10, 0, 0, 4, color2));
        //player3
        myGamefield.add(new GameField(108, 9, 9, 0, 1, color3));
        myGamefield.add(new GameField(109, 10, 9, 0, 2, color3));
        myGamefield.add(new GameField(110, 9, 10, 0, 3, color3));
        myGamefield.add(new GameField(111, 10, 10, 0, 4, color3));
        //player 4
        myGamefield.add(new GameField(112, 0, 10, 0, 1, color4));
        myGamefield.add(new GameField(113, 0, 9, 0, 2, color4));
        myGamefield.add(new GameField(114, 1, 9, 0, 3, color4));
        myGamefield.add(new GameField(115, 1, 10, 0, 4, color4));

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
