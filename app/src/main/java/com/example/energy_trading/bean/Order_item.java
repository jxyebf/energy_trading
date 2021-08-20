package com.example.energy_trading.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Order_item implements Parcelable {
    private String order_id;
    private String order_pay_time;
    private String order_status;
    private String buyer_id;
    private String order_number;
    private String order_total_price;
    private String order_trading_date;
    private String order_trading_time;
    private String seller_username;

    public Order_item() {
        super();
    }

    public Order_item(String order_id, String order_pay_time, String order_status, String buyer_id, String order_number, String order_total_price, String order_trading_date, String order_trading_time, String seller_username) {
        this.order_id = order_id;
        this.order_pay_time = order_pay_time;
        this.order_status = order_status;
        this.buyer_id = buyer_id;
        this.order_number = order_number;
        this.order_total_price = order_total_price;
        this.order_trading_date = order_trading_date;
        this.order_trading_time = order_trading_time;
        this.seller_username = seller_username;
    }

    public Order_item(String order_pay_time, String order_status, String buyer_id, String order_number, String order_total_price, String order_trading_date, String order_trading_time, String seller_username) {
        this.order_pay_time = order_pay_time;
        this.order_status = order_status;
        this.buyer_id = buyer_id;
        this.order_number = order_number;
        this.order_total_price = order_total_price;
        this.order_trading_date = order_trading_date;
        this.order_trading_time = order_trading_time;
        this.seller_username = seller_username;
    }

    protected Order_item(Parcel in) {
        order_pay_time = in.readString();
        order_status = in.readString();
        buyer_id = in.readString();
        order_number = in.readString();
        order_total_price = in.readString();
        order_trading_date = in.readString();
        order_trading_time = in.readString();
        seller_username = in.readString();
    }

    public static final Creator<Order_item> CREATOR = new Creator<Order_item>() {
        @Override
        public Order_item createFromParcel(Parcel in) {
            return new Order_item(in);
        }

        @Override
        public Order_item[] newArray(int size) {
            return new Order_item[size];
        }
    };

    public String getOrder_pay_time() {
        return order_pay_time;
    }

    public void setOrder_pay_time(String order_pay_time) {
        this.order_pay_time = order_pay_time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_total_price() {
        return order_total_price;
    }

    public void setOrder_total_price(String order_total_price) {
        this.order_total_price = order_total_price;
    }

    public String getOrder_trading_date() {
        return this.order_trading_date;
    }

    public void setOrder_trading_date(String order_trading_date) {
        this.order_trading_date = order_trading_date;
    }

    public String getOrder_trading_time() {
        return this.order_trading_time;
    }

    public void setOrder_trading_time(String order_trading_time) {
        this.order_trading_time = order_trading_time;
    }

    public String getSeller_username() {
        return seller_username;
    }

    public void setSeller_username(String seller_username) {
        this.seller_username = seller_username;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(order_id);
        parcel.writeString(order_pay_time);
        parcel.writeString(order_status);
        parcel.writeString(buyer_id);
        parcel.writeString(order_number);
        parcel.writeString(order_total_price);
        parcel.writeString(order_trading_date);
        parcel.writeString(order_trading_time);
        parcel.writeString(seller_username);
    }
}
