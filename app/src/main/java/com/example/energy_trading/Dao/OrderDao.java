package com.example.energy_trading.Dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.energy_trading.DBHelper.OrderDBHelper;
import com.example.energy_trading.TableContanst.OrderTableContanst;
import com.example.energy_trading.bean.Trade_Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.energy_trading.TableContanst.OrderTableContanst.ORDER_TABLE;


public class OrderDao {
    private OrderDBHelper dbHelper;
    private Cursor cursor;
    public OrderDao(OrderDBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
    // 添加一个Order对象数据到数据库表
    public long addOrder(Trade_Item o) {
        ContentValues values = new ContentValues();
       // values.put(OrderTableContanst.OrderColumns.USERNAME, o.getUsername());
        values.put(OrderTableContanst.OrderColumns.NUMBER, o.getNumber());
        values.put(OrderTableContanst.OrderColumns.UNIT_PRICE, o.getUnit_price());
        values.put(OrderTableContanst.OrderColumns.TOTAL_PRICE, o.getTotal_price());
        values.put(OrderTableContanst.OrderColumns.TRADING_DATE, o.getTrading_date());
        values.put(OrderTableContanst.OrderColumns.TRADING_TIME, o.getTrading_date());
        return dbHelper.getWritableDatabase().insert(ORDER_TABLE, null, values);
    }
    // 删除一个id所对应的数据库表 Trade_Item 的记录
    public int deleteOrderById(long id) {
        return dbHelper.getWritableDatabase().delete(ORDER_TABLE,
                OrderTableContanst.OrderColumns.ID + "=?", new String[] { id + "" });
    }
    // 更新一个id所对应数据库表 Trade_Item 的记录
    public int updateOrder(Trade_Item o) {
        ContentValues values = new ContentValues();
       // values.put(OrderTableContanst.OrderColumns.USERNAME, o.getUsername());
        values.put(OrderTableContanst.OrderColumns.NUMBER, o.getNumber());
        values.put(OrderTableContanst.OrderColumns.UNIT_PRICE, o.getUnit_price());
        values.put(OrderTableContanst.OrderColumns.TOTAL_PRICE, o.getTotal_price());
        values.put(OrderTableContanst.OrderColumns.TRADING_DATE, o.getTrading_date());
        values.put(OrderTableContanst.OrderColumns.TRADING_TIME, o.getTrading_date());
        return dbHelper.getWritableDatabase().update(ORDER_TABLE, values,
                OrderTableContanst.OrderColumns.ID + "=?", new String[] { o.getId() + "" });
    }
    // 查询所有的记录
    public List<Map<String,Object>> getAllOrder() {
        //modify_time desc
        List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
        Cursor cursor = dbHelper.getWritableDatabase().query(OrderTableContanst.ORDER_TABLE,null,null,null,null,null,null);
        while(cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>(6);
            long id = cursor.getInt(cursor.getColumnIndex(OrderTableContanst.OrderColumns.ID));
            map.put(OrderTableContanst.OrderColumns.ID, id);
           // String username = cursor.getString(cursor.getColumnIndex(OrderTableContanst.OrderColumns.USERNAME));
           // map.put(OrderTableContanst.OrderColumns.USERNAME, username);
            int number = cursor.getInt(cursor.getColumnIndex(OrderTableContanst.OrderColumns.NUMBER));
            map.put(OrderTableContanst.OrderColumns.NUMBER, number);
            String unit_price = cursor.getString(cursor.getColumnIndex(OrderTableContanst.OrderColumns.UNIT_PRICE));
            map.put(OrderTableContanst.OrderColumns.UNIT_PRICE, unit_price);
            String total_price = cursor.getString(cursor.getColumnIndex(OrderTableContanst.OrderColumns.TOTAL_PRICE));
            map.put(OrderTableContanst.OrderColumns.TOTAL_PRICE, total_price);
            String trading_date = cursor.getString(cursor.getColumnIndex(OrderTableContanst.OrderColumns.TRADING_DATE));
            map.put(OrderTableContanst.OrderColumns.TRADING_DATE, trading_date);
            String trading_time = cursor.getString(cursor.getColumnIndex(OrderTableContanst.OrderColumns.TRADING_TIME));
            map.put(OrderTableContanst.OrderColumns.TRADING_TIME, trading_time);
            data.add(map);
        }
        return data;
    }
   /* //模糊查询一条记录
    public Cursor findOrder(String username){
        Cursor cursor = dbHelper.getWritableDatabase().query(OrderTableContanst.ORDER_TABLE,  null, "username like ?",
                new String[] { "%" + username + "%" }, null, null, null,null);
        return cursor;      }

    */
    //按数量进行排序
    public Cursor sortByNumber(){
        Cursor cursor = dbHelper.getWritableDatabase().query(OrderTableContanst.ORDER_TABLE,  null,null,
                null, null, null,OrderTableContanst.OrderColumns.NUMBER);
        return cursor;     }
    //按单价进行排序
    public Cursor sortByUnitPrice(){
        Cursor cursor = dbHelper.getWritableDatabase().query(OrderTableContanst.ORDER_TABLE,  null,null,
                null, null, null,OrderTableContanst.OrderColumns.UNIT_PRICE);
        return cursor;
    }
    //按学号进行排序
    public Cursor sortByID(){
        Cursor cursor = dbHelper.getWritableDatabase().query(OrderTableContanst.ORDER_TABLE,  null,null,
                null, null, null,OrderTableContanst.OrderColumns.ID);
        return cursor;    }
    public void closeDB() {
        dbHelper.close();     }//自定义的方法通过View和Id得到一个student对象

    /*public Trade_Item getOrderFromView(View view, long id) {
        TextView nameView = (TextView) view.findViewById(R.id.tv_stu_name);
        TextView ageView = (TextView) view.findViewById(R.id.tv_stu_age);
        TextView sexView = (TextView) view.findViewById(R.id.tv_stu_sex);
        TextView likeView = (TextView) view.findViewById(R.id.tv_stu_likes);
        TextView phoneView = (TextView) view.findViewById(R.id.tv_stu_phone);
        TextView dataView = (TextView) view.findViewById(R.id.tv_stu_traindate);
        String name = nameView.getText().toString();
        int age = Integer.parseInt(ageView.getText().toString());
        String sex = sexView.getText().toString();
        String like = likeView.getText().toString();
        String phone = phoneView.getText().toString();
        String data = dataView.getText().toString();
        Trade_Item order = new Trade_Item(id, username, number, unit_price, total_price, trading_date, trading_time,null);
        return
                order;
    }
     */
}
