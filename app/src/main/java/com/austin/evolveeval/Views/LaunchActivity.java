package com.austin.evolveeval.Views;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.View;
import android.widget.CheckBox;

import com.austin.evolveeval.Helpers.DatabaseHelper;
import com.austin.evolveeval.Models.DataBase;
import com.austin.evolveeval.R;

public class LaunchActivity extends AppCompatActivity {

    private FloatingActionButton mButtonStart;
    private CheckBox mLaunchPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBase.db = new DatabaseHelper(this);
        if (DataBase.db.hideLaunch()) {
            start(true);
        }

        setContentView(R.layout.activity_launch);
        mLaunchPreference = (CheckBox) findViewById(R.id.launch_preference);
        mButtonStart = (FloatingActionButton) findViewById(R.id.button_start);
        getWindow().setExitTransition(new Fade());
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLaunchPreference.isChecked()) {
                    DataBase.db.setHideLaunchPreference();
                }
                start(false);
            }
        });
    }

    private void start(boolean direct) {
        Intent intent = new Intent(LaunchActivity.this, MainActivity.class);;
        if (direct) {
            startActivity(intent);
        } else {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    LaunchActivity.this, mButtonStart, "transitionElement");
            startActivity(intent, options.toBundle());
        }
    }

}
