package com.example.energy_trading.app;

import android.app.Activity;
import android.os.Handler;

import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import com.blankj.utilcode.util.Utils;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;

/**

 */

public class Configurator {

    private static final HashMap<Object, Object> ET_CONFIGS = new HashMap<>();
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();
    private static final Handler HANDLER = new Handler();

    private Configurator() {
        //.name() 是以字符串的形式输出出来 name()：返回实例名
        ET_CONFIGS.put(ConfigType.CONFIG_READY, false);
        ET_CONFIGS.put(ConfigType.HANDLER, HANDLER);
    }

    /**
     使用静态内部类方法保证单例模式
     *
     * @return
     */
    public static Configurator getInstance() {
        return Holder.INSTANCE;
    }

    final HashMap<Object, Object> getEgConfigs() {
        return ET_CONFIGS;
    }

    /**
     * 静态内部类单例模式的初始化
     */
    private static class Holder {
        private static final Configurator INSTANCE = new Configurator();
    }

    /**
     * 配置完成
     */

    public final void configure() {
        initIcons();
        Logger.addLogAdapter(new AndroidLogAdapter());
        ET_CONFIGS.put(ConfigType.CONFIG_READY, true);
        Utils.init(Energy_trading.getApplication());
    }



    public final Configurator withApiHost(String host) {
        ET_CONFIGS.put(ConfigType.API_HOST, host);
        return this;
    }
    /**
     * 对字体的初始化调用
     */
    private void initIcons() {
        if (ICONS.size() > 0) {
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
            for (int i = 1; i < ICONS.size(); i++) {
                initializer.with(ICONS.get(i));
            }
        }
    }

    public final Configurator withIcon(IconFontDescriptor descriptor) {//使用建造者模式设计配置项类，对配置项进行管理，通过链式调用方便使用
        ICONS.add(descriptor);
        return this;
    }
/*
传入拦截器，进行初始化
 */
    public final Configurator withInterceptor(Interceptor interceptor) {
        INTERCEPTORS.add(interceptor);
        ET_CONFIGS.put(ConfigType.INTERCEPTOR, INTERCEPTORS);
        return this;
    }

    public final Configurator withInterceptors(ArrayList<Interceptor> interceptors) {
        INTERCEPTORS.addAll(interceptors);
        ET_CONFIGS.put(ConfigType.INTERCEPTOR, INTERCEPTORS);
        return this;
    }

    public final Configurator withWeChatAppId(String appId) {
        ET_CONFIGS.put(ConfigType.WE_CHAT_APP_ID, appId);
        return this;
    }

    public final Configurator withWeChatAppSecret(String appSecret) {
        ET_CONFIGS.put(ConfigType.WE_CHAT_APP_SECRET, appSecret);
        return this;
    }

    public final Configurator withActivity(Activity activity) {
        ET_CONFIGS.put(ConfigType.ACTIVITY, activity);
        return this;
    }

    public final Configurator withJavaScriptInterface(String name) {
        ET_CONFIGS.put(ConfigType.JAVASCRIPT_INTERFACE, name);
        return this;
    }
/*
    public final Configurator withWebEvent(@NonNull String name, @NonNull Event event) {
        final EventManager manager = EventManager.getInstance();
        manager.addEvent(name, event);
        return this;
    }

 */

    public final Configurator withWebHost(String host) {
        ET_CONFIGS.put(ConfigType.WEB_HOST, host);
        return this;
    }

    private void checkConfiguration() {
        //
        final boolean isReady = (boolean) ET_CONFIGS.get(ConfigType.CONFIG_READY);
        //如果配置没有完成，抛出异常
        if (!isReady) {
            throw new RuntimeException("Configuration is not ready,call config");
        }
    }

    /**
     * @param key
     * @param <T>
     * @return 存储的是Object，通过泛型返回数据
     * 内部使用HashMap保存所有配置信息，key使用枚举类（单例），在多线程时保证安全
     */
    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key) { //获取配置信息
        checkConfiguration();//检查是否配置完毕
        Object value = ET_CONFIGS.get(key);
        if (value == null) {
            throw new NullPointerException(key.toString() + " IS NULL");
        }
        return (T) ET_CONFIGS.get(key);
    }
}
