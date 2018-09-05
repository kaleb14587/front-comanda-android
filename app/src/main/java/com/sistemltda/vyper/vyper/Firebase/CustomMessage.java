package com.sistemltda.vyper.vyper.Firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.sistemltda.vyper.vyper.AppVyper.VyperApp;
import com.sistemltda.vyper.vyper.Models.DAO.ItemDao;
import com.sistemltda.vyper.vyper.Models.SQLite.Item;
import com.sistemltda.vyper.vyper.VyperServerAPI.Retornos.Notification;

public class CustomMessage extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getNotification()!=null)
        {
            Notification noti =   (new Gson()).fromJson(remoteMessage.getNotification().getBody(), Notification.class);
            if(noti!=null)
            trataNotify(noti);
        }
        return ;
    }

    private void trataNotify(Notification noti) {

        if(noti.getType().equals("item")){
            VyperApp app = (VyperApp)getApplication();
            ItemDao dao = app.getVyperDatabase().daoItem();
            Item it = noti.getData();
//            it.setId(null);
            dao.insert(noti.getData());

        }
    }
}
