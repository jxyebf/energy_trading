package com.example.energy_trading.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;


import com.example.energy_trading.Fragment.Energy_trade_Fragment;
import com.example.energy_trading.MainActivity;
import com.example.energy_trading.R;
import com.example.energy_trading.app.AccountManager;
import com.example.energy_trading.app.ILauncherListener;
import com.example.energy_trading.app.IUserChecker;
import com.example.energy_trading.app.OnSignInTag;
import com.example.energy_trading.bean.userevent;
import com.example.energy_trading.ui.sign.ISignListener;
import com.example.energy_trading.ui.sign.SignInFragment;
import com.example.energy_trading.ui.sign.SignUpFragment;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;
import qiu.niorgai.StatusBarCompat;

/*
隐藏ActionBar，传入的Fragment视图，完成一些需要在Activity中进行初始化的功能
 */
public class ExampleActivity extends ProxyActivity implements ISignListener {
    private ILauncherListener mILauncherListener = null;
    private SharedPreferences sharedPreferences;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {//隐藏的是搜索地方的toolbar
            actionBar.hide();
        }
        //P2P.getConfigurator().withActivity(this);
        StatusBarCompat.translucentStatusBar(this, false);//隐藏toolbar上面的状态栏：true：隐藏
        sharedPreferences = getSharedPreferences("energy_trading", MODE_PRIVATE);

            Log.d("输入框获取的密码", "123456789888888888888888888888888888888888888888888888888888888888801234567890: ");

            String getemail = sharedPreferences.getString("email", "");
            String getpassword = sharedPreferences.getString("password", "");
        if(getemail!="" && getpassword!=""){
            Log.d("输入框获取的密码", "12121212121212121212121212: " + getemail);
            Log.d("输入框获取的密码", "121212121212121212121212112: " + getpassword);
            final userevent ue = new userevent();
            ue.setUseremail(getemail);
            ue.setUserpassword(getpassword);
            EventBus.getDefault().postSticky(ue);
            startActivity(new Intent(ExampleActivity.this, MainActivity.class));
        }
        //StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.app_main));
        //StatusBarUtil.setColor(this, getResources().getColor(R.color.app_main));
        //checkSignIn();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public Energy_trade_Fragment setRootDelegate() {
        return new SignInFragment();
    }
    @Override
    public void onSignInSuccess() {
        Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show();
        startActivity(new Intent(ExampleActivity.this, MainActivity.class));
    }

    @Override
    public void onSignUpSuccess() {
        getSupportDelegate().startWithPop(new SignInFragment());
    }

//    public void onLauncherFinish(OnSignInTag tag) {
//        switch (tag) {
//            case SIGNED:
//                //Toast.makeText(this, "启动结束，用户登录了", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(ExampleActivity.this, MainActivity.class));
//                break;
//            case NOT_SIGNED:
//                //Toast.makeText(this, "启动结束，用户没登录", Toast.LENGTH_LONG).show();
//                getSupportDelegate().startWithPop(new SignInFragment());
//                break;
//            default:
//                break;
//        }
//    }
//    private void checkSignIn(){
////检查用户是否登录APP
//        AccountManager.checkAccount(new IUserChecker() {
//            @Override
//            public void onSignIn() {
//                if (mILauncherListener != null) {
//                    mILauncherListener.onLauncherFinish(OnSignInTag.SIGNED);
//                }
//            }
//
//            @Override
//            public void onNotSignIn() {
//                if (mILauncherListener != null) {
//                    mILauncherListener.onLauncherFinish(OnSignInTag.NOT_SIGNED);
//                }
//            }
//        });
//    }


}
