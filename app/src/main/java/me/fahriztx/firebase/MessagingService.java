package me.fahriztx.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        Log.d("REG_TOKEN", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0){

            Map<String, String> data =  remoteMessage.getData();

            if(data.containsKey("sender") && data.containsKey("message")){
                String sender = data.get("sender");
                String message = data.get("message");

                Log.d("NOTIF_SENDER", sender);
                Log.d("NOTIF_MESSAGE", message);
            }

            if(data.containsKey("title") && data.containsKey("content")){
                String title = data.get("title");
                String content = data.get("content");

                Log.d("NOTIF_TITLE", title);
                Log.d("NOTIF_CONTENT", content);
            }

        }
    }
}
