package org.secuso.privacyfriendlyludo.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import org.secuso.privacyfriendlyludo.R;
import org.secuso.privacyfriendlyludo.logic.BoardModel;
import org.secuso.privacyfriendlyludo.logic.Dicer;

import java.util.ArrayList;


/**
 * Created by Julchen on 15.05.2017.
 */

public class BoardView extends GridLayout{

    public boolean player_changed;

    public FieldView[][] getBoard() {
        return board;
    }

    public void setBoard(FieldView[][] board) {
        this.board = board;
    }

    private FieldView[][] board;
    private AttributeSet attrs;
    private boolean layoutDone;
    BoardModel model;
    Dicer dicer;
    int dicer_number;
    public Drawable d;
    View.OnClickListener myOnlyhandler;


    public BoardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.attrs = attrs;
        this.layoutDone=false;
       // this.controller = controller;
        this.model = model;

    }


    public void createBoard(BoardModel model)
    {
        int count = 0;
        //create Array of size of the board
        this.board = new FieldView[11][11];
        this.setRowCount(11);
        this.setColumnCount(11);
        //initialize complete board with empty Imageviews
        for(int i=0; i<11; i++) {
            for (int j = 0; j < 11; j++) {
               // filled = false;
                board[i][j] = new FieldView(getContext(), this.attrs, 0, null, false);
                count = count +1;
            }
        }

        //set colors for main field
        for(int i=0; i<model.getMy_game_field().getMyGamefield().size(); i++)
        {
            int x = model.getMy_game_field().getMyGamefield().get(i).getX();
            int y = model.getMy_game_field().getMyGamefield().get(i).getY();
            int field_index = model.getMy_game_field().getMyGamefield().get(i).getIndex();
            //TODO ContextCompat.getColor(getContext(), model.getMy_game_field().getMyGamefield().get(i).getMyColor()  );
            int mycolor = getResources().getColor(model.getMy_game_field().getMyGamefield().get(i).getMyColor());
            int player_id = model.getMy_game_field().getMyGamefield().get(i).getPlayer_id();
            if (player_id != 0)
            {
                // add player
                int player_color = getResources().getColor(model.getPlayers().get(player_id-1).getColor());
                board[x][y] = new FieldView(getContext(), this.attrs, mycolor, ContextCompat.getDrawable(getContext(), R.drawable.field2), true);
                board[x][y].getLayersDrawable().getDrawable(1).setColorFilter(player_color, PorterDuff.Mode.MULTIPLY);
            }
            else
            {
                // build normal field
                // no player on this field
                board[x][y] = new FieldView(getContext(), this.attrs, mycolor, ContextCompat.getDrawable(getContext(), R.drawable.field2), false);
            }
            board[x][y].setId(field_index);
        }
        //add figures and start fields
        for(int i=0; i<model.getStart_player_map().getMyGamefield().size(); i++)
        {
            int x = model.getStart_player_map().getMyGamefield().get(i).getX();
            int y = model.getStart_player_map().getMyGamefield().get(i).getY();
            int field_index = model.getStart_player_map().getMyGamefield().get(i).getIndex();
            int mycolor = getResources().getColor(model.getStart_player_map().getMyGamefield().get(i).getMyColor());
            //int mycolor = model.getStart_player_map().getMyGamefield().get(i).getMyColor();
            int player_id = model.getStart_player_map().getMyGamefield().get(i).getPlayer_id();
            if (player_id != 0)
            {
                // add player
                int player_color = getResources().getColor(model.getPlayers().get(player_id-1).getColor());
                board[x][y] = new FieldView(getContext(), this.attrs, mycolor, ContextCompat.getDrawable(getContext(), R.drawable.field2), true);
                board[x][y].getLayersDrawable().getDrawable(1).setColorFilter(player_color, PorterDuff.Mode.MULTIPLY);
            }
            else
            {
                // build normal field
                // no player on this field
                board[x][y] = new FieldView(getContext(), this.attrs, mycolor, ContextCompat.getDrawable(getContext(), R.drawable.field2), false);
            }

            board[x][y].setId(field_index);
        }

        //add design of imageviews to the board
        // add it after all colors are set
        for(int i=0; i<11; i++) {
            for (int j = 0; j < 11; j++) {

                addView(board[i][j]);
            }
        }
    }

    public ArrayList<Integer> getFigurePositionOnBoard(BoardModel mymodel, int figure_id )
    {
        ArrayList<Integer> coordinates = new ArrayList<Integer>();
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
        board[x][y].markPossibleFigures(color);
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
        board[x][y].hidePossibleFigure();
        board[x][y].setClickable(false);

    }

    public void setFigureToNewPosition(BoardModel mymodel, int old_position, int new_position, boolean knocked_out)
    {
        this.model = mymodel;
        removeOldFigure(mymodel, old_position);
        insertNewFigure(mymodel, new_position, knocked_out);

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
    board[x][y].getLayersDrawable().getDrawable(1).setAlpha(0);
}

    public void insertNewFigure(BoardModel mymodel, int new_position, boolean knocked_out)
    {
        int x;
        int y;
        int color;
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

        if (knocked_out == false)
        {
            color = getResources().getColor(model.getRecent_player().getColor());
        }
        else
        {
            color = getResources().getColor(model.getOpponent_player().getColor());
        }

        //add figure layer
        board[x][y].getLayersDrawable().getDrawable(1).setAlpha(255);
        board[x][y].getLayersDrawable().getDrawable(1).setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            if (!this.layoutDone) {
                for (int i = 0; i < 11; i++) {
                    for (int j = 0; j < 11; j++) {
                        ViewGroup.LayoutParams params = board[i][j].getLayoutParams();
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            params.width = (int) ((right - left)) / this.getColumnCount();
                            params.height = (int) ((right - left)) / this.getRowCount();

                        } else {
                            params.width = (int) ((bottom - top)) / this.getColumnCount();
                            params.height = (int) ((bottom - top)) / this.getRowCount();
                        }
                        board[i][j].setLayoutParams(params);
                    }
                }
                this.layoutDone = true;
            }
    }

}
