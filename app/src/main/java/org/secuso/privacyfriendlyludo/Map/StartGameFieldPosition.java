package org.secuso.privacyfriendlyludo.Map;

import android.graphics.Color;

import org.secuso.privacyfriendlyludo.R;
import org.secuso.privacyfriendlyludo.logic.BoardModel;
import org.secuso.privacyfriendlyludo.logic.GameField;

import java.util.ArrayList;

/**
 * Created by Julchen on 01.06.2017.
 */

public class StartGameFieldPosition {

    public ArrayList<GameField> getMyGamefield() {
        return myGamefield;
    }

    public void setMyGamefield(ArrayList<GameField> myGamefield) {
        this.myGamefield = myGamefield;
    }

    private ArrayList<GameField> myGamefield = new ArrayList<GameField>();

    //40 fields

    public StartGameFieldPosition() {
        int color1 = R.color.red;
        int color2 = R.color.darkblue;
        int color3 = R.color.green;
        int color4 = R.color.yellow;
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
        for (int i=1; i< model.getPlayers().size()+1; i++)
        {
            for (int j=1; j< model.getPlayers().get(i-1).getFigures().size()+1; j++)
            {
                int index = model.getPlayers().get(i-1).getFigures().get(j-1).getField_position_index();
                myGamefield.get((index-1) %100).setFigure_id(j);
                myGamefield.get((index-1) % 100).setPlayer_id(i);
            }
        }
    }
}
