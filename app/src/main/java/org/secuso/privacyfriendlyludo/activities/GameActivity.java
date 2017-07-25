package org.secuso.privacyfriendlyludo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import org.secuso.privacyfriendlyludo.R;
import org.secuso.privacyfriendlyludo.logic.BoardModel;
import org.secuso.privacyfriendlyludo.logic.Player;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.WriteAbortedException;
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
                // repair id if necessary
                for (int i=0; i<playerArrayList.size(); i++)
                {
                    playerArrayList.get(i).setId(i+1);
                }
                model = new BoardModel(this, playerArrayList);
                // save settings
                FileOutputStream fos = null;
                ObjectOutputStream oos = null;
                try {
                    fos = this.openFileOutput("saveSettings", Context.MODE_PRIVATE);
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(model.getPlayers());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if (oos != null) try { oos.close(); } catch (IOException ignored) {}
                    if (fos != null) try { fos.close(); } catch (IOException ignored) {}
                }

            }

        //String textzeile = intent.getStringExtra("PlayerNames");
        boardView = (BoardView) findViewById(R.id.board);
        //boardView = (GridLayout) findViewById(R.id.board);
        boardView.setColumnCount(11);
        boardView.setRowCount(11);
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
                        // show message for player change
                        rollDice.setClickable(true);

                    }
                    else
                    {
                        markMovableFigures();
                    }

                }
            });

        myOnlyhandler = new View.OnClickListener() {
            public void onClick(View v) {

                int old_figure_index = v.getId();
                //Log.i("tag", "old Pos: " + old_figure_index);
                int marked_figures;
                // i=1 because of dice_result
                for (int i = 1; i < model.getMovable_figures().size(); i++) {
                    marked_figures = model.getMovable_figures().get(i);
                    boardView.HidePossiblePlayers(model, marked_figures);
                }
                model.getMovable_figures().clear();
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
                if (player_changed) {
                    // show a message for player changed
                    String playername = model.getRecent_player().getName();
                    String playerMessageString = getString(R.string.player_name);
                    playerMessageString = playerMessageString.replace("%p", playername);
                    playermessage.setText(playerMessageString);
                    countRollDice = 0;
                }
                rollDice.setClickable(true);
                if (model.isGame_finished()) {
                    playermessage.setText(getString(R.string.finished_Game));

                }

                }
            };

        // check if movable figures --> for orientation change + saved game
      /*  if (model.getMovable_figures() != null && model.getMovable_figures().size()>1)
        {
            markMovableFigures();
        }
        */
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



        // app will be closed, state will be saved in a file
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

