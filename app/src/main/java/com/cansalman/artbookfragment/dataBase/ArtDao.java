package com.cansalman.artbookfragment.dataBase;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cansalman.artbookfragment.model.Art;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ArtDao {


    @Query("SELECT * FROM Art  ")
    Flowable<List<Art>> getAll();

    @Query("DELETE FROM Art WHERE id = :user_id ")
    Completable delete(int user_id);

    @Insert
    Completable insert(Art art);

    @Delete
    Completable delete(Art art);
}
