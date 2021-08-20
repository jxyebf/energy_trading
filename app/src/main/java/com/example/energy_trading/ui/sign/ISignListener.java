package com.example.energy_trading.ui.sign;

/**
 ISignListener接口。

 主要作用：两个方法：登录成功、注册成功
 */

public interface ISignListener {
    //登录成功
    void onSignInSuccess();

    //注册成功
    void onSignUpSuccess();
}
