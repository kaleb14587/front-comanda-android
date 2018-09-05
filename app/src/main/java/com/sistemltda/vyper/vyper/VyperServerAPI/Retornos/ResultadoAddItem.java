package com.sistemltda.vyper.vyper.VyperServerAPI.Retornos;

import com.google.gson.annotations.SerializedName;

public class ResultadoAddItem {
    public static final int ERROR_CLIENTE_NOT_FOUND = 291;
    @SerializedName("error")
    public boolean error;
    @SerializedName("code")
    public int code;
}
