package com.example.energy_trading.ui.personal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.energy_trading.R;
import com.example.energy_trading.bean.Trade_Item;

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

public class Personal_Information_detail extends FragmentActivity {
    @BindView(R.id.personal_information_img_back)
    ImageView personalInformationImgBack;
    @BindView(R.id.view_personal_information_1)
    View viewPersonalInformation1;
    @BindView(R.id.personal_information_username)
    TextView personalInformationUsername;
    @BindView(R.id.personal_information_tablelayout_)
    TableLayout personalInformationTablelayout;
    @BindView(R.id.personal_information_email)
    TextView personalInformationEmail;
    @BindView(R.id.personal_information_tablelayout_1)
    TableLayout personalInformationTablelayout1;
    @BindView(R.id.personal_information_telephone)
    TextView personalInformationTelephone;
    @BindView(R.id.personal_information_tablelayout_2)
    TableLayout personalInformationTablelayout2;
    @BindView(R.id.personal_information_address)
    TextView personalInformationAddress;
    @BindView(R.id.personal_information_tablelayout_3)
    TableLayout personalInformationTablelayout3;
    private Handler handler = null;
    private String username,email,telephone,address;
    private int user_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_personal_information);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        @SuppressLint("ResourceType") ViewGroup vg = findViewById(R.layout.sale_order_item_detail);
        personalInformationImgBack = this.findViewById(R.id.personal_information_img_back);
        personalInformationImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initDate();
        initView();
    }

    private void initDate() {
        Bundle bundle = getIntent().getExtras();
        Trade_Item tradeitem = new Trade_Item();
        tradeitem = (Trade_Item) bundle.getSerializable("tradeitem");
        String username = tradeitem.getUsername();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
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
                .url("http://192.168.138.48/energy_trade/personal_information.php")
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
                String trade_item_list = response.body().string();
                Log.i(TAG, "11111111111111111111111111111111111111111111 " + trade_item_list);
                try {
                    JSONObject json = new JSONObject(String.valueOf(trade_item_list));
                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id", json.getInt("user_id"));
                    bundle.putString("username", json.getString("username"));
                    bundle.putString("email", json.getString("email"));
                    bundle.putString("telephone", json.getString("telephone"));
                    bundle.putString("address", json.getString("address"));
                    Message msg = new Message();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Log.i(TAG, "2222222222222222222222222222222222222222222: " + tradeitem.getUsername());

    }
    private void modify_information(){
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",user_id );
            jsonObject.put("username", personalInformationUsername.getText().toString());
            jsonObject.put("email", personalInformationEmail.getText().toString());
            jsonObject.put("telephone", personalInformationTelephone.getText().toString());
            jsonObject.put("address", personalInformationAddress.getText().toString());
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
                .url("http://192.168.138.48/energy_trade/update_personal_information.php")
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
                            Toast.makeText(Personal_Information_detail.this,"Personal information modified successfully!",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Personal_Information_detail.this,"Personal information modification failure!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void initView() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                user_id=bundle.getInt("user_id");
                username = bundle.getString("username");
                email = bundle.getString("email");
                telephone = bundle.getString("telephone");
                address = bundle.getString("address");
                personalInformationUsername = Personal_Information_detail.this.findViewById(R.id.personal_information_username);
                personalInformationUsername.setText(username);
                personalInformationEmail = Personal_Information_detail.this.findViewById(R.id.personal_information_email);
                personalInformationEmail.setText(email);
                personalInformationTelephone = Personal_Information_detail.this.findViewById(R.id.personal_information_telephone);
                personalInformationTelephone.setText(telephone);
                personalInformationAddress = Personal_Information_detail.this.findViewById(R.id.personal_information_address);
                personalInformationAddress.setText(address);
            }
        };
    }
}
