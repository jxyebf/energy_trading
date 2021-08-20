package com.example.energy_trading.DataBase;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "push_message")
public class PushMessage {
    private long message_Id=0;
    private int notificationId=0;
    private String title = null;
    private String msgId = null;
    private String message = null;
    private String extra = null;
    private String alert = null;

    public PushMessage(int notificationId, String title, String msgId, String message, String extra, String alert) {
        this.notificationId = notificationId;
        this.title = title;
        this.msgId = msgId;
        this.message = message;
        this.extra = extra;
        this.alert = alert;
    }
    @Generated(hash = 641969249)
    public PushMessage(long message_Id, int notificationId, String title,
            String msgId, String message, String extra, String alert) {
        this.message_Id = message_Id;
        this.notificationId = notificationId;
        this.title = title;
        this.msgId = msgId;
        this.message = message;
        this.extra = extra;
        this.alert = alert;
    }
    @Generated(hash = 1468533071)
    public PushMessage() {
    }
    public long getMessage_Id() {
        return this.message_Id;
    }
    public void setMessage_Id(long message_Id) {
        this.message_Id = message_Id;
    }
    public int getNotificationId() {
        return this.notificationId;
    }
    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getMsgId() {
        return this.msgId;
    }
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getExtra() {
        return this.extra;
    }
    public void setExtra(String extra) {
        this.extra = extra;
    }
    public String getAlert() {
        return this.alert;
    }
    public void setAlert(String alert) {
        this.alert = alert;
    }

}
