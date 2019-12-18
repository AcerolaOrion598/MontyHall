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
        GameRoom.databaseWriteExecutor.execute(() -> {
            mGameDao.insert(game);
        });
    }
}
