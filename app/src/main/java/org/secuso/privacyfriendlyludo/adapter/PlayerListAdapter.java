package org.secuso.privacyfriendlyludo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.secuso.privacyfriendlyludo.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Julchen on 16.05.2017.
 */

public class PlayerListAdapter extends RecyclerView.Adapter {

    List<String> playerNames = new LinkedList<>();
    List<String> activeColors = new LinkedList<>();

    public PlayerListAdapter(List<String> playerNames, List<String> activeColors) {
        this.playerNames = playerNames;
        this.activeColors = activeColors;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {
            case 0:
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_item, parent, false);
                return new PlayerViwHolder(itemView);
            case 1:
                //..

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(holder.getItemViewType()) {

        }
    }

    @Override
    public int getItemCount() {
        return playerNames.size();
    }

    class PlayerViwHolder extends ViewHolder {

        Button playerIndicator;
        TextView playerName;
        ImageButton playerColor;

        public PlayerViwHolder(View itemView) {
            super(itemView);

            playerIndicator = (Button) itemView.findViewById(R.id.button);
            playerName = (TextView) itemView.findViewById(R.id.textView);
            playerColor = (ImageButton) itemView.findViewById(R.id.imageButton);

        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
