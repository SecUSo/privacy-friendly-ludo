package org.secuso.privacyfriendlyludo.logic;

import android.graphics.Color;
import android.util.Log;

import org.secuso.privacyfriendlyludo.Map.GameFieldPosition;
import org.secuso.privacyfriendlyludo.Map.StartGameFieldPosition;
import org.secuso.privacyfriendlyludo.R;

import java.util.ArrayList;

/**
 * Created by Julchen on 28.05.2017.
 */

public class BoardModel {
    ArrayList all_player_observer = new ArrayList();
    boolean dice =true;

    public boolean isGame_finished() {
        return game_finished;
    }

    boolean game_finished = false;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    ArrayList<Player> players = new ArrayList<Player>();
    ArrayList<String> state = new ArrayList<String>();
    boolean possible_figure_moved = false;
    int dice_number;

    boolean inHouse = false;

    public Boolean getRollDiceAgain() {
        return rollDiceAgain;
    }

    public void setRollDiceAgain(Boolean rollDiceAgain) {
        this.rollDiceAgain = rollDiceAgain;
    }

    public Boolean getThreeTimesRollAllowed() {
        return threeTimesRollAllowed;
    }

    public void setThreeTimesRollAllowed(Boolean threeTimesRollAllowed) {
        this.threeTimesRollAllowed = threeTimesRollAllowed;
    }


    public void setStart_player_map(StartGameFieldPosition start_player_map) {
        this.start_player_map = start_player_map;
    }

    Boolean rollDiceAgain = true;
    Boolean threeTimesRollAllowed = false;

    public Player getRecent_player() {
        return recent_player;
    }

    // which player has the control
    Player recent_player = new Player();

    public void setOpponent_player(Player opponent) {
        this.opponent_player = opponent;
    }

    public Player getOpponent_player() {
        return opponent_player;
    }

    Player opponent_player = new Player();
    ArrayList<Figure> figure = new ArrayList<Figure>();

    public GameFieldPosition getMy_game_field() {
        return my_game_field;
    }

    public void setMy_game_field(GameFieldPosition my_game_field) {
        this.my_game_field = my_game_field;
    }

    private GameFieldPosition my_game_field;

    public StartGameFieldPosition getStart_player_map() {
        return start_player_map;
    }

    StartGameFieldPosition start_player_map;

    public BoardModel(){//ArrayList <Player> players) {
        players.add(new Player(1, R.color.red, "Max"));
        players.add(new Player(2, R.color.darkblue, "Mickey"));
        players.add(new Player(3, R.color.green, "Mini"));
        players.add(new Player(4, R.color.yellow, "Lisa"));
        recent_player = players.get(1);
        start_player_map = new StartGameFieldPosition(players);
        my_game_field = new GameFieldPosition(players);
        start_player_map.fill_with_players(this);
    }

    // get all Figures which can be moved according to the rules
    public ArrayList<Integer> checkMovableFigures(int player_id) {
        int new_position = 0;
        boolean movePossible = false;
        boolean moveOfFigureAllowed = false;
        ArrayList<Integer> figure_ids = new ArrayList<Integer>();

        //check each figure
        for (int i = 0; i < recent_player.getFigures().size(); i++) {
            int figure_id = recent_player.getFigures().get(i).getId();
            moveOfFigureAllowed = figureIsAllowedToMove(dice_number, figure_id);
            if (moveOfFigureAllowed == true) {
                new_position = getNewPosition(figure_id, dice_number);
                if (new_position == 0) {
                    movePossible = false;
                } else {
                    movePossible = isEmptyofSamePlayer(new_position);
                }
            }

            //move allowed and possible
            if (moveOfFigureAllowed == true && movePossible == true) {
                figure_ids.add(figure_id);
                // markfigures
            }

        }
        return figure_ids;

    }

    public int getNewPosition(int figure_id, int dice_result)
    {
        // figure is finished

        if (recent_player.getFigures().get(figure_id - 1).isFinished())
        {
            return 0;
        }
        else
        {
            String figureState = recent_player.getFigures().get(figure_id - 1).getState();
            inHouse = false;
            //check if figureState is allowed to move
            int recent_index = recent_player.getFigures().get(figure_id - 1).getField_position_index();
            int new_index = 0;
            switch (figureState) {
            case "start":
                switch (recent_player.getId()) {
                    case 1:
                        return 1;
                    case 2:
                        return 11;
                    case 3:
                        return 21;
                    case 4:
                        return 31;
                    default:
                        return 0;
                }
            case "inGame":
                // check count steps
                int count_steps = recent_player.getFigures().get(figure_id - 1).getCount_steps();
                // because field 40 has to be included
                if ((count_steps + dice_result) <= 40) {
                    new_index = (recent_index + dice_result) % 40;
                    if (new_index == 0)
                    {
                        new_index = 40;
                    }
                } else if (count_steps + dice_result > 44) {
                    // new position not possible because end of field
                    return 0;
                } else {
                    new_index = 40 + (((count_steps + dice_result) - 40) + (4 * (recent_player.getId()-1)));
                    inHouse = true;
                    return new_index;
                }

                return new_index;
            case "end":
                new_index = (recent_index + dice_result);
                inHouse = true;
                if (new_index < 44 + (recent_player.getId() - 1) * 4) {
                    return new_index;
                } else {
                    return 0;
                }
            default:
                return 0;
            }
        }
    }

    public boolean figureIsAllowedToMove(int dice_result, int figure_id) {
        String figureState = recent_player.getFigures().get(figure_id - 1).getState();
        //all FigureTypes can move
        if (dice_result == 6) {
            return true;
        }
        // no Figure is movable
        else if (dice_result != 6 && figureState != "start") {
            return true;
        } else {
            return false;
        }
    }

    //checks if already a figure of this player is there
    public boolean isEmptyofSamePlayer(int fieldindex) {

        if (my_game_field.getMyGamefield().get(fieldindex - 1).getPlayer_id() != recent_player.getId()) {
            return true;
            // other GameField necessary
        } else {
            return false;
        }

    }

    //checks if already a figure of this player is there
    public Boolean recent_player_on_field(int fieldindex) {
        if (my_game_field.getMyGamefield().get(fieldindex - 1).getPlayer_id() == 0) {
            return true;
            // other GameField necessary
        }
        else {
            return false;
        }

    }

    public void updatePlayer(int player_id, int figure_id, int dice_result, int new_position, boolean kickedOut) {
        opponent_player = players.get(player_id-1);
        Figure moved_figure = opponent_player.getFigures().get(figure_id - 1);
        int count_steps;
        if (kickedOut)
        {
          count_steps = 0;
        }
        // if figure comes out to the gamefield
        else if (moved_figure.getState() == "start")
        {
            count_steps = 1;
        }
        else
        {
            count_steps = (recent_player.getFigures().get(figure_id - 1).getCount_steps()) + dice_result;
        }
        moved_figure.setCount_steps(count_steps);
        moved_figure.setField_position_index(new_position);
        moved_figure.setState(moved_figure.getField_position_index());

    }

    public void updateBoard(int player_id, int figure_id, int old_position, int new_position) {
        GameField field_position_old;
        GameField field_position_new;
        opponent_player = players.get(player_id-1);
        if (old_position >= 100) {
            field_position_old = getStart_player_map().getMyGamefield().get(old_position %100);
        } else {
            field_position_old = getMy_game_field().getMyGamefield().get(old_position - 1);
        }
        field_position_old.setFigure_id(0);
        field_position_old.setPlayer_id(0);
        if (new_position >= 100) {
            field_position_new = getStart_player_map().getMyGamefield().get(new_position %100);
        } else {
            field_position_new = getMy_game_field().getMyGamefield().get(new_position - 1);
        }
        field_position_new.setPlayer_id(opponent_player.getId());
        field_position_new.setFigure_id(figure_id);
    }

    public ArrayList<Integer> getMovable_figures() {
        return movable_figures;
    }

    public void setMovable_figures(ArrayList<Integer> movable_figures) {
        this.movable_figures = movable_figures;
    }

    ArrayList<Integer> movable_figures;

    public ArrayList<Integer> processDiceResult() {

        movable_figures = new ArrayList<Integer>();
        // check if Dice Roll is allowed
        //roll Dice
        Dicer dicer = new Dicer();
        dice = !dice;
        dice_number = dicer.rollDice(dice);
        //ask model what to do next --> for game rules
        // return all movable figures
        movable_figures = checkMovableFigures(recent_player.getId());
        movable_figures.add(0, dice_number);

        return movable_figures;
    }

    public boolean playerChanged(int count_Calls) {

        boolean notfinished = false;
        for (int i=0; i<recent_player.getFigures().size(); i++)
        {
            String state = recent_player.getFigures().get(i).getState();
            boolean isfinished = recent_player.getFigures().get(i).isFinished();
            if (state == "end" && !isfinished)
            {
                notfinished = true;
            }
            else
            {
            }

        }
        // no movable figures, 3 times roll dice done, move is finished
        if (dice_number != 6 && count_Calls>=3 && movable_figures.size()==1)
        {
            recent_player = players.get((recent_player.getId()) % 4);
            return true;
        }
        // figure was moved, move is finished or not finished but in house
        else if ((dice_number != 6 && movable_figures.size()>1))
        {
            recent_player = players.get((recent_player.getId()) % 4);
            return true;
        }
        else if (dice_number != 6 && notfinished && count_Calls >= 1)
        {
            recent_player = players.get((recent_player.getId()) % 4);
            return true;
        }
        else //same player is again
        {
            return false;
        }
    }

    public int moveFigure(int figure_index, boolean kicked_out)
    {
        int figure_id;
        int player_id;
        if (figure_index >=100)
        {
            // figure is on startfields
            figure_id = start_player_map.getMyGamefield().get(figure_index % 100).getFigure_id();
            player_id = start_player_map.getMyGamefield().get(figure_index % 100).getPlayer_id();
        }
        else
        {
            figure_id = my_game_field.getMyGamefield().get(figure_index-1).getFigure_id();
            player_id = my_game_field.getMyGamefield().get(figure_index-1).getPlayer_id();
        }
        int new_position;
        if (kicked_out)
        {
            //setFigureBackToStart
            new_position = start_player_map.getMyGamefield().get((figure_id-1) + 4*(player_id-1)).getIndex();
        }
        else
        {
            new_position = getNewPosition(figure_id ,dice_number);
        }
        updatePlayer(player_id, figure_id, dice_number, new_position, kicked_out);
        updateBoard(player_id, figure_id,figure_index, new_position);
        updateFinishFlag(player_id, figure_id,new_position);

        return new_position;
    }

    private void updateFinishFlag(int player_id, int figure_id, int new_position)
    {
        if (players.get(player_id-1).getFigures().get(figure_id-1).getCount_steps() > 40)
        {
            int max_possible_fieldindex = 44 + 4* (player_id-1);
            int count_not_empty_fields = 0;
            int count_fields = max_possible_fieldindex - new_position;
            for (int i=1; i<(count_fields+1);i++)
            {
                if (isEmptyofSamePlayer(new_position+i) == true)
                {
                }
                else
                {
                    count_not_empty_fields = count_not_empty_fields + 1;
                }
            }
            // no fields are empty, figure is finished
            if (count_not_empty_fields == 0)
            {
                players.get(player_id-1).getFigures().get(figure_id-1).setFinished(true);
                int count_figures_finished = 0;
                for (int i=0; i<players.size(); i++)
                {
                    for(int j=0; j<players.get(player_id-1).getFigures().size(); j++)
                    {
                        if (players.get(i).getFigures().get(j).isFinished())
                        {
                            count_figures_finished = count_figures_finished + 1 ;
                        }
                    }
                }

                if (count_figures_finished == 16)
                {
                    // all figures are in the house
                    game_finished = true;
                }

            }
        }
        else
        {

        }
    }


}
