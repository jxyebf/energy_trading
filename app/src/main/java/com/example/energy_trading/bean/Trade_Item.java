package com.example.energy_trading.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Trade_Item implements Parcelable, Serializable {
    private String id;
    private String username;
    private String number;
    private String unit_price;
    private String total_price;
    private String trading_date;
    private String trading_time;

    public Trade_Item() {
        super();
    }
    public Trade_Item(String id, String username, String number, String unit_price, String total_price, String trading_date,
                      String trading_time) {
        super();
        this.id = id;
        this.username = username;
        this.number = number;
        this.unit_price = unit_price;
        this.total_price = total_price;
        this.trading_date = trading_date;
        this.trading_time = trading_time;
    }
    public Trade_Item(String username, String number, String unit_price, String total_price, String trading_date,
                      String trading_time) {
        super();
        this.username = username;
        this.number = number;
        this.unit_price = unit_price;
        this.total_price = total_price;
        this.trading_date = trading_date;
        this.trading_time = trading_time;
    }

    protected Trade_Item(Parcel in) {
        id = in.readString();
        username = in.readString();
        number = in.readString();
        unit_price = in.readString();
        total_price = in.readString();
        trading_date = in.readString();
        trading_time = in.readString();
    }

    public static final Creator<Trade_Item> CREATOR = new Creator<Trade_Item>() {
        @Override
        public Trade_Item createFromParcel(Parcel in) {
            return new Trade_Item(in);
        }

        @Override
        public Trade_Item[] newArray(int size) {
            return new Trade_Item[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getTrading_date() {
        return trading_date;
    }

    public void setTrading_date(String trading_date) {
        this.trading_date = trading_date;
    }

    public String getTrading_time() {
        return trading_time;
    }

    public void setTrading_time(String trading_time) {
        this.trading_time = trading_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(username);
        parcel.writeString(number);
        parcel.writeString(unit_price);
        parcel.writeString(total_price);
        parcel.writeString(trading_date);
        parcel.writeString(trading_time);
    }
}
