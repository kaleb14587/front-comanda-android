package com.sistemltda.vyper.vyper.Models.SQLite;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.sistemltda.vyper.vyper.Models.SQLite.Converts.ValorItemConvert;

@Entity
public class Item {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int _id;
    @SerializedName("id")
    private String id_vyper;
    @SerializedName("id_user")
    private String id_user;
    @SerializedName("nome")
    private String nome;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("img_nome")
    private String img_nome;

    @ColumnInfo(name="valor_vigente",typeAffinity =2)
    @SerializedName("valor_vigente")
    private ValorItem valor_vigente;
    @SerializedName("qtd")
    private int qtd;
    @SerializedName("total")
    private float total;

    public boolean isNew= false;
    @NonNull
    public int get_id() {
        return _id;
    }

    public void set_id(@NonNull int _id) {
        this._id = _id;
    }

    public String getId_vyper() {
        return id_vyper;
    }

    public void setId_vyper(String id_vyper) {
        this.id_vyper = id_vyper;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getImg_nome() {
        return img_nome;
    }

    public void setImg_nome(String img_nome) {
        this.img_nome = img_nome;
    }

    public ValorItem getValor_vigente() {
        return valor_vigente;
    }

    public void setValor_vigente(ValorItem valor_vigente) {
        this.valor_vigente = valor_vigente;
    }
    public void setQtd(int qtd) {
        this.qtd = qtd;
    }
    public void setTotal(float total) {
        this.total= total;
    }
    @Override
    public String toString() {
        return this.nome;
    }

    public int getQtd() {
        return qtd;
    }

    public float getTotal() {
        return total;
    }
}
