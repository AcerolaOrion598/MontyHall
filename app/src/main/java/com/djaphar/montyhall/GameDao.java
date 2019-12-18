package com.djaphar.montyhall;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface GameDao {

    @Insert
    void insert(Game game);

    @Query("SELECT COUNT(*) FROM game_table WHERE isChanged = :changed")
    LiveData<Integer> getGames(Boolean changed);

    @Query("SELECT COUNT(*) FROM game_table WHERE isChanged = :changed AND win = :win")
    LiveData<Integer> getWins(Boolean changed, Boolean win);
}
