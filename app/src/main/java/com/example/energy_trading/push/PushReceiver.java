package com.example.energy_trading.push;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.example.energy_trading.Activity.ExampleActivity;
import com.example.energy_trading.DataBase.DatabaseManager;
import com.example.energy_trading.MainActivity;
import com.example.energy_trading.R;
import com.example.energy_trading.Util.Log.Energy_Trade_Logger;
import com.example.energy_trading.bean.PushMessage;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.constraintlayout.widget.Constraints.TAG;

/**

 */

public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        final Set<String> keys = bundle.keySet();
        final JSONObject json = new JSONObject();
        for (String key : keys) {
            final Object val = bundle.get(key);
            json.put(key, val);
        }
        Energy_Trade_Logger.json("PushReceiver", json.toJSONString());
        final String pushAction = intent.getAction();
        if (pushAction.equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
            //处理接收到的消息
            onReceivedMessage(bundle);
        } else if (pushAction.equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
            //打开相应的Notification
            onOpenNotification(context, bundle);
        }
    }
    private void onReceivedMessage(Bundle bundle) {
        final String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        final String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        final int notification = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        final String notificationId=String.valueOf(notification);
        final String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        final String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        final String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        org.json.JSONObject jsonObject=new org.json.JSONObject();
        try {
            jsonObject.put("notificationId", notificationId);
            jsonObject.put("title", title);
            jsonObject.put("msgId", msgId);
            jsonObject.put("message", message);
            jsonObject.put("extra", extra);
            jsonObject.put("alert", alert);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json=jsonObject.toString();
        Log.i(TAG,json);
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON,json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url("http://192.168.138.48/energy_trade/insert_message.php")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                    Log.i(TAG, "onFailure: " + e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int retCode = 0;
                try {
                    String jsondate = response.body().string();
                    org.json.JSONObject json = new org.json.JSONObject(String.valueOf(jsondate));
                    retCode = json.getInt("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //客户端自己判断是否成功。
                if (retCode == 1) {
                            //AccountManager.setSignState(true);
                            //mISignListener.onSignInSuccess();
                    Log.i(TAG, "onSuccess: ");
                } else {
                    Log.i(TAG, "onFailure: ");
                }
            }
//            final PushMessage pushMessage = new PushMessage(notificationId, title, msgId, message, extra, alert);
//        DatabaseManager.getInstance().getDao().insert(pushMessage);
//        List list=DatabaseManager.getInstance().getDao().queryBuilder().list();
//        Log.i(TAG, "123123123123123123123123123123123123123123123123123: " + list);
        });
    }
    private void onOpenNotification(Context context, Bundle bundle) {
        final String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        final Bundle openActivityBundle = new Bundle();
        final Intent intent = new Intent(context, ExampleActivity.class);
        intent.putExtras(openActivityBundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ContextCompat.startActivity(context, intent, null);
    }
}
