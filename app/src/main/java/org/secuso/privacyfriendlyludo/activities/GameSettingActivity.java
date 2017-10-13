package org.secuso.privacyfriendlyludo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;

import org.secuso.privacyfriendlyludo.R;
import org.secuso.privacyfriendlyludo.logic.Player;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

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


public class GameSettingActivity extends AppCompatActivity {

    RecyclerViewCollectionAdapter adapter;
    private ArrayList<Player> player = new ArrayList<>();
    private ArrayList<String> playername_saved = new ArrayList<>();
    int listposition;
    private Set<Integer> generated = new LinkedHashSet<>();
    private int max_players;
    int gametyp;
    boolean useOwnDice;
    protected SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            player = savedInstanceState.getParcelable("Players");
            playername_saved = savedInstanceState.getStringArrayList("Playernames_saved");
        }
        setContentView(R.layout.activity_game_setting);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        useOwnDice = mSharedPreferences.getBoolean("own_dice", false);
        Intent intent = getIntent();
        Bundle mybundle = intent.getExtras();
        if (mybundle != null) {
            // save it for later
           if (intent.getParcelableArrayListExtra("Players") != null) {
                // when setting loaded
                player = intent.getParcelableArrayListExtra("Players");
               for (int i=0; i<player.size(); i++)
               {
                   // initialize playername_saved with "player"
                   if (!player.get(i).isAI())
                   {
                       playername_saved.add(player.get(i).getName());
                   }
                   else
                   {
                       playername_saved.add("");
                   }

               }
            }
            if (useOwnDice)
            {
                // own dice is used, all players can only be real player
                for (int i=0; i<player.size(); i++)
                {
                    player.get(i).setAI(false);
                    if (playername_saved.get(i).equals(""))
                    {
                        playername_saved.set(i,getResources().getString(R.string.playername_default_value));
                        player.get(i).setName(getResources().getString(R.string.playername_default_value));
                    }
                    else {
                        player.get(i).setName(playername_saved.get(i));
                    }
                }
            }
        }

        RecyclerView mPlayerList = (RecyclerView) findViewById(R.id.playerList);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mPlayerList.setHasFixedSize(true);
        adapter = new RecyclerViewCollectionAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mPlayerList.setLayoutManager(mLayoutManager);
        mPlayerList.setItemAnimator(new DefaultItemAnimator());
        mPlayerList.setAdapter(adapter);
        // determine gametype
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        gametyp = mSharedPreferences.getInt("lastChosenPage", 0);

        // 4 players
        if(gametyp==0)
        {
            max_players = 4;
        }
        // 6 players
        else
        {
            max_players = 6;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_game_start:
                // onclick startgame
                if (min_player_reached() && checkColorUniqueness() && checkPlayerNames()) {

                    Intent intent = new Intent(GameSettingActivity.this, GameActivity.class);
                    intent.putParcelableArrayListExtra("Players", player);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    saveSettings();
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    public void  saveSettings() {

        // save recent settings
        // save settings
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        String file_name = "";
        switch (max_players) {
            case 4:
                file_name = "save_settings_4players";
                break;
            case 6:
                file_name = "save_settings_6players";
                break;
            default:
                break;
        }
        try {
            fos = openFileOutput(file_name, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(player);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) try {
                oos.close();
            } catch (IOException ignored) {
            }
            if (fos != null) try {
                fos.close();
            } catch (IOException ignored) {
            }
        }
    }

    private class RecyclerViewCollectionAdapter extends RecyclerView.Adapter<ViewHolder> {

        static final int PLAYER = 0;
        static final int ADDPLAYER = 1;

        @Override
        public int getItemCount() {

            if (player == null)
            {
                return 1;
            }
            // maximum player reached
            else if (player.size()==max_players)
            {
                return player.size();
            }
            else
            {
                return (player.size() + 1);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view;
            switch(viewType) {
                case PLAYER:
                    view = getLayoutInflater().inflate(R.layout.player_item, viewGroup, false);
                    return new PlayerViewHolder(view);
                case ADDPLAYER:
                    view = getLayoutInflater().inflate(R.layout.add_player, viewGroup, false);
                    return new AddPlayerViewHolder(view);
                default:
                    view = getLayoutInflater().inflate(R.layout.add_player, viewGroup, false);
                    return new AddPlayerViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder vh, int position) {
            switch(vh.getItemViewType()) {

                case PLAYER:
                    // initializing
                    final PlayerViewHolder playerViewHolder = (PlayerViewHolder)vh;
                    playerViewHolder.playerColor.setBackgroundColor(player.get(position).getColor());
                    playerViewHolder.playerName.setText(player.get(position).getName());
                    if (useOwnDice)
                    {
                        playerViewHolder.playertype.setVisibility(View.INVISIBLE);
                        playerViewHolder.person.setVisibility(View.VISIBLE);
                            player.get(position).setAI(false);
                    }
                    else {
                        playerViewHolder.playertype.setVisibility(View.VISIBLE);
                        playerViewHolder.person.setVisibility(View.INVISIBLE);
                        playerViewHolder.playertype.getThumbDrawable().setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.darkblue), PorterDuff.Mode.SRC_ATOP);
                        if (player.get(position).isAI()) {
                            playerViewHolder.playertype.setChecked(false);
                        } else {
                            playerViewHolder.playertype.setChecked(true);
                        }
                    }

                    //on click DeletePlayer
                    playerViewHolder.delete_player.setOnClickListener(new View.OnClickListener() {
                        boolean deleted = false;
                        @Override
                        public void onClick(View view) {
                            if(deleted) return;
                            deleted = true;
                            int listPosition = vh.getAdapterPosition();
                            player.remove(listPosition);
                            playername_saved.remove(listPosition);
                            adapter.notifyItemRemoved(listPosition);
                        }
                    });
                    // on click playerColor
                    playerViewHolder.playerColor.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(final View view) {

                            ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
                            colorPickerDialog.initialize(
                                   R.string.choose_color, getResources().getIntArray(R.array.playerColors), R.color.black, 3, getResources().getIntArray(R.array.playerColors).length);
                            colorPickerDialog.show(getFragmentManager(),"tag");
                            colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

                                @Override
                                public void onColorSelected(int color) {
                                    int listPosition = vh.getAdapterPosition();
                                    // check if another player has the same color
                                    for (int i=0; i<player.size(); i++)
                                    {
                                        if (player.get(i).getColor() == color)
                                        {
                                            // swap both colors
                                            int old_color = ((ColorDrawable)view.getBackground()).getColor();
                                            player.get(i).setColor(old_color);
                                        }
                                    }
                                    player.get(listPosition).setColor(color);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });

                    //on click playertype
                    playerViewHolder.playertype.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {
                            int listPosition = vh.getAdapterPosition();
                            player.get(listPosition).setAI(!player.get(listPosition).isAI());
                                adapter.notifyItemChanged(listPosition);
                                if (player.get(listPosition).isAI())
                                {
                                    // change playername
                                    player.get(listPosition).setName(getResources().getString(R.string.computername_default_value));
                                    //view.setBackgroundResource( R.drawable.ic_computer_black_24dp);
                                }
                                else
                                {
                                    if (playername_saved.get(listPosition) == "")
                                    {
                                        player.get(listPosition).setName(getResources().getString(R.string.playername_default_value));
                                    }
                                    else
                                    {
                                        player.get(listPosition).setName(playername_saved.get(listPosition));
                                    }
                                }

                        }
                    });

                    // only visible in own_dice_mode
                    playerViewHolder.person.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(GameSettingActivity.this, getString(R.string.only_person_player), Toast.LENGTH_LONG).show();
                        }
                    }
                    );

                    playerViewHolder.playerName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final int listPosition = vh.getAdapterPosition();
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(GameSettingActivity.this);
                            View Dialogview = getLayoutInflater().inflate(R.layout.playername_dialog, null);
                            builder.setView(Dialogview);
                            final EditText playername_typed = (EditText) Dialogview.findViewById(R.id.editText);

                            //if original value is still in field --> delete value
                            if (player.get(listPosition).getName().equals(getResources().getString(R.string.playername_default_value)))
                            {
                                playername_typed.setText("");
                            }
                            else
                            {
                                playername_typed.setText(player.get(listPosition).getName());
                            }

                            builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (!playername_typed.getText().toString().equals("")) {
                                        player.get(listPosition).setName(playername_typed.getText().toString());
                                        if(!player.get(listPosition).isAI())
                                        {
                                            playername_saved.set(listPosition, playername_typed.getText().toString());
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                    dialog.dismiss();

                                }
                            });

                            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }
                    });
                    break;

                case ADDPLAYER:
                    final AddPlayerViewHolder addplayerViewHolder = (AddPlayerViewHolder) vh;
                    //onclick addPlayer
                    addplayerViewHolder.addPlayer.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {
                            if ((player!=null) && (player.size() == max_players)) {
                                Toast.makeText(GameSettingActivity.this, getString(R.string.max_player_reached), Toast.LENGTH_LONG).show();
                            } else {
                                  Set<Integer> generated = new LinkedHashSet<>();
                                // give each new player a unique color
                                    // step 1: add all already used colors to a array
                                    for (int i = 0; i < player.size(); i++) {
                                        generated.add(player.get(i).getColor());
                                    }
                                    // step 2: choose randomly another color for the new Player
                                    int[] androidColors = getBaseContext().getResources().getIntArray(R.array.playerColors);
                                int colorToUse = R.color.black;
                                while (generated.size() < (player.size())+1) {
                                        int random_id = new Random().nextInt(androidColors.length);
                                        // As we're adding to a set, this will automatically do a containment check
                                        colorToUse = androidColors[random_id];
                                        generated.add(colorToUse);
                                    }
                                String playername =  getResources().getString(R.string.playername_default_value);
                                player.add(new Player(3, colorToUse, playername, false, 1));
                                //save shadowcopy of playername in another array
                                playername_saved.add(playername);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                        break;
                default:
                    // nix :)
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (player ==null)
            {
                return ADDPLAYER;
            }
            // Max_player_reached
            else if (player.size()==max_players )
            {
                return PLAYER;
            }
            else
            {
                return (position < player.size()) ? PLAYER : ADDPLAYER;
            }
        }
    }

    abstract class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class AddPlayerViewHolder extends ViewHolder {

        ImageButton addPlayer;

        AddPlayerViewHolder(View itemView) {
            super(itemView);
            addPlayer = (ImageButton) itemView.findViewById(R.id.button_add_player);
        }
    }

    private class PlayerViewHolder extends ViewHolder {

        ImageButton playerColor;
        Switch playertype;
        ImageButton delete_player;
        TextView playerName;
        ImageView person;

        PlayerViewHolder(final View itemView) {
            super(itemView);
            person = (ImageView) itemView.findViewById(R.id.image_player_type);
            playertype = (Switch) itemView.findViewById(R.id.switch_player_type);
            playerColor = (ImageButton) itemView.findViewById(R.id.button_player_color);
            playerName = (TextView) itemView.findViewById(R.id.textView_player_name);
            delete_player = (ImageButton) itemView.findViewById(R.id.button_delete_player);
        }
    }

    public boolean min_player_reached()
    {
        if (player.size()<2)
        {
            Toast.makeText(GameSettingActivity.this, getString(R.string.enough_players), Toast.LENGTH_LONG).show();
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean checkColorUniqueness() {
        generated.clear();
        for (int i = 0; i < player.size(); i++) {
            generated.add(player.get(i).getColor());
        }
        if (generated.size() == player.size()) {
            return true;
        } else {

            Toast.makeText(GameSettingActivity.this, getString(R.string.color_uniqueness), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean checkPlayerNames() {
        for (int i = 0; i < player.size(); i++) {

            if (player.get(i).getName().equals("")) {
                Toast.makeText(GameSettingActivity.this, getString(R.string.Missing_playername), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putParcelableArrayList("Player", player);
        savedInstanceState.putBoolean("useOwnDice", useOwnDice);
        savedInstanceState.putStringArrayList("Playernames_saved", playername_saved);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        saveSettings();
    }

}


