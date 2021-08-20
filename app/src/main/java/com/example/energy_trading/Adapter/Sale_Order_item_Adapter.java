package com.example.energy_trading.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.energy_trading.R;
import com.example.energy_trading.bean.Order_item;


import java.util.ArrayList;

public class Sale_Order_item_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Order_item> orderitemlistData;

    public Sale_Order_item_Adapter(Context context, ArrayList<Order_item> orderitemlistData) {
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
            view=inflate.inflate(R.layout.order_list_item,null);//此处layout是listview item的布局,我的命名不规范！！！
            viewholder = new ViewHolder();
            viewholder.style_of_order_username = (TextView)view.findViewById(R.id.style_of_order_username);
            viewholder.order_username11111 = (TextView)view.findViewById(R.id.order_username11111);
            viewholder.order_number_1 = (TextView)view.findViewById(R.id.order_number_1);
            viewholder.order_total_price_1 = (TextView)view.findViewById(R.id.order_total_price_1);
            viewholder.tv_order_status = (TextView)view.findViewById(R.id.tv_order_status);
            view.setTag(viewholder);
        }
        viewholder = (ViewHolder)view.getTag();
        Order_item order_item = orderitemlistData.get(i);
        viewholder.style_of_order_username.setText("Buyer: ");
        viewholder.order_username11111.setText(order_item.getBuyer_id());
        viewholder.order_number_1.setText(order_item.getOrder_number());
        viewholder.order_total_price_1.setText(order_item.getOrder_total_price());
        viewholder.tv_order_status.setText(order_item.getOrder_status());
        return view;
    }
    class ViewHolder {
        private TextView style_of_order_username;
        private TextView order_username11111;
        private TextView order_number_1;
        private TextView order_total_price_1;
        private TextView tv_order_status;
    }
}
