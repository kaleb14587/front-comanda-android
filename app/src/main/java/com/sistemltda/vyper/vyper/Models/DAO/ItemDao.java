package com.sistemltda.vyper.vyper.Models.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.graphics.Movie;

import com.sistemltda.vyper.vyper.Models.SQLite.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(Item... itens);

    @Query("SELECT COUNT(*) FROM item")
    int count();

    @Query("SELECT * FROM item")
    List<Item> fetchAllData();
    @Query("SELECT * FROM item where nome like :titulo ")
    List<Item> likeItem(String titulo);
    @Query("SELECT * FROM item WHERE _id =:id")
    Item getSingleRecord(int id);

    @Update
    void update(Item... items);

}
