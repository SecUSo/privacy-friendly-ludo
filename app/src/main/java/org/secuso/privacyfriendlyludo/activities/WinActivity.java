package org.secuso.privacyfriendlyludo.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.secuso.privacyfriendlyludo.R;
import org.secuso.privacyfriendlyludo.logic.Player;

import java.util.ArrayList;

public class WinActivity extends AppCompatActivity {

    private RecyclerView mPlayerList;
    private RecyclerView.Adapter adapter;
    private ArrayList<Player> players;
    private ArrayList<Integer> WinnerOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        Intent intent = getIntent();
        players =  intent.getParcelableArrayListExtra("Players");
        WinnerOrder =  intent.getIntegerArrayListExtra("WinnerOrder");
        mPlayerList = (RecyclerView) findViewById(R.id.winDetailsList);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        adapter = new WinActivity.RecyclerViewCollectionAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mPlayerList.setLayoutManager(mLayoutManager);
        mPlayerList.setItemAnimator(new DefaultItemAnimator());
        mPlayerList.setAdapter(adapter);
    }

    class RecyclerViewCollectionAdapter extends RecyclerView.Adapter<WinActivity.ViewHolder> {

        public static final int TITLE = 0;
        public static final int PLAYER = 1;

        @Override
        public int getItemCount() {
            return players.size()+1;
        }

        @Override
        public WinActivity.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view;
            switch(viewType) {
                case PLAYER:
                    view = getLayoutInflater().inflate(R.layout.win_details_item, viewGroup, false);
                    return new WinActivity.PlayerViewHolder(view);
                case TITLE:
                    view = getLayoutInflater().inflate(R.layout.win_title_item, viewGroup, false);
                    return new WinActivity.TitleViewHolder(view);
                default:
                    view = getLayoutInflater().inflate(R.layout.win_title_item, viewGroup, false);
                    return new WinActivity.TitleViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(final WinActivity.ViewHolder vh, int position) {
            switch(vh.getItemViewType()) {
                case PLAYER:
                    // initializing
                    // check the order of winners
                    int playerid=0;
                    playerid = WinnerOrder.get(position-1);

                    WinActivity.PlayerViewHolder playerViewHolder = (WinActivity.PlayerViewHolder)vh;
                    playerViewHolder.playerName.setText(players.get(playerid-1).getName());
                    int[] stats = players.get(playerid-1).getStatistics();
                    playerViewHolder.dice1.setText("" + stats[0] + "");
                    playerViewHolder.dice2.setText("" + stats[1] + "");
                    playerViewHolder.dice3.setText("" + stats[2] + "");
                    playerViewHolder.dice4.setText("" + stats[3] + "");
                    playerViewHolder.dice5.setText("" + stats[4] + "");
                    playerViewHolder.dice6.setText("" + stats[5] + "");
                    playerViewHolder.rank_number.setText("" + position + "");
                    break;
                case TITLE:
                    WinActivity.TitleViewHolder titleViewHolder = (WinActivity.TitleViewHolder) vh;
                    break;
                default:
                    // nix :)
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            return (position==0) ? TITLE : PLAYER;
        }
    }

    abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    };

    class TitleViewHolder extends WinActivity.ViewHolder {

        public TitleViewHolder(View itemView) {
            super(itemView);
        }
    }

    class PlayerViewHolder extends WinActivity.ViewHolder {

        TextView playerName;
        TextView rank_number;
        TextView dice1;
        TextView dice2;
        TextView dice3;
        TextView dice4;
        TextView dice5;
        TextView dice6;

        public PlayerViewHolder(final View itemView) {
            super(itemView);
            playerName = (TextView) itemView.findViewById(R.id.textView_player_name);
            dice1 = (TextView) itemView.findViewById(R.id.textView_d1);
            dice2 = (TextView) itemView.findViewById(R.id.textView_d2);
            dice3 = (TextView) itemView.findViewById(R.id.textView_d3);
            dice4 = (TextView) itemView.findViewById(R.id.textView_d4);
            dice5 = (TextView) itemView.findViewById(R.id.textView_d5);
            dice6 = (TextView) itemView.findViewById(R.id.textView_d6);
            rank_number = (TextView) itemView.findViewById(R.id.textView_rank);
        }
    }
}
