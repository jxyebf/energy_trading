package com.example.energy_trading.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.energy_trading.R;
import com.example.energy_trading.bean.Order_item;
import com.example.energy_trading.bean.Trade_Item;

import java.util.ArrayList;

public class On_list_Order_item_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Trade_Item> orderitemlistData;

    public On_list_Order_item_Adapter(Context context, ArrayList<Trade_Item> orderitemlistData) {
        this.context = context;
        this.orderitemlistData = orderitemlistData;
    }

    @Override
    public int getCount() {
        return orderitemlistData.size();
    }

    @Override
    public Object getItem(int i) {
        return orderitemlistData.get(i);
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
            view=inflate.inflate(R.layout.on_list_order_item,null);//此处layout是listview item的布局,我的命名不规范！！！
            viewholder = new ViewHolder();
            viewholder.onlist_item_number = (TextView)view.findViewById(R.id.onlist_item_number);
            viewholder.onlist_item_unit_price = (TextView)view.findViewById(R.id.onlist_item_unit_price);
            viewholder.onlist_item_total_price = (TextView)view.findViewById(R.id.onlist_item_total_price);
            viewholder.onlist_item_trade_date = (TextView)view.findViewById(R.id.onlist_item_trade_date);
            viewholder.onlist_item_trade_time = (TextView)view.findViewById(R.id.onlist_item_trade_time);
            view.setTag(viewholder);
        }
        viewholder = (ViewHolder)view.getTag();
        Trade_Item order_item = orderitemlistData.get(i);
        viewholder.onlist_item_number.setText(order_item.getNumber());
        viewholder.onlist_item_unit_price.setText(order_item.getUnit_price());
        viewholder.onlist_item_total_price.setText(order_item.getTotal_price());
        viewholder.onlist_item_trade_date.setText(order_item.getTrading_date());
        viewholder.onlist_item_trade_time.setText(order_item.getTrading_time());
        return view;
    }
    class ViewHolder {
        private TextView onlist_item_number;
        private TextView onlist_item_unit_price;
        private TextView onlist_item_total_price;
        private TextView onlist_item_trade_date;
        private TextView onlist_item_trade_time;
    }
}
