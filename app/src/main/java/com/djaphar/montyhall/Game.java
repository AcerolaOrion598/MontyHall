package com.djaphar.montyhall;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "game_table")
public class Game {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "isChanged")
    private Boolean isChanged;

    @NonNull
    @ColumnInfo(name = "win")
    private Boolean win;

    public Game(@NonNull Boolean isChanged, @NonNull Boolean win) {
        this.isChanged = isChanged;
        this.win = win;
    }

    @NonNull
    public Boolean getChanged() {
        return this.isChanged;
    }

    @NonNull
    public Boolean getWin() {
        return this.win;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
