package com.example.energy_trading.ui.personal.order;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;

import com.example.energy_trading.Adapter.Buy_Order_item_Adapter;
import com.example.energy_trading.R;
import com.example.energy_trading.bean.Order_item;
import com.example.energy_trading.bean.userevent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Bought_Fragment extends FragmentActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.order_list_name)
    AppCompatTextView orderListName;
    @BindView(R.id.status_order_list)
    ListView statusOrderList;
    TextView style_of_order_name_username_1;
    String uemail, upassword;
    private ListView listView;
    private Handler handler=null;
    private Buy_Order_item_Adapter buy_order_item_adapter;
    private ArrayList<Order_item> order_list=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muti_status_order_list);
        imgBack = this.findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish ();
            }
        });
        EventBus.getDefault().register(this);
        initView();
        get_sale_order_json();
        listView = this.findViewById(R.id.status_order_list);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                ArrayList list =bundle.getParcelableArrayList("order_list_date");
                Log.i(TAG, "3333333333333333333333333333333333333333333333333333333333: " + list);
                ArrayList<Order_item> order_list = list;
                //EventBus.getDefault().post(new SecondEvent(order_list));
                Log.i(TAG, "4444444444444444444444444444444444444444444444444444444444: " + order_list);
                buy_order_item_adapter = new Buy_Order_item_Adapter(Bought_Fragment.this,order_list);
                Log.i(TAG, "5555555555555555555555555555555555555555555555555555555555555: " + buy_order_item_adapter);
                listView.setAdapter(buy_order_item_adapter);
                buy_order_item_adapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent1 = new Intent();
                        Order_item data= order_list.get(i);
                        Bundle bundle1=new Bundle();
                        bundle1.putString("order_number",data.getOrder_number());
                        bundle1.putString("order_status",data.getOrder_status());
                        bundle1.putString("order_id",data.getOrder_id());
                        bundle1.putString("order_total_price",data.getOrder_total_price());
                        bundle1.putString("order_seller_name",data.getSeller_username());
                        bundle1.putString("Order_pay_time",data.getOrder_pay_time());
                        Log.i(TAG, "6666666666666666666666666666666666666666666666666666666666666: " + bundle1);
                        intent1.putExtras(bundle1);
                        intent1.setClass(Bought_Fragment.this, Buy_Order_Detail_Fragment.class);
                        startActivity(intent1);
                    }
                });
            }
        };

    }
    private void initView(){
        orderListName=this.findViewById(R.id.order_list_name);
        orderListName.setText("My Purchase Orders");
    }
    @Subscribe(sticky = true)  //必须使用EventBus的订阅注解
    public void onEventMainThread(userevent userevent) {
        uemail = userevent.getUseremail();
        upassword = userevent.getUserpassword();
    }
    private void get_sale_order_json(){
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
                .url("http://192.168.138.48/energy_trade/bought_order.php")
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
                JSONArray jsonArray = new JSONArray();
                ArrayList<Order_item> orderlistData=new ArrayList<Order_item>();
                Log.i(TAG, "11111111111111111111111111111111111111111111 " + trade_item_list);
                try {
                    jsonArray = new JSONArray(String.valueOf(trade_item_list));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject = jsonArray.getJSONObject(i);
                        Order_item order_item = new Order_item();
                        order_item.setOrder_id(jsonObject.getString("order_id"));
                        order_item.setOrder_pay_time(jsonObject.getString("order_pay_time"));
                        order_item.setOrder_status(jsonObject.getString("order_status"));
                        order_item.setSeller_username(jsonObject.getString("seller_id"));
                        order_item.setOrder_number(jsonObject.getString("order_number"));
                        order_item.setOrder_total_price(jsonObject.getString("order_total_price"));
                        order_item.setOrder_trading_date(jsonObject.getString("order_trading_date"));
                        order_item.setOrder_trading_time(jsonObject.getString("order_trading_time"));
                        //order_item.setSeller_username(jsonObject.getString("username"));
                        orderlistData.add(order_item);
                        Log.i(TAG, "2222222222222222222222222222222222222222222: " + orderlistData);
                    }
                    //EventBus.getDefault().postSticky(orderlistData);
                    Bundle bundle=new Bundle();
                    bundle.putParcelableArrayList("order_list_date",orderlistData);
                    Message msg = new Message();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public class SecondEvent {
        private ArrayList mArrayList;
        public SecondEvent(ArrayList mArrayList) {
            this.mArrayList = mArrayList;
        }
        public ArrayList getmArrayList() {
            return mArrayList;
        }
    }
}
