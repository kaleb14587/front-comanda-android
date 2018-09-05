package com.sistemltda.vyper.vyper.Models.SQLite;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Info {
    @NonNull
    @PrimaryKey
    private int infoId;
    public String registroVyper;
    private String registrationId;
    private String bearerToken;
    private String refreshToken;

    public String getRegistroVyper() {
        return registroVyper;
    }

    public void setRegistroVyper(String registroVyper) {
        this.registroVyper = registroVyper;
    }

    @NonNull
    public int getInfoId() {
        return infoId;
    }

    public void setInfoId(@NonNull int infoId) {
        this.infoId = infoId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
