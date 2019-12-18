package com.djaphar.montyhall;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
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

        int prizeId = (int) (Math.random() * 3);
        mGameViewModel.createDoors(prizeId);

        RadioButton firstDoor = findViewById(R.id.firstDoor);
        RadioButton secondDoor= findViewById(R.id.secondDoor);
        RadioButton thirdDoor = findViewById(R.id.thirdDoor);

        View.OnClickListener rbListener = view -> {
            RadioButton rb = (RadioButton)view;
            switch (rb.getId()) {
                case R.id.firstDoor:
                    mGameViewModel.showDialogByOptions(this, prizeId, 0);
                    break;
                case R.id.secondDoor:
                    mGameViewModel.showDialogByOptions(this, prizeId, 1);
                    break;
                case R.id.thirdDoor:
                    mGameViewModel.showDialogByOptions(this, prizeId, 2);
                    break;
            }
        };

        firstDoor.setOnClickListener(rbListener);
        secondDoor.setOnClickListener(rbListener);
        thirdDoor.setOnClickListener(rbListener);
    }

    public float getPercent(float a, float b) {
        return a / b * 100;
    }
}
