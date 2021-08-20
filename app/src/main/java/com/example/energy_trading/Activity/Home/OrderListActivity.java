package com.example.energy_trading.Activity.Home;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.energy_trading.DBHelper.OrderDBHelper;
import com.example.energy_trading.Dao.OrderDao;
import com.example.energy_trading.R;
import com.example.energy_trading.TableContanst.OrderTableContanst;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderListActivity extends ListActivity {
/*
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private ListView listView;
    private Button buy;
    private OrderDao dao;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        dao = new OrderDao(new OrderDBHelper(this));
        buy = findViewById(R.id.btn_buy);
        listView = getListView();
    }

    // 调用load()方法将数据库中的所有记录显示在当前页面
    @Override
    protected void onStart() {
        super.onStart();
        load();

    }

    public void load() {
        OrderDBHelper OrderDBHelper = new OrderDBHelper(
                OrderListActivity.this);
        SQLiteDatabase database = OrderDBHelper.getWritableDatabase();
        cursor = database.query(OrderTableContanst.ORDER_TABLE, null, null, null,
                null, null, OrderTableContanst.OrderColumns.ID + " desc");
        startManagingCursor(cursor);
        adapter = new SimpleCursorAdapter(this, R.layout.trade_list_item,
                cursor, new String[]{
                //OrderTableContanst.OrderColumns.USERNAME,
                OrderTableContanst.OrderColumns.NUMBER,
                OrderTableContanst.OrderColumns.UNIT_PRICE,
                OrderTableContanst.OrderColumns.TOTAL_PRICE,
                OrderTableContanst.OrderColumns.TRADING_DATE,
                OrderTableContanst.OrderColumns.TRADING_TIME}, new int[]{R.id.tv_order_number,R.id.tv_order_unit_price,
                R.id.tv_order_total_price, R.id.tv_order_trading_date, R.id.tv_order_trading_time});
        listView.setAdapter(adapter);
    }

 */



}
