package org.secuso.privacyfriendlyludo.logic;

/**
 * Created by Julchen on 15.05.2017.
 */

public class Coordinates {
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    private int x;

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private int y;
    public Coordinates(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
