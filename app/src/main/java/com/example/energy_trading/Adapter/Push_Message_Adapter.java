package com.example.energy_trading.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.energy_trading.R;
import com.example.energy_trading.bean.PushMessage;
import com.example.energy_trading.bean.Trade_Item;
import com.example.energy_trading.ui.personal.Personal_Information_detail;

import java.util.ArrayList;

public class Push_Message_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<PushMessage> itemlistData;

    public Push_Message_Adapter(Context context, ArrayList<PushMessage> orderlistData) {
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
            view=inflate.inflate(R.layout.item_message,null);//此处layout是listview item的布局,我的命名不规范！！！
            viewholder = new ViewHolder();
            viewholder.message_title = (TextView)view.findViewById(R.id.message_title);
            viewholder.message_message = (TextView)view.findViewById(R.id.message_message);
            view.setTag(viewholder);
        }
        viewholder = (ViewHolder)view.getTag();
        PushMessage pushMessage = itemlistData.get(i);
        viewholder.message_title.setText(pushMessage.getTitle());
        viewholder.message_message.setText(pushMessage.getAlert());
//        viewholder.tv_order_username.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //开始传值
//                Intent intent=new Intent(context, Personal_Information_detail.class);
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("tradeitem",tradeitem);
//                intent.putExtras(bundle);
//                //利用上下文开启跳转
//                context.startActivity(intent);
//            }
//        });
        return view;
    }
    class ViewHolder {
        private TextView message_title;
        private TextView message_message;
    }
}
