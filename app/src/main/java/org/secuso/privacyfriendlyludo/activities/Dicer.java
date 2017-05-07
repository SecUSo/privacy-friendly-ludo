package org.secuso.privacyfriendlyludo.activities;

import android.util.Log;

import java.security.SecureRandom;


public class Dicer {

    public int rollDice(){
        int dice = 0;
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[6];
        random.nextBytes(bytes);
        dice = random.nextInt(6) +1;

        return dice;
    }

}
