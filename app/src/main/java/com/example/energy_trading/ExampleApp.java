package com.example.energy_trading;

import android.app.Application;

import androidx.annotation.NonNull;


import com.example.energy_trading.DataBase.DatabaseManager;
import com.example.energy_trading.app.Energy_trading;
import com.example.energy_trading.icon.FontEcModule;
import com.example.energy_trading.interceptors.AddCookieInterceptor;
import com.example.energy_trading.interceptors.InterceptorUtil;
import com.joanzapata.iconify.fonts.FontAwesomeModule;


import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 */

public class ExampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String baseUrl="http://192.168.138.48:80/RestServer/api/";
        Energy_trading.init(this)
                .withIcon(new FontAwesomeModule())
                .withIcon(new FontEcModule())
                .withApiHost(baseUrl)
                //.withInterceptor(new DebugInterceptor("index", R.raw.test))
                .withInterceptor(InterceptorUtil.LoggingInterceptor())
                .withJavaScriptInterface("latte")
                //.withWebEvent("test", new TestEvent())
                //.withWebEvent("share", new shareEvent())
                //添加Cookie拦截器
                .withWebHost("http://www.baidu.com/")
                //.withInterceptor(new DebugInterceptor("test", R.anim))
                //.withWeChatAppId("")
                //.withWeChatAppSecret("")
                .withInterceptor(new AddCookieInterceptor())
                .configure();
               // initStetho();
        DatabaseManager.getInstance().init(this); //初始化数据库
        //开启极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //建议添加tag标签，发送消息的之后就可以指定tag标签来发送了
        Set<String> set = new HashSet<>();
        set.add("andfixdemo");//名字任意，可多添加几个
        JPushInterface.setTags(this, set, null);//设置标签
/*
        CallbackManager.getInstance()
                .addCallback(CallbackType.TAG_OPEN_PUSH, new IGlobalCallback() {
                    @Override
                    public void executeCallback(@NonNull Object args) {
                        if (JPushInterface.isPushStopped(P2P.getApplicationContext())) {
                            //开启极光推送
                            JPushInterface.setDebugMode(true);
                            JPushInterface.init(P2P.getApplicationContext());
                        }
                    }
                })
                .addCallback(CallbackType.TAG_STOP_PUSH, new IGlobalCallback() {
                    @Override
                    public void executeCallback(@NonNull Object args) {
                        if (!JPushInterface.isPushStopped(P2P.getApplicationContext())) {
                            JPushInterface.stopPush(P2P.getApplicationContext());
                        }
                    }
                });

 */
    }
/*
    HttpLoggingInterceptor mLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            Energy_Trade_Logger.i("RetrofitLog", message);
        }
    }).setLevel(HttpLoggingInterceptor.Level.BODY);


    private void initStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

 */
}
