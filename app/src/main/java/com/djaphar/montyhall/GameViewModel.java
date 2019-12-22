package com.djaphar.montyhall;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class GameViewModel extends AndroidViewModel {

    private GameDao mGameDao;
    private LiveData<Integer> notChangedGames, notChangedWins, changedGames, changedWins,
            notChangedGamesAuto, notChangedWinsAuto, changedGamesAuto, changedWinsAuto;

    public GameViewModel(@NonNull Application application) {
        super(application);
        GameRoom db = GameRoom.getDatabase(application);
        mGameDao = db.gameDao();

        notChangedGames = mGameDao.getGames(false, false);
        notChangedWins = mGameDao.getWins(false, true, false);
        changedGames = mGameDao.getGames(true, false);
        changedWins = mGameDao.getWins(true, true, false);
        notChangedGamesAuto = mGameDao.getGames(false, true);
        notChangedWinsAuto = mGameDao.getWins(false, true, true);
        changedGamesAuto = mGameDao.getGames(true, true);
        changedWinsAuto = mGameDao.getWins(true, true, true);
    }

    LiveData<Integer> getNotChangedGames(Boolean auto) {
        if (auto) {
            return notChangedGamesAuto;
        } else {
            return notChangedGames;
        }
    }

    LiveData<Integer> getNotChangedWins(Boolean auto) {
        if (auto) {
            return notChangedWinsAuto;
        } else {
            return notChangedWins;
        }
    }

    LiveData<Integer> getChangedGames(Boolean auto) {
        if (auto) {
            return changedGamesAuto;
        } else {
            return changedGames;
        }
    }

    LiveData<Integer> getChangedWins(Boolean auto) {
        if (auto) {
            return changedWinsAuto;
        } else {
            return changedWins;
        }
    }

    private void insert(Game game) {
        GameRoom.databaseWriteExecutor.execute(() -> mGameDao.insert(game));
    }

    void clearStats(Boolean auto) {
        GameRoom.databaseWriteExecutor.execute(() -> mGameDao.clearTable(auto));
    }

    void autoPlay(int games) {

        for (int i = 0; i < games; i++) {
            int prizeId = (int) (Math.random() * 3);
            int myDoor = (int) (Math.random() * 3);

            if (i % 2 == 0) {
                //Блок с изменением выбора
                if (myDoor == prizeId) {
                     createGame(true, false, true);
                } else {
                    createGame(true, true, true);
                }
            } else {
                //Блок без изменения выбора
                if (myDoor == prizeId) {
                    createGame(false, true, true);
                } else {
                    createGame(false, false, true);
                }
            }
        }
    }

    int figureOutEmptyDoor(int prizeId, int myDoor) {

        if (prizeId != myDoor) {
            for (int i = 0; true; i++) {
                if (i != prizeId && i != myDoor) {
                    return i;
                }
            }
        } else {
            return randomizeMe(prizeId);
        }
    }

    private int randomizeMe(int prizeId) {
        int rand = (int) (Math.random() * 3);
        if (rand == prizeId) {
            rand = randomizeMe(prizeId);
        }

        return rand;
    }

    float getPercent(float a, float b) {
        return round(a / b * 100);
    }

    private float round(float f) {
        f = f * 100;
        int i = Math.round(f);
        return (float) i / 100;
    }

    void createGame(Boolean isChanged, Boolean win, Boolean auto) {
        insert(new Game(isChanged, win, auto));
    }
}
