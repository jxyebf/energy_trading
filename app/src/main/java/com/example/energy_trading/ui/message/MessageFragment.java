package com.example.energy_trading.ui.message;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.energy_trading.Adapter.Push_Message_Adapter;
import com.example.energy_trading.R;
import com.example.energy_trading.bean.PushMessage;
import com.example.energy_trading.bean.Trade_Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MessageFragment extends Fragment {
    @BindView(R.id.order_message)
    ListView orderMessage;
    @BindView(R.id.scrollview_message)
    ScrollView scrollviewMessage;
    private Handler handler = null;
    private Push_Message_Adapter push_message_adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message, container, false);
        orderMessage = (ListView)view.findViewById(R.id.order_message);
        initData();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                ArrayList list =bundle.getParcelableArrayList("order_list_date");
                Log.i(TAG, "111111111111111111111111111111111111111111111111111111111111111111: " + list);
                ArrayList<PushMessage> item_list = list;
                push_message_adapter = new Push_Message_Adapter(getActivity(),item_list);
                orderMessage.setAdapter(push_message_adapter);
                push_message_adapter.notifyDataSetChanged();
            }
        };
        return view;
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建一个OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                //创建一个请求对象
                Request request = new Request.Builder()
                        .url("http://192.168.138.48/energy_trade/message_list.php")
                        .get()
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
                        Log.i(TAG, "1111111111111111111111111111111111111111111111111111111111111: " + trade_item_list);
                        JSONArray jsonArray = new JSONArray();
                        ArrayList<PushMessage> messagelist = new ArrayList<PushMessage>();
                        try {
                            jsonArray = new JSONArray(String.valueOf(trade_item_list));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject = jsonArray.getJSONObject(i);
                                PushMessage pushMessage = new PushMessage();
                                pushMessage.setTitle(jsonObject.getString("title"));
                                pushMessage.setAlert(jsonObject.getString("alert"));
                                messagelist.add(pushMessage);
                                Log.i(TAG, "onFailure: " + messagelist);
                            }
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("order_list_date", messagelist);
                            Message msg = new Message();
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }
}