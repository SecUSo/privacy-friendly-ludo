package org.secuso.privacyfriendlyludo.helpers;

import android.content.Context;

import org.secuso.privacyfriendlyludo.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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

public class HelpDataDump {

    private Context context;

    public HelpDataDump(Context context) {
        this.context = context;
    }

    public LinkedHashMap<String, List<String>> getDataGeneral() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        List<String> GameTyp = new ArrayList<String>();
        GameTyp.add(context.getResources().getString(R.string.help_playerNumber_answer));

        expandableListDetail.put(context.getResources().getString(R.string.help_playerNumber), GameTyp);

        List<String> general = new ArrayList<String>();
        general.add(context.getResources().getString(R.string.help_whatis_answer));

        expandableListDetail.put(context.getResources().getString(R.string.help_whatis), general);

        List<String> privacy = new ArrayList<String>();
        privacy.add(context.getResources().getString(R.string.help_privacy_answer));

        expandableListDetail.put(context.getResources().getString(R.string.help_privacy), privacy);

        List<String> permissions = new ArrayList<String>();
        permissions.add(context.getResources().getString(R.string.help_permission_answer));

        expandableListDetail.put(context.getResources().getString(R.string.help_permission), permissions);

        return expandableListDetail;
    }

}
