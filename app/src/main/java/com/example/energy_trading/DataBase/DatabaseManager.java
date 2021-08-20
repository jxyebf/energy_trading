package com.example.energy_trading.DataBase;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

/**
 *
 */

public class DatabaseManager {

    private DaoSession mDaoSession= null;
   // private UserProfileDao mUserProfileDao= null;
    private PushMessageDao mPushMessageDao=null;

    private DatabaseManager() {
    }

    public DatabaseManager init(Context context) {
        initDao(context);
        return this;
    }

    private static final class Holder {  //静态内部类单例模式
        private static final DatabaseManager INSTANCE = new DatabaseManager();
    }

    public static DatabaseManager getInstance() {
        return Holder.INSTANCE;
    }

    private void initDao(Context context) {
        final ReleaseOpenHelper helper = new ReleaseOpenHelper(context, "energy_trading.db"); //传入数据库名，在Map集合中与对应的实体类建立映射关系
        final Database db = helper.getWritableDb(); //以读写方式得到数据库
        mDaoSession = new DaoMaster(db).newSession();
        //mUserProfileDao = mDaoSession.getUserProfileDao();  //获取该数据库的实体类操作对象
        mPushMessageDao=mDaoSession.getPushMessageDao();
    }
    public final PushMessageDao getDao() {
        return mPushMessageDao;
    }

//    public final UserProfileDao getDao() {
//        return mUserProfileDao;
//    }
}
