package org.secuso.privacyfriendlyludo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlyludo.R;
import org.secuso.privacyfriendlyludo.logic.BoardModel;

import org.secuso.privacyfriendlyludo.logic.Player;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

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


public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
    private ImageView mArrowLeft;
    private ImageView mArrowRight;
    private BoardModel model;
    private Boolean game_continuable;
    Boolean switchState;
    Switch own_dice;

    @Override
    protected void onRestart() {
        super.onRestart();
        model = loadFile();
        Button game_continue = (Button) findViewById(R.id.game_button_continue);
        if (model == null || model.isGame_finished())
        {
            // no saved game available
            game_continuable = false;
            game_continue.setClickable(true);
            game_continue.setBackgroundColor(ContextCompat.getColor(getBaseContext(),(R.color.middlegrey)));
        }
        else
        {
            game_continuable = true;
            game_continue.setClickable(true);
            game_continue.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overridePendingTransition(0, 0);

        final MainActivity.SectionsPagerAdapter mSectionsPagerAdapter = new MainActivity.SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.scroller);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        int index = mSharedPreferences.getInt("lastChosenPage", 0);
        switchState = mSharedPreferences.getBoolean("own_dice", false);
        own_dice = (Switch) findViewById(R.id.switch_own_dice);

        if (switchState)
        {
            own_dice.setChecked(true);
            // activate switch
        }
        else
        {
            own_dice.setChecked(false);
        }
        mViewPager.setCurrentItem(index);
        mArrowLeft = (ImageView) findViewById(R.id.arrow_left);
        mArrowRight = (ImageView) findViewById(R.id.arrow_right);

        //care for initial postiton of the ViewPager
        mArrowLeft.setVisibility((index == 0) ? View.INVISIBLE : View.VISIBLE);
        mArrowRight.setVisibility((index == mSectionsPagerAdapter.getCount() - 1) ? View.INVISIBLE : View.VISIBLE);

        //Update ViewPager on change
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mArrowLeft.setVisibility((position == 0) ? View.INVISIBLE : View.VISIBLE);
                mArrowRight.setVisibility((position == mSectionsPagerAdapter.getCount() - 1) ? View.INVISIBLE : View.VISIBLE);

                //save position in settings
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putInt("lastChosenPage", position);
                editor.apply();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        model = loadFile();
        Button game_continue = (Button) findViewById(R.id.game_button_continue);
        if (model == null || model.isGame_finished())
        {
            // no saved game available
            game_continuable = false;
            game_continue.setClickable(true);
            game_continue.setBackgroundResource(R.drawable.button_disabled);
        }
        else
        {
            game_continuable = true;
            game_continue.setClickable(true);
            game_continue.setBackgroundResource(R.drawable.button_fullwidth);
        }
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_home;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_left:
                mViewPager.arrowScroll(View.FOCUS_LEFT);
                break;
            case R.id.arrow_right:
                mViewPager.arrowScroll(View.FOCUS_RIGHT);
                break;
            case R.id.game_button_start:
                if (own_dice.isChecked())
                {
                    switchState = true;
                }
                else
                {
                    switchState = false;
                }
                if (game_continuable)
                {
                    // show alertDialog
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    // Setting Dialog Title
                    builder.setTitle(R.string.OverwriteResumableGameTitle);
                    // Setting Dialog Message
                    builder.setMessage(R.string.OverwriteResumableGame);

                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // delete file
                            deleteFile("savedata");
                            // open Settings
                            Intent intent = new Intent(MainActivity.this, GameSettingActivity.class);
                            // check if switch is activated and save it for later

                            ArrayList<Player> last_players = loadSettings(switchState);
                            if (last_players != null)
                            {
                                intent.putParcelableArrayListExtra("Players", last_players);

                            }
                            //save owndice_state in settings
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putBoolean("own_dice", switchState);
                            editor.apply();
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()     {
                        public void onClick(DialogInterface dialog, int id) {
                            //do nothing
                            dialog.dismiss();
                        }
                    });
                    if (!this.isFinishing())
                    {
                        builder.show();
                    }

                }
                else
                {
                    int index = mSharedPreferences.getInt("lastChosenPage", 0);
                    Intent intent = new Intent(MainActivity.this, GameSettingActivity.class);
                    // check if switch is activated and save it for later

                    ArrayList<Player> last_players = loadSettings(switchState);
                    if (last_players != null)
                    {
                        intent.putParcelableArrayListExtra("Players", last_players);

                    }
                    //save own_dice_state in settings
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putBoolean("own_dice", switchState );
                    editor.apply();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }


                break;
            case R.id.game_button_continue:
                if (own_dice.isChecked())
                {
                    switchState = true;
                }
                else
                {
                    switchState = false;
                }
                if (game_continuable)
                {
                    Intent myintent = new Intent(MainActivity.this, GameActivity.class);
                    myintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(myintent);
                }
                else
                {
                    Toast.makeText(MainActivity.this, getString(R.string.no_resumable_game), Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.Info_own_dice:
                        AlertDialog.Builder infoBbuilder = new AlertDialog.Builder(MainActivity.this);
                        infoBbuilder.setTitle(getString(R.string.info_own_dice_question));
                        infoBbuilder.setMessage(R.string.info_own_dice_answer);
                        infoBbuilder.show();
                break;
            default:
                break;
        }
    }

    private BoardModel loadFile() {
        ObjectInputStream ois = null;
        FileInputStream fis = null;
        try {
            fis = this.openFileInput("savedata");
            ois = new ObjectInputStream(fis);
            model = (BoardModel) ois.readObject();
            model.setContext(getBaseContext());
            return model;
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) try { ois.close(); } catch (IOException e) { e.printStackTrace();}
            if (fis != null) try { fis.close(); } catch (IOException e) { e.printStackTrace();}
        }
        return null;
    }

    private ArrayList<Player> loadSettings(Boolean useOwnDice) {
        ObjectInputStream ois = null;
        FileInputStream fis = null;
        String file_name="";
        ArrayList<Player> last_players;
        switch (mSharedPreferences.getInt("lastChosenPage", 0)) {
            case 0:
                file_name = "save_settings_4players";
                break;
            case 1:
                file_name = "save_settings_6players";
                break;
        }
        try {
            fis = this.openFileInput(file_name);
            ois = new ObjectInputStream(fis);
            last_players = (ArrayList<Player>) ois.readObject();
            // change isAI state for all players to false if ownDice is used
            /*
            if (useOwnDice)
            {
                for (int i=0; i<last_players.size(); i++)
                {
                    last_players.get(i).setAI(false);
                }
            }
            */
            return last_players;
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) try { ois.close(); } catch (IOException e) { e.printStackTrace();}
            if (fis != null) try { fis.close(); } catch (IOException e) { e.printStackTrace();}
        }
        return null;
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PageFragment (defined as a static inner class below).
            return MainActivity.PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

    public static class PageFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MainActivity.PageFragment newInstance(int sectionNumber) {
            MainActivity.PageFragment fragment = new MainActivity.PageFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PageFragment() {

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int id=0;

            View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

            ImageView imageView = (ImageView) rootView.findViewById(R.id.gameTypeImage);

            switch ((getArguments().getInt(ARG_SECTION_NUMBER)))
            {
                case 0:
                    imageView.setImageResource(R.drawable.ludo);
                    id=4;
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.ludo6);
                    id=6;
                    break;
                default:
                    break;

            }

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            String playertypeMessageString = getString(R.string.game_type);
            playertypeMessageString = playertypeMessageString.replace("%max", String.valueOf(id));
            textView.setText(playertypeMessageString);
            return rootView;
        }
    }

}