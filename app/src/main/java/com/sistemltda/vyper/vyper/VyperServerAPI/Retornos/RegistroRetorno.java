package com.sistemltda.vyper.vyper.VyperServerAPI.Retornos;

import com.google.gson.annotations.SerializedName;
import com.sistemltda.vyper.vyper.Models.SQLite.Item;

import java.util.ArrayList;
import java.util.List;

public class RegistroRetorno {
    @SerializedName("id")
    public String id;
    @SerializedName("items")
    public List<Item> item = new ArrayList<>();

}
