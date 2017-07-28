package org.secuso.privacyfriendlyludo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
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
import java.io.WriteAbortedException;
import java.security.SecureRandom;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private ImageView rollDice;
    private TextView playermessage;
    SharedPreferences sharedPreferences;
    private int countRollDice;
    Bundle mybundle;

    private BoardView boardView;
    BoardModel model;
    ArrayList<Integer> movable_figures;
    int dice_number;
    boolean player_changed;
    View.OnClickListener myOnlyhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        mybundle = intent.getExtras();
          if (mybundle != null) {
              // there is a resumeable game
              model = mybundle.getParcelable("BoardModel");
              if (model == null)
              {
                  // control comes from gameSetting
                  mybundle = null;
              }
              else
              {
                  // control comes from main activity
                  savedInstanceState = mybundle;
                  mybundle = null;
              }

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
                model = new BoardModel(this, playerArrayList, game_typ);
            }

        //String textzeile = intent.getStringExtra("PlayerNames");
        boardView = (BoardView) findViewById(R.id.board);
        //boardView = (GridLayout) findViewById(R.id.board);
        boardView.createBoard(model);
        boardView.getBoard();

        //Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //Button
        Display display = getWindowManager().getDefaultDisplay();
            String playername = model.getRecent_player().getName();
            String playerMessageString = getString(R.string.player_name);
            playerMessageString = playerMessageString.replace("%p", playername);
            playermessage = (TextView) findViewById(R.id.changePlayerMessage);
            playermessage.setTextColor(getResources().getColor(R.color.black));
            playermessage.setText(playerMessageString);

        rollDice = (ImageView) findViewById(R.id.resultOne);
            rollDice.setImageResource(0);
            android.view.ViewGroup.LayoutParams layoutParams = rollDice.getLayoutParams();
            layoutParams.width = display.getWidth() / 6;
            layoutParams.height = display.getWidth() / 6;
            rollDice.setLayoutParams(layoutParams);

            rollDice.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    rollDice();
                }
            });

        myOnlyhandler = new View.OnClickListener() {
            public void onClick(View v) {

                int old_figure_index = v.getId();
                setFigures(old_figure_index);
                }
            };

        // roll dice automatically if it is an AI
        if (model.getRecent_player().isAI())
        {
            new CountDownTimer(1000, 1000) {

                @Override
                public void onTick(long l) {

                }

                public void onFinish() {
                    // rollDice.setClickable(true);
                    rollDice.performClick();
                }
            }.start();
        }
    }

    private void setFigures(int old_figure_index)
    {
        //Log.i("tag", "old Pos: " + old_figure_index);
        int marked_figures;
        // i=1 because of dice_result
        for (int i = 1; i < model.getMovable_figures().size(); i++) {
            marked_figures = model.getMovable_figures().get(i);
            boardView.HidePossiblePlayers(model, marked_figures);
        }
        // check if allready another figure is on the new calculated field
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

        int old_opponent_index = model.getNewPosition(figure_id, dice_number);
        isEmpty = model.recent_player_on_field(old_opponent_index);
        if (!isEmpty) {
            model.setOpponent_player(model.getPlayers().get(player_id - 1));
            new_opponent_index = model.moveFigure(old_opponent_index, true);
            boardView.setFigureToNewPosition(model, old_opponent_index, new_opponent_index, true);
        }

        int new_figure_index = model.moveFigure(old_figure_index, false);

        //Log.i("tag", "new Pos: " + new_figure_index);
        // set Figure to new Position
        boardView.setFigureToNewPosition(model, old_figure_index, new_figure_index, false);
        player_changed = model.playerChanged(countRollDice);
        // show details about recent player
        if (player_changed) {
            // show a message for player changed
            String playername = model.getRecent_player().getName();
            String playerMessageString = getString(R.string.player_name);
            playerMessageString = playerMessageString.replace("%p", playername);
            playermessage.setText(playerMessageString);
            countRollDice = 0;
        }
        if (model.isGame_finished()) {
            // Game is finished
            playermessage.setText(getString(R.string.finished_Game));
            rollDice.setClickable(false);
            rollDice.setImageAlpha(255);
        }
        else
        { // automatically roll dice if player is AI
            if (model.getRecent_player().isAI())
            {
                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long l) {

                    }

                    public void onFinish() {
                        rollDice.performClick();
                    }
                }.start();
            }
            else
            {
                rollDice.setClickable(true);
            }
        }
        // clear old movable figures list
        model.getMovable_figures().clear();

    }

    private void rollDice()
    {
        countRollDice = countRollDice + 1;
        movable_figures = model.processDiceResult();
        dice_number = movable_figures.get(0);
        initResultDiceViews(dice_number, rollDice);
        flashDiceResult(rollDice);


        if(movable_figures.size()==1)
        {
            // no movable Figures
            boolean player_changed = model.playerChanged(countRollDice);
            if (player_changed)
            {
                // show a message for player changed
                String playername = model.getRecent_player().getName();
                String playerMessageString = getString(R.string.player_name);
                playerMessageString = playerMessageString.replace("%p", playername);
                playermessage.setText(playerMessageString);
                countRollDice = 0;
            }
            // check if it is a AI
            if (model.getRecent_player().isAI())
            {
                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long l) {

                    }

                    public void onFinish() {
                       // rollDice.setClickable(true);
                        rollDice.performClick();
                    }
                }.start();
            }
            else
            {
                rollDice.setClickable(true);
            }

        }
        else
        {
            markMovableFigures();
            // for AI randomly chose figure
            if (model.getRecent_player().isAI()) {
                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long l) {

                    }

                    public void onFinish() {
                        int id;
                        SecureRandom random = new SecureRandom();
                        // -2 because of zero and dice_number
                        int max_figures = movable_figures.size()-1;
                        int random_num = random.nextInt(max_figures) + 1;
                        // integer between  zero and max_figures
                        int figure_id = movable_figures.get(random_num);
                        int position_id = model.getRecent_player().getFigures().get(figure_id-1).getField_position_index();

                        Log.i("Nr.", "Max Figures: " + max_figures + "; Figure_id: " + figure_id + "\n");

                        setFigures(position_id);
                    }
                }.start();
            }
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
                myImageview.setImageResource(R.drawable.d1);
                break;
            case 2:
                myImageview.setImageResource(R.drawable.d2);
                break;
            case 3:
                myImageview.setImageResource(R.drawable.d3);
                break;
            case 4:
                myImageview.setImageResource(R.drawable.d4);
                break;
            case 5:
                myImageview.setImageResource(R.drawable.d5);
                break;
            case 6:
                myImageview.setImageResource(R.drawable.d6);
                break;
            case 0:
                myImageview.setImageResource(0);
                break;
            default:
                break;
        }


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
    protected void onStop() {
        super.onStop();

        //state will be saved in a file
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = this.openFileOutput("savedata", Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(model);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) try { oos.close(); } catch (IOException ignored) {}
            if (fos != null) try { fos.close(); } catch (IOException ignored) {}
        }

    }
}

