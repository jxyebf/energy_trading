package com.example.energy_trading.Util;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Message;

import com.example.energy_trading.bean.Trade_Item;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Handler;

import org.json.JSONObject;

public class HttpUtils {
    public static void getNewsJSON(final String url, final Handler handler){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn;
                InputStream is;
                try {
                    conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("GET");
                    is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line = "";
                    StringBuilder result = new StringBuilder();
                    while ( (line = reader.readLine()) != null ){
                        result.append(line);
                    }
                    Message msg = new Message();
                    msg.obj = result.toString();
                    //handler.sendMessage(msg);  //把接收到的信息通知主线程（传入进来的Handle线程）
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //需要传入的参数Url及主线程的Handle，因为线程在运行的到数据后，需要通过handle通知主线程
    public static ArrayList<Trade_Item> parseJSONOrgin(String json) {
        ArrayList<Trade_Item> list = new ArrayList<Trade_Item>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Trade_Item tradeItem = new Trade_Item();
                tradeItem.setUsername(jsonObject.getString("username"));
                tradeItem.setNumber(jsonObject.getString("number"));
                tradeItem.setUnit_price(jsonObject.getString("unit_price"));
                tradeItem.setTotal_price(jsonObject.getString("total_price"));
                tradeItem.setTrading_date(jsonObject.getString("trading_date"));
                tradeItem.setTrading_time(jsonObject.getString("trading_time"));
                list.add(tradeItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
