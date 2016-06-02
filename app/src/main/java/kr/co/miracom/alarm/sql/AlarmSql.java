package kr.co.miracom.alarm.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import kr.co.miracom.alarm.util.DBHelper;
import kr.co.miracom.alarm.vo.SimpleVo;

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

    public void insertSimpleAlarm(SimpleVo simpleVo){
        db.execSQL("");
        db.close();
    }
}
