package com.example.energy_trading.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.energy_trading.TableContanst.OrderTableContanst;

public class OrderDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "OrderDBHelper";
    public static final String DB_NAME = "energy_trading.db";
    public static final int VERSION = 1;    //构造方法
    private Context context;
    public OrderDBHelper(Context context, String name, CursorFactory factory, int version)
    {
        super(context, DB_NAME, null, VERSION);
        //上下文，数据库文件名，null，版本号
        this.context = context;
    }

    public OrderDBHelper(Context context) {
        this(context, DB_NAME, null, VERSION);
    }



    //创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "onCreate");
        String sql = "create table pushmessage(_id integer primary key autoincrement,number char(20),unit_price char(20),total_price char(20),trading_date date,trading_time time )";
        db.execSQL(sql);
    }
    //更新数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG, "onUpgrade");
        db.execSQL("drop table if exists pushmessage");
        onCreate(db);
    }


}
