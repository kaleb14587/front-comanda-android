package com.sistemltda.vyper.vyper.AppVyper;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Intent;

import com.sistemltda.vyper.vyper.Listeners.SendItem;
import com.sistemltda.vyper.vyper.Models.DAO.InfoDao;
import com.sistemltda.vyper.vyper.Models.SQLite.Info;
import com.sistemltda.vyper.vyper.VyperDatabase.VyperDatabase;
import com.sistemltda.vyper.vyper.VyperServerAPI.Retornos.ClienteComanda;
import com.varvet.barcodereadersample.barcode.BarcodeCaptureActivity;

public class VyperApp extends Application {
    private static final String DATABASE_NAME = "Vyper_database";
    private VyperDatabase vyperDatabase;
    private String fingerCadas;
    private ClienteComanda cli;
    private SendItem listenerItem;
    @Override
    public void onCreate() {
        super.onCreate();
        vyperDatabase = Room.databaseBuilder(getApplicationContext(),
                VyperDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries()
                .build();
    }
    public String getBearerToken(){
        InfoDao dao =  vyperDatabase.daoAcess();
        Info inf  = dao.selectInfo();
        return inf.getBearerToken();
    }
    public String getVyperRegister(){
        InfoDao dao =  vyperDatabase.daoAcess();
        Info inf  = dao.selectInfo();
        return inf.getRegistroVyper();
    }
    public VyperDatabase getVyperDatabase() {
        return vyperDatabase;
    }

    public String getFingerCadas() {
        return fingerCadas;
    }
    public void comandaAberta(ClienteComanda cli){
        this.cli = cli;
    }

    public ClienteComanda getCli() {
        return cli;
    }

    public void setFingerCadas(String fingerCadas) {
        this.fingerCadas = fingerCadas;
    }
    public SendItem getItemPComanda(){
        return this.listenerItem;
    }
    public void setListenerItem(SendItem it){
        this.listenerItem= it;
    }
}
