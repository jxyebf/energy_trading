package com.example.energy_trading.interceptors;

import com.example.energy_trading.Util.Log.Energy_Trade_Logger;
import com.example.energy_trading.Util.storage.Energy_tradingPreference;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 */

public final class AddCookieInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request.Builder builder = chain.request().newBuilder();
        //通过just( )方式 直接触发onNext()，just中传递的参数将直接在Observer的onNext()方法中接收到
        Observable
                .just(Energy_tradingPreference.getCustomAppProfile("cookie"))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String cookie) throws Exception {
                        Energy_Trade_Logger.d("AddCookieInterceptor", "cookie---》" +cookie);
                        //给原生API请求附带上WebView拦截下来的Cookie
                        builder.addHeader("Cookie", cookie);
                    }
                });
        return chain.proceed(builder.build());
    }
}
