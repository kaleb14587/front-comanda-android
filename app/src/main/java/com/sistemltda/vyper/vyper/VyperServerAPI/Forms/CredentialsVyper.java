package com.sistemltda.vyper.vyper.VyperServerAPI.Forms;

import com.google.gson.annotations.SerializedName;

public class CredentialsVyper {
    @SerializedName("grant_type")
    public String grant_type;
    @SerializedName("client_id")
    public String client_id;
    @SerializedName("client_secret")
    public String client_secret;
    @SerializedName("username")
    public String username;
    @SerializedName("password")
    public String password;
    @SerializedName("scope")
    public String scope;
}
