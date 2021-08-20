package com.example.energy_trading.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PushMessage implements Parcelable {
    private int message_Id;
    private String notificationId;
    private String title;
    private String msgId;
    private String message;
    private String extra;
    private String alert;
    public PushMessage() {
        super();
    }
    public PushMessage(int message_Id, String notificationId, String title, String msgId, String message, String extra, String alert) {
        this.message_Id = message_Id;
        this.notificationId = notificationId;
        this.title = title;
        this.msgId = msgId;
        this.message = message;
        this.extra = extra;
        this.alert = alert;
    }

    public PushMessage(String notificationId, String title, String msgId, String message, String extra, String alert) {
        this.notificationId = notificationId;
        this.title = title;
        this.msgId = msgId;
        this.message = message;
        this.extra = extra;
        this.alert = alert;
    }

    protected PushMessage(Parcel in) {
        message_Id = in.readInt();
        notificationId = in.readString();
        title = in.readString();
        msgId = in.readString();
        message = in.readString();
        extra = in.readString();
        alert = in.readString();
    }

    public static final Creator<PushMessage> CREATOR = new Creator<PushMessage>() {
        @Override
        public PushMessage createFromParcel(Parcel in) {
            return new PushMessage(in);
        }

        @Override
        public PushMessage[] newArray(int size) {
            return new PushMessage[size];
        }
    };

    public int getMessage_Id() {
        return message_Id;
    }

    public void setMessage_Id(int message_Id) {
        this.message_Id = message_Id;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(message_Id);
        parcel.writeString(notificationId);
        parcel.writeString(title);
        parcel.writeString(msgId);
        parcel.writeString(message);
        parcel.writeString(extra);
        parcel.writeString(alert);
    }
}
