package com.example.energy_trading.ui.home;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.energy_trading.Adapter.Trade_item_Adapter;
import com.example.energy_trading.DBHelper.OrderDBHelper;
import com.example.energy_trading.R;
import com.example.energy_trading.bean.Order_item;
import com.example.energy_trading.bean.Trade_Item;
import com.example.energy_trading.ui.personal.order.Bought_Fragment;
import com.example.energy_trading.ui.personal.order.Buy_Order_Detail_Fragment;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.loader.ImageLoaderInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {
    TextView tv_order_username,tv_order_number,tv_order_unit_price, tv_order_total_price, tv_order_trading_date,tv_order_trading_time;
    @BindView(R.id.banner)
    Banner banner;
    private ListView listView;
    String TAG = "TAG";
    private SQLiteDatabase db;
    OrderDBHelper orderdbhelper;
    private Trade_item_Adapter tradeitemAdapter;
    private HomeViewModel homeViewModel;
    private List<Integer> image = new ArrayList<>();
    private List<String> title = new ArrayList<>();
    private Button button1;
    private Button button2;
    private Handler handler=null;

    private void initData() {
        image.add(R.drawable.uoko_guide_background_1);
        image.add(R.drawable.uoko_guide_background_2);
        image.add(R.drawable.uoko_guide_background_3);
        image.add(R.drawable.uoko_guide_background_4) ;
        image.add(R.drawable.uoko_guide_background_5);
        title.add("Peer-to-Peer energy trading");
        title.add("Peer-to-Peer energy trading");
        title.add("Peer-to-Peer energy trading");
        title.add("Peer-to-Peer energy trading");
        title.add("Peer-to-Peer energy trading");
    }
    private void initView() {
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setImageLoader((ImageLoaderInterface) new MyImageLoader());
        banner.setImages(image);
        banner.setBannerAnimation(Transformer.Default);
        banner.isAutoPlay(true);
        banner.setBannerTitles(title);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setDelayTime(3000);
        banner.start();
    }

    private class MyImageLoader extends ImageLoader {
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        banner = view.findViewById(R.id.banner);
        initData();
        initView();
        listView = (ListView)view.findViewById(R.id.order_list);
        getList_json();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                ArrayList list =bundle.getParcelableArrayList("order_list_date");
                Log.i(TAG, "111111111111111111111111111111111111111111111111111111111111111111: " + list);
                ArrayList<Trade_Item> item_list = list;
                tradeitemAdapter = new Trade_item_Adapter(getActivity(),item_list);
                listView.setAdapter(tradeitemAdapter);
                tradeitemAdapter.notifyDataSetChanged();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent1 = new Intent();
                        Trade_Item item= item_list.get(i);
                        Bundle bundle1=new Bundle();
                        bundle1.putSerializable("tradeitem",item);
                        Log.i(TAG, "6666666666666666666666666666666666666666666666666666666666666: " + bundle1);
                        intent1.putExtras(bundle1);
                        intent1.setClass(getActivity(), Buy_on_list_order_Fragment.class);
                        startActivity(intent1);
                    }
                });
            }
        };
        return view;
    }
    private void getList_json(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建一个OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                //创建一个请求对象
                Request request = new Request.Builder()
                        .url("http://192.168.138.48/energy_trade/item_list.php")
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
                        JSONArray jsonArray = new JSONArray();
                        ArrayList<Trade_Item>  orderlistData=new ArrayList<Trade_Item>();
                        try {
                            jsonArray = new JSONArray(String.valueOf(trade_item_list));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject = jsonArray.getJSONObject(i);
                                Trade_Item tradeItem = new Trade_Item();
                                tradeItem.setId(jsonObject.getString("item_id"));
                                tradeItem.setUsername(jsonObject.getString("username"));
                                tradeItem.setNumber(jsonObject.getString("number"));
                                tradeItem.setUnit_price(jsonObject.getString("unit_price"));
                                tradeItem.setTotal_price(jsonObject.getString("total_price"));
                                tradeItem.setTrading_date(jsonObject.getString("trading_date"));
                                tradeItem.setTrading_time(jsonObject.getString("trading_time"));
                                orderlistData.add(tradeItem);
                                Log.i(TAG, "onFailure: " + orderlistData);
                            }
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
        }).start();
    }

}