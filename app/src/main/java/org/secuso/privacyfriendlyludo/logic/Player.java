package org.secuso.privacyfriendlyludo.logic;

import java.util.ArrayList;

/**
 * Created by Julchen on 28.05.2017.
 */

public class Player {

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

}
