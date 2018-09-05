package com.sistemltda.vyper.vyper.Models.SQLite;


import com.google.gson.annotations.SerializedName;

public class ValorItem {

    @SerializedName("id")
    private String id;
    @SerializedName("id_item")
    private String id_item;
    @SerializedName("valor_final")
    private String valor_final;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public String getValor_final() {
        return valor_final;
    }

    public void setValor_final(String valor_final) {
        this.valor_final = valor_final;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
