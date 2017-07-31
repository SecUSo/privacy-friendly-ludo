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
    private ArrayList<Player> player = new ArrayList<>();
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
            } else {
                // when color changed and when setting loaded
                player = intent.getParcelableArrayListExtra("Players");
            }
        } else {
            player.add(new Player(1, R.color.white, getString(R.string.initial_name_field), false));
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
            TypedArray ta = getResources().obtainTypedArray(R.array.playerColors);
            int colorToUse = ta.getResourceId(color - 1, R.color.white);
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

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            View view = v;
            int id = v.getId();
            View parent = (View) v.getParent();
            while (!(parent instanceof RecyclerView)) {
                view = parent;
                parent = (View) parent.getParent();
            }
            listposition = mPlayerList.getChildAdapterPosition(view);

            switch (id) {
                //playertype is clicked
                case 0:
                    if (!useOwnDice)
                    {
                        player.get(listposition).setAI(!player.get(listposition).isAI());
                        adapter.notifyItemChanged(listposition);
                        if (player.get(listposition).isAI())
                        {
                            //v.setBackgroundColor(0xFF00FF00 );
                            v.setBackgroundResource( R.drawable.ic_computer);
                        }
                        else
                        {
                            //v.setBackgroundColor(0x0000AA00 );
                            v.setBackgroundResource( R.drawable.ic_person );
                        }
                    }

                    break;
                //playerColor is clicked
                case 1:
                    showAlertDialog();
                    break;
                case 2:
                    //playerName is clicked
                    // player.get(listposition).setName("Alfred");
                    // adapter.notifyItemChanged(listposition);
                    break;
                case 3:
                    //delete Player is clicked
                    player.remove(listposition);
                    adapter.notifyItemRemoved(listposition);
                    break;
                default:
                    break;
            }
        }
    };

    class RecyclerViewCollectionHolder extends RecyclerView.ViewHolder {

        ImageButton playerColor;
        ImageButton playertype;
        ImageButton delete_player;
        public EditText playerName;

        public RecyclerViewCollectionHolder(final View itemView) {
            super(itemView);
            playertype = (ImageButton) itemView.findViewById(R.id.button_player_type);
            playertype.setId(valueOf(0));
            playertype.setOnClickListener(mOnClickListener);
            playerColor = (ImageButton) itemView.findViewById(R.id.button_player_color);
            playerColor.setId(valueOf(1));
            playerColor.setOnClickListener(mOnClickListener);
            playerName = (EditText) itemView.findViewById(R.id.textView_player_name);
            playerName.setId(valueOf(2));
            playerName.setOnClickListener(mOnClickListener);
            delete_player = (ImageButton) itemView.findViewById(R.id.button_delete_player);
            delete_player.setId(valueOf(3));
            delete_player.setOnClickListener(mOnClickListener);
            playerName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    View view = itemView;
                    View parent = (View) view.getParent();
                    if (parent != null)  {
                        while (!(parent instanceof RecyclerView)) {
                            view = parent;
                            if (parent.getParent() != null) {
                                parent = (View) parent.getParent();
                            }
                        }
                        listposition = mPlayerList.getChildAdapterPosition(view);
                        player.get(listposition).setName(String.valueOf(editable));
                    }
                }
            });
        }
    }

    class RecyclerViewCollectionAdapter extends RecyclerView.Adapter<RecyclerViewCollectionHolder> {

        @Override
        public int getItemCount() {
            return player.size();
        }

        @Override
        public RecyclerViewCollectionHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.player_item, viewGroup, false);
            return new RecyclerViewCollectionHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewCollectionHolder recyclerViewCollectionHolder, int i) {
            recyclerViewCollectionHolder.playerColor.setBackgroundColor(ContextCompat.getColor(getBaseContext(),player.get(i).getColor()));
            recyclerViewCollectionHolder.playerName.setText(player.get(i).getName());
            if (player.get(i).isAI())
            {
                recyclerViewCollectionHolder.playertype.setBackgroundResource(R.drawable.ic_computer);
            }
            else
            {
                recyclerViewCollectionHolder.playertype.setBackgroundResource(R.drawable.ic_person);
            }
        }
    }

    public boolean checkColorUniqueness() {
        boolean white_inUse = false;
        for (int i = 0; i < player.size(); i++) {
            generated.add(player.get(i).getColor());
            if (player.get(i).getColor() == R.color.white) {
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

            if (player.get(i).getName().equals(getString(R.string.initial_name_field))) {
                Toast.makeText(GameSettingActivity.this, getString(R.string.Missing_playername), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_game:
                if (checkColorUniqueness() && checkPlayerNames()) {

                    Intent intent = new Intent(GameSettingActivity.this, GameActivity.class);
                    intent.putExtra("own_dice", useOwnDice);
                    intent.putParcelableArrayListExtra("Players", player);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // save recent settings
                    // save settings
                    FileOutputStream fos = null;
                    ObjectOutputStream oos = null;
                    String file_name="";
                    switch (gametyp) {
                        case 0:
                            file_name = "save_settings_4players";
                            break;
                        case 1:
                            file_name = "save_settings_6players";
                            break;
                    }
                    try {
                        fos = this.openFileOutput(file_name, Context.MODE_PRIVATE);
                        oos = new ObjectOutputStream(fos);
                        oos.writeObject(player);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        if (oos != null) try { oos.close(); } catch (IOException ignored) {}
                        if (fos != null) try { fos.close(); } catch (IOException ignored) {}
                    }
                    startActivity(intent);
                }
                break;
            case R.id.button_add_player:
                if (player.size() == max_players) {
                    Toast.makeText(GameSettingActivity.this, getString(R.string.max_player_reached), Toast.LENGTH_SHORT).show();
                } else {
                    player.add(new Player(3, R.color.white, getString(R.string.initial_name_field), false));
                    mPlayerList.setAdapter(adapter);
                }
                break;
            default:
                break;
        }
    }

    private void showAlertDialog() {
        // Prepare grid view
        GridView gridView = new GridView(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        int[] androidColors = getResources().getIntArray(R.array.playerColors);
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

        gridView.setAdapter(new ArrayAdapter<Integer>(this, R.layout.test, mList) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.test, null);
                }
                Integer item = mList.get(position);
                if (item != 0) {
                    Button button = (Button) v.findViewById(R.id.button_colors);
                    button.setTag(item);
                    // button.setText(String.valueOf(item));
                    TypedArray ta = getResources().obtainTypedArray(R.array.playerColors);
                    int colorToUse = ta.getResourceId(item - 1, R.color.black);
                    int backgroundColor = getResources().getColor(colorToUse);
                    button.setBackgroundColor(backgroundColor);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // save information in local variable, before they will be deleted
                            int pos = listposition;
                            ArrayList<Player> recent_player = player;
                            // ArrayList<Player> play = getIntent().getParcelableArrayExtra("Player");
                            Intent intent = new Intent(getContext(), GameSettingActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("Color", (Integer) v.getTag());
                            intent.putExtra("Position", pos);
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


