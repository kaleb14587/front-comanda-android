package com.sistemltda.vyper.vyper.VyperServerAPI.Retornos;

import com.google.gson.annotations.SerializedName;

public class ResultadoPagamento {

    @SerializedName("error")
    public boolean error;
    @SerializedName("code")
    public int code;
    @SerializedName("valor_pago")
    public float valor_pago;
}
