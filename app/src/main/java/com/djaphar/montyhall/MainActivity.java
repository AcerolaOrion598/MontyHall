package com.djaphar.montyhall;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
                    mGameViewModel.showDialogByOptions(this, prizeId, 0, activity);
                    break;
                case R.id.secondDoor:
                    mGameViewModel.showDialogByOptions(this, prizeId, 1, activity);
                    break;
                case R.id.thirdDoor:
                    mGameViewModel.showDialogByOptions(this, prizeId, 2, activity);
                    break;
            }
        };

        firstDoor.setOnClickListener(rbListener);
        secondDoor.setOnClickListener(rbListener);
        thirdDoor.setOnClickListener(rbListener);

        Button clearBtn = findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(view -> mGameViewModel.clearStats());
    }

    public float getPercent(float a, float b) {
        return round(a / b * 100);
    }

    public float round(float f) {
        f = f * 100;
        int i = Math.round(f);
        return (float)i / 100;
    }
}
