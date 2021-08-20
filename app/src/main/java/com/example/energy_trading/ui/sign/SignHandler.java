package com.example.energy_trading.ui.sign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.energy_trading.DataBase.DatabaseManager;
import com.example.energy_trading.DataBase.UserProfile;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 通过登录、注册成功后，获取到已经准备好的json数据，读取json数据保存到数据库中；并设置登录、注册状态以及成功的回调。
 */

public class SignHandler {

    public static void onSignUp(String response, ISignListener signListener) {
        final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        final JSONObject profileJson=new JSONObject();
        final long userId = profileJson.getLong("userId");
        final String username = profileJson.getString("username");
        final String email = profileJson.getString("email");
        final String telephone = profileJson.getString("telephone");
        final String password = profileJson.getString("password");
        String json=profileJson.toString();
        RequestBody requestBody = RequestBody.create(JSON,json);
        //final UserProfile profile = new UserProfile(userId, username, email,telephone, password);//从JSON中获取数据，插入数据库
        //DatabaseManager.getInstance().getDao().insert(profile);//将数据插入数据库
        //已经注册并且登录成功
        //AccountManager.setSignState(true);
        signListener.onSignUpSuccess();
    }

    public static void onSignIn(String response, ISignListener signListener) {
        final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");
        final int success = profileJson.getInteger("success");
        /*
        final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        final JSONObject profileJson=new JSONObject();
        final long userId = profileJson.getLong("userId");
        final String username = profileJson.getString("username");
        final String email = profileJson.getString("email");
        final String telephone = profileJson.getString("telephone");
        final String password = profileJson.getString("password");
        String json=profileJson.toString();
        RequestBody requestBody = RequestBody.create(JSON,json);
        //final UserProfile profile = new UserProfile(userId, username, email,telephone, password);
        //DatabaseManager.getInstance().getDao().insertOrReplace(profile);
        //已经注册并且登录成功
        //AccountManager.setSignState(true);*/
        signListener.onSignInSuccess();

    }
}
