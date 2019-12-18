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
    private Door[] doors = new Door[3];

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
        GameRoom.databaseWriteExecutor.execute(() -> {
            mGameDao.insert(game);
        });
    }

    void clearStats() {
        GameRoom.databaseWriteExecutor.execute(() -> {
            mGameDao.clearTable();
        });
    }

    void createDoors(int prizeId) {
        for (int i = 0; i < 3; i++) {
            if (i == prizeId) {
                doors[i] = new Door(true);
            } else {
                doors[i] = new Door(false);
            }
        }
    }

    void showDialogByOptions(Context context, int prizeId, int myDoor, MainActivity activity) {

        if (prizeId != myDoor) {
            for (int i = 0; true; i++) {
                if (i != prizeId && i != myDoor) {
                    dialog(context, i, prizeId, myDoor, activity);
                    break;
                }
            }
        } else {
            int randomDoor = randomizeMe(prizeId);
            dialog(context, randomDoor, prizeId, myDoor, activity);
        }

    }

    private int randomizeMe(int prizeId) {
        int rand = (int) (Math.random() * 3);
        if (rand == prizeId) {
            rand = randomizeMe(prizeId);
        }
        return rand;
    }

    private void dialog(Context context, int emptyDoor, int prizeId, int myDoor, MainActivity activity) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle("Шаг 2")
                .setMessage("Дверь " + (emptyDoor + 1) +" пустая. Хотите поменять свой выбор?")
                .setPositiveButton("Да", (dialogInterface, i) -> {
                    if (myDoor == prizeId) {
                        endGame(true, false, "проиграли!", context, activity);
                    } else {
                        endGame(true, true, "выиграли!", context, activity);
                    }
                })
                .setNegativeButton("Нет", (dialogInterface, i) -> {
                    if (myDoor == prizeId) {
                        endGame(false, true, "выиграли!", context, activity);
                    } else {
                        endGame(false, false, "проиграли!", context, activity);
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void endGame(Boolean isChanged, Boolean win, String status, Context context, MainActivity activity) {
        Game game = new Game(isChanged, win);
        insert(game);
        Toast.makeText(context, "Вы " + status, Toast.LENGTH_LONG).show();
        activity.recreate();
    }
}
