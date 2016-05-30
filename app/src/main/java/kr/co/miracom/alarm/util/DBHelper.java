package kr.co.miracom.alarm.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kimsungmog on 2016-05-30.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String simpleAlarmSql = "create table tb_simple ("+
                "_id integer primary key autoincrement,"+
                "date not null," +
                "days," +
                "cycle," +
                "type," +
                "sound," +
                "volum," +
                "repeat)";

        db.execSQL(simpleAlarmSql);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == DATABASE_VERSION) {

        }
    }
}
