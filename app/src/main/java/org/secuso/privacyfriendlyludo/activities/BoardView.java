package org.secuso.privacyfriendlyludo.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import org.secuso.privacyfriendlyludo.R;
import org.secuso.privacyfriendlyludo.logic.BoardModel;

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


public class BoardView extends GridLayout{

    private FieldView[][] board;
    private AttributeSet attrs;
    private boolean layoutDone;
    BoardModel model;
    int board_size;


    public BoardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.attrs = attrs;
        this.layoutDone=false;
    }


    public void createBoard(BoardModel model)
    {
        int count = 0;
        //create Array of size of the board
        switch (model.getGame_type()) {
            case Four_players:
                board_size = 11;
                break;
            case Six_players:
                board_size = 15;
                break;
        }
        this.board = new FieldView[board_size][board_size];
        this.setRowCount(board_size);
        this.setColumnCount(board_size);
        //initialize complete board with empty Imageviews
        for(int i=0; i<board_size; i++) {
            for (int j = 0; j < board_size; j++) {
               // filled = false;
                board[i][j] = new FieldView(getContext(), this.attrs, 0, null, false);
                board[i][j].setTag("false");
                count = count +1;
            }
        }

        //set colors for main field
        for(int i=0; i<model.getMy_game_field().getMyGamefield().size(); i++)
        {
            int x = model.getMy_game_field().getMyGamefield().get(i).getX();
            int y = model.getMy_game_field().getMyGamefield().get(i).getY();
            int field_index = model.getMy_game_field().getMyGamefield().get(i).getIndex();
            int mycolor = model.getMy_game_field().getMyGamefield().get(i).getMyColor();
            int player_id = model.getMy_game_field().getMyGamefield().get(i).getPlayer_id();
            if (player_id != 0)
            {
                // add player
                int player_color = model.getPlayers().get(player_id-1).getColor();
                board[x][y] = new FieldView(getContext(), this.attrs, mycolor, ContextCompat.getDrawable(getContext(), R.drawable.field), true);
                board[x][y].getLayersDrawable().getDrawable(2).setColorFilter(player_color, PorterDuff.Mode.MULTIPLY);
            }
            else
            {
                // build normal field
                // no player on this field
                board[x][y] = new FieldView(getContext(), this.attrs, mycolor, ContextCompat.getDrawable(getContext(), R.drawable.field), false);
            }
            board[x][y].setTag("true");
            board[x][y].setId(field_index);
        }

        //add figures and start fields
        for(int i=0; i<model.getStart_player_map().getMyGamefield().size(); i++)
        {
            int x = model.getStart_player_map().getMyGamefield().get(i).getX();
            int y = model.getStart_player_map().getMyGamefield().get(i).getY();
            int field_index = model.getStart_player_map().getMyGamefield().get(i).getIndex();
            int mycolor = model.getStart_player_map().getMyGamefield().get(i).getMyColor();
            int player_id = model.getStart_player_map().getMyGamefield().get(i).getPlayer_id();
            if (player_id != 0)
            {
                // add player
                int player_color = model.getPlayers().get(player_id-1).getColor();
                board[x][y] = new FieldView(getContext(), this.attrs, mycolor, ContextCompat.getDrawable(getContext(), R.drawable.field), true);
                board[x][y].getLayersDrawable().getDrawable(2).setColorFilter(player_color, PorterDuff.Mode.MULTIPLY);

            }
            else
            {
                // build normal field
                // no player on this field
                board[x][y] = new FieldView(getContext(), this.attrs, mycolor, ContextCompat.getDrawable(getContext(), R.drawable.field), false);
            }

            board[x][y].setId(field_index);
            board[x][y].setTag("true");
        }


        //add design of imageviews to the board
        // add it after all colors are set
        for(int i=0; i<board_size; i++) {
            for (int j = 0; j < board_size; j++) {

                addView(board[i][j]);
            }
        }
    }

    public ArrayList<Integer> getFigurePositionOnBoard(BoardModel mymodel, int figure_id )
    {
        ArrayList<Integer> coordinates = new ArrayList<>();
        this.model = mymodel;
        String state = mymodel.getRecent_player().getFigures().get(figure_id - 1).getState();
        int index = mymodel.getRecent_player().getFigures().get(figure_id - 1).getField_position_index();
        int x=0;
        int y=0;

        switch(state)
        {
            case "start":
                // depending on player
                x=model.getStart_player_map().getMyGamefield().get((index) % 100).getX();
                y=model.getStart_player_map().getMyGamefield().get((index) % 100).getY();
                break;
            case "inGame":
                x=model.getMy_game_field().getMyGamefield().get(index-1).getX();
                y=model.getMy_game_field().getMyGamefield().get(index-1).getY();
                break;
            case "end":
                x=model.getMy_game_field().getMyGamefield().get(index-1).getX();
                y=model.getMy_game_field().getMyGamefield().get(index-1).getY();
                break;
            case "finished":
                x=model.getMy_game_field().getMyGamefield().get(index-1).getX();
                y=model.getMy_game_field().getMyGamefield().get(index-1).getY();
                break;

        }
        coordinates.add(x);
        coordinates.add(y);
        return coordinates;
    }

    public void MarkPossiblePlayers(BoardModel mymodel, int figure_id, View.OnClickListener myOnlyhandler)
    {
        this.model = mymodel;
        int color = model.getRecent_player().getColor();
        //Log.i("tag", model.getRecent_player().getName());
        int x = (getFigurePositionOnBoard(mymodel, figure_id)).get(0);
        int y = (getFigurePositionOnBoard(mymodel, figure_id)).get(1);
        board[x][y].markPossibleFigure(color);
        // only set OnclickListener and Clickable if it is not a AI
        if (!mymodel.getRecent_player().isAI())
        {
            board[x][y].setOnClickListener(myOnlyhandler);
            board[x][y].setClickable(true);
        }
    }

    public void HidePossiblePlayers(BoardModel mymodel, int figure_id)
    {
        this.model = mymodel;
        int x = (getFigurePositionOnBoard(mymodel, figure_id)).get(0);
        int y = (getFigurePositionOnBoard(mymodel, figure_id)).get(1);
        int color = mymodel.getRecent_player().getColor();
        board[x][y].hidePossibleFigure(color);
        board[x][y].setClickable(false);
    }

    // for free mode
    public void SelectAndDeselectPlayer(BoardModel mymodel, int figure_index, int playerID, boolean marked)
    {
        this.model = mymodel;
        int color, x, y;
        color = mymodel.getPlayers().get(playerID-1).getColor();
        if (figure_index >=100)
        {
            x = mymodel.getStart_player_map().getMyGamefield().get(figure_index%100).getX();
            y = mymodel.getStart_player_map().getMyGamefield().get(figure_index%100).getY();
        }
        else
        {
            x = mymodel.getMy_game_field().getMyGamefield().get(figure_index-1).getX();
            y = mymodel.getMy_game_field().getMyGamefield().get(figure_index-1).getY();
        }
        if (marked)
        {

            board[x][y].hidePossibleFigure(color);
        }
        else
        {
            board[x][y].markPossibleFigure(color);
        }
    }

    public void makeAllFieldsClickable(View.OnClickListener myOnlyhandler)
    {
        for (int i=0; i<board_size; i++)
        {
            for(int j=0; j<board_size; j++)
            {
                if (board[i][j].getTag()=="true") {
                    board[i][j].setClickable(true);
                    board[i][j].setOnClickListener(myOnlyhandler);
                }
            }
        }
    }

    public void setFigureToNewPosition(BoardModel mymodel, int player_id, int old_position, int new_position,  boolean knocked_out)
    {
        this.model = mymodel;
        removeOldFigure(mymodel, old_position);
        insertNewFigure(mymodel, new_position, player_id, knocked_out);
    }

public void removeOldFigure(BoardModel mymodel, int old_position)
{
    this.model = mymodel;
    int x;
    int y;
    if(old_position >=100)
    {
        x=model.getStart_player_map().getMyGamefield().get(old_position%100).getX();
        y=model.getStart_player_map().getMyGamefield().get(old_position%100).getY();
    }
    else
    {
        x=model.getMy_game_field().getMyGamefield().get(old_position-1).getX();
        y=model.getMy_game_field().getMyGamefield().get(old_position-1).getY();
    }
    //remove figure layer
    board[x][y].getLayersDrawable().getDrawable(2).setAlpha(0);
}

    public ArrayList<Integer> getPlayerInfos(BoardModel mymodel, int fieldindex)
    {
        this.model = mymodel;
        ArrayList<Integer> playerInfo = new ArrayList<>();
        int player_id, figure_id;

        if(fieldindex>=100)
        {
            player_id=model.getStart_player_map().getMyGamefield().get((fieldindex) % 100).getPlayer_id();
            figure_id=model.getStart_player_map().getMyGamefield().get((fieldindex) % 100).getFigure_id();
        }
        else
        {
            player_id =model.getMy_game_field().getMyGamefield().get(fieldindex-1).getPlayer_id();
            figure_id =model.getMy_game_field().getMyGamefield().get(fieldindex-1).getFigure_id();
        }
        playerInfo.add(player_id);
        playerInfo.add(figure_id);
        return playerInfo;
    }

    public void insertNewFigure(BoardModel mymodel, int new_position, int player_id, boolean knocked_out)
    {
        int color;
        int x;
        int y;
        this.model = mymodel;
        if(new_position >=100)
        {
            x=model.getStart_player_map().getMyGamefield().get((new_position) % 100).getX();
            y=model.getStart_player_map().getMyGamefield().get((new_position) % 100).getY();
        }
        else
        {
            x = model.getMy_game_field().getMyGamefield().get(new_position - 1).getX();
            y = model.getMy_game_field().getMyGamefield().get(new_position - 1).getY();
        }

        if (!knocked_out)
            {
                color = model.getPlayers().get(player_id-1).getColor();
            }
            else
            {
                player_id = (getPlayerInfos(mymodel, new_position)).get(0);
                color = model.getPlayers().get(player_id-1).getColor();
            }
        //add figure layer
        board[x][y].getLayersDrawable().getDrawable(2).setAlpha(255);
        board[x][y].getLayersDrawable().getDrawable(2).setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            if (!this.layoutDone) {
                for (int i = 0; i < board_size; i++) {
                    for (int j = 0; j < board_size; j++) {
                        ViewGroup.LayoutParams params = board[i][j].getLayoutParams();
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            params.width = (right - left) / this.getColumnCount();
                            params.height = (right - left) / this.getRowCount();

                        } else {
                            params.width = (bottom - top) / this.getColumnCount();
                            params.height = (bottom - top) / this.getRowCount();
                        }
                        board[i][j].setLayoutParams(params);
                    }
                }
                this.layoutDone = true;
            }
    }

}
