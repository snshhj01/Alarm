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
    public static final String COLUMN_ALARM_SOUND = "alarm_sound";
    public static final String COLUMN_ALARM_TYPE = "alarm_type";
    public static final String COLUMN_ALARM_VOLUME = "alarm_volume";
    public static final String COLUMN_ALARM_SNOOZE = "alarm_snooze";

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
                    + COLUMN_ALARM_SOUND + " TEXT NOT NULL, "
                    + COLUMN_ALARM_TYPE + " INTEGER NOT NULL, "
                    + COLUMN_ALARM_VOLUME + " INTEGER NOT NULL, "
                    + COLUMN_ALARM_SNOOZE + " TEXT NOT NULL)";
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
        String soundJson = gson.toJson(alarmInfo.getAlarmSound());
        String snoozeJson = gson.toJson(alarmInfo.getSnooze());
        String daysJson = gson.toJson(alarmInfo.getDays());

        cv.put(COLUMN_ALARM_NAME, alarmInfo.getAlarmName());
        cv.put(COLUMN_ALARM_ID, alarmInfo.getAlarmId());
        cv.put(COLUMN_ALARM_ACTIVE, alarmInfo.getActive());
        cv.put(COLUMN_ALARM_TIME, timeJson);
        cv.put(COLUMN_ALARM_DAYS, daysJson);
        cv.put(COLUMN_ALARM_SOUND, soundJson);
        cv.put(COLUMN_ALARM_TYPE, alarmInfo.getAlarmType());
        cv.put(COLUMN_ALARM_VOLUME, alarmInfo.getVolume());
        cv.put(COLUMN_ALARM_SNOOZE, snoozeJson);
        sqlDB.insert(ALARM_TABLE, null, cv);
    }

    /**
     * 모든 알람 리스트를 가져 옴
     * @return
     */
    public List<AlarmInfo> selectAll() {
        Logger.d(this.getClass(), "%s", "Get all alarm list");
        List<AlarmInfo> alarmList = new ArrayList<AlarmInfo>();
        String[] columns = new String[] {
                COLUMN_ID,
                COLUMN_ALARM_NAME,
                COLUMN_ALARM_ID,
                COLUMN_ALARM_ACTIVE,
                COLUMN_ALARM_TIME,
                COLUMN_ALARM_DAYS,
                COLUMN_ALARM_SOUND,
                COLUMN_ALARM_TYPE,
                COLUMN_ALARM_VOLUME,
                COLUMN_ALARM_SNOOZE
        };
        Cursor cursor = sqlDB.query(ALARM_TABLE, columns, null, null, null, null, COLUMN_ID);
        if (cursor.moveToFirst()) {
            do {
                AlarmInfo alarm = new AlarmInfo();
                alarm.set_id(cursor.getInt(0));
                alarm.setAlarmName(cursor.getString(1));
                alarm.setAlarmId(cursor.getInt(2));
                alarm.setActive(cursor.getInt(3));
                alarm.setTime((HashMap<String, Integer>) gson.fromJson(cursor.getString(4),hashTypeSI));
                alarm.setDays((ArrayList<Integer>) gson.fromJson(cursor.getString(5), arrTypeI));
                alarm.setAlarmSound((HashMap<String, String>) gson.fromJson(cursor.getString(6), HashMap.class));
                alarm.setAlarmType(cursor.getInt(7));
                alarm.setVolume(cursor.getInt(8));
                alarm.setSnooze((HashMap<String, Integer>) gson.fromJson(cursor.getString(9), hashTypeSI));
                alarmList.add(alarm);
                Logger.d(this.getClass(), "%s", "Alarm info : " + alarm.toString());
            } while (cursor.moveToNext());
        }
        Logger.d(this.getClass(), "%s", "Alarm count : " + alarmList.size());
        /*
        ==> 아래와 같이 값이 저장되며 map형태의 값은 map에서 꺼내서 사용해야 함.
         alarmName=알람테스트
         alarmSound={path=/storage/emulated/0/melon/5 Seconds Of Summer-01-Amnesia (삼성 갤럭시 노트 엣지 광고 삽입곡)-128.mp3, title=Amnesia (삼성 갤럭시 노트 엣지 광고 삽입곡)}
         days=[2, 3, 4, 7]
         snooze={count=3, interval=5}
         time={hour=18, minute=43}
         _id=2
         active=1
         alarmId=1768778836
         alarmType=2
         volume=9
         */
        cursor.close();
        return alarmList;
    }

    /**
     * 선택 된 알람 정보를 가져 옴
     * @param id
     * @return
     */
    public AlarmInfo selectAlarm(int id, String selection) {
        Logger.d(this.getClass(), "%s", "Get alarm");
        AlarmInfo alarm = new AlarmInfo();
        String[] columns = new String[] {
                COLUMN_ID,
                COLUMN_ALARM_NAME,
                COLUMN_ALARM_ID,
                COLUMN_ALARM_ACTIVE,
                COLUMN_ALARM_TIME,
                COLUMN_ALARM_DAYS,
                COLUMN_ALARM_SOUND,
                COLUMN_ALARM_TYPE,
                COLUMN_ALARM_VOLUME,
                COLUMN_ALARM_SNOOZE
        };
        Cursor cursor = sqlDB.query(ALARM_TABLE, columns, selection+"="+id, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                alarm.set_id(cursor.getInt(0));
                alarm.setAlarmName(cursor.getString(1));
                alarm.setAlarmId(cursor.getInt(2));
                alarm.setActive(cursor.getInt(3));

                alarm.setTime((HashMap<String, Integer>) gson.fromJson(cursor.getString(4),hashTypeSI));
                alarm.setDays((ArrayList<Integer>) gson.fromJson(cursor.getString(5), arrTypeI));
                alarm.setAlarmSound((HashMap<String, String>) gson.fromJson(cursor.getString(6), HashMap.class));
                alarm.setAlarmType(cursor.getInt(7));
                alarm.setVolume(cursor.getInt(8));
                alarm.setSnooze((HashMap<String, Integer>) gson.fromJson(cursor.getString(9), hashTypeSI));
                Logger.d(this.getClass(), "%s", "Alarm info : " + alarm.toString());
            } while (cursor.moveToNext());
        }
        cursor.close();
        return alarm;
    }

    /**
     * 알람 수정 된 내용 update
     * @param alarm
     * @return
     */
    public int update(AlarmInfo alarm) {
        ContentValues cv = new ContentValues();
        String timeJson = gson.toJson(alarm.getTime());
        String soundJson = gson.toJson(alarm.getAlarmSound());
        String snoozeJson = gson.toJson(alarm.getSnooze());
        String daysJson = gson.toJson(alarm.getDays());

        cv.put(COLUMN_ALARM_NAME, alarm.getAlarmName());
        cv.put(COLUMN_ALARM_ID, alarm.getAlarmId());
        cv.put(COLUMN_ALARM_ACTIVE, alarm.getActive());
        cv.put(COLUMN_ALARM_TIME, timeJson);
        cv.put(COLUMN_ALARM_DAYS, daysJson);
        cv.put(COLUMN_ALARM_SOUND, soundJson);
        cv.put(COLUMN_ALARM_TYPE, alarm.getAlarmType());
        cv.put(COLUMN_ALARM_VOLUME, alarm.getVolume());
        cv.put(COLUMN_ALARM_SNOOZE, snoozeJson);
        return sqlDB.update(ALARM_TABLE, cv, COLUMN_ID+"="+alarm.get_id(), null);
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
