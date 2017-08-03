package org.secuso.privacyfriendlyludo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import org.secuso.privacyfriendlyludo.R;
import org.secuso.privacyfriendlyludo.logic.Player;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Integer.valueOf;

public class GameSettingActivity extends AppCompatActivity {

    private RecyclerView mPlayerList;
    RecyclerViewCollectionAdapter adapter;
    private ArrayList<Player> player = new ArrayList<Player>();
    List<Integer> mList = new ArrayList<>();
    int listposition;
    int color;
    boolean color_changed;
    private Set<Integer> generated = new LinkedHashSet<>();
    private int max_players;
    int gametyp;
    boolean useOwnDice;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            player = savedInstanceState.getParcelable("Players");
        }
        setContentView(R.layout.activity_game_setting);
        Intent intent = getIntent();
        Bundle mybundle = intent.getExtras();
        if (mybundle != null) {
            // save it for later
            useOwnDice = intent.getBooleanExtra("own_dice", false);
            if (mybundle.getInt("Color") != 0) {
                // there is a color_change
                color = mybundle.getInt("Color");
                listposition = mybundle.getInt("Position");
                color_changed = true;
                player = intent.getParcelableArrayListExtra("Players");
                // no players saved
            } else if (intent.getParcelableArrayListExtra("Players")==null) {
            }
            else
            {
                // when color changed and when setting loaded
                player = intent.getParcelableArrayListExtra("Players");
            }
        } else {
            player.add(new Player(1, getResources().getColor(R.color.white), "", false));
        }

        mPlayerList = (RecyclerView) findViewById(R.id.playerList);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mPlayerList.setHasFixedSize(true);
        adapter = new RecyclerViewCollectionAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mPlayerList.setLayoutManager(mLayoutManager);
        mPlayerList.setItemAnimator(new DefaultItemAnimator());
        mPlayerList.setAdapter(adapter);
        if (color_changed) {
           // TypedArray ta = getResources().obtainTypedArray(R.array.playerColors);
           // int colorToUse = ta.getResourceId(color - 1, R.color.white);
            int[] androidColors = getResources().getIntArray(R.array.playerColors);
            int colorToUse = androidColors[color-1];
            player.get(listposition).setColor(colorToUse);
        }
        // determine gametype
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        gametyp = mSharedPreferences.getInt("lastChosenPage", -1);

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

    class RecyclerViewCollectionAdapter extends RecyclerView.Adapter<ViewHolder> {

        public static final int PLAYER = 0;
        public static final int ADDPLAYER = 1;

        @Override
        public int getItemCount() {

            if (player == null)
            {
                return 1;
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
                    PlayerViewHolder playerViewHolder = (PlayerViewHolder)vh;
                    playerViewHolder.playerColor.setBackgroundColor(player.get(position).getColor());
                    playerViewHolder.playerName.setText(player.get(position).getName());
                    if (player.get(position).isAI()) {
                        playerViewHolder.playertype.setBackgroundResource(R.drawable.ic_computer);
                    } else {
                        playerViewHolder.playertype.setBackgroundResource(R.drawable.ic_person);
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
                            adapter.notifyItemRemoved(listPosition);
                        }
                    });
                    // on click playerColor
                    playerViewHolder.playerColor.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {
                            int listPosition = vh.getAdapterPosition();
                            showAlertDialog(listPosition);
                        }
                    });

                    //on click playertype
                    playerViewHolder.playertype.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {
                            int listPosition = vh.getAdapterPosition();
                            if (!useOwnDice)
                            {
                                player.get(listPosition).setAI(!player.get(listPosition).isAI());
                                adapter.notifyItemChanged(listPosition);
                                if (player.get(listPosition).isAI())
                                {
                                    view.setBackgroundResource( R.drawable.ic_computer);
                                }
                                else
                                {
                                    view.setBackgroundResource( R.drawable.ic_person );
                                }
                            }
                        }
                    });

                    // on change playername
                    playerViewHolder.playerName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }
                        @Override
                        public void afterTextChanged(Editable editable) {
                            int listPosition = vh.getAdapterPosition();
                            player.get(listPosition).setName(String.valueOf(editable));
                        }
                    });
                    break;
                case ADDPLAYER:
                    AddPlayerViewHolder addplayerViewHolder = (AddPlayerViewHolder) vh;
                    // onclick startgame
                    addplayerViewHolder.start_game.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {
                            if (min_player_reached() && checkColorUniqueness() && checkPlayerNames())  {

                                Intent intent = new Intent(GameSettingActivity.this, GameActivity.class);
                                intent.putExtra("own_dice", useOwnDice);
                                intent.putParcelableArrayListExtra("Players", player);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                // save recent settings
                                // save settings
                                FileOutputStream fos = null;
                                ObjectOutputStream oos = null;
                                String file_name = "";
                                switch (gametyp) {
                                    case 0:
                                        file_name = "save_settings_4players";
                                        break;
                                    case 1:
                                        file_name = "save_settings_6players";
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
                                startActivity(intent);
                            }
                        }
                    });
                    //onclick addPlayer
                    addplayerViewHolder.addPlayer.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {
                            if ((player!=null) && (player.size() == max_players)) {
                                Toast.makeText(GameSettingActivity.this, getString(R.string.max_player_reached), Toast.LENGTH_SHORT).show();
                            } else {
                                player.add(new Player(3, getResources().getColor(R.color.white), "", false));
                             //   mPlayerList.setAdapter(adapter);
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
            else
            {
                return (position < player.size()) ? PLAYER : ADDPLAYER;
            }
                  // +
        }
    }

    abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    };

    class AddPlayerViewHolder extends ViewHolder {

        ImageButton addPlayer;
        Button start_game;

        public AddPlayerViewHolder(View itemView) {
            super(itemView);
            addPlayer = (ImageButton) itemView.findViewById(R.id.button_add_player);
            start_game = (Button) itemView.findViewById(R.id.button_game_start);
        }
    }

    class PlayerViewHolder extends ViewHolder {

        ImageButton playerColor;
        ImageButton playertype;
        ImageButton delete_player;
        public EditText playerName;

        public PlayerViewHolder(final View itemView) {
            super(itemView);
            playertype = (ImageButton) itemView.findViewById(R.id.button_player_type);
            playerColor = (ImageButton) itemView.findViewById(R.id.button_player_color);
            playerName = (EditText) itemView.findViewById(R.id.textView_player_name);
            delete_player = (ImageButton) itemView.findViewById(R.id.button_delete_player);
        }
    }

    public boolean min_player_reached()
    {
        if (player.size()<2)
        {
            Toast.makeText(GameSettingActivity.this, getString(R.string.enough_players), Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean checkColorUniqueness() {
        boolean white_inUse = false;
        for (int i = 0; i < player.size(); i++) {
            generated.add(player.get(i).getColor());
            if (player.get(i).getColor() == getResources().getColor(R.color.white)) {
                white_inUse = true;
            }
        }
        if (generated.size() == player.size() && !white_inUse) {
            return true;
        } else {

            Toast.makeText(GameSettingActivity.this, getString(R.string.color_uniqueness), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean checkPlayerNames() {
        for (int i = 0; i < player.size(); i++) {

            if (player.get(i).getName().equals("")) {
                Toast.makeText(GameSettingActivity.this, getString(R.string.Missing_playername), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;

    }

    private void showAlertDialog(final int listPosition) {
        // Prepare grid view
        GridView gridView = new GridView(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final int[] androidColors = getResources().getIntArray(R.array.playerColors);
        int count_colors = androidColors.length;
      /*  TypedArray ta;
        int colorToUse;
        ta = getResources().obtainTypedArray(R.array.playerColors); */
      // clear list before filling with colors
      mList.clear();

        for (int i = 1; i < count_colors + 1; i++) {
            // colorToUse = ta.getResourceId(i, R.color.black);
            mList.add(i);
        }

        gridView.setAdapter(new ArrayAdapter<Integer>(this, R.layout.color_list, mList) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.color_list, null);
                }
                Integer item = mList.get(position);
                if (item != 0) {
                    Button button = (Button) v.findViewById(R.id.button_colors);
                    button.setTag(item);
                    // button.setText(String.valueOf(item));
                    TypedArray ta = getResources().obtainTypedArray(R.array.playerColors);
                    int colorToUse = androidColors[item-1];
                   // int backgroundColor = getResources().getColor(colorToUse);
                    button.setBackgroundColor(colorToUse);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // save information in local variable, before they will be deleted
                            int pos = listPosition;
                            ArrayList<Player> recent_player = player;
                            Intent intent = new Intent(getContext(), GameSettingActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("Color", (Integer) v.getTag());
                            intent.putExtra("Position", listPosition);
                            intent.putParcelableArrayListExtra("Players", recent_player);
                            getContext().startActivity(intent);

                        }
                    });
                }

                return v;
            }
        });
        gridView.setNumColumns(3);

        // Set grid view to alertDialog
        builder.setView(gridView);
        builder.setTitle(getString(R.string.choose_color));
        builder.show();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt("Position", listposition);
        savedInstanceState.putParcelableArrayList("Player", player);
        savedInstanceState.putBoolean("useOwnDice", useOwnDice);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

}


