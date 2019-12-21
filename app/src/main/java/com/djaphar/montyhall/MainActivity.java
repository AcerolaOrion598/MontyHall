package com.djaphar.montyhall;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {

    private GameViewModel mGameViewModel;
    private RadioButton firstDoor, secondDoor, thirdDoor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView changedStat = findViewById(R.id.changedStat);
        TextView notChangedStat = findViewById(R.id.notChangedStat);

        mGameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);


        mGameViewModel.getChangedWins().observe(this, val -> {
            if (val != null) {
                mGameViewModel.getChangedGames().observe(this, secVal -> {
                    if (secVal != null) {
                        String statVal = val.toString() + "/" + secVal.toString() + " (" + getPercent(val, secVal) + "%)";
                        changedStat.setText(statVal);
                    }
                });
            }
        });

        mGameViewModel.getNotChangedWins().observe(this, val -> {
            if (val != null) {
                mGameViewModel.getNotChangedGames().observe(this, secVal -> {
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
            int myDoor = -1;

            RadioButton rb = (RadioButton)view;
            switch (rb.getId()) {
                case R.id.firstDoor:
                    myDoor = 0;
                    break;
                case R.id.secondDoor:
                    myDoor = 1;
                    break;
                case R.id.thirdDoor:
                    myDoor = 2;
                    break;
            }

            if (myDoor != -1) {
                ad = dialog(mGameViewModel.figureOutEmptyDoor(prizeId, myDoor), prizeId, myDoor);
            }

            if (ad != null) {
                ad.show();
            }
        };

        firstDoor.setOnClickListener(rbListener);
        secondDoor.setOnClickListener(rbListener);
        thirdDoor.setOnClickListener(rbListener);
    }

    private AlertDialog.Builder dialog(int emptyDoor, int prizeId, int myDoor) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle(R.string.alert_title)
            .setMessage(this.getResources().getString(R.string.alert_message_first) + " " + (emptyDoor + 1)
                    + " " + this.getResources().getString(R.string.alert_message_second))
            .setPositiveButton(R.string.alert_pos_button, (dialogInterface, i) -> {
                if (myDoor == prizeId) {
                    endGame(true, false, this.getResources().getString(R.string.endgame_toast_lose));
                } else {
                    endGame(true, true, this.getResources().getString(R.string.endgame_toast_win));
                }
            })
            .setNegativeButton(R.string.alert_neg_button, (dialogInterface, i) -> {
                if (myDoor == prizeId) {
                    endGame(false, true, this.getResources().getString(R.string.endgame_toast_win));
                } else {
                    endGame(false, false, this.getResources().getString(R.string.endgame_toast_lose));
                }
            })
            .setCancelable(false);

        return ad;
    }

    private void endGame(Boolean isChanged, Boolean win, String status) {
        Game game = new Game(isChanged, win);
        mGameViewModel.insert(game);
        Toast.makeText(this, this.getResources().getString(R.string.endgame_toast_begin) + " "
                + status, Toast.LENGTH_LONG).show();

        int prizeId = (int) (Math.random() * 3);
        resetRadioListener(prizeId);
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
