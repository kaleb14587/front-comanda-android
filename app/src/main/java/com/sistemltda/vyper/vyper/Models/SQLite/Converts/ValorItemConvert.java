package com.sistemltda.vyper.vyper.Models.SQLite.Converts;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.sistemltda.vyper.vyper.Models.SQLite.ValorItem;

public class ValorItemConvert {
    @TypeConverter
    public static String obj2String(ValorItem vt){
        return  new Gson().toJson(vt);
    }

    @TypeConverter
    public static ValorItem string2Object(String obj){

        return new Gson().fromJson(obj,ValorItem.class);
    }
}
