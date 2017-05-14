package org.secuso.privacyfriendlyludo.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import org.secuso.privacyfriendlyludo.activities.ShakeListener;

import org.secuso.privacyfriendlyludo.R;

public class GameActivity extends AppCompatActivity {

    private ImageView myimageView;
    boolean shakingEnabled;
    boolean vibrationEnabled;
    SharedPreferences sharedPreferences;

    // for Shaking
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeListener shakeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        doFirstRun();


        //Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


        //Button
        Button rollDiceButton = (Button) findViewById(R.id.rollButton);

        rollDiceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                evaluate((Vibrator) getSystemService(Context.VIBRATOR_SERVICE));

            }
        });

        //Shaking
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeListener = new ShakeListener();
        shakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {

            public void onShake(int count) {

                if (shakingEnabled) {
                    evaluate((Vibrator) getSystemService(Context.VIBRATOR_SERVICE));
                }
            }
        });

    }

    public void evaluate(Vibrator vibrator) {

        applySettings();

        Dicer dicer = new Dicer();
        int dice = dicer.rollDice();
        initResultDiceViews();

        Display display = getWindowManager().getDefaultDisplay();

        switchDice(myimageView, dice);
        android.view.ViewGroup.LayoutParams layoutParams = myimageView.getLayoutParams();
        layoutParams.width = display.getWidth() / 6;
        layoutParams.height = display.getWidth() / 6;

        myimageView.setLayoutParams(layoutParams);
        flashResult(myimageView);
        if (vibrationEnabled) {
            vibrator.vibrate(50);
        }


    }

        public void flashResult(ImageView imageView) {

            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(500);
            animation.setStartOffset(20);
            animation.setRepeatMode(Animation.REVERSE);
            imageView.startAnimation(animation);

        }

        public void initResultDiceViews() {
           // imageViews = new ImageView[1];

            myimageView = (ImageView) findViewById(R.id.resultOne);
            myimageView.setImageResource(0);
        }

        public void switchDice(ImageView imageView, int result) {

            switch (result) {
                case 1:
                    imageView.setImageResource(R.drawable.d1);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.d2);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.d3);
                    break;
                case 4:
                    imageView.setImageResource(R.drawable.d4);
                    break;
                case 5:
                    imageView.setImageResource(R.drawable.d5);
                    break;
                case 6:
                    imageView.setImageResource(R.drawable.d6);
                    break;
                case 0:
                    imageView.setImageResource(0);
                    break;
                default:
                    break;
            }
        }



        public void applySettings() {
            shakingEnabled = sharedPreferences.getBoolean("enable_shaking", false);
            vibrationEnabled = sharedPreferences.getBoolean("enable_vibration", false);
        }




        private void doFirstRun() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.edit().putString("firstShow", "").commit();
            SharedPreferences settings = getSharedPreferences("firstShow", getBaseContext().MODE_PRIVATE);
        }


}

