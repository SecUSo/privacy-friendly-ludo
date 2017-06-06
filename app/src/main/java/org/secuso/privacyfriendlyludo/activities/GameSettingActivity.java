package org.secuso.privacyfriendlyludo.activities;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.secuso.privacyfriendlyludo.R;
import org.secuso.privacyfriendlyludo.adapter.PlayerListAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameSettingActivity extends AppCompatActivity{

  //  RecyclerView mPlayerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setting);
/*
        mPlayerList = (RecyclerView) findViewById(R.id.playerList);
        List<String> playerList = new LinkedList<>();
        playerList.add("Hallo");
        mPlayerList.setAdapter(new PlayerListAdapter(playerList, new LinkedList<String>()));
        */
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_game:
                Intent intent = new Intent(GameSettingActivity.this, GameActivity.class);
                intent.putExtra("PlayerNames", "Anna");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            default:
        }
    }
}
