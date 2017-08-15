package org.secuso.privacyfriendlyludo.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import org.secuso.privacyfriendlyludo.R;
import org.secuso.privacyfriendlyludo.logic.BoardModel;
import org.secuso.privacyfriendlyludo.logic.GameType;
import org.secuso.privacyfriendlyludo.logic.Player;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import android.os.Handler;

/*  @author: Julia Schneider

  This file is part of the Game Ludo.

 Ludo is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 You should have received a copy of the GNU General Public License
 along with Ludo.  If not, see <http://www.gnu.org/licenses/>.
 */

public class GameActivity extends AppCompatActivity {

    private ImageView rollDice;
    private TextView playermessage;
    private TextView showTask;
    SharedPreferences sharedPreferences;
    Bundle mybundle;
    Dialog dialog;
    private BoardView boardView;
    BoardModel model;
    View.OnClickListener myOnlyhandler;
    int timer;
    boolean marked;
    int old_figure_index;
    Drawable[] layers = new Drawable[2];
    Resources r;
    LayerDrawable layersDrawable;
    boolean isCounterRunning;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        isCounterRunning=false;
        timer=1000;
        setContentView(R.layout.activity_game);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("keepScreenOn", false))
        {
            // keep screen on
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }


        Intent intent = getIntent();
        mybundle = intent.getExtras();
          if (mybundle != null) {
              model = mybundle.getParcelable("BoardModel");
              if (model != null) {
                  // control comes from main activity
                  // there is a resumeable game
                  savedInstanceState = mybundle;
              }
              //else --> control comes from gameSetting
              // reset mybundle
              mybundle = null;

          }
        super.onCreate(savedInstanceState);

        // check if instancestate is empty
        // important for display orientation changes
            if (savedInstanceState != null) {
                model = savedInstanceState.getParcelable("BoardModel");
            }
            else
            {
                // new Game
                ArrayList<Player> playerArrayList = intent.getParcelableArrayListExtra("Players");
                boolean useOwnDice = intent.getBooleanExtra("own_dice", false);
                // determine gametype
                SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                int type = mSharedPreferences.getInt("lastChosenPage", -1);
                // repair id if necessary
                for (int i=0; i<playerArrayList.size(); i++)
                {
                    playerArrayList.get(i).setId(i+1);
                }
                GameType game_typ;
                switch(type)
                {
                    case 0:
                        game_typ = GameType.Four_players;
                        break;
                    case 1:
                        game_typ = GameType.Six_players;
                        break;
                    default:
                        game_typ=GameType.Four_players;
                }
                model = new BoardModel(this, playerArrayList, game_typ, useOwnDice);
            }

        //String textzeile = intent.getStringExtra("PlayerNames");
        boardView = null;
        boardView = (BoardView) findViewById(R.id.board);
        //boardView = (GridLayout) findViewById(R.id.board);
        boardView.createBoard(model);

        //Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        // only show info about recent player if Own dice is not used
        if (!model.useOwnDice)
        {
            String playername = model.getRecent_player().getName();
            String playerMessageString = getString(R.string.player_name);
            playerMessageString = playerMessageString.replace("%p", playername);
            playermessage = (TextView) findViewById(R.id.changePlayerMessage);
            playermessage.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.black));
            playermessage.setText(playerMessageString);
            showTask = (TextView) findViewById(R.id.showTask);
            if (!model.getRecent_player().isAI()) {
                showTask.setText(R.string.Task_Roll_Dice);
            }
        }

        // only build a dice if switch for use own dice is deactivated
        if (!model.useOwnDice)
        {
            rollDice = (ImageView) findViewById(R.id.resultOne);
            r  = getResources();
            layers[0] = ContextCompat.getDrawable(getBaseContext(), R.drawable.dice);
            if (model.getDice_number() == 0)
            {
                layers[1] = ContextCompat.getDrawable(getBaseContext(), R.drawable.d4);
            }
            else
            {
                initResultDiceViews(model.getDice_number(), rollDice);
            }

            layersDrawable = new LayerDrawable(layers);

            layersDrawable.mutate();
            layersDrawable.setLayerInset(0, 0, 0, 0, 0);
            layersDrawable.setLayerInset(0, 0, 0, 0, 0);
            // change color of dice
            int new_color = model.getRecent_player().getColor();
            layersDrawable.getDrawable(0).setColorFilter(new_color, PorterDuff.Mode.SRC);
            rollDice.setImageDrawable(layersDrawable);
            rollDice.setVisibility(View.VISIBLE);
            Point size = new Point();
            this.getWindowManager().getDefaultDisplay().getSize(size);
            android.view.ViewGroup.LayoutParams layoutParams = rollDice.getLayoutParams();
            layoutParams.width = size.x / 6;
            layoutParams.height = size.x / 6;
            rollDice.setLayoutParams(layoutParams);

            rollDice.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    rollDice();
                }
            });

        }

        myOnlyhandler = new View.OnClickListener() {
            public void onClick(View v) {

                if (model.useOwnDice)
                {
                    interactWithFigure(v);
                }
                else
                {
                    int old_figure_index = v.getId();
                    setFigures(old_figure_index);
                    // clear old movable figures list
                    model.getMovable_figures().clear();
                    Next_player();
                }

                }
            };

        if (model.isGame_finished()) {

            handler.removeCallbacks(doAIActions);
            ShowStatistics();
        }

        if(model.getMovable_figures()!=null && model.getMovable_figures().size()>1)
        {
            markMovableFigures();
            showTask.setText(R.string.TaskSetFigure);
        }

        // roll dice automatically if it is an AI
        if (model.getRecent_player().isAI() && !model.useOwnDice && !model.isGame_finished()) {

            handler.postDelayed(doAIActions, timer);
        }
        else if (model.useOwnDice)
        {
            boardView.makeAllFieldsClickable(myOnlyhandler);
        }
    }

    private final Runnable doAIActions = new Runnable() {
        @Override
        public void run() {

                // List of movable Figures is empty
                // for first move
                if (model.getMovable_figures() == null || (model.getMovable_figures().size() <= 1)) {
                    rollDice();
                }
                // there are movable Figures
                else {
                    // choose randomly a figureid of movablefigures
                    SecureRandom random = new SecureRandom();
                    // -2 because of zero and dice_number
                    int max_figures = model.getMovable_figures().size() - 1;
                    int random_num = random.nextInt(max_figures) + 1;
                    // integer between  zero and max_figures
                    int figure_id = model.getMovable_figures().get(random_num);
                    int position_id = model.getRecent_player().getFigures().get(figure_id - 1).getField_position_index();
                    // move figure
                    setFigures(position_id);
                    model.getMovable_figures().clear();
                    //give control to next player
                    Next_player();
                }
                if (!model.isGame_finished() && model.getRecent_player().isAI())
                {
                    handler.postDelayed(this, timer);
                }

        }
    };

    private void interactWithFigure(View v)
    {
        int new_figure_index = v.getId();
        ArrayList<Integer>playerInfos;
        ArrayList<Integer>playerInfos_opponent;
        // check if a figure is on chosen position
        boolean is_empty = model.no_player_on_field(new_figure_index);

        if ((marked) && old_figure_index == new_figure_index && !is_empty)
        {
            // deselect figure
            playerInfos = boardView.getPlayerInfos(model,old_figure_index);
            boardView.SelectAndDeselectPlayer(model,old_figure_index, playerInfos.get(0), true);
            marked=false;
            // save index for next time
            old_figure_index = new_figure_index;
        }
        else if ((marked) && (old_figure_index != new_figure_index))
        {
            // second click on another field move marked figure to this field
            //check if another figure is already there
            playerInfos_opponent = boardView.getPlayerInfos(model, new_figure_index);
            playerInfos = boardView.getPlayerInfos(model,old_figure_index);

            if(!is_empty) {
                // check if it is same player
                boolean not_same_player_on_field = model.isEmptyofSamePlayer(new_figure_index, playerInfos.get(0));
                if (not_same_player_on_field && (new_figure_index <= model.getLast_field_index())) {
                    // set figure back to start
                    int new_opponent_figure_index = model.moveFigure(new_figure_index, true);
                    boardView.setFigureToNewPosition(model, playerInfos_opponent.get(0), new_figure_index, new_opponent_figure_index, true);
                    // update player and board information
                    model.updateBoard(playerInfos_opponent.get(0), playerInfos_opponent.get(1), new_figure_index, new_opponent_figure_index);
                    model.updatePlayer(playerInfos_opponent.get(0), playerInfos_opponent.get(1), 0, new_opponent_figure_index, true);
                    // deselect figure
                    // set figure to choosen point
                    boardView.SelectAndDeselectPlayer(model, old_figure_index, playerInfos.get(0), true);
                    boardView.setFigureToNewPosition(model, playerInfos.get(0), old_figure_index, new_figure_index, false);
                    // update player and board information
                    model.updateBoard(playerInfos.get(0), playerInfos.get(1), old_figure_index, new_figure_index);
                    model.updatePlayer(playerInfos.get(0), playerInfos.get(1), 0, new_figure_index, false);
                    marked = false;
                    // save index for next time
                    old_figure_index = 0;

                }
            }
            else
            {
                // deselect figure
                playerInfos = boardView.getPlayerInfos(model,old_figure_index);
                boardView.SelectAndDeselectPlayer(model,old_figure_index, playerInfos.get(0), true);
                boardView.setFigureToNewPosition(model, playerInfos.get(0), old_figure_index, new_figure_index, false);
                // update player and board information
                model.updateBoard(playerInfos.get(0), playerInfos.get(1),old_figure_index,new_figure_index);
                model.updatePlayer(playerInfos.get(0), playerInfos.get(1), 0, new_figure_index, false);
                marked = false;
                // save index for next time
                old_figure_index = 0;
            }
        }
        else if (!is_empty)// not marked
        {
            // select figure
            playerInfos = boardView.getPlayerInfos(model,new_figure_index);
            boardView.SelectAndDeselectPlayer(model,new_figure_index, playerInfos.get(0), false);
            marked=true;
            // save index for next time
            old_figure_index = new_figure_index;
        }
    }

    private void rollDice()
    {
        // count all number of roll dice of one player
        model.countRollDice =  model.countRollDice + 1;
        model.setMovable_figures(model.processDiceResult());
        model.setDice_number(model.getMovable_figures().get(0));
        initResultDiceViews(model.getDice_number(), rollDice);
        flashDiceResult(rollDice);

        if(model.getMovable_figures().size()==1) {
            // no movable Figures
            // choose next player
            Next_player();
        }
        else {
            // there are movable figures
            markMovableFigures();
            //change color of dice
            int new_color = model.getRecent_player().getColor();
            layersDrawable.getDrawable(0).setColorFilter(new_color, PorterDuff.Mode.SRC);
            rollDice.setImageDrawable(layersDrawable);
            // for AI randomly chose figure
            if (!model.getRecent_player().isAI()) {
                {
                    showTask = (TextView) findViewById(R.id.showTask);
                    showTask.setText(R.string.TaskSetFigure);
                }
            }
        }
    }

    private void setFigures(int old_figure_index)
    {
        int marked_figures;
        // i=1 because of dice_result
        // deselect all marked_figures
        for (int i = 1; i < model.getMovable_figures().size(); i++) {
            marked_figures = model.getMovable_figures().get(i);
            boardView.HidePossiblePlayers(model, marked_figures);
        }
        // check if allready another figure is on the new calculated field
        // kick out behaviour checking
        boolean isEmpty;
        int new_opponent_index;
        int figure_id;
        int player_id;
        if (old_figure_index < 100) {
            figure_id = model.getMy_game_field().getMyGamefield().get(old_figure_index - 1).getFigure_id();
            player_id = model.getMy_game_field().getMyGamefield().get(old_figure_index - 1).getPlayer_id();
        } else {
            figure_id = model.getStart_player_map().getMyGamefield().get(old_figure_index % 100).getFigure_id();
            player_id = model.getStart_player_map().getMyGamefield().get(old_figure_index % 100).getPlayer_id();
        }

        int old_opponent_index = model.getNewPosition(figure_id, model.getDice_number());
        isEmpty = model.no_player_on_field(old_opponent_index);
        if (!isEmpty) {
            model.setOpponent_player(model.getPlayers().get(player_id - 1));
            new_opponent_index = model.moveFigure(old_opponent_index, true);
            boardView.setFigureToNewPosition(model, model.getOpponent_player().getId(), old_opponent_index, new_opponent_index , true);

        }

        // calculate new position
        int new_figure_index = model.moveFigure(old_figure_index, false);

        // set Figure to new Position
        boardView.setFigureToNewPosition(model, model.getRecent_player().getId(), old_figure_index, new_figure_index, false);
    }

    private void Next_player()
    {
        boolean player_changed = model.playerChanged(model.countRollDice);
        int count_ready_player = 0;
        // change player until playerstate is not finished or game is finished
        while (model.getRecent_player().isFinished()) {
            player_changed = model.playerChanged(-1);
            count_ready_player = count_ready_player + 1;
            if (count_ready_player == (model.getPlayers().size()-1)) {
                // all players except one are finished
                model.setGame_finished(true);
                break;
            }
        }
        if (model.isGame_finished()) {

            handler.removeCallbacks(doAIActions);
            ShowStatistics();
        }
        else // still players are not ready, next players turn
        {
                    // change message for next player and reset countRollDice
                    if (player_changed) {
                        // show a message for player changed
                        String playername = model.getRecent_player().getName();
                        String playerMessageString = getString(R.string.player_name);
                        playerMessageString = playerMessageString.replace("%p", playername);
                        playermessage.setText(playerMessageString);
                        //reset counter of roll dice
                        model.countRollDice = 0;
                    }
                    //change color of dice
                    int new_color = model.getRecent_player().getColor();
                    layersDrawable.getDrawable(0).setColorFilter(new_color, PorterDuff.Mode.SRC);
                    rollDice.setImageDrawable(layersDrawable);
                    // check if it is a AI
                    if (!model.getRecent_player().isAI())
                    {
                        handler.removeCallbacks(doAIActions);
                        rollDice.setClickable(true);
                        showTask = (TextView) findViewById(R.id.showTask);
                        showTask.setText(R.string.Task_Roll_Dice);
                    }
                    else if (player_changed)
                    {
                        showTask.setText("");
                        handler.removeCallbacks(doAIActions);
                        handler.postDelayed(doAIActions, timer);
                    }
        }
    }

    private void ShowStatistics() {
        //game is finished
        playermessage.setText(R.string.finished_Game);
        showTask = (TextView) findViewById(R.id.showTask);
        showTask.setText("");
        rollDice.setVisibility(View.INVISIBLE);

        dialog = new Dialog(this, R.style.WinDialog);
        dialog.setContentView(R.layout.win_screen_layout);
        if (dialog.getWindow()!=null)
        {
            dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
            dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        }
        TextView name1 = (TextView) dialog.findViewById(R.id.textView_name1);
        TextView name2 = (TextView) dialog.findViewById(R.id.textView_name2);
        TextView name3 = (TextView) dialog.findViewById(R.id.textView_name3);
        TextView name4 = (TextView) dialog.findViewById(R.id.textView_name4);
        TextView name5 = (TextView) dialog.findViewById(R.id.textView_name5);
        TextView name6 = (TextView) dialog.findViewById(R.id.textView_name6);

        // add last player to winnerlist
        for(int i=0; i<model.getPlayers().size(); i++)
        {
            if (!model.getPlayers().get(i).isFinished() && model.order_of_winners.size() < model.getPlayers().size())
            {
                int playerid = model.getPlayers().get(i).getId();
                model.order_of_winners.add(playerid);
            }
        }


        // set text in correct order
        for (int i = 0; i < model.order_of_winners.size(); i++) {
            int player_id = model.order_of_winners.get(i);
            String winnerString = getString(R.string.Winner);
            int position = i+1;
            winnerString = winnerString.replace("%n", "" + position);
            String playerName = " " + model.getPlayers().get(player_id-1).getName();
            String concatenate = winnerString + playerName;

            switch (i) {
                case 0:
                    name1.setText(concatenate);
                    break;
                case 1:
                    name2.setText(concatenate);
                    break;
                case 2:
                    name3.setText(concatenate);
                    break;
                case 3:
                    name4.setText(concatenate);
                    break;
                case 4:
                    name5.setText(concatenate);
                    break;
                case 5:
                    name6.setText(concatenate);
                    break;
                default:
                    break;
            }
        }

        // Register onClickListener
        dialog.findViewById(R.id.win_more_info_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open new dialog with statistic information
                Intent intent = new Intent(GameActivity.this, WinActivity.class);
                intent.putParcelableArrayListExtra("Players", model.getPlayers());
                intent.putExtra("WinnerOrder", model.getOrder_of_winners());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        dialog.findViewById(R.id.win_open_Main_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show alertDialog
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(GameActivity.this);
                // Setting Dialog Title
                alertBuilder.setTitle(R.string.LeaveWinDialogTitle);
                // Setting Dialog Message
                alertBuilder.setMessage(R.string.LeaveWinDialog);

                alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(GameActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        dialog.dismiss();
                    }
                });

                alertBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()     {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing
                        dialog.dismiss();
                    }
                });

            }
        });
        if (!this.isFinishing())
        {
            dialog.show();
        }
    }

    private void markMovableFigures() {
        // i = 1 because of dice_result
        for(int i=1; i<model.getMovable_figures().size(); i++)
        {
            int figure_id = model.getMovable_figures().get(i);
            boardView.MarkPossiblePlayers(model, figure_id, myOnlyhandler);
            rollDice.setClickable(false);
        }
    }


    public void initResultDiceViews(int dice, ImageView myImageview) {

        switch (dice) {
            case 1:
                layers[1] = ContextCompat.getDrawable(getBaseContext(), R.drawable.d1);
                break;
            case 2:
                layers[1] = ContextCompat.getDrawable(getBaseContext(), R.drawable.d2);
                break;
            case 3:
                layers[1] = ContextCompat.getDrawable(getBaseContext(), R.drawable.d3);
                break;
            case 4:
                layers[1] = ContextCompat.getDrawable(getBaseContext(), R.drawable.d4);
                break;
            case 5:
                layers[1] = ContextCompat.getDrawable(getBaseContext(), R.drawable.d5);
                break;
            case 6:
                layers[1] = ContextCompat.getDrawable(getBaseContext(), R.drawable.d6);
                break;
            case 0:
                myImageview.setImageResource(0);
                break;
            default:
                break;
        }
        layersDrawable = new LayerDrawable(layers);

        layersDrawable.mutate();
        layersDrawable.setLayerInset(0, 0, 0, 0, 0);
        layersDrawable.setLayerInset(0, 0, 0, 0, 0);

        myImageview.setImageDrawable(layersDrawable);

    }

    public void flashDiceResult(ImageView myImageview)
    {
        //flash result
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        animation.setStartOffset(20);
        animation.setRepeatMode(Animation.REVERSE);
        myImageview.startAnimation(animation);
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putParcelable("BoardModel", model);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        //show warning
        // show alertDialog
      /*  AlertDialog.Builder alertBuilder = new AlertDialog.Builder(GameActivity.this);
        // Setting Dialog Title
        alertBuilder.setTitle(R.string.LeaveGameTitle);
        // Setting Dialog Message
        alertBuilder.setMessage(R.string.LeaveGame);

        alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                GameActivity.super.onBackPressed();
            }
        });

        alertBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()     {
            public void onClick(DialogInterface dialog, int id) {
                //do nothing
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show(); */
            super.onBackPressed();
            finish();
    }

    public void onPause()
    {
        if(dialog!=null && dialog.isShowing())
        {
            dialog.dismiss();
        }

        handler.removeCallbacks(doAIActions);
        //state will be saved in a file
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = openFileOutput("savedata", Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(model);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) try { oos.close(); } catch (IOException ignored) {}
            if (fos != null) try { fos.close(); } catch (IOException ignored) {}
        }
        super.onPause();
    }
}
