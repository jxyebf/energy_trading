package com.example.energy_trading.Fragment;



public abstract class Energy_trade_Fragment extends PermissionCheckerFragment {
/*
对外使用的Fragment  拥有BaseDelegate和PermissionCheckerDelegate类的所有功能；包含获取父类Fragment的功能
 */
    @SuppressWarnings("unchecked")
    public <T extends Energy_trade_Fragment> T getParentDelegate() {
        return (T) getParentFragment();
    }
}
