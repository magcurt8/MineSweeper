package com.example.maggie.minesweeper;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Switch;

import com.example.maggie.minesweeper.view.MineSweeperView;

public class MainActivity extends AppCompatActivity {

    public ScrollView layoutContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutContent=(ScrollView) findViewById(R.id.layoutContent);

        final MineSweeperView gameField=
                (MineSweeperView) findViewById(R.id.gameField);
        Button btnRestart=(Button) findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameField.clearGameField();
            }
        });
        Switch fmswitch=(Switch) findViewById(R.id.fmswitch);
        fmswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    gameField.setFlag(true);
                }else {
                    gameField.setFlag(false);
                }
            }
        });

    }
    public void showSnackbarMessage(String msg){
        Snackbar.make(layoutContent, msg, Snackbar.LENGTH_LONG).show();
    }

}
