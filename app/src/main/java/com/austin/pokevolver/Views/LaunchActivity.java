package com.austin.pokevolver.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.austin.pokevolver.Helpers.DatabaseHelper;
import com.austin.pokevolver.Models.DataBase;
import com.austin.pokevolver.R;

public class LaunchActivity extends AppCompatActivity {

    private Button mButtonStart;
    private CheckBox mLaunchPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBase.db = new DatabaseHelper(this);
        if (DataBase.db.hideLaunch()) {
            start();
        }

        //TODO Double Check this logic for preferences
        setContentView(R.layout.activity_launch);
        mLaunchPreference = (CheckBox) findViewById(R.id.launch_preference);
        mButtonStart = (Button) findViewById(R.id.button_start);
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLaunchPreference.isChecked()) {
                    DataBase.db.setHideLaunchPreference();
                }
                start();
            }
        });
    }

    private void start() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
