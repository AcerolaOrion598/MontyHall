package com.djaphar.montyhall;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface GameDao {

    @Insert
    void insert(Game game);

    @Query("DELETE FROM game_table WHERE auto = :auto")
    void clearTable(Boolean auto);

    @Query("SELECT COUNT(*) FROM game_table WHERE isChanged = :changed AND auto = :auto")
    LiveData<Integer> getGames(Boolean changed, Boolean auto);

    @Query("SELECT COUNT(*) FROM game_table WHERE isChanged = :changed AND win = :win AND auto = :auto")
    LiveData<Integer> getWins(Boolean changed, Boolean win, Boolean auto);
}
