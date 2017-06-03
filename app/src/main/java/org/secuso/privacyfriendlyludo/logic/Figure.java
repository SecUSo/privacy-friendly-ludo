package org.secuso.privacyfriendlyludo.logic;

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
        else if (field_position_index>40 && field_position_index<56)
        {
            this.state="end";
        }
        else
        {
            this.state="finished";
        }

    }

    private String state;

    public Figure(int id, int field_position_index) {
        this.field_position_index = field_position_index;
        this.state = "start";
       // this.state="inGame";
       // this.count_steps = field_position_index;
        this.count_steps = 0;
                this.id = id;
    }

    public int getField_position_index() {
        return field_position_index ;
    }

    public void setField_position_index(int field_position_index) {
        this.field_position_index = field_position_index;
    }

}
