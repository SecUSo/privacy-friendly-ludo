package org.secuso.privacyfriendlyludo.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;

import org.secuso.privacyfriendlyludo.Map.GameFieldPosition;
import org.secuso.privacyfriendlyludo.Map.StartGameFieldPosition;
import org.secuso.privacyfriendlyludo.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Created by Julchen on 28.05.2017.
 */

public class BoardModel implements Parcelable, Serializable {

    public static final Parcelable.Creator<BoardModel> CREATOR = new Parcelable.Creator<BoardModel>() {
        @Override
        public BoardModel createFromParcel(Parcel in) {
            return new BoardModel(in);
        }

        @Override
        public BoardModel[] newArray(int size) {
            return new BoardModel[size];
        }
    };
    private boolean dice = true;
    private boolean game_finished = false;
    private ArrayList<Player> players = new ArrayList<>();
    private int dice_number;
    // which player has the control
    private Player recent_player = new Player();
    private Player opponent_player = new Player();
    private GameFieldPosition my_game_field;
    private StartGameFieldPosition start_player_map;
    private transient Set<Integer> generated = new LinkedHashSet<>();
    private ArrayList<Integer> colors = new ArrayList<>();
    private transient Context context;
    private ArrayList<Integer> movable_figures;
    private int distance_between_2_colors;
    private int max_players;
    private GameType game_type;
    private int last_field_index;
    public int countRollDice = 0;
    public boolean useOwnDice;
    private transient SharedPreferences prefs;
    public boolean switch_dice_3times;

    public BoardModel(Context context, ArrayList<Player> settingplayers, GameType type, Boolean useOwnDice) {
        this.context = context;
        this.game_type = type;
        this.useOwnDice = useOwnDice;

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.switch_dice_3times = prefs.getBoolean("switch_dice_3times", false);
        // define variables depending on gametype
        switch (game_type) {
            case Four_players:
                distance_between_2_colors = 10;
                last_field_index = 40;
                max_players = 4;
                break;
            case Six_players:
                distance_between_2_colors = 12;
                last_field_index = 72;
                max_players = 6;
                break;
        }
        for (int i = 0; i < settingplayers.size(); i++) {
            this.players.add(new Player(i + 1, settingplayers.get(i).getColor(), settingplayers.get(i).getName(), settingplayers.get(i).isAI));
        }
        recent_player = players.get(0);
        setColors();
        start_player_map = new StartGameFieldPosition(colors, game_type);
        my_game_field = new GameFieldPosition(colors, game_type);
        start_player_map.fill_with_players(this);

    }

    private BoardModel(Parcel in) {
        dice = in.readByte() != 0x00;
        game_finished = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            players = new ArrayList<>();
            in.readList(players, Player.class.getClassLoader());
        } else {
            players = null;
        }
        dice_number = in.readInt();
        recent_player = (Player) in.readValue(Player.class.getClassLoader());
        opponent_player = (Player) in.readValue(Player.class.getClassLoader());
        my_game_field = (GameFieldPosition) in.readValue(GameFieldPosition.class.getClassLoader());
        start_player_map = (StartGameFieldPosition) in.readValue(StartGameFieldPosition.class.getClassLoader());
        if (in.readByte() == 0x01) {
            colors = new ArrayList<>();
            in.readList(colors, Integer.class.getClassLoader());
        } else {
            colors = null;
        }
        if (in.readByte() == 0x01) {
            movable_figures = new ArrayList<>();
            in.readList(movable_figures, Integer.class.getClassLoader());
        } else {
            movable_figures = null;
        }
        distance_between_2_colors = in.readInt();
        game_type = (GameType) in.readValue(GameType.class.getClassLoader());
        last_field_index = in.readInt();
        max_players = in.readInt();
        countRollDice = in.readInt();
        useOwnDice = in.readByte() != 0x00;
        switch_dice_3times = in.readByte() != 0x00;
    }

    public GameType getGame_type() {
        return game_type;
    }

    public void setGame_type(GameType game_type) {
        this.game_type = game_type;
    }

    public int getMax_players() {
        return max_players;
    }

    public int getLast_field_index() {
        return last_field_index;
    }

    public boolean isGame_finished() {
        return game_finished;
    }

    public void setGame_finished(boolean game_finished) {
        this.game_finished = game_finished;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getRecent_player() {
        return recent_player;
    }

    public void setOpponent_player(Player opponent) {
        this.opponent_player = opponent;
    }

    public GameFieldPosition getMy_game_field() {
        return my_game_field;
    }

    public StartGameFieldPosition getStart_player_map() {
        return start_player_map;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        if (context != null) {
            this.context = context;
        }
    }

    private void setColors() {
        for (int i = 0; i < players.size(); i++) {
            generated.add(players.get(i).getColor());
        }
        // add colors if not all figures are played
        TypedArray ta = context.getResources().obtainTypedArray(R.array.playerColors);
        int[] androidColors = context.getResources().getIntArray(R.array.playerColors);
        while (generated.size() < max_players) {
            int random_id = new Random().nextInt(androidColors.length);
            // As we're adding to a set, this will automatically do a containment check
            int colorToUse = ta.getResourceId(random_id, R.color.black);
            generated.add(colorToUse);
        }
        colors.addAll(0, generated);
    }

    public Player getOpponent_player() {
        return opponent_player;
    }

    // get all Figures which can be moved according to the rules
    private ArrayList<Integer> checkMovableFigures() {
        int new_position;
        boolean movePossible = false;
        boolean moveOfFigureAllowed;
        ArrayList<Integer> figure_ids = new ArrayList<>();

        // make first field free if still players are in the house
        boolean freeHouse;

        boolean freeFirstField;
        freeFirstField = checkFirstFieldFree();
        freeHouse = checkHouseisFree();

        if (!freeHouse && !freeFirstField) {
            // move figure on free first field
            int position = 1 + (recent_player.getId() - 1) * 10;
            int figure_id = getMy_game_field().getMyGamefield().get(position - 1).getFigure_id();
            new_position = getNewPosition(figure_id, dice_number);

            if (isEmptyofSamePlayer(new_position, recent_player.getId())) {
                figure_ids.add(figure_id);
            } else {
                figure_id = getMy_game_field().getMyGamefield().get(new_position - 1).getFigure_id();
                figure_ids.add(figure_id);
            }

        } else {
            //check each figure
            for (int i = 0; i < recent_player.getFigures().size(); i++) {
                int figure_id = recent_player.getFigures().get(i).getId();

                moveOfFigureAllowed = figureIsAllowedToMove(dice_number, figure_id, freeHouse);
                if (moveOfFigureAllowed) {
                    new_position = getNewPosition(figure_id, dice_number);
                    movePossible = new_position != 0 && isEmptyofSamePlayer(new_position, recent_player.getId());
                }

                //move allowed and possible
                if (moveOfFigureAllowed && movePossible) {
                    figure_ids.add(figure_id);
                    // markfigures
                }

            }
        }

        return figure_ids;

    }

    private boolean checkFirstFieldFree() {
        boolean freeField;
        int position = 1 + (recent_player.getId() - 1) * 10;
        freeField = isEmptyofSamePlayer(position, recent_player.getId());
        return freeField;
    }

    private boolean checkHouseisFree() {
        boolean freeHouse = true;
        for (int i = 0; i < recent_player.getFigures().size(); i++) {
            if (Objects.equals(recent_player.getFigures().get(i).getState(), "start")) {
                freeHouse = false;
            }
        }
        return freeHouse;
    }

    public int getNewPosition(int figure_id, int dice_result) {
        // figure is finished

        if (recent_player.getFigures().get(figure_id - 1).isFinished()) {
            return 0;
        }
        else {
            String figureState = recent_player.getFigures().get(figure_id - 1).getState();
            //check if figureState is allowed to move
            int recent_index = recent_player.getFigures().get(figure_id - 1).getField_position_index();
            int new_index;
            switch (figureState) {
                case "start":
                    return 1 + ((recent_player.getId() - 1) * distance_between_2_colors);
                case "inGame":
                    // check count steps
                    int count_steps = recent_player.getFigures().get(figure_id - 1).getCount_steps();
                    // because last_field_index has to be included
                    if ((count_steps + dice_result) <= last_field_index) {
                        new_index = (recent_index + dice_result) % last_field_index;
                        if (new_index == 0) {
                            new_index = last_field_index;
                        }
                    } else if (count_steps + dice_result > (last_field_index + 4)) {
                        // new position not possible because end of field
                        return 0;
                    } else {
                        new_index = last_field_index + (((count_steps + dice_result) - last_field_index) +
                                (4 * (recent_player.getId() - 1)));
                        return new_index;
                    }

                    return new_index;
                case "end":
                    new_index = (recent_index + dice_result);
                    if (new_index <= (last_field_index + 4) + (recent_player.getId() - 1) * 4) {
                        return new_index;
                    } else {
                        return 0;
                    }
                default:
                    return 0;
            }
        }

    }

    private boolean figureIsAllowedToMove(int dice_result, int figure_id, boolean freeHouse) {
        String figureState = recent_player.getFigures().get(figure_id - 1).getState();
        return dice_result == 6 && freeHouse || dice_result == 6 && Objects.equals(figureState, "start") || (dice_result != 6 && !Objects.equals(figureState, "start"));
    }

    //checks if already a figure of this player is there
    public boolean isEmptyofSamePlayer(int fieldindex, int player_id) {

        int player_id_on_field=0;

        if (fieldindex>=100)
        {
            player_id_on_field = start_player_map.getMyGamefield().get(fieldindex%100).getPlayer_id();
        }
        else
        {
            player_id_on_field = my_game_field.getMyGamefield().get(fieldindex - 1).getPlayer_id();
        }

        return player_id_on_field == 0 || player_id_on_field != player_id;

    }

    //checks if already a figure is there
    public Boolean no_player_on_field(int fieldindex) {
        if (fieldindex>=100)
        {
            return (start_player_map.getMyGamefield().get(fieldindex%100).getPlayer_id() == 0);
        }
        else
        {
            return (my_game_field.getMyGamefield().get(fieldindex - 1).getPlayer_id() == 0);
        }

    }

    public void updatePlayer(int player_id, int figure_id, int dice_result, int new_position, boolean kickedOut) {
        int count_steps;
        Figure moved_figure = players.get(player_id-1).getFigures().get(figure_id-1);
        if (dice_result!=0)
        {
            // no free mode
            if (kickedOut) {
                count_steps = 0;
            }
            // if figure comes out to the gamefield
            else if (Objects.equals(moved_figure.getState(), "start")) {
                count_steps = 1;
            } else {
                count_steps = (recent_player.getFigures().get(figure_id - 1).getCount_steps()) + dice_result;
            }
            moved_figure.setCount_steps(count_steps);
        }

        moved_figure.setField_position_index(new_position);
        moved_figure.setState(moved_figure.getField_position_index(), this);

    }

    public void updateBoard(int player_id, int figure_id, int old_position, int new_position) {
        GameField field_position_old;
        GameField field_position_new;
        if (old_position >= 100) {
            field_position_old = getStart_player_map().getMyGamefield().get(old_position % 100);
        } else {
            field_position_old = getMy_game_field().getMyGamefield().get(old_position - 1);
        }
        field_position_old.setFigure_id(0);
        field_position_old.setPlayer_id(0);
        if (new_position >= 100) {
            field_position_new = getStart_player_map().getMyGamefield().get(new_position % 100);
        } else {
            field_position_new = getMy_game_field().getMyGamefield().get(new_position - 1);
        }
        field_position_new.setPlayer_id(player_id);
        field_position_new.setFigure_id(figure_id);
    }

    public ArrayList<Integer> getMovable_figures() {
        return movable_figures;
    }

    public ArrayList<Integer> processDiceResult() {

        movable_figures = new ArrayList<>();
        // check if Dice Roll is allowed
        //roll Dice
        Dicer dicer = new Dicer();
        dice = !dice;
        dice_number = dicer.rollDice(dice);
        //ask model what to do next --> for game rules
        // return all movable figures
        movable_figures = checkMovableFigures();
        movable_figures.add(0, dice_number);

        return movable_figures;
    }

    public boolean playerChanged(int count_Calls) {
        Log.i("count", "Anz: würfe: " + count_Calls);
        // check if special case, playerchanged is called again
        if (count_Calls == -1) {
            recent_player = players.get((recent_player.getId()) % players.size());
            return true;
        } else {
            // no movable figures, 3 times roll dice not done but allowed acccording to settings, move is not finished
            if (dice_number != 6 && (count_Calls < 3 && switch_dice_3times) && movable_figures.size() == 1) {
                // update information on recent player
                recent_player = players.get(recent_player.getId() - 1);
                return false;
            }
            // no movable figures, 3 times roll dice done or not allowed according to settings, move is finished
            if (dice_number != 6 && (count_Calls >= 3 || switch_dice_3times) && movable_figures.size() == 1) {
                recent_player = players.get((recent_player.getId()) % players.size());
                return true;
            }
            // figure was moved, move is finished or not finished but in house
            else if ((dice_number != 6 && count_Calls >= 1)) {
                recent_player = players.get((recent_player.getId()) % players.size());
                return true;
            } else //same player is again because 6 was dice result
            {
                // update information on recent player
                recent_player = players.get(recent_player.getId() - 1);
                return false;
            }
        }

    }

    public int moveFigure(int figure_index, boolean kicked_out) {
        int figure_id;
        int player_id;
        if (figure_index >= 100) {
            // figure is on startfields
            figure_id = start_player_map.getMyGamefield().get(figure_index % 100).getFigure_id();
            player_id = start_player_map.getMyGamefield().get(figure_index % 100).getPlayer_id();
        } else {
            figure_id = my_game_field.getMyGamefield().get(figure_index - 1).getFigure_id();
            player_id = my_game_field.getMyGamefield().get(figure_index - 1).getPlayer_id();
        }
        int new_position;
        if (kicked_out) {
            //setFigureBackToStart
            new_position = start_player_map.getMyGamefield().get((figure_id - 1) + 4 * (player_id - 1)).getIndex();
        } else {
            new_position = getNewPosition(figure_id, dice_number);
        }
        updatePlayer(player_id, figure_id, dice_number, new_position, kicked_out);
        updateBoard(player_id, figure_id, figure_index, new_position);
        if (recent_player.getFigures().get(figure_id - 1).getCount_steps() > last_field_index) {
            updateFinishFlag(player_id);
        }

        return new_position;
    }

    private void updateFinishFlag(int player_id) {
        // figure is in Goal fields
        // check finish state of all other figures --> because jumping is possible
        int max_possible_fieldindex = (last_field_index + 4) + (4 * (player_id - 1));
        int count_empty_fields = 0;
        for (int count_figures = 0; count_figures < 4; count_figures++) {
            int figure_pos = players.get(player_id - 1).getFigures().get(count_figures).getField_position_index();
            int count_steps = players.get(player_id - 1).getFigures().get(count_figures).getCount_steps();
            int count_fields = max_possible_fieldindex - figure_pos;
            // if figure is already finished
            if (!players.get(player_id - 1).getFigures().get(count_figures).isFinished() && figure_pos<100 && count_steps > last_field_index) {
                // check field above figure_pos
                for (int i = 1; i < (count_fields + 1); i++) {
                    if (isEmptyofSamePlayer(figure_pos + i, recent_player.getId())) {
                        count_empty_fields = count_empty_fields + 1;
                    }
                }
                // no fields are empty, figure is finished
                if (count_empty_fields == 0) {
                    players.get(player_id - 1).getFigures().get(count_figures).setFinished(true);
                }
            }
        }

        // check if complete player is finished
        int count_figures_finished = 0;
        for (int j = 0; j < players.get(player_id - 1).getFigures().size(); j++) {
            if (players.get(player_id - 1).getFigures().get(j).isFinished()) {
                count_figures_finished = count_figures_finished + 1;
            }
        }
        if (count_figures_finished == 4) {
            // all figures from one player are in the house
            players.get(player_id - 1).setFinished(true);
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (dice ? 0x01 : 0x00));
        dest.writeByte((byte) (game_finished ? 0x01 : 0x00));
        if (players == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(players);
        }
        dest.writeInt(dice_number);
        dest.writeValue(recent_player);
        dest.writeValue(opponent_player);
        dest.writeValue(my_game_field);
        dest.writeValue(start_player_map);
        if (colors == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(colors);
        }
        if (movable_figures == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(movable_figures);
        }
        dest.writeInt(distance_between_2_colors);
        dest.writeValue(game_type);
        dest.writeInt(last_field_index);
        dest.writeInt(max_players);
        dest.writeInt(countRollDice);
        dest.writeByte((byte) (useOwnDice ? 0x01 : 0x00));
        dest.writeByte((byte) (switch_dice_3times ? 0x01 : 0x00));
    }
}

