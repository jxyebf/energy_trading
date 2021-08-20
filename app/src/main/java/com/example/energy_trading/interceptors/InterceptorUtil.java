package com.example.energy_trading.interceptors;


import com.example.energy_trading.Util.Log.Energy_Trade_Logger;

import okhttp3.logging.HttpLoggingInterceptor;


public class InterceptorUtil {
    private static String TAG = "RetrofitLog";

    //日志拦截器
    public static HttpLoggingInterceptor LoggingInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Energy_Trade_Logger.i(TAG, message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}
