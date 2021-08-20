package com.example.energy_trading.ui.personal.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;

import com.example.energy_trading.Adapter.Buy_Order_item_Adapter;
import com.example.energy_trading.Adapter.On_list_Order_item_Adapter;
import com.example.energy_trading.Adapter.Trade_item_Adapter;
import com.example.energy_trading.R;
import com.example.energy_trading.bean.Order_item;
import com.example.energy_trading.bean.Trade_Item;
import com.example.energy_trading.bean.userevent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Publish_Order_Fragment extends FragmentActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.order_list_name)
    AppCompatTextView orderListName;
    @BindView(R.id.status_order_list)
    ListView statusOrderList;
    String uemail, upassword;
    private Handler handler=null;
    private On_list_Order_item_Adapter on_list_order_item_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muti_status_order_list);
        initView();
        EventBus.getDefault().register(this);
        imgBack = this.findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish ();
            }
        });
        get_onlist_order_json();
        statusOrderList = (ListView)this.findViewById(R.id.status_order_list);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                ArrayList list =bundle.getParcelableArrayList("order_list_date");
                Log.i(TAG, "3333333333333333333333333333333333333333333333333333333333: " + list);
                ArrayList<Trade_Item> order_list = list;
                on_list_order_item_adapter = new On_list_Order_item_Adapter(Publish_Order_Fragment.this,order_list);
                statusOrderList.setAdapter(on_list_order_item_adapter);
                on_list_order_item_adapter.notifyDataSetChanged();
                statusOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent1 = new Intent();
                        Trade_Item data= order_list.get(i);
                        Bundle bundle1=new Bundle();
                        bundle1.putString("item_id",data.getId());
                        bundle1.putString("number",data.getNumber());
                        bundle1.putString("unit_price",data.getUnit_price());
                        bundle1.putString("trading_date",data.getTrading_date());
                        bundle1.putString("trading_time",data.getTrading_time());
                        Log.i(TAG, "6666666666666666666666666666666666666666666666666666666666666: " + bundle1);
                        intent1.putExtras(bundle1);
                        intent1.setClass(Publish_Order_Fragment.this, On_list_Trade_Item_Detail_Fragment.class);
                        startActivity(intent1);
                    }
                });
            }
        };
    }
    private void initView(){
        orderListName=this.findViewById(R.id.order_list_name);
        orderListName.setText("My Posted Orders");
    }
    @Subscribe(sticky = true)  //必须使用EventBus的订阅注解
    public void onEventMainThread(userevent userevent) {
        uemail = userevent.getUseremail();
        upassword = userevent.getUserpassword();
    }
    private void get_onlist_order_json(){
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
                .url("http://192.168.138.48/energy_trade/get_on_list_order.php")
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
                ArrayList<Trade_Item> itemlistData=new ArrayList<Trade_Item>();
                Log.i(TAG, "11111111111111111111111111111111111111111111 " + trade_item_list);
                try {
                    jsonArray = new JSONArray(String.valueOf(trade_item_list));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject = jsonArray.getJSONObject(i);
                        Trade_Item trade_item = new Trade_Item();
                        trade_item.setId(jsonObject.getString("item_id"));
                        trade_item.setNumber(jsonObject.getString("number"));
                        trade_item.setUnit_price(jsonObject.getString("unit_price"));
                        trade_item.setTotal_price(jsonObject.getString("total_price"));
                        trade_item.setTrading_date(jsonObject.getString("trading_date"));
                        trade_item.setTrading_time(jsonObject.getString("trading_time"));
                        itemlistData.add(trade_item);
                        Log.i(TAG, "2222222222222222222222222222222222222222222: " + itemlistData);
                    }
                    Bundle bundle=new Bundle();
                    bundle.putParcelableArrayList("order_list_date",itemlistData);
                    Message msg = new Message();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
