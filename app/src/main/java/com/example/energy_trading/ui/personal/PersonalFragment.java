package com.example.energy_trading.ui.personal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.energy_trading.Activity.ExampleActivity;
import com.example.energy_trading.R;
import com.example.energy_trading.bean.userevent;
import com.example.energy_trading.ui.personal.order.Bought_Fragment;
import com.example.energy_trading.ui.personal.order.Publish_Order_Fragment;
import com.example.energy_trading.ui.personal.order.Saled_Fragment;
import com.example.energy_trading.ui.personal.order.Update_Password;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;


public class PersonalFragment extends Fragment {
    String uemail, upassword;
    @BindView(R.id.img_user_avatar)
    CircleImageView imgUserAvatar;
    @BindView(R.id.tv_personal_username)
    TextView tvPersonalUsername;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.ll_receive)
    LinearLayout llReceive;
    @BindView(R.id.ll_evaluate)
    LinearLayout llEvaluate;
    @BindView(R.id.ll_pay)
    LinearLayout llPay;
    @BindView(R.id.img_update_personal_information)
    ImageView imgUpdatePersonalInformation;
    @BindView(R.id.img_update_password)
    ImageView imgUpdatePassword;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    private Publish_Order_Fragment publish_order_fragment;
    private FragmentTransaction transaction;
    private FragmentManager manager;
    private Handler handler = null;
    private String username = null;
    private Intent intent;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        tvPersonalUsername = view.findViewById(R.id.tv_personal_username);
        View llPay = view.findViewById(R.id.ll_pay);
        llPay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "111111111111111111111111111111111111111111", Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), Publish_Order_Fragment.class);
                startActivity(intent);
            }
        });
        View llReceive = view.findViewById(R.id.ll_receive);
        llReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "222222222222222222222222222222222222", Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), Saled_Fragment.class);
                startActivity(intent);
            }
        });
        View llEvaluate = view.findViewById(R.id.ll_evaluate);
        llEvaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "3333333333333333333333333333333333", Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), Bought_Fragment.class);
                startActivity(intent);
            }
        });
        EventBus.getDefault().register(this);
        get_username_json();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                username = bundle.getString("username");
                Log.i(TAG, "111111111111111111111111111111111111111111111111111111111111111111: " + username);
                tvPersonalUsername.setText(username);
            }
        };
        imgUpdatePersonalInformation = view.findViewById(R.id.img_update_personal_information);
        imgUpdatePersonalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("email", uemail);
                bundle.putString("password", upassword);
                intent.putExtras(bundle);
                intent.setClass(getActivity(), Update_Personal_Information.class);
                startActivity(intent);
            }
        });
        imgUpdatePassword = view.findViewById(R.id.img_update_password);
        imgUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("email", uemail);
                bundle.putString("password", upassword);
                intent.putExtras(bundle);
                intent.setClass(getActivity(), Update_Password.class);
                startActivity(intent);
            }
        });
        btnLogout=view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getActivity().getSharedPreferences("energy_trading", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("email");
                editor.remove("password");
                editor.apply();
                Intent intent = new Intent(getActivity(), ExampleActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Subscribe(sticky = true)  //必须使用EventBus的订阅注解
    public void onEventMainThread(userevent userevent) {
        uemail = userevent.getUseremail();
        upassword = userevent.getUserpassword();
    }

    private void get_username_json() {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", uemail);
            jsonObject.put("password", upassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jsonObject.toString();
        Log.i(TAG, json);
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url("http://192.168.138.48/energy_trade/personal.php")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String username_date = null;
                String jsondate = response.body().string();
                Log.i(TAG, "11111111111111111111111111111111111111111111111111111111111: " + jsondate);
                try {
                    JSONObject json = new JSONObject(String.valueOf(jsondate));
                    username_date = json.getString("username");
                    Log.i(TAG, "11111111111111111111111111111111111111111111111111111111111: " + username_date);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username_date);
                    Message msg = new Message();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
}
