package org.secuso.privacyfriendlyludo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.secuso.privacyfriendlyludo.R;
import org.secuso.privacyfriendlyludo.logic.BoardModel;
import org.secuso.privacyfriendlyludo.logic.Player;
import org.secuso.privacyfriendlyludo.tutorial.PrefManager;
import org.secuso.privacyfriendlyludo.tutorial.TutorialActivity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import static android.R.attr.id;

public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
    private ImageView mArrowLeft;
    private ImageView mArrowRight;
    private Button game_continue;
    private BoardModel model;

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
        game_continue = (Button) findViewById(R.id.game_button_continue);
        if (model == null || model.isGame_finished())
        {
            // no saved game available
            game_continue.setClickable(false);
            game_continue.setBackgroundColor(getResources().getColor(R.color.middlegrey));
        }
        else
        {
            game_continue.setClickable(true);
            game_continue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_example;
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
                Intent intent = new Intent(MainActivity.this, GameSettingActivity.class);
                ArrayList<Player> last_players = loadSettings();
                if (last_players != null)
                {
                    intent.putParcelableArrayListExtra("Players", last_players);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.game_button_continue:
                Intent myintent = new Intent(MainActivity.this, GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("BoardModel", model);
                myintent.putExtras(bundle);
                myintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myintent);
                break;
            default:
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
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if (ois != null) try { ois.close(); } catch (IOException e) {}
            if (fis != null) try { fis.close(); } catch (IOException e) {}
        }
        return null;
    }

    private ArrayList<Player> loadSettings() {
        ObjectInputStream ois = null;
        FileInputStream fis = null;
        try {
            fis = this.openFileInput("saveSettings");
            ois = new ObjectInputStream(fis);
            ArrayList<Player> last_players = (ArrayList<Player>) ois.readObject();
            return last_players;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if (ois != null) try { ois.close(); } catch (IOException e) {}
            if (fis != null) try { fis.close(); } catch (IOException e) {}
        }
        return null;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
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
            int id;
          //  if (getArguments() != null) {

            View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

            ImageView imageView = (ImageView) rootView.findViewById(R.id.gameTypeImage);

             if (getArguments().getInt(ARG_SECTION_NUMBER) == 0)
             {

                 imageView.setImageResource(R.drawable.ludo);
                id=4;
             }
             else
             {
                 imageView.setImageResource(R.drawable.ludo6);
                 id=6;
             }
          //  }


            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            int max_players = id;
            String playertypeMessageString = getString(R.string.game_type);
            playertypeMessageString = playertypeMessageString.replace("%max", String.valueOf(id));
            textView.setText(playertypeMessageString);
            return rootView;
        }
    }

}