package org.secuso.privacyfriendlyludo.logic;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.ArrayList;

/**
 * Created by Julchen on 15.05.2017.
 */

public class BoardView extends GridLayout {

    private Field[][] board;
    private AttributeSet attrs;
    private boolean layoutDone;


    public BoardView(Context context, AttributeSet attrs)
    {

        super(context, attrs);
        this.attrs = attrs;
        this.layoutDone=false;
    }

    public void createBoard()
    {
        Colors color0 = new Colors(Color.WHITE);
        ArrayList <Coordinates> coordinates0 = new ArrayList<Coordinates>();
        //vertical
        coordinates0.add(new Coordinates(0,4));
        coordinates0.add(new Coordinates(1,4));
        coordinates0.add(new Coordinates(2,4));
        coordinates0.add(new Coordinates(3,4));
        coordinates0.add(new Coordinates(4,4));
        coordinates0.add(new Coordinates(6,4));
        coordinates0.add(new Coordinates(7,4));
        coordinates0.add(new Coordinates(8,4));
        coordinates0.add(new Coordinates(9,4));
        coordinates0.add(new Coordinates(1,6));
        coordinates0.add(new Coordinates(2,6));
        coordinates0.add(new Coordinates(3,6));
        coordinates0.add(new Coordinates(4,6));
        coordinates0.add(new Coordinates(6,6));
        coordinates0.add(new Coordinates(7,6));
        coordinates0.add(new Coordinates(8,6));
        coordinates0.add(new Coordinates(9,6));
        coordinates0.add(new Coordinates(10,6));
        //horizontal
        coordinates0.add(new Coordinates(4,1));
        coordinates0.add(new Coordinates(4,2));
        coordinates0.add(new Coordinates(4,3));
        coordinates0.add(new Coordinates(4,7));
        coordinates0.add(new Coordinates(4,8));
        coordinates0.add(new Coordinates(4,9));
        coordinates0.add(new Coordinates(4,10));
        coordinates0.add(new Coordinates(6,0));
        coordinates0.add(new Coordinates(6,1));
        coordinates0.add(new Coordinates(6,2));
        coordinates0.add(new Coordinates(6,3));
        coordinates0.add(new Coordinates(6,7));
        coordinates0.add(new Coordinates(6,8));
        coordinates0.add(new Coordinates(6,9));
        //4 single points
        coordinates0.add(new Coordinates(0,5));
        coordinates0.add(new Coordinates(5,0));
        coordinates0.add(new Coordinates(5,10));
        coordinates0.add(new Coordinates(10,5));
        color0.setCoordinates(coordinates0);
        Colors color1 = new Colors(Color.GREEN);
        ArrayList <Coordinates> coordinates1 = new ArrayList<Coordinates>();
        coordinates1.add(new Coordinates(0,0));
        coordinates1.add(new Coordinates(1,0));
        coordinates1.add(new Coordinates(0,1));
        coordinates1.add(new Coordinates(1,1));
        coordinates1.add(new Coordinates(4,0));
        coordinates1.add(new Coordinates(5,1));
        coordinates1.add(new Coordinates(5,2));
        coordinates1.add(new Coordinates(5,3));
        coordinates1.add(new Coordinates(5,4));
        color1.setCoordinates(coordinates1);
        ArrayList <Coordinates> coordinates2 = new ArrayList<Coordinates>();
        Colors color2 = new Colors(Color.BLUE);
        coordinates2.add(new Coordinates(9,0));
        coordinates2.add(new Coordinates(10,0));
        coordinates2.add(new Coordinates(9,1));
        coordinates2.add(new Coordinates(10,1));
        coordinates2.add(new Coordinates(9,5));
        coordinates2.add(new Coordinates(8,5));
        coordinates2.add(new Coordinates(7,5));
        coordinates2.add(new Coordinates(6,5));
        coordinates2.add(new Coordinates(10,4));
        color2.setCoordinates(coordinates2);
        Colors color3 = new Colors(Color.YELLOW);
        ArrayList <Coordinates> coordinates3 = new ArrayList<Coordinates>();
        coordinates3.add(new Coordinates(9,9));
        coordinates3.add(new Coordinates(10,9));
        coordinates3.add(new Coordinates(9,10));
        coordinates3.add(new Coordinates(10,10));
        coordinates3.add(new Coordinates(6,10));
        coordinates3.add(new Coordinates(5,6));
        coordinates3.add(new Coordinates(5,7));
        coordinates3.add(new Coordinates(5,8));
        coordinates3.add(new Coordinates(5,9));
        color3.setCoordinates(coordinates3);
        Colors color4 = new Colors(Color.RED);
        ArrayList <Coordinates> coordinates4 = new ArrayList<Coordinates>();
        coordinates4.add(new Coordinates(0,9));
        coordinates4.add(new Coordinates(0,10));
        coordinates4.add(new Coordinates(1,10));
        coordinates4.add(new Coordinates(1,9));
        coordinates4.add(new Coordinates(0,6));
        coordinates4.add(new Coordinates(1,5));
        coordinates4.add(new Coordinates(2,5));
        coordinates4.add(new Coordinates(3,5));
        coordinates4.add(new Coordinates(4,5));
        color4.setCoordinates(coordinates4);

        boolean filled = false;
        //create Array of size of the board
        this.board = new Field[11][11];
        this.setRowCount(11);
        this.setColumnCount(11);

        //initialize complete board with empty Imageviews
        for(int i=0; i<11; i++) {
            for (int j = 0; j < 11; j++) {
                filled = false;
                board[i][j] = new Field(getContext(), this.attrs, color1, filled);
            }
        }
        //change Imageviews for colors
        for(int k=0; k<color1.getCoordinates().size(); k++)
        {
            filled=true;
            board[color1.getCoordinates().get(k).getX()][color1.getCoordinates().get(k).getY()] = new Field(getContext(), this.attrs, color1, filled);
            board[color2.getCoordinates().get(k).getX()][color2.getCoordinates().get(k).getY()] = new Field(getContext(), this.attrs, color2, filled);
            board[color3.getCoordinates().get(k).getX()][color3.getCoordinates().get(k).getY()] = new Field(getContext(), this.attrs, color3, filled);
            board[color4.getCoordinates().get(k).getX()][color4.getCoordinates().get(k).getY()] = new Field(getContext(), this.attrs, color4, filled);
        }
        //change Imageviews for rest of the fields
        for (int l=0; l<color0.getCoordinates().size(); l++)
        {
            filled=true;
            board[color0.getCoordinates().get(l).getX()][color0.getCoordinates().get(l).getY()] = new Field(getContext(), this.attrs, color0, filled);
        }
        //add design of imageviews to the board
        // add it after all colors are set
        for(int i=0; i<11; i++) {
            for (int j = 0; j < 11; j++) {

                addView(board[i][j]);
            }
        }

        //add player
        board[0][0].addPlayer(Color.GREEN, getContext());
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(!this.layoutDone)
        {
            for (int i = 0; i < 11; i++) {
                for (int j = 0; j < 11; j++)
                {
                    ViewGroup.LayoutParams params = board[i][j].getLayoutParams();
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        params.width = (int) ((right-left)) / this.getColumnCount();
                        params.height = (int) ((right-left)) / this.getRowCount();

                    }
                    else {
                        params.width = (int) ((bottom-top)) / this.getColumnCount();
                        params.height = (int) ((bottom-top))/ this.getRowCount();
                    }
                    board[i][j].setLayoutParams(params);
                }
            }
            this.layoutDone=true;
        }
    }
}
