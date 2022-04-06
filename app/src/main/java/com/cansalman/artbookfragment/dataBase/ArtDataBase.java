package com.cansalman.artbookfragment.dataBase;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.cansalman.artbookfragment.model.Art;

@Database(entities = {Art.class},version = 1)
public abstract class ArtDataBase extends RoomDatabase {
    public abstract ArtDao artDao();
}
