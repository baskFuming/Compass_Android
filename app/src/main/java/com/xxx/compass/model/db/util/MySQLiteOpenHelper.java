//package com.xxx.compass.model.db.util;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//
//import com.xxx.compass.model.db.greendao.DaoMaster;
//import com.xxx.compass.model.log.LogUtil;
//
//import org.greenrobot.greendao.AbstractDao;
//import org.greenrobot.greendao.database.Database;
//
//public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
//
//    private Class[] dataArray = new Class[]{
//            null,  //资产表
//            null  //资产下的钱包表
//    };
//
//    /**
//     * @param context 上下文
//     * @param name    原来定义的数据库的名字   新旧数据库一致
//     * @param factory 可以null
//     */
//    MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
//        super(context, name, factory);
//    }
//
//    /**
//     * @param db
//     * @param oldVersion
//     * @param newVersion 更新数据库的时候自己调用
//     */
//    @Override
//    public void onUpgrade(Database db, int oldVersion, int newVersion) {
//        LogUtil.showLog(LogUtil.DATABASE_TAG, "开始更新数据库");
//        MyMigrationHelper.migrate(db, dataArray);
//        LogUtil.showLog(LogUtil.DATABASE_TAG, "更新数据库成功");
//    }
//}