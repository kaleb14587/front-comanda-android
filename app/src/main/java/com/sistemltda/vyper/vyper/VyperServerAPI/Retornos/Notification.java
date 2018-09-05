package com.sistemltda.vyper.vyper.VyperServerAPI.Retornos;

import com.google.gson.annotations.SerializedName;
import com.sistemltda.vyper.vyper.Models.SQLite.Item;

public class Notification {
    @SerializedName("type")
    private String type;

    @SerializedName("data")
    private Item data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Item getData() {
        return data;
    }

    public void setData(Item data) {
        this.data = data;
    }
}
