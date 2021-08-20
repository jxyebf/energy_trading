package com.example.energy_trading.ui.personal.order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.energy_trading.Activity.ExampleActivity;
import com.example.energy_trading.R;
import com.example.energy_trading.ui.sign.SignInFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.energy_trading.ui.camera.Energy_trade_Camera.start;

public class Update_Password extends FragmentActivity {
    @BindView(R.id.tv_update_password_cancel)
    TextView tvUpdatePasswordCancel;
    @BindView(R.id.btn_update_password_confirm)
    Button btnUpdatePasswordConfirm;
    @BindView(R.id.tv_old_password)
    TextView tvOldPassword;
    @BindView(R.id.et_old_password)
    EditText etOldPassword;
    @BindView(R.id.tv_new_password)
    TextView tvNewPassword;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.tv_repeat_new_password)
    TextView tvRepeatNewPassword;
    @BindView(R.id.et_repeat_new_password)
    EditText etRepeatNewPassword;
    private SharedPreferences sharedPreferences;

    private String oldpassword,newpassword,repeatnewpassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_update_password);
        ButterKnife.bind(this);
        btnUpdatePasswordConfirm=this.findViewById(R.id.btn_update_password_confirm);
        etOldPassword=this.findViewById(R.id.et_old_password);
        etNewPassword=this.findViewById(R.id.et_new_password);
        etRepeatNewPassword=this.findViewById(R.id.et_repeat_new_password);
        newpassword = etNewPassword.getText().toString();
        repeatnewpassword = etRepeatNewPassword.getText().toString();
        tvUpdatePasswordCancel=this.findViewById(R.id.tv_update_password_cancel);
        tvUpdatePasswordCancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnUpdatePasswordConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForm()) {
                    //开启一个线程，做联网操作
                    new Thread() {
                        @Override
                        public void run() {
                            Bundle bundle = getIntent().getExtras();
                            String user_password = bundle.getString("password");
                            String user_email = bundle.getString("email");
                            final   String oldpassword = etOldPassword.getText().toString();
                            final String repeatnewpassword = etRepeatNewPassword.getText().toString();
                            Log.i(TAG, "11111111111111111111111111111111111111111111111111111111111: " + user_password);
                            Log.i(TAG, "11111111111111111111111111111111111111111111111111111111111: " + user_email);
                            Log.i(TAG, "11111111111111111111111111111111111111111111111111111111111: " + oldpassword);
                            Log.i(TAG, "11111111111111111111111111111111111111111111111111111111111: " + repeatnewpassword);
                            if (user_password.equals(oldpassword)) {
                                sharedPreferences = getSharedPreferences("energy_trading", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("email");
                                editor.remove("password");
                                editor.apply();
                                update_password(user_email,repeatnewpassword);
                            }else {
                                Looper.prepare();
                                Toast.makeText(Update_Password.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    }.start();
                }
            }
        });
    }
    private boolean checkForm() {
        final String oldpassword = etOldPassword.getText().toString();
        final String newpassword = etNewPassword.getText().toString();
        final String repeatnewpassword = etRepeatNewPassword.getText().toString();
        boolean isPass = true;
        if (newpassword.isEmpty() || repeatnewpassword.isEmpty()||newpassword.length() < 6 ||repeatnewpassword.length() < 6) {
            Toast.makeText(Update_Password.this, "Password is less than six characters", Toast.LENGTH_SHORT).show();
            isPass = false;
        }else{
            if (!(newpassword.equals(repeatnewpassword))) {
                Toast.makeText(Update_Password.this, "The password entered twice does not match", Toast.LENGTH_SHORT).show();
                isPass = false;
            }
        }
            return isPass;
    }
    private void update_password(String email, String password){
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        //申明给服务端传递一个json串
        JSONObject jsonObject=new JSONObject();
        //JSONArray jsonArray=new JSONArray();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
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
                .url("http://192.168.138.48/energy_trade/update_password.php")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
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
                            Toast.makeText(Update_Password.this,"Password change successfully, please log in again!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Update_Password.this, ExampleActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Update_Password.this,"Password change failure!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
