package com.djaphar.montyhall;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class GameViewModel extends AndroidViewModel {

    private GameDao mGameDao;
    private LiveData<Integer> notChangedGames, notChangedWins, changedGames, changedWins;

    public GameViewModel(@NonNull Application application) {
        super(application);
        GameRoom db = GameRoom.getDatabase(application);
        mGameDao = db.gameDao();

        notChangedGames = mGameDao.getGames(false);
        notChangedWins = mGameDao.getWins(false, true);
        changedGames = mGameDao.getGames(true);
        changedWins = mGameDao.getWins(true, true);
    }

    LiveData<Integer> getNotChangedGames() {
        return notChangedGames;
    }

    LiveData<Integer> getNotChangedWins() {
        return notChangedWins;
    }

    LiveData<Integer> getChangedGames() {
        return changedGames;
    }

    LiveData<Integer> getChangedWins() {
        return changedWins;
    }

    private void insert(Game game) {
        GameRoom.databaseWriteExecutor.execute(() -> mGameDao.insert(game));
    }

    void clearStats() {
        GameRoom.databaseWriteExecutor.execute(() -> mGameDao.clearTable());
    }

    AlertDialog.Builder showDialogByOptions(Context context, int prizeId, int myDoor, MainActivity activity) {
        AlertDialog.Builder ad;

        if (prizeId != myDoor) {
            for (int i = 0; true; i++) {
                if (i != prizeId && i != myDoor) {
                    ad = dialog(context, i, prizeId, myDoor, activity);
                    break;
                }
            }
        } else {
            int randomDoor = randomizeMe(prizeId);
            ad = dialog(context, randomDoor, prizeId, myDoor, activity);
        }
        return ad;
    }

    private int randomizeMe(int prizeId) {
        int rand = (int) (Math.random() * 3);
        if (rand == prizeId) {
            rand = randomizeMe(prizeId);
        }
        return rand;
    }

    private AlertDialog.Builder dialog(Context context, int emptyDoor, int prizeId, int myDoor, MainActivity activity) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle(R.string.alert_title)
                .setMessage(context.getResources().getString(R.string.alert_message_first) + " " + (emptyDoor + 1)
                        + " " + context.getResources().getString(R.string.alert_message_second))
                .setPositiveButton(R.string.alert_pos_button, (dialogInterface, i) -> {
                    if (myDoor == prizeId) {
                        endGame(true, false, context.getResources().getString(R.string.endgame_toast_lose),
                                context, activity);
                    } else {
                        endGame(true, true, context.getResources().getString(R.string.endgame_toast_win),
                                context, activity);
                    }
                })
                .setNegativeButton(R.string.alert_neg_button, (dialogInterface, i) -> {
                    if (myDoor == prizeId) {
                        endGame(false, true, context.getResources().getString(R.string.endgame_toast_win),
                                context, activity);
                    } else {
                        endGame(false, false, context.getResources().getString(R.string.endgame_toast_lose),
                                context, activity);
                    }
                })
                .setCancelable(false);
        return ad;
    }

    private void endGame(Boolean isChanged, Boolean win, String status, Context context, MainActivity activity) {
        Game game = new Game(isChanged, win);
        insert(game);
        Toast.makeText(context, context.getResources().getString(R.string.endgame_toast_begin) + " "
                + status, Toast.LENGTH_LONG).show();

        int prizeId = (int) (Math.random() * 3);
        activity.resetRadioListener(prizeId);
    }
}
