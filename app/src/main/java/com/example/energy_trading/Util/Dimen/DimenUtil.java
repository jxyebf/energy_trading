package com.example.energy_trading.Util.Dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.energy_trading.app.Energy_trading;


/**

 */

public class DimenUtil {

    public static int getScreenWidth() {
        final Resources resources = Energy_trading.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = Energy_trading.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
