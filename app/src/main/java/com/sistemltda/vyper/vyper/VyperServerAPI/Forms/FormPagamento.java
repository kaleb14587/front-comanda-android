package com.sistemltda.vyper.vyper.VyperServerAPI.Forms;

import com.google.gson.annotations.SerializedName;

public class FormPagamento {
    public static final String FORMA_PAGA_CARTAO    = "cartao";
    public static final String FORMA_PAGA_DINHEIRO  = "dinheiro";

    @SerializedName("registroVyper")
    public String registroVyper;


    @SerializedName("valor_pago")
    public float valor_pago;
    @SerializedName("forma_pagamento")
    public String forma_pagamento;
    @SerializedName("id_comanda")
    public String id_comanda;
}
