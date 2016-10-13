package com.rilin.lzy.mybase.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rilin.lzy.mybase.model.ClockModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzy on 16/9/30.
 */
public class ClockDBUtil {
    public static final String DB_NAME = "clock";
    public static final String TABLE_NAME = "table_clock";
    public static final int DB_VERSION = 2;
    private static final String TAG = "com.rilintech.hxk_301.db.ClockDBUtil";
    public static final String KEY_ID = "_id";
    private static Context mContext;

    public static final String DATABASE_CREATE = "create table if not exists table_clock ("
            + "_id Integer primary key autoincrement, " +
            "time varchar(255), " +
            "repeat varchar(255), " +
            "tag varchar(255), " +
            "space varchar(255), " +
            "ring varchar(255), " +
            "mSwitch varchar(255), " +
            "uuid varchar(255) )";
    private static SQLiteDatabase mSQLiteDatabase = null;
    private static DataBaseManagementHelper mDatabaseHelper = null;

    private static class DataBaseManagementHelper extends SQLiteOpenHelper {

        DataBaseManagementHelper(Context context) {

            super(context, DB_NAME, null, DB_VERSION);
            onCreate(getWritableDatabase());
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    public ClockDBUtil(Context context){
        mContext = context;
    }

    /**
     * 打开数据库
     * @throws SQLException
     */
    public void openDataBase() throws SQLException{
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
    }

    /**
     * 关闭数据库
     * @throws SQLException
     */
    public void closeDataBase()throws SQLException{
        mDatabaseHelper.close();
    }

    /**
     * 移除表中某一项数据
     * @param _rowIndex
     */
    public void removeEntry(String _rowIndex){
        mSQLiteDatabase.delete(TABLE_NAME,KEY_ID + " = " + _rowIndex, null);
    }

    /**
     * 查找所有闹铃
     *
     * @return
     */
    public Cursor queryAllCursor() {
        Cursor cursor = mSQLiteDatabase.rawQuery(
                "select * from table_clock order by time asc", null);
        return cursor;
    }

    /**
     * 查找所有闹铃,返回list集合
     *
     * @return medds
     */
    public List<ClockModel> getAllClock() {

        List<ClockModel> list_clocks = new ArrayList<>();
        ClockModel info = null;
        Cursor c = queryAllCursor();
        while (c.moveToNext()) {
            info = new ClockModel();

            info.setId(c.getInt(c.getColumnIndex("_id")));
            info.setTime(c.getString(c.getColumnIndex("time")));
            info.setRepeat(c.getString(c.getColumnIndex("repeat")));
            info.setSpace(c.getString(c.getColumnIndex("space")));
            info.setTag(c.getString(c.getColumnIndex("tag")));
            info.setRing(c.getString(c.getColumnIndex("ring")));
            info.setUuid(c.getString(c.getColumnIndex("uuid")));
            info.setmSwitch(c.getString(c.getColumnIndex("mSwitch")));

            list_clocks.add(info);
        }
        c.close();
        return list_clocks;
    }
    /**
    * 查找所有闹铃ID
    */
    public Cursor queryAllId() {
        Cursor cursor = mSQLiteDatabase.rawQuery(
                "select _id from table_clock order by _id asc", null);
        return cursor;
    }
    /**
     * 查找最大的一个ID
     *
     * @return id
     */
    public Integer getLastId() {
        int id = 0;
        Cursor c = queryAllId();
        while (c.moveToNext()) {
            id = c.getInt(c.getColumnIndex("_id"));
        }
        c.close();
        return id;
    }

    /*
     * 查找一个闹铃
     */
    public Cursor queryOneClock(String id) {
        Cursor cursor = mSQLiteDatabase.rawQuery(
                "select * from table_clock where _id = ? ",
                new String[]{id});
        return cursor;
    }

    /*
     * 查找一个闹铃实体
     */
    public ClockModel getOneClock(String id) {

        ClockModel info = null;
        Cursor c = queryOneClock(id);
        while (c.moveToNext()) {
            info = new ClockModel();

            info.setId(c.getInt(c.getColumnIndex("_id")));
            info.setTime(c.getString(c.getColumnIndex("time")));
            info.setRepeat(c.getString(c.getColumnIndex("repeat")));
            info.setSpace(c.getString(c.getColumnIndex("space")));
            info.setTag(c.getString(c.getColumnIndex("tag")));
            info.setRing(c.getString(c.getColumnIndex("ring")));
            info.setUuid(c.getString(c.getColumnIndex("uuid")));
            info.setmSwitch(c.getString(c.getColumnIndex("mSwitch")));
        }

        return info;
    }

    /*
     * 通过uuid查找一个闹铃
     *
     */
    public Cursor queryOneClockByUuid(String uuid) {
        Cursor cursor = mSQLiteDatabase.rawQuery(
                "select * from table_clock where uuid = ? ",
                new String[]{uuid});
        return cursor;
    }

    /*
     * 通过uuid查找一个闹铃实体
     */
    public ClockModel getOneClockByUuid(String uuid) {

        ClockModel info = null;
        Cursor c = queryOneClockByUuid(uuid);
        while (c.moveToNext()) {
            info = new ClockModel();

            info.setId(c.getInt(c.getColumnIndex("_id")));
            info.setTime(c.getString(c.getColumnIndex("time")));
            info.setRepeat(c.getString(c.getColumnIndex("repeat")));
            info.setSpace(c.getString(c.getColumnIndex("space")));
            info.setTag(c.getString(c.getColumnIndex("tag")));
            info.setRing(c.getString(c.getColumnIndex("ring")));
            info.setUuid(c.getString(c.getColumnIndex("uuid")));
            info.setmSwitch(c.getString(c.getColumnIndex("mSwitch")));
        }

        return info;
    }

    /*
     * 添加
     */
    public long addClock(ClockModel info) {
        ContentValues cv = new ContentValues();

        cv.put("time",info.getTime());
        cv.put("repeat", info.getRepeat());
        cv.put("tag", info.getTag());
        cv.put("space", info.getSpace());
        cv.put("ring", info.getRing());
        cv.put("uuid", info.getUuid());
        cv.put("mSwitch", info.getmSwitch());

        return mSQLiteDatabase.insert(TABLE_NAME, null, cv);
    }

    /**
     * 删除闹铃
     * @param id
     */
    public Integer deleteOneClock(int id){
        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(id)};
        return mSQLiteDatabase.delete(TABLE_NAME, whereClause, whereArgs);
    }

    /**
     * 更新
     */
    public int updateClock(ClockModel value, String id) {
        ContentValues values = new ContentValues();

        values.put("time",value.getTime());
        values.put("repeat", value.getRepeat());
        values.put("tag", value.getTag());
        values.put("space", value.getSpace());
        values.put("ring", value.getRing());
        values.put("uuid", value.getUuid());
        values.put("mSwitch", value.getmSwitch());

        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(id)};

        return mSQLiteDatabase.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    /**
     * 更新flag（服务开关）值
     */
    public void updateMSwitch(int flag, int id) {
        ContentValues c = new ContentValues();
        c.put("mSwitch", flag);
        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(id)};

        mSQLiteDatabase.update(TABLE_NAME, c, whereClause, whereArgs);
    }

}
