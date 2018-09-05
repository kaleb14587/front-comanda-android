package com.sistemltda.vyper.vyper.VyperServerAPI.Forms;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddItemComanda {
    @SerializedName("registroVyper")
    public String registroVyper;
    @SerializedName("cliente")
    public String cliente;
    @SerializedName("id_comanda")
    public String id_comanda;
    @SerializedName("item")
    public ArrayList<Integer> item;

}
