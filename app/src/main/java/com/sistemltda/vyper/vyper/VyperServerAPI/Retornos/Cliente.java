package com.sistemltda.vyper.vyper.VyperServerAPI.Retornos;

import com.google.gson.annotations.SerializedName;

public class Cliente {
    @SerializedName("id")
    public String id;
    @SerializedName("nome")
    public String nome;
    @SerializedName("cpf")
    public String cpf;
    @SerializedName("email")
    public String email;
}
