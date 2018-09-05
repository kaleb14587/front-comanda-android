package com.sistemltda.vyper.vyper.Firebase;


import android.arch.persistence.room.Database;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.sistemltda.vyper.vyper.AppVyper.VyperApp;
import com.sistemltda.vyper.vyper.Models.DAO.InfoDao;
import com.sistemltda.vyper.vyper.Models.SQLite.Info;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private InfoDao vyperAppDB;
    @Override
    public void onTokenRefresh() {
        //For registration of token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        vyperAppDB = (InfoDao) ((VyperApp) getApplication()).getVyperDatabase().daoAcess();
        Info inf  = vyperAppDB.selectInfo();
        inf.setRegistrationId(refreshedToken);
        //To displaying token on logcat
        Log.d("TOKEN: ", refreshedToken);

    }
}
