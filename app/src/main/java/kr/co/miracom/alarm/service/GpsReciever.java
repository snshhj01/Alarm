package kr.co.miracom.alarm.service;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.Date;
import java.util.HashMap;

import kr.co.miracom.alarm.activity.NotiActivity;
import kr.co.miracom.alarm.common.Constants;
import kr.co.miracom.alarm.vo.ext.AlarmInfo;

/**
 * Created by wjw on 2016-05-13.
 */
public class GpsReciever extends BroadcastReceiver {
    private String mExpectedAction;
    private Intent mLastReceivedIntent;
    private final static int ONE_HOUR = 60 * 60 * 1000;

    public GpsReciever(String expectedAction) {
        mExpectedAction = expectedAction;
        mLastReceivedIntent = null;
    }

    public IntentFilter getFilter() {
        IntentFilter filter = new IntentFilter(mExpectedAction);
        return filter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("AlarmReceiver connect", "GpsReciever connect");

        if (intent != null) {
            mLastReceivedIntent = intent;
            int id = intent.getIntExtra("alartUniqId", 0);
            double lat = intent.getDoubleExtra("lat", 0.0D);
            double lng = intent.getDoubleExtra("lng", 0.0D);
            AlarmInfo alarm = (AlarmInfo)intent.getSerializableExtra("AlarmInfo");

            HashMap<String,Integer> timeMap = alarm.getTime();
            int hour = timeMap.get(Constants.TIME_HOUR);
            int min = timeMap.get(Constants.TIME_MINUTE);

            Date nowDate = new Date();
            Date selDate = new Date();

            selDate.setHours(hour);
            selDate.setMinutes(min);

            if ((selDate.getTime() + ONE_HOUR) >= nowDate.getTime()) {
                intent.putExtra("AlarmInfo",alarm);

                Intent alarmIntent = new Intent(context, NotiActivity.class);
                alarmIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //   alarmIntent.putExtra("alarmName", "근처다." + id + " : " + ala);

                alarmIntent.putExtra("alartUniqId", id);
                alarmIntent.putExtra("lat", lat);
                alarmIntent.putExtra("lng", lng);
                alarmIntent.putExtra("AlarmInfo",alarm);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, id, alarmIntent, 0);

                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
        }
    }

    public Intent getLastRecievedIetent() {
        return mLastReceivedIntent;
    }

    public void clearReceivedIntents() {
        mLastReceivedIntent = null;
    }
}
