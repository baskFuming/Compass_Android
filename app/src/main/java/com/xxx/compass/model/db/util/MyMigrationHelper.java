//package com.xxx.compass.model.db.util;
//
//import android.database.Cursor;
//import android.database.SQLException;
//import android.os.Build;
//import android.support.annotation.NonNull;
//import android.support.annotation.RequiresApi;
//import android.text.TextUtils;
//
//import org.greenrobot.greendao.AbstractDao;
//import org.greenrobot.greendao.database.Database;
//import org.greenrobot.greendao.internal.DaoConfig;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class MyMigrationHelper {
//
//    private static final String SQLITE_MASTER = "sqlite_master";
//    private static final String SQLITE_TEMP_MASTER = "sqlite_temp_master";
//
//    @SafeVarargs
//    public static void migrate(Database database, Class<? extends AbstractDao<?, ?>>... daoClasses) {
//        //生成临时的数据表
//        generateTempTables(database, daoClasses);
//        //转移所有数据到新的临时表
//        dropAllTables(database, daoClasses);
//        //创建新的数据表
//        createAllTables(database, daoClasses);
//        //转移数据
//        restoreData(database, daoClasses);
//    }
//
//    @SafeVarargs
//    private static void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
//        for (Class<? extends AbstractDao<?, ?>> daoClass : daoClasses) {
//            String tempTableName;
//
//            DaoConfig daoConfig = new DaoConfig(db, daoClass);
//            String tableName = daoConfig.tablename;
//            if (!isTableExists(db, false, tableName)) {
//                continue;
//            }
//            try {
//                tempTableName = daoConfig.tablename.concat("_TEMP");
//                db.execSQL("DROP TABLE IF EXISTS " + tempTableName + ";");
//                String insertTableStringBuilder = "CREATE TEMPORARY TABLE " + tempTableName + " AS SELECT * FROM " + tableName + ";";
//                db.execSQL(insertTableStringBuilder);
//            } catch (SQLException ignored) {
//                ignored.printStackTrace();
//            }
//        }
//    }
//
//    @SafeVarargs
//    private static void dropAllTables(Database db, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            reflectMethod(db, "dropTable", true, daoClasses);
//        }
//    }
//
//    @SafeVarargs
//    private static void createAllTables(Database db, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            reflectMethod(db, "createTable", false, daoClasses);
//        }
//    }
//
//    @SafeVarargs
//    private static void restoreData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
//        for (Class<? extends AbstractDao<?, ?>> daoClass : daoClasses) {
//            DaoConfig daoConfig = new DaoConfig(db, daoClass);
//            String tableName = daoConfig.tablename;
//            String tempTableName = daoConfig.tablename.concat("_TEMP");
//
//            if (!isTableExists(db, true, tempTableName)) {
//                continue;
//            }
//
//            try {
//                // get all columns from tempTable, take careful to use the columns list
//                List<String> columns = getColumns(db, tempTableName);
//                ArrayList<String> properties = new ArrayList<>(columns.size());
//                for (int j = 0; j < daoConfig.properties.length; j++) {
//                    String columnName = daoConfig.properties[j].columnName;
//                    if (columns.contains(columnName)) {
//                        properties.add(columnName);
//                    }
//                }
//                if (properties.size() > 0) {
//                    final String columnSQL = TextUtils.join(",", properties);
//
//                    StringBuilder insertTableStringBuilder = new StringBuilder();
//                    insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (");
//                    insertTableStringBuilder.append(columnSQL);
//                    insertTableStringBuilder.append(") SELECT ");
//                    insertTableStringBuilder.append(columnSQL);
//                    insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");
//                    db.execSQL(insertTableStringBuilder.toString());
//                }
//                StringBuilder dropTableStringBuilder = new StringBuilder();
//                dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);
//                db.execSQL(dropTableStringBuilder.toString());
//            } catch (SQLException ignored) {
//                ignored.printStackTrace();
//            }
//        }
//    }
//
//
//    private static boolean isTableExists(Database db, boolean isTemp, String tableName) {
//        if (db == null || TextUtils.isEmpty(tableName)) {
//            return false;
//        }
//        String dbName = isTemp ? SQLITE_TEMP_MASTER : SQLITE_MASTER;
//        String sql = "SELECT COUNT(*) FROM " + dbName + " WHERE type = ? AND name = ?";
//        Cursor cursor = null;
//        int count = 0;
//        try {
//            cursor = db.rawQuery(sql, new String[]{"table", tableName});
//            if (cursor == null || !cursor.moveToFirst()) {
//                return false;
//            }
//            count = cursor.getInt(0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) cursor.close();
//        }
//        return count > 0;
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @SafeVarargs
//    private static void reflectMethod(Database db, String methodName, boolean isExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
//        if (daoClasses.length < 1) {
//            return;
//        }
//        try {
//            for (Class cls : daoClasses) {
//                Method method = cls.getDeclaredMethod(methodName, Database.class, boolean.class);
//                method.invoke(null, db, isExists);
//            }
//        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    //获取到表的所有字段
//    private static List<String> getColumns(Database db, String tableName) {
//        List<String> columns = null;
//        Cursor cursor = null;
//        try {
//            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 0", null);
//            if (null != cursor && cursor.getColumnCount() > 0) {
//                columns = Arrays.asList(cursor.getColumnNames());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) cursor.close();
//            if (null == columns) columns = new ArrayList<>();
//        }
//        return columns;
//    }
//
//}