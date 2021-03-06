package com.example.energy_trading.ui.sign;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.energy_trading.Fragment.Energy_trade_Fragment;
import com.example.energy_trading.R;
import com.example.energy_trading.Util.Log.Energy_Trade_Logger;
import com.example.energy_trading.app.AccountManager;
import com.example.energy_trading.app.IUserChecker;
import com.example.energy_trading.net.callback.ISuccess;
import com.example.energy_trading.net.rx.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SignUpFragment extends Energy_trade_Fragment {
    @BindView(R.id.edit_sign_up_username)
    EditText editSignUpUsername;
    @BindView(R.id.edit_sign_up_email_address)
    EditText editSignUpEmailAddress;
    @BindView(R.id.edit_sign_up_phone_number)
    EditText editSignUpPhoneNumber;
    @BindView(R.id.edit_sign_up_password)
    EditText editSignUpPassword;
    @BindView(R.id.edit_sign_up_repeat_password)
    EditText editSignUpRepeatPassword;
    @BindView(R.id.btn_sign_up)
    Button btnSignUp;
    @BindView(R.id.tv_link_sign_in)
    TextView tvLinkSignIn;
    int retCode;
    String s;

    private ISignListener mISignListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity; //??????????????????ISignListener???activity?????????????????????
        }
    }

    private boolean checkForm() {
        final String username = editSignUpUsername.getText().toString();
        final String email = editSignUpEmailAddress.getText().toString();
        final String phone = editSignUpPhoneNumber.getText().toString();
        final String password = editSignUpPassword.getText().toString();
        final String rePassword = editSignUpRepeatPassword.getText().toString();

        boolean isPass = true;

        if (username.isEmpty()) {
            editSignUpUsername.setError("Please enter your username");
            isPass = false;
        } else {
            editSignUpEmailAddress.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editSignUpEmailAddress.setError("Please enter your email address correctly");
            isPass = false;
        } else {
            editSignUpEmailAddress.setError(null);
        }

        if (phone.isEmpty()) {
            editSignUpPhoneNumber.setError("This phone number is not valid");
            isPass = false;
        } else {
            editSignUpPhoneNumber.setError(null);
        }

        if (password.isEmpty()) {
            editSignUpPassword.setError("Password is less than six characters");
            isPass = false;
        } else {
            editSignUpPassword.setError(null);
        }

        if (rePassword.isEmpty() || rePassword.length() < 6 || !(rePassword.equals(password))) {
            editSignUpRepeatPassword.setError("Password verification error");
            isPass = false;
        } else {
            editSignUpRepeatPassword.setError(null);
        }

        return isPass;
    }
    @Override
    public Object setLayout() {
        return R.layout.fragment_signup;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
    }

    @OnClick({R.id.btn_sign_up})
    public void onClickSignUp() {
        if (checkForm()) {
            //????????????????????????????????????
            new Thread() {
                @Override
                public void run() {
                    postJson();
                }
            }.start();
        }
    }
    @OnClick({R.id.tv_link_sign_in})
    public void onClickLink() {
        getSupportDelegate().start(new SignInFragment());
    }

    private void postJson() {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        //??????????????????????????????json???
        JSONObject jsonObject=new JSONObject();
        //JSONArray jsonArray=new JSONArray();
        try {

            jsonObject.put("username",editSignUpUsername.getText().toString());
            jsonObject.put("email", editSignUpEmailAddress.getText().toString());
            jsonObject.put("telephone", editSignUpPhoneNumber.getText().toString());
            jsonObject.put("password", editSignUpPassword.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json=jsonObject.toString();
        Log.i(TAG,json);
        //????????????OkHttpClient??????
        OkHttpClient okHttpClient = new OkHttpClient();
        //????????????RequestBody(??????1??????????????? ??????2?????????json???)
        RequestBody requestBody = RequestBody.create(JSON,json);
        //????????????????????????
        Request request = new Request.Builder()
                .url("http://192.168.138.48/energy_trade/Register.php")
                .post(requestBody)
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
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
                        //????????????????????????????????????
                        if (retCode == 1) {
                            //Intent intent= new Intent(getActivity(), MainActivity.class);
                            //startActivity(intent);
                            //AccountManager.setSignState(true);
                            Toast.makeText(getActivity(),"Register successfully!",Toast.LENGTH_SHORT).show();
                            mISignListener.onSignUpSuccess();
                            //AccountManager.setSignState(true);
                        }else if(retCode == 2) {
                            Toast.makeText(getActivity(),"Username already exists, please change to another username!",Toast.LENGTH_SHORT).show();
                        }else if(retCode == 3) {
                            Toast.makeText(getActivity(),"Phone number is already registered!",Toast.LENGTH_SHORT).show();
                        }else if(retCode == 4) {
                            Toast.makeText(getActivity(),"Email already registered!",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(),"Registration Failure!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }



}
