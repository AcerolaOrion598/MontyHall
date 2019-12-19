package com.djaphar.montyhall;

import android.app.AlertDialog;
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
    private RadioButton firstDoor, secondDoor, thirdDoor;

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

        firstDoor = findViewById(R.id.firstDoor);
        secondDoor= findViewById(R.id.secondDoor);
        thirdDoor = findViewById(R.id.thirdDoor);

        int prizeId = (int) (Math.random() * 3);
        resetRadioListener(prizeId);
        Button clearBtn = findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(view -> {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle(R.string.alert_clear_title)
                .setMessage(this.getResources().getString(R.string.alert_clear_message))
                .setPositiveButton(R.string.alert_pos_button, (dialogInterface, i) -> {
                    mGameViewModel.clearStats();
                })
                .setNegativeButton(R.string.alert_neg_button, (dialogInterface, i) -> {
                    dialogInterface.cancel();
                })
                .show();
        });
    }

    public void resetRadioListener(int prizeId) {
        View.OnClickListener rbListener = view -> {
            AlertDialog.Builder ad = null;

            RadioButton rb = (RadioButton)view;
            switch (rb.getId()) {
                case R.id.firstDoor:
                    ad = mGameViewModel.createDialogByOptions(this, prizeId, 0, activity);
                    break;
                case R.id.secondDoor:
                    ad = mGameViewModel.createDialogByOptions(this, prizeId, 1, activity);
                    break;
                case R.id.thirdDoor:
                    ad = mGameViewModel.createDialogByOptions(this, prizeId, 2, activity);
                    break;
            }

            if (ad != null) {
                ad.show();
            }
        };

        firstDoor.setOnClickListener(rbListener);
        secondDoor.setOnClickListener(rbListener);
        thirdDoor.setOnClickListener(rbListener);
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
