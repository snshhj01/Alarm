package kr.co.miracom.alarm.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import kr.co.miracom.alarm.util.DBHelper;

/**
 * Created by kimsungmog on 2016-05-30.
 */
public class AlarmSql {

    private DBHelper helper;
    private SQLiteDatabase db;
    private Cursor cursor;

    public AlarmSql(Context context){
        helper = new DBHelper(context,"tb_simple",null, DBHelper.DATABASE_VERSION);
        db = helper.getWritableDatabase();
    }

    public void insertSimpleAlarm(){
        db.execSQL("");
        db.close();
    }
}
