package com.sistemltda.vyper.vyper.VyperServerAPI.Forms;

import com.google.gson.annotations.SerializedName;

public class FormCliente {
    @SerializedName("nome")
     public String nome;
    @SerializedName("cpf")
     public String cpf;
    @SerializedName("email")
     public String email;
    @SerializedName("fingercode")
     public String fingercode;
    @SerializedName("registroVyper")
     public String registroVyper;

}
