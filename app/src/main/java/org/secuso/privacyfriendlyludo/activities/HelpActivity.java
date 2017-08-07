package org.secuso.privacyfriendlyludo.activities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import android.os.Bundle;
import android.widget.ExpandableListView;

import org.secuso.privacyfriendlyludo.helpers.ExpandableListAdapter;
import org.secuso.privacyfriendlyludo.helpers.HelpDataDump;
import org.secuso.privacyfriendlyludo.R;

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


public class HelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ExpandableListAdapter expandableListAdapter;
        HelpDataDump helpDataDump = new HelpDataDump(this);

        ExpandableListView generalExpandableListView = (ExpandableListView) findViewById(R.id.generalExpandableListView);

        LinkedHashMap<String, List<String>> expandableListDetail = helpDataDump.getDataGeneral();
        List<String> expandableListTitleGeneral = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new ExpandableListAdapter(this, expandableListTitleGeneral, expandableListDetail);
        generalExpandableListView.setAdapter(expandableListAdapter);

        overridePendingTransition(0, 0);
    }

    protected int getNavigationDrawerID() {
        return R.id.nav_help;
    }

}
