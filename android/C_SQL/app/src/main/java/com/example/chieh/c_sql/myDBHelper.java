package com.example.chieh.c_sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Chieh on 2017/4/27.
 */

public class myDBHelper extends SQLiteOpenHelper {
    // 資料庫名稱
    public static final String DATABASE_NAME = "mydata.db";
    public static int VERSION = 1;

    private static SQLiteDatabase database;
    private Cursor cursor;

    public myDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }
    //只有在資料庫不存在時,才會呼叫onCreate()建立資料庫
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        onCreate(db);
    }
    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new myDBHelper(context, DATABASE_NAME, null, VERSION).getWritableDatabase();
        }
        return database;
    }

    public String change (SQLiteDatabase db, String tableName, int num, String msg){
        db.execSQL("DROP TABLE IF EXISTS temp_" + tableName + ";");

        if(msg.length()>0) {
            try {
                cursor = db.query(tableName, null, null, null, null, null, null);
                String temp[] = cursor.getColumnNames();

                String old_cNames = "";
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    old_cNames += temp[i];
                    if (i < cursor.getColumnCount() - 1)
                        old_cNames += ", ";
                }

                temp[num] = msg;

                String new_cNames = "";
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    new_cNames += temp[i];
                    if (i < cursor.getColumnCount() - 1)
                        new_cNames += ", ";
                }
                cursor.close();
                db.execSQL("ALTER TABLE " + tableName + " RENAME TO temp_" + tableName + ";");
                db.execSQL("CREATE TABLE " + tableName + " (" + new_cNames + ");");
                db.execSQL("INSERT INTO " + tableName + " (" + new_cNames + ") SELECT " + old_cNames + " FROM temp_" + tableName + ";");
                db.execSQL("DROP TABLE IF EXISTS temp_" + tableName + ";");

            } catch (Exception ex) {
                return ex.toString();
            }
        }
        return "";
    }

    public String alterTableDrop(SQLiteDatabase db, String tableName, String columnName){
        db.execSQL("DROP TABLE IF EXISTS temp_" + tableName + ";");

        try {
            cursor = db.query(tableName, null, null, null, null, null, null);
            String temp[] = cursor.getColumnNames();

            String old_cNames = "";
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                if(!old_cNames.equals(""))
                    old_cNames += ", ";
                old_cNames += temp[i];
            }
            //姓名
            //測試

            //姓名, 測試

            //姓名
            String new_cNames = "";
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                if(!columnName.equals(temp[i])) {
                    if (!new_cNames.equals(""))
                        new_cNames += ", ";
                    new_cNames += temp[i];
                }
            }
            cursor.close();

            db.execSQL("ALTER TABLE " + tableName + " RENAME TO temp_" + tableName + ";");
            db.execSQL("CREATE TABLE " + tableName + " (" + new_cNames + ");");
            db.execSQL("INSERT INTO " + tableName + " (" + new_cNames + ") SELECT " + new_cNames + " FROM temp_" + tableName + ";");
            db.execSQL("DROP TABLE IF EXISTS temp_" + tableName + ";");


        } catch (Exception ex) {
            return ex.toString();
        }
        return "Column Drop";
    }

    public void updata(SQLiteDatabase db, String tableName, String msg, String columnName, String id){
        db.execSQL("UPDATE " + tableName + " SET " + columnName + " = '" + msg + "' WHERE id = " + id + ";");
    };
}
