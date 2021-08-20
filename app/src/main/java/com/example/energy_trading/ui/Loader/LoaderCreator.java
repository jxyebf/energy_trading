package com.example.energy_trading.ui.Loader;

import android.content.Context;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.WeakHashMap;

/**
 * 考虑网络较差情况下的网络加载，这时候会在UI上出现一个Loader；
 *
 * 在retrofit的回调中进行loader的删除。
 内部通过反射获取View，官方每次请求都反射一次，这里使用WeakHashmap进行缓存
 * 以一种缓存的方式创建loader，就不需要每次使用loader时就反射一次，提高性能
 * 创建出Loader的View
 */

public final class LoaderCreator {

    private static final WeakHashMap<String, Indicator> LOADING_MAP = new WeakHashMap<>();

    static AVLoadingIndicatorView create(String type, Context context) {
        final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        if (LOADING_MAP.get(type) == null) {
            final Indicator indicator = getIndicator(type);
            LOADING_MAP.put(type, indicator);
        }
        avLoadingIndicatorView.setIndicator(LOADING_MAP.get(type));
        return avLoadingIndicatorView;
    }

    private static Indicator getIndicator(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        //通过反射获取包名如：com.wang.avi.indicators.BallPulseIndicator
        final StringBuilder drawableClassName = new StringBuilder();
        //说明传入的是类名
        if (!name.equals(".")) {
            final String defaultPackageName = AVLoadingIndicatorView.class.getPackage().getName();  //反射获取包名
            drawableClassName.append(defaultPackageName)
                    .append(".indicators")
                    .append(".");
        }
        drawableClassName.append(name);  //生成具体View
        try {
            final Class<?> drawableClass = Class.forName(drawableClassName.toString());
            return (Indicator) drawableClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
