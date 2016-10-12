package kr.co.miracom.alarm.service;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import kr.co.miracom.alarm.activity.NotiActivity;
import kr.co.miracom.alarm.common.Constants;
import kr.co.miracom.alarm.util.AlarmUtils;
import kr.co.miracom.alarm.util.DBHelper;
import kr.co.miracom.alarm.vo.ext.AlarmInfo;

/**
 * @author kws
 * @date 2016-06-02
 * @since 0.1
 */
public class AlarmReceiver extends BroadcastReceiver {

    private DBHelper mDbHelper;
    private int _id;
    private int active;
    private ArrayList<Integer> dbDays;
    public static final String COLUMN_ALARM_ID = "alarm_id";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("AlarmReceiver connect", "AlarmReceiver connect1 : " +  intent.getIntExtra(Constants.ALARM_ID, 0));

        mDbHelper = new DBHelper(context);
        mDbHelper.open();
        Bundle extra = intent.getExtras();
        //intent에서 넘겨준 AlramId를 가져옴

        if (extra != null) {

            if(intent.getIntExtra(Constants.ALARM_ID,0) != 0) {
                _id = intent.getIntExtra(Constants.ALARM_ID, 0);
                AlarmInfo alarm = mDbHelper.selectAlarm(_id, COLUMN_ALARM_ID);
                dbDays = new ArrayList<Integer>();
                active = alarm.getActive();
                dbDays = alarm.getDays();

                Calendar cal = Calendar.getInstance();

                if(active == 1){
                    if ("LOCATION".equals(alarm.getFlag())){ //위치 알람이면.
                        Log.e("AlarmReceiver connect", "AlarmReceiver site : " +  intent.getIntExtra(Constants.ALARM_ID, 0));

                        //GPS init 후 3번 정도?? GPS enabl
                        AlarmUtils.getInstance().isGPSEnabled = true;
                        if (AlarmUtils.getInstance().isGPSInit == false) {
                            AlarmUtils.getInstance().isGPSInit = true;
                            AlarmUtils.getInstance().gpsInit(context);
                        }

                        intent.putExtra("AlarmInfo", alarm);
                        AlarmUtils.getInstance().setAlarmIntent(intent);
                    } else if(dbDays.isEmpty()){
                        intentNotiActivity(context, alarm,_id);
                    } else {
                        for(Integer i : dbDays){
                            if(i == cal.get(Calendar.DAY_OF_WEEK)){
                                intentNotiActivity(context, alarm,_id);
                            }
                        }
                    }
                }
            }
        }
    }

    private void intentNotiActivity(Context context, AlarmInfo alarm,int _id){
        Log.e("AlarmReceiver connect", "AlarmReceiver connect2");

        Intent alarmIntent = new Intent(context, NotiActivity.class);

        //alarmIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        alarmIntent.putExtra("AlarmInfo",alarm);
        alarmIntent.putExtra(Constants.ALARM_ID, _id);
        PendingIntent p = PendingIntent.getActivity(context, _id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            p.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    };
}