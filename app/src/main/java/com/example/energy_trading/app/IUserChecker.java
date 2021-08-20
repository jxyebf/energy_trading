package com.example.energy_trading.app;

/**
 IUserChecker接口。
 主要作用：两个方法：登录、未登录
 */

public interface IUserChecker {

    void onSignIn();

    void onNotSignIn();
}
