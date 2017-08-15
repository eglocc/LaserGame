package com.ergizgizer.lasergame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViewById(R.id.start_game).setOnClickListener(this);
        findViewById(R.id.settings).setOnClickListener(this);
        findViewById(R.id.help_me).setOnClickListener(this);
        findViewById(R.id.credits).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_game:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
            case R.id.settings:
                //TODO pass to settingsActivity
                break;
            case R.id.help_me:
                //TODO pass to helpActivity
                break;
            case R.id.credits:
                //TODO pass to CreditsActivity
                break;
        }
    }
}
