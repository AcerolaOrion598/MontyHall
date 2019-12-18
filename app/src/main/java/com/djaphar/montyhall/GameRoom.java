package com.djaphar.montyhall;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Game.class}, version = 1, exportSchema = false)
public abstract class GameRoom extends RoomDatabase {

    public abstract GameDao gameDao();

    private static volatile GameRoom INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static GameRoom getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GameRoom.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GameRoom.class, "game_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
