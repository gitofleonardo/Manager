package com.example.leo.manager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.List;

public class SendAMsg {
    private String number;
    private String content;
    private Intent sendIntent=new Intent("SENT_SMS_ACTION");
    private Intent deliveredIntent=new Intent("DELIVERED_SMS_ACTION");
    private PendingIntent sent_pd;
    private PendingIntent deliver_pd;
    public SendAMsg(String number, String content, Context context){
        this.number=number;
        this.content=content;
        sent_pd=PendingIntent.getBroadcast(context,0,sendIntent,0);
        deliver_pd=PendingIntent.getBroadcast(context,0,deliveredIntent,0);
    }
    public void send(){
        android.telephony.SmsManager manager=android.telephony.SmsManager.getDefault();
        List<String> list=manager.divideMessage(content);
        for (String text:list){
            manager.sendTextMessage(number,null,text,sent_pd,deliver_pd);
        }
    }
}
