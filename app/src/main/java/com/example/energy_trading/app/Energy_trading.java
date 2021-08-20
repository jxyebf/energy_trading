package com.example.energy_trading.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 */

public final class Energy_trading {

    /**
     * 返回配置
     *
     * @param context
     */
    public static Configurator init(Context context) { //初始化配置项，入口

        getConfigurator().getEgConfigs().put(ConfigType.APPLICATION_CONTEXT, context.getApplicationContext());
        getConfigurator().getEgConfigs().put(ConfigType.APPLICATION, context);
        return Configurator.getInstance();
    }

    public static Configurator getConfigurator() {//获取Configurator类的对象
        return Configurator.getInstance();
    }

    public static <T> T getConfiguration(Object key) {//获取配置信息
        return getConfigurator().getConfiguration(key);
    }

    public static Context getApplicationContext() {
        return getConfiguration(ConfigType.APPLICATION_CONTEXT);
    }

    public static Application getApplication() {
        return getConfiguration(ConfigType.APPLICATION);
    }

    public static Handler getHandler() {
        return getConfiguration(ConfigType.HANDLER);
    }
}
