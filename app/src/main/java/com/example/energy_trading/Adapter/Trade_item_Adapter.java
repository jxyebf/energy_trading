package com.example.energy_trading.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;

import com.example.energy_trading.R;
import com.example.energy_trading.bean.Trade_Item;
import com.example.energy_trading.ui.home.Buy_on_list_order_Fragment;
import com.example.energy_trading.ui.personal.Personal_Information_detail;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Trade_item_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Trade_Item> itemlistData;

    public Trade_item_Adapter(Context context, ArrayList<Trade_Item> orderlistData) {
        this.context = context;
        this.itemlistData = orderlistData;
    }

    @Override
    public int getCount() {
        return itemlistData.size();
    }

    @Override
    public Object getItem(int i) {
        return itemlistData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewholder = null;
        if(view == null){
            LayoutInflater inflate=LayoutInflater.from(context);
            view=inflate.inflate(R.layout.trade_list_item,null);//此处layout是listview item的布局,我的命名不规范！！！
            viewholder = new ViewHolder();
            viewholder.tv_order_username = (TextView)view.findViewById(R.id.tv_order_username);
            viewholder.tv_order_number = (TextView)view.findViewById(R.id.tv_order_number);
            viewholder.tv_order_unit_price = (TextView)view.findViewById(R.id.tv_order_unit_price);
            viewholder.tv_order_total_price = (TextView)view.findViewById(R.id.tv_order_total_price);
            viewholder.tv_order_trading_date = (TextView)view.findViewById(R.id.tv_order_trading_date);
            viewholder.tv_order_trading_time = (TextView)view.findViewById(R.id.tv_order_trading_time);
            viewholder.btn_buy=view.findViewById(R.id.btn_buy);
            view.setTag(viewholder);
        }
        viewholder = (ViewHolder)view.getTag();
        Trade_Item tradeitem = itemlistData.get(i);
        viewholder.tv_order_username.setText(tradeitem.getUsername());
        viewholder.tv_order_number.setText(tradeitem.getNumber());
        viewholder.tv_order_unit_price.setText(tradeitem.getUnit_price());
        viewholder.tv_order_total_price.setText(tradeitem.getTotal_price());
        viewholder.tv_order_trading_date.setText(tradeitem.getTrading_date());
        viewholder.tv_order_trading_time.setText(tradeitem.getTrading_time());
        viewholder.tv_order_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开始传值
                Intent intent=new Intent(context, Personal_Information_detail.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("tradeitem",tradeitem);
                intent.putExtras(bundle);
                //利用上下文开启跳转
                context.startActivity(intent);
            }
        });
        viewholder.btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Buy_on_list_order_Fragment.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("tradeitem",tradeitem);
                intent.putExtras(bundle);
                //利用上下文开启跳转
                context.startActivity(intent);
            }
        });
        return view;
    }
    class ViewHolder {
        private TextView tv_order_username;
        private TextView tv_order_number;
        private TextView tv_order_unit_price;
        private TextView tv_order_total_price;
        private TextView tv_order_trading_date;
        private TextView tv_order_trading_time;
        private Button btn_buy;
    }
}
