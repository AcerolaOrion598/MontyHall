package com.djaphar.montyhall;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class AutoPlayActivity extends AppCompatActivity {

    private GameViewModel mGameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_play);

        TextView changedStatAuto = findViewById(R.id.changedStatAuto);
        TextView notChangedStatAuto = findViewById(R.id.notChangedStatAuto);

        mGameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);

        mGameViewModel.getChangedWins(true).observe(this, val -> {
            if (val != null) {
                mGameViewModel.getChangedGames(true).observe(this, secVal -> {
                    if (secVal != null) {
                        String statVal = val.toString() + "/" + secVal.toString() + " (" + mGameViewModel.getPercent(val, secVal) + "%)";
                        changedStatAuto.setText(statVal);
                    }
                });
            }
        });

        mGameViewModel.getNotChangedWins(true).observe(this, val -> {
            if (val != null) {
                mGameViewModel.getNotChangedGames(true).observe(this, secVal -> {
                    if (secVal != null) {
                        String statVal = val.toString() + "/" + secVal.toString() + " (" + mGameViewModel.getPercent(val, secVal) + "%)";
                        notChangedStatAuto.setText(statVal);
                    }
                });
            }
        });

        Button autoPlayBtn = findViewById(R.id.autoPlayLaunchBtn);
        autoPlayBtn.setOnClickListener(view -> mGameViewModel.autoPlay(100));

        Button clearAutoBtn = findViewById(R.id.clearAutoBtn);
        clearAutoBtn.setOnClickListener(view -> {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle(R.string.alert_clear_title)
                .setMessage(this.getResources().getString(R.string.alert_clear_message))
                .setPositiveButton(R.string.alert_pos_button, (dialogInterface, i) -> mGameViewModel.clearStats(true))
                .setNegativeButton(R.string.alert_neg_button, (dialogInterface, i) -> dialogInterface.cancel())
                .show();
        });

        Button mainActivityBtn = findViewById(R.id.mainActivityBtn);
        mainActivityBtn.setOnClickListener(view -> startActivity(new Intent (this, MainActivity.class)));
    }
}
