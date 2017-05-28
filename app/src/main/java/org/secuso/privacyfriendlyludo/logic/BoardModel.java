package org.secuso.privacyfriendlyludo.logic;

import java.util.ArrayList;

/**
 * Created by Julchen on 28.05.2017.
 */

public class BoardModel implements BoardModelInterface {

    ArrayList all_player_observer = new ArrayList();
    int dice = 0;

    @Override
    public void setFigurePosition(ArrayList player_array) {

    }

    @Override
    public ArrayList getFigurePosition() {
        return null;
    }

    @Override
    public void setDice(int dice_number) {

    }

    @Override
    public int getDice() {
        return dice;
    }

    @Override
    public void registerObserver(PlayerObserver player) {
        all_player_observer.add(player);
    }

    @Override
    public void removeObserver(PlayerObserver player) {
        int i = all_player_observer.indexOf(player);
        if (i>=0)
        {
            all_player_observer.remove(i);
        }

    }

    public void notifyallPlayerObserver()
    {
        for(int i=0; i < all_player_observer.size(); i++)
        {
            PlayerObserver observer = (PlayerObserver) all_player_observer.get(i);
            observer.updatePosition();
        }
    }
}
