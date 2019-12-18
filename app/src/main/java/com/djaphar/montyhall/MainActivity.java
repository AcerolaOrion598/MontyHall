package com.djaphar.montyhall;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {

    private GameViewModel mGameViewModel;
    private MainActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView changedStat = findViewById(R.id.changedStat);
        TextView notChangedStat = findViewById(R.id.notChangedStat);

        mGameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);


        mGameViewModel.getChangedWins().observe(activity, val -> {
            if (val != null) {
                mGameViewModel.getChangedGames().observe(activity, secVal -> {
                    if (secVal != null) {
                        String statVal = val.toString() + "/" + secVal.toString() + " (" + getPercent(val, secVal) + "%)";
                        changedStat.setText(statVal);
                    }
                });
            }
        });

        mGameViewModel.getNotChangedWins().observe(activity, val -> {
            if (val != null) {
                mGameViewModel.getNotChangedGames().observe(activity, secVal -> {
                    if (secVal != null) {
                        String statVal = val.toString() + "/" + secVal.toString() + " (" + getPercent(val, secVal) + "%)";
                        notChangedStat.setText(statVal);
                    }
                });
            }
        });
    }

    public float getPercent(float a, float b) {
        return a / b * 100;
    }
}
