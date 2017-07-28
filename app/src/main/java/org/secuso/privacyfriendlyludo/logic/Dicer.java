package org.secuso.privacyfriendlyludo.logic;

import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.security.SecureRandom;


public class Dicer {

    public int rollDice(boolean mydice)
    {
        int dice = 0;
        if (mydice)
        {
            dice = 5;
        }
        else
        {
            dice=6;
        }
/*
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[6];
        random.nextBytes(bytes);
        dice = random.nextInt(6) +1;
*/
        return dice;
    }

}
