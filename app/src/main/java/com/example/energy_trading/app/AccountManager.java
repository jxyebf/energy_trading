package com.example.energy_trading.app;


import com.example.energy_trading.Util.storage.Energy_tradingPreference;

/**
 管理用户信息，内部通过sharedPreference保存登陆状态，检查用户状态--回调IUserChecker接口中方法
 */

public class AccountManager {

    private enum SignTag {
        SIGN_TAG
    }

    /**
     //保存用户登录状态，登录后调用
     *
     */
    public static void setSignState(boolean state) {
        Energy_tradingPreference.setAppFlag(SignTag.SIGN_TAG.name(), state);
    }

    public static boolean isSignIn() {
        return Energy_tradingPreference.getAppFlag(SignTag.SIGN_TAG.name());
    }

    public static void checkAccount(IUserChecker checker) {  //检查登陆状态
        if (isSignIn()) {
            checker.onSignIn();
        } else {
            checker.onNotSignIn();
        }
    }
}
