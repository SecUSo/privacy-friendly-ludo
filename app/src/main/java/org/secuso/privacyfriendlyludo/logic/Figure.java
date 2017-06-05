package org.secuso.privacyfriendlyludo.logic;

import android.util.Log;

/**
 * Created by Julchen on 28.05.2017.
 */

public class Figure {

    private int field_position_index;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public int getCount_steps() {
        return count_steps;
    }

    public void setCount_steps(int count_steps) {
        this.count_steps = count_steps;
    }

    private int count_steps;

    public String getState() {
        return state;
    }

    public void setState(int field_position_index) {
        if (field_position_index <=40 && field_position_index >0)
        {
            this.state="inGame";
        }
        else if (field_position_index >100)
        {
            this.state="start";
        }
        else if (field_position_index>40 && field_position_index<56)
        {
            this.state="end";
        }
        else
        {
            Log.i("tag", "error");
        }


    }

    private String state;

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    private boolean finished;

    public Figure(int id, int field_position_index) {
        this.field_position_index = field_position_index;
        this.state = "start";
        this.count_steps = 0;
        this.id = id;
        this.finished = false;
    }

    public int getField_position_index() {
        return field_position_index ;
    }

    public void setField_position_index(int field_position_index) {
        this.field_position_index = field_position_index;
    }

}
