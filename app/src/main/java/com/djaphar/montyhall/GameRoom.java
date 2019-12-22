package com.djaphar.montyhall;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Game.class}, version = 2, exportSchema = false)
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
                        .addMigrations(MIGRATION_1_2)
                        .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE game_table ADD COLUMN auto INTEGER DEFAULT 0 NOT NULL");
        }
    };
}
