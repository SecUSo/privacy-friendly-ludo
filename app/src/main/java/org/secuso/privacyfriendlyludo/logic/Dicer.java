package org.secuso.privacyfriendlyludo.logic;

import android.util.Log;

import java.security.SecureRandom;

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

class Dicer {


    int rollDice(BoardModel model)
    {
       int dice;
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[6];
        random.nextBytes(bytes);

        // if (model.getRecent_player().getId()==1 || model.getRecent_player().getId()==3)
        {
            dice = random.nextInt(6) +1;
        }
        //else
        //{
        //    dice = 3;
        //}




        return dice;
    }

}
