//package com.xxx.compass.model.db.util;
//
//import com.xxx.compass.base.App;
//import com.xxx.compass.ConfigClass;
//import com.xxx.compass.model.db.greendao.DaoMaster;
//import com.xxx.compass.model.db.greendao.DaoSession;
//
//import org.greenrobot.greendao.database.Database;
//
//public class GreenDaoUtil {
//
//    private static final Object SYC = new Object();
//
//    private static DaoSession session;
//
//    private GreenDaoUtil() {
//        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(App.getContext(), ConfigClass.DB_NAME, null);
//        Database database = helper.getWritableDb();
//        DaoMaster daoMaster = new DaoMaster(database);
//        session = daoMaster.newSession();
//    }
//
//    public static DaoSession getInstance() {
//        if (session == null) {
//            synchronized (SYC) {
//                if (session == null) {
//                    new GreenDaoUtil();
//                }
//            }
//        }
//        return session;
//    }
//}
