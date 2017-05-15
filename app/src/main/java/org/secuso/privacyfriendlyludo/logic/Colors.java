package org.secuso.privacyfriendlyludo.logic;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Julchen on 15.05.2017.
 */

public class Colors {

    public ArrayList<Coordinates> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Coordinates> coordinates) {
        this.coordinates = coordinates;
    }

    //positions of colors
    private ArrayList<Coordinates> coordinates;


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private int color;


    public Colors(int color){
        this.color = color;

    }



}
