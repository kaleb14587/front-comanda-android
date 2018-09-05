package com.sistemltda.vyper.vyper.VyperServerAPI.Retornos;

import com.google.gson.annotations.SerializedName;

public class CompEmail {
    @SerializedName("error")
    public boolean error;
    @SerializedName("message")
    public String message;
}
