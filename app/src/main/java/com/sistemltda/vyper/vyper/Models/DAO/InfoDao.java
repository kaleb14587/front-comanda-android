package com.sistemltda.vyper.vyper.Models.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sistemltda.vyper.vyper.Models.SQLite.Info;

@Dao
public interface InfoDao {
    @Insert
    void insertInfo(Info info);
    @Query("SELECT * FROM Info ")
    Info selectInfo ();
    @Update
    void updateInfo (Info info);
}
