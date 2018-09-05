package com.sistemltda.vyper.vyper.VyperServerAPI.Retornos;

import com.google.gson.annotations.SerializedName;
import com.sistemltda.vyper.vyper.Models.SQLite.Item;

import java.util.ArrayList;

public class ClienteComanda {
    public static final int ERROR_CLIENTE_NOT_FOUND = 291;
    @SerializedName("id")
    public int id;
    @SerializedName("error")
    public boolean error;
    @SerializedName("code")
    public int code;
    @SerializedName("cliente")
    public Cliente cliente;
    @SerializedName("items")
    public ArrayList<Item> items;
    public String getMessage(){
        switch (code){
            case 290:
                return "Dispositivo com problemas!";
            case ERROR_CLIENTE_NOT_FOUND:
                return "Cliente nao encontrado !";

        }
        return "";
    }

    public String calculaValor(){
        float total = 0;
        if(this.items==null )return "0,00";
        for (Item it:this.items){
            if(it.getValor_vigente()!=null)
            total =total +   Float.parseFloat(it.getValor_vigente().getValor_final());
        }

        return (total+"").replace(".",",");
    }
}
