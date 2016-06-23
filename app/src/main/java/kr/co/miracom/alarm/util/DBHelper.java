package kr.co.miracom.alarm.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.miracom.alarm.common.Constants;
import kr.co.miracom.alarm.vo.ext.AlarmInfo;

/**
 * Created by kimsungmog on 2016-05-30.
 */
public class DBHelper {
    public static SQLiteDatabase sqlDB;
    private DatabaseHelper scloudDBHelper;
    private Context mCtx;
    private Gson gson = CommonUtils.getGsonInstance();

    public static final String ALARM_TABLE = "tb_alarm_management";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ALARM_NAME = "alarm_name";
    public static final String COLUMN_ALARM_ID = "alarm_id";
    public static final String COLUMN_ALARM_ACTIVE = "alarm_active";
    public static final String COLUMN_ALARM_TIME = "alarm_time";
    public static final String COLUMN_ALARM_DAYS = "alarm_days";
    public static final String COLUMN_ALARM_SOUND_URI = "alarm_sound_uri";
    public static final String COLUMN_ALARM_TYPE = "alarm_type";
    public static final String COLUMN_ALARM_VOLUME = "alarm_volume";

    //public static final String SITE_ALARM_TABLE = "tb_site_alarm_management";
    public static final String COLUMN_SITE_FLAG = "alarm_site_flg";
    public static final String COLUMN_SITE_LATITUDE = "alarm_latitude";
    public static final String COLUMN_SITE_LONGITUDE = "alarm_longitude";
    public static final String COLUMN_SITE_RADIUS = "alarm_radius";
    public static final String COLUMN_SITE_ADDR = "alarm_addr";

    Type hashTypeSI = new TypeToken<HashMap<String, Integer>>(){}.getType();
    Type arrTypeI = new TypeToken<ArrayList<Integer>>(){}.getType();

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTable = "CREATE TABLE IF NOT EXISTS " + ALARM_TABLE + " ( "
                    + COLUMN_ID + " INTEGER primary key autoincrement, "
                    + COLUMN_ALARM_NAME + " TEXT NOT NULL, "
                    + COLUMN_ALARM_ID + " INTEGER NOT NULL, "
                    + COLUMN_ALARM_ACTIVE + " INTEGER NOT NULL, "
                    + COLUMN_ALARM_TIME + " TEXT NOT NULL, "
                    + COLUMN_ALARM_DAYS + " TEXT NOT NULL, "
                    + COLUMN_ALARM_SOUND_URI + " TEXT NOT NULL, "
                    + COLUMN_ALARM_TYPE + " INTEGER NOT NULL, "
                    + COLUMN_ALARM_VOLUME + " INTEGER NOT NULL, "
                    + COLUMN_SITE_FLAG + " TEXT, "
                    + COLUMN_SITE_LATITUDE + " TEXT, "
                    + COLUMN_SITE_LONGITUDE + " TEXT, "
                    + COLUMN_SITE_RADIUS + " INTEGER, "
                    + COLUMN_SITE_ADDR + " TEXT) ";
            db.execSQL(createTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE);
            onCreate(db);
        }
    }
    public DBHelper(Context context) {
        this.mCtx = context;
    }
    public DBHelper open() throws SQLException {
        scloudDBHelper = new DatabaseHelper(mCtx, Constants.DB_NAME, null, Constants.DB_VERSION);
        sqlDB = scloudDBHelper.getWritableDatabase();
        return this;
    }

    /**
     * 새로운 알람 추가시 Insert 함.
     * @param alarmInfo
     */
    public void insertAlarmInfo(AlarmInfo alarmInfo) {
        ContentValues cv = new ContentValues();

        String timeJson = gson.toJson(alarmInfo.getTime());
        String daysJson = gson.toJson(alarmInfo.getDays());

        cv.put(COLUMN_ALARM_NAME, alarmInfo.getAlarmName());
        cv.put(COLUMN_ALARM_ID, alarmInfo.getAlarmId());
        cv.put(COLUMN_ALARM_ACTIVE, alarmInfo.getActive());
        cv.put(COLUMN_ALARM_TIME, timeJson);
        cv.put(COLUMN_ALARM_DAYS, daysJson);
        cv.put(COLUMN_ALARM_SOUND_URI, String.valueOf(alarmInfo.getSoundUri()));
        cv.put(COLUMN_ALARM_TYPE, alarmInfo.getAlarmType());
        cv.put(COLUMN_ALARM_VOLUME, alarmInfo.getVolume());
        cv.put(COLUMN_SITE_FLAG, alarmInfo.getFlag());
        cv.put(COLUMN_SITE_LATITUDE, alarmInfo.getLatitude());
        cv.put(COLUMN_SITE_LONGITUDE, alarmInfo.getLongitude());
        cv.put(COLUMN_SITE_RADIUS, alarmInfo.getRadius());
        cv.put(COLUMN_SITE_ADDR, alarmInfo.getAddr());
        sqlDB.insert(ALARM_TABLE, null, cv);
    }

    /**
     * Type별 모든 알람 리스트를 가져 옴
     * @return
     */
    public List<AlarmInfo> selectAll(String flag) {
        Logger.d(this.getClass(), "%s", "Get all alarm list");
        String strWhere = ("Y".equals(flag) ? COLUMN_SITE_FLAG+"='"+flag +"'" : COLUMN_SITE_FLAG+" IS NULL ");
        List<AlarmInfo> alarmList = new ArrayList<AlarmInfo>();
        String[] columns = new String[] {
                COLUMN_ID,
                COLUMN_ALARM_NAME,
                COLUMN_ALARM_ID,
                COLUMN_ALARM_ACTIVE,
                COLUMN_ALARM_TIME,
                COLUMN_ALARM_DAYS,
                COLUMN_ALARM_SOUND_URI,
                COLUMN_ALARM_TYPE,
                COLUMN_ALARM_VOLUME,
                COLUMN_SITE_FLAG,
                COLUMN_SITE_LATITUDE,
                COLUMN_SITE_LONGITUDE,
                COLUMN_SITE_RADIUS,
                COLUMN_SITE_ADDR
        };
        Cursor cursor = null;
        try {
            cursor = sqlDB.query(ALARM_TABLE, columns, strWhere, null, null, null, COLUMN_ID);
            if (cursor.moveToFirst()) {
                do {
                    AlarmInfo alarm = new AlarmInfo();
                    alarm.set_id(cursor.getInt(0));
                    alarm.setAlarmName(cursor.getString(1));
                    alarm.setAlarmId(cursor.getInt(2));
                    alarm.setActive(cursor.getInt(3));
                    alarm.setTime((HashMap<String, Integer>) gson.fromJson(cursor.getString(4), hashTypeSI));
                    alarm.setDays((ArrayList<Integer>) gson.fromJson(cursor.getString(5), arrTypeI));
                    alarm.setSoundUri(cursor.getString(6));
                    alarm.setAlarmType(cursor.getInt(7));
                    alarm.setVolume(cursor.getInt(8));
                    alarm.setFlag(cursor.getString(9));
                    alarm.setLatitude(cursor.getString(10));
                    alarm.setLongitude(cursor.getString(11));
                    alarm.setRadius(cursor.getString(12));
                    alarm.setAddr(cursor.getString(13));
                    alarmList.add(alarm);
                    Logger.d(this.getClass(), "%s", "Alarm info : " + alarm.toString());
                } while (cursor.moveToNext());

            }
            Logger.d(this.getClass(), "%s", "Alarm count : " + alarmList.size());
        }finally {
            if(cursor != null){
                cursor.close();

            }
        }
        return alarmList;
    }


    /**
     * 선택 된 알람정보를 가져 옴
     * @param id
     * @return
     */
    public AlarmInfo selectAlarm(int id, String selection) {
        Logger.d(this.getClass(), "%s", "Get alarm, id:"+id);
        AlarmInfo alarm = new AlarmInfo();
        String[] columns = new String[] {
                COLUMN_ID,
                COLUMN_ALARM_NAME,
                COLUMN_ALARM_ID,
                COLUMN_ALARM_ACTIVE,
                COLUMN_ALARM_TIME,
                COLUMN_ALARM_DAYS,
                COLUMN_ALARM_SOUND_URI,
                COLUMN_ALARM_TYPE,
                COLUMN_ALARM_VOLUME,
                COLUMN_SITE_FLAG,
                COLUMN_SITE_LATITUDE,
                COLUMN_SITE_LONGITUDE,
                COLUMN_SITE_RADIUS,
                COLUMN_SITE_ADDR
        };
        Cursor cursor = null;
        try {
            cursor = sqlDB.query(ALARM_TABLE, columns, selection + "=" + id, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    alarm.set_id(cursor.getInt(0));
                    alarm.setAlarmName(cursor.getString(1));
                    alarm.setAlarmId(cursor.getInt(2));
                    alarm.setActive(cursor.getInt(3));
                    alarm.setTime((HashMap<String, Integer>) gson.fromJson(cursor.getString(4), hashTypeSI));
                    alarm.setDays((ArrayList<Integer>) gson.fromJson(cursor.getString(5), arrTypeI));
                    alarm.setSoundUri(cursor.getString(6));
                    alarm.setAlarmType(cursor.getInt(7));
                    alarm.setVolume(cursor.getInt(8));
                    alarm.setFlag(cursor.getString(9));
                    alarm.setLatitude(cursor.getString(10));
                    alarm.setLongitude(cursor.getString(11));
                    alarm.setRadius(cursor.getString(12));
                    alarm.setAddr(cursor.getString(13));
                    Logger.d(this.getClass(), "%s", "Site Alarm info : " + alarm.toString());
                } while (cursor.moveToNext());
            }
        }finally {

            if(cursor != null){
                cursor.close();

            }
        }
        return alarm;
    }

    /**
     * 알람 수정 된 내용 update
     * @param alarmInfo
     * @return
     */
    public int updateAlarm(AlarmInfo alarmInfo) {
        ContentValues cv = new ContentValues();
        String timeJson = gson.toJson(alarmInfo.getTime());
        String daysJson = gson.toJson(alarmInfo.getDays());

        cv.put(COLUMN_ALARM_NAME, alarmInfo.getAlarmName());
        cv.put(COLUMN_ALARM_ID, alarmInfo.getAlarmId());
        cv.put(COLUMN_ALARM_ACTIVE, alarmInfo.getActive());
        cv.put(COLUMN_ALARM_TIME, timeJson);
        cv.put(COLUMN_ALARM_DAYS, daysJson);
        cv.put(COLUMN_ALARM_SOUND_URI, String.valueOf(alarmInfo.getSoundUri()));
        cv.put(COLUMN_ALARM_TYPE, alarmInfo.getAlarmType());
        cv.put(COLUMN_ALARM_VOLUME, alarmInfo.getVolume());
        cv.put(COLUMN_SITE_FLAG, alarmInfo.getFlag());
        cv.put(COLUMN_SITE_LATITUDE, alarmInfo.getLatitude());
        cv.put(COLUMN_SITE_LONGITUDE, alarmInfo.getLongitude());
        cv.put(COLUMN_SITE_RADIUS, alarmInfo.getRadius());
        cv.put(COLUMN_SITE_ADDR, alarmInfo.getAddr());
        return sqlDB.update(ALARM_TABLE, cv, COLUMN_ID+"="+alarmInfo.get_id(), null);
    }

    /**
     * 모든 알람목록 삭제
     * @return
     */
    public int deleteAll(){
        return sqlDB.delete(ALARM_TABLE, "1", null);
    }

    /**
     * 선택 된 알람만 삭제
     * @param id
     * @return
     */
    public int deleteAlarm(int id){
        return sqlDB.delete(ALARM_TABLE, COLUMN_ID + "=" + id, null);
    }
}
