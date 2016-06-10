package kr.co.miracom.alarm.service;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kr.co.miracom.alarm.activity.NotiActivity;
import kr.co.miracom.alarm.common.Constants;
import kr.co.miracom.alarm.util.DBHelper;
import kr.co.miracom.alarm.util.Logger;
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


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("AlarmReceiver connect", "AlarmReceiver connect");

        mDbHelper = new DBHelper(context);
        mDbHelper.open();
        Bundle extra = intent.getExtras();
        //intent에서 넘겨준 AlramId를 가져옴

        if (extra != null) {

            if(intent.getIntExtra("alartUniqId",0) != 0) {

                _id = intent.getIntExtra("alartUniqId", 0);
                AlarmInfo alarm = mDbHelper.selectAlarm(_id);
                //수정 시 기존 알람 정보를 세팅해 줌.

                dbDays = new ArrayList<Integer>();
                active = alarm.getActive();
                dbDays = alarm.getDays();

                Calendar cal = Calendar.getInstance();

                if(active == 1){
                    for(Integer i : dbDays){
                        if(dbDays.get(i) == cal.get(Calendar.DAY_OF_WEEK)){
                            Intent alarmIntent = new Intent(context, NotiActivity.class);
                            alarmIntent.putExtra("AlarmInfo",alarm);
                            alarmIntent.putExtra("alartUniqId",_id);
                            PendingIntent p = PendingIntent.getActivity(context, _id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            try {
                                p.send();
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
