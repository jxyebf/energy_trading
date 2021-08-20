package com.example.energy_trading.ui.personal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.energy_trading.Activity.ExampleActivity;
import com.example.energy_trading.R;
import com.example.energy_trading.ui.personal.order.Update_Password;

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

public class Update_Personal_Information extends FragmentActivity {
    @BindView(R.id.img_update_back)
    ImageView imgUpdateBack;
    @BindView(R.id.view_update_imformation_1)
    View viewUpdateImformation1;
    @BindView(R.id.et_personal_information_username)
    EditText etPersonalInformationUsername;
    @BindView(R.id.tl_update_personal_information)
    TableLayout tlUpdatePersonalInformation;
    @BindView(R.id.et_update_personal_information_email)
    EditText etUpdatePersonalInformationEmail;
    @BindView(R.id.tl_update_personal_information_1)
    TableLayout tlUpdatePersonalInformation1;
    @BindView(R.id.et_update_personal_information_telephone)
    EditText etUpdatePersonalInformationTelephone;
    @BindView(R.id.tl_update_personal_information_2)
    TableLayout tlUpdatePersonalInformation2;
    @BindView(R.id.et_update_personal_information_address)
    EditText etUpdatePersonalInformationAddress;
    @BindView(R.id.tl_update_personal_information_3)
    TableLayout tlUpdatePersonalInformation3;
    @BindView(R.id.btn_update_information_update)
    Button btnUpdateInformationUpdate;
//    @BindView(R.id.btn_update_information_cancel)
//    Button btnUpdateInformationCancel;
private Handler handler = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_update_personal_information);
        ButterKnife.bind(this);
        imgUpdateBack=this.findViewById(R.id.img_update_back);
        imgUpdateBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        etPersonalInformationUsername=this.findViewById(R.id.et_personal_information_username);
        etUpdatePersonalInformationEmail=this.findViewById(R.id.et_update_personal_information_email);
        etUpdatePersonalInformationTelephone=this.findViewById(R.id.et_update_personal_information_telephone);
        etUpdatePersonalInformationAddress=this.findViewById(R.id.et_update_personal_information_address);
        initData();
        initView();
        btnUpdateInformationUpdate=this.findViewById(R.id.btn_update_information_update);
        btnUpdateInformationUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                update();
                Toast.makeText(Update_Personal_Information.this,"Personal information updated successfully!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initData(){
        Bundle bundle = getIntent().getExtras();
        String user_password = bundle.getString("password");
        String user_email = bundle.getString("email");
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        //申明给服务端传递一个json串
        JSONObject jsonObject=new JSONObject();
        //JSONArray jsonArray=new JSONArray();
        try {
            jsonObject.put("email", user_email);
            jsonObject.put("password", user_password);
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
                .url("http://192.168.138.48/energy_trade/get_personal_information.php")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {

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
    }
    private void initView(){
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String username = bundle.getString("username");
                String email = bundle.getString("email");
                String telephone = bundle.getString("telephone");
                String address = bundle.getString("address");
                etPersonalInformationUsername = Update_Personal_Information.this.findViewById(R.id.et_personal_information_username);
                etPersonalInformationUsername.setText(username);
                etUpdatePersonalInformationEmail = Update_Personal_Information.this.findViewById(R.id.et_update_personal_information_email);
                etUpdatePersonalInformationEmail.setText(email);
                etUpdatePersonalInformationTelephone = Update_Personal_Information.this.findViewById(R.id.et_update_personal_information_telephone);
                etUpdatePersonalInformationTelephone.setText(telephone);
                etPersonalInformationUsername = Update_Personal_Information.this.findViewById(R.id.et_update_personal_information_address);
                etUpdatePersonalInformationAddress.setText(address);
            }
        };
    }
    private void update(){
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                int user_id = 0;
                user_id=bundle.getInt("user_id");
                etPersonalInformationUsername = Update_Personal_Information.this.findViewById(R.id.et_personal_information_username);
                etUpdatePersonalInformationEmail = Update_Personal_Information.this.findViewById(R.id.et_update_personal_information_email);
                etUpdatePersonalInformationTelephone = Update_Personal_Information.this.findViewById(R.id.et_update_personal_information_telephone);
                etUpdatePersonalInformationAddress = Update_Personal_Information.this.findViewById(R.id.et_update_personal_information_address);
                final String update_username = etPersonalInformationUsername.getText().toString();
                final String update_email = etPersonalInformationUsername.getText().toString();
                final String update_telephone = etPersonalInformationUsername.getText().toString();
                final String update_address = etPersonalInformationUsername.getText().toString();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                //申明给服务端传递一个json串
                JSONObject jsonObject=new JSONObject();
                //JSONArray jsonArray=new JSONArray();
                try {
                    jsonObject.put("user_id", user_id);
                    jsonObject.put("username", update_username);
                    jsonObject.put("email", update_email);
                    jsonObject.put("telephone", update_telephone);
                    jsonObject.put("address", update_address);
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
                        .url("http://192.168.138.48/energy_trade/update_personal_information.php")
                        .post(requestBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
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
                                    Toast.makeText(Update_Personal_Information.this,"Personal information updated successfully!",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else if(retCode == 2) {
                                    Toast.makeText(Update_Personal_Information.this,"Username already exists, please change to another username!",Toast.LENGTH_SHORT).show();
                                }else if(retCode == 3) {
                                    Toast.makeText(Update_Personal_Information.this,"Phone number is already registered!",Toast.LENGTH_SHORT).show();
                                }else if(retCode == 4) {
                                    Toast.makeText(Update_Personal_Information.this,"Email already registered!",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(Update_Personal_Information.this, "Personal information update failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        };
    }

}
