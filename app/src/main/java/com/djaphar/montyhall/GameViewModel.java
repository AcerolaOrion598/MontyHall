package com.djaphar.montyhall;

import android.app.Application;

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

    void insert(Game game) {
        GameRoom.databaseWriteExecutor.execute(() -> mGameDao.insert(game));
    }

    void clearStats() {
        GameRoom.databaseWriteExecutor.execute(() -> mGameDao.clearTable());
    }

    void autoPlay(int games) {

        for (int i = 0; i < games; i++) {
            int prizeId = (int) (Math.random() * 3);
            int myDoor = (int) (Math.random() * 3);

            if (i % 2 == 0) {
                //Блок с изменением выбора
                if (myDoor == prizeId) {
                    //insert в бд для автоплея с поражением
                } else {
                    //insert в бд для автоплея с победой
                }
            } else {
                //Блок без изменения выбора
                if (myDoor == prizeId) {
                    //insert в бд для автоплея с победой
                } else {
                    //insert в бд для автоплея с поражением
                }
            }
        }
    }

    int figureOutEmptyDoor(int prizeId, int myDoor) {
        int emptyDoor;

        if (prizeId != myDoor) {
            for (int i = 0; true; i++) {
                if (i != prizeId && i != myDoor) {
                    emptyDoor = i;
                    break;
                }
            }
        } else {
            emptyDoor = randomizeMe(prizeId);
        }

        return emptyDoor;
    }

    private int randomizeMe(int prizeId) {
        int rand = (int) (Math.random() * 3);
        if (rand == prizeId) {
            rand = randomizeMe(prizeId);
        }

        return rand;
    }
}
