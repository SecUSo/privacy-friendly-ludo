package org.secuso.privacyfriendlyludo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.secuso.privacyfriendlyludo.R;

public class GameSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setting);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_game:
                Intent intent = new Intent(GameSettingActivity.this, GameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            default:
        }
    }
}
