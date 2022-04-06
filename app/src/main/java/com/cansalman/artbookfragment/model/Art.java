package com.cansalman.artbookfragment.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Art implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public  String name;
    @ColumnInfo(name = "painter")
    public  String painter;
    @ColumnInfo(name = "year")
    public  String year;

    @ColumnInfo(name = "image")
    public byte[] image;

    public Art(String name,String painter,String year,byte[] image){
        this.name=name;
        this.painter=painter;
        this.year=year;
        this.image=image;


    }


}
