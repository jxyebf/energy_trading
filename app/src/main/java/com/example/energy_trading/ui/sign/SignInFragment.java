package com.example.energy_trading.ui.sign;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.energy_trading.Activity.ExampleActivity;
import com.example.energy_trading.Fragment.Energy_trade_Fragment;
import com.example.energy_trading.MainActivity;
import com.example.energy_trading.R;
import com.example.energy_trading.Util.MD5Utils;
import com.example.energy_trading.app.AccountManager;
import com.example.energy_trading.app.IUserChecker;
import com.example.energy_trading.bean.userevent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class SignInFragment extends Energy_trade_Fragment{

    @BindView(R.id.edit_sign_in_email)
    EditText editSignInEmail;
    @BindView(R.id.edit_sign_in_password)
    EditText editSignInPassword;
    @BindView(R.id.btn_sign_in)
    Button btnSignIn;
    @BindView(R.id.tv_link_sign_up)
    TextView tvLinkSignUp;
    private ISignListener mISignListener = null;
    private SharedPreferences sharedPreferences;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity;
        }
    }
    @Override
    public Object setLayout() {
        return R.layout.fragment_sign_in;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
    }
    @OnClick({R.id.btn_sign_in})
    public void onClickSignUp() {
        if (checkForm()) {
            //开启一个线程，做联网操作
            new Thread() {
                @Override
                public void run() {
                    sharedPreferences = getActivity().getSharedPreferences("energy_trading", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String getemail = editSignInEmail.getText().toString();
                    String getpassword = editSignInPassword.getText().toString();
                    editor.putString("email", getemail);
                    editor.putString("password", getpassword);
                    editor.commit();
                    String email = sharedPreferences.getString("email", "");
                    String password = sharedPreferences.getString("password", "");
                    final userevent ue = new userevent();
                    ue.setUseremail(email);
                    ue.setUserpassword(password);
                    Log.d("输入框获取的密码", "11111111111111111111111111: " + getemail);
                    Log.d("输入框获取的密码", "222222222222222222222222: " + getpassword);
                    EventBus.getDefault().postSticky(ue);
                    postJson();
                    //Looper.prepare();
                    //Toast.makeText(getActivity(), ue.getUseremail(), Toast.LENGTH_SHORT).show();
                    postJson();
                    //Looper.loop();
                }
            }.start();
        }
    }
    private void saveaccount(){
        sharedPreferences = getActivity().getSharedPreferences("energy_trading", MODE_PRIVATE);
        if(sharedPreferences.getString("id", null) == null){

        }else{
            String getemail = sharedPreferences.getString("username", "");
            String getpassword = sharedPreferences.getString("password", "");
            Log.d("输入框获取的密码", "11111111111111111111111111: " + getemail);
            Log.d("输入框获取的密码", "222222222222222222222222: " + getpassword);
            final userevent ue = new userevent();
            ue.setUseremail(getemail);
            ue.setUserpassword(getpassword);
            EventBus.getDefault().postSticky(ue);
            postJson();
        }
    }
    @OnClick({R.id.tv_link_sign_up})
    public void onClickLink() {
        start(new SignUpFragment());
    }
    private boolean checkForm() {
        final String email = editSignInEmail.getText().toString();
        final String password = editSignInPassword.getText().toString();

        boolean isPass = true;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editSignInEmail.setError("Please enter your email address correctly");
            isPass = false;
        } else {
            editSignInEmail.setError(null);
        }

        if (password.isEmpty()) {
            editSignInPassword.setError("Password is less than six characters");
            isPass = false;
        } else {
            editSignInPassword.setError(null);
        }
        return isPass;
    }

    public static String JSONTokener(String str_json) {
        // consume an optional byte order mark (BOM) if it exists
        if (str_json != null && str_json.startsWith("\ufeff")) {
            String istr_json = str_json.substring(1);
        }
        return str_json;
    }
    private void postJson() {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        //申明给服务端传递一个json串
        JSONObject jsonObject=new JSONObject();
        //JSONArray jsonArray=new JSONArray();
        try {
            jsonObject.put("email", editSignInEmail.getText().toString());
            jsonObject.put("password", editSignInPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json=jsonObject.toString();
        Log.i(TAG,json);
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON,json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url("http://192.168.138.48/energy_trade/Login.php")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int retCode = 0;
                        try {
                            String jsondate = response.body().string();
                            JSONObject json = new JSONObject(String.valueOf(jsondate));
                            retCode = json.getInt("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //客户端自己判断是否成功。
                        if (retCode == 1) {
                            //AccountManager.setSignState(true);
                            mISignListener.onSignInSuccess();
                            //AccountManager.setSignState(true);
                        } else {
                            Toast.makeText(getActivity(),"Login Error!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}
