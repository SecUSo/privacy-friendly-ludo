package org.secuso.privacyfriendlyludo.logic;

import java.util.ArrayList;

/**
 * Created by Julchen on 28.05.2017.
 */

public interface BoardModelInterface {

    //functions of the model
    void setFigurePosition(ArrayList player_array);
    ArrayList getFigurePosition();
    void setDice(int dice_number);
    int getDice();
    void registerObserver(PlayerObserver player);
    void removeObserver(PlayerObserver player);


}
