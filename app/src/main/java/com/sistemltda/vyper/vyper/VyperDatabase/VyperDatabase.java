package com.sistemltda.vyper.vyper.VyperDatabase;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.sistemltda.vyper.vyper.Models.DAO.InfoDao;
import com.sistemltda.vyper.vyper.Models.DAO.ItemDao;
import com.sistemltda.vyper.vyper.Models.SQLite.Converts.ValorItemConvert;
import com.sistemltda.vyper.vyper.Models.SQLite.Info;
import com.sistemltda.vyper.vyper.Models.SQLite.Item;

@Database(entities = {Info.class, Item.class}, version = 6, exportSchema = false)
@TypeConverters({ValorItemConvert.class})
public abstract class VyperDatabase extends RoomDatabase {
public abstract InfoDao daoAcess();
public abstract ItemDao daoItem();
}
