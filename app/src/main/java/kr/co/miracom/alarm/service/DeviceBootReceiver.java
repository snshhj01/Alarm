package kr.co.miracom.alarm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import kr.co.miracom.alarm.common.Constants;
import kr.co.miracom.alarm.util.AlarmUtils;
import kr.co.miracom.alarm.util.CommonUtils;
import kr.co.miracom.alarm.util.DBHelper;
import kr.co.miracom.alarm.util.Logger;
import kr.co.miracom.alarm.vo.ext.AlarmInfo;

/**
 * @author kws
 * @date 2016-06-02
 * @since 0.1
 */
public class DeviceBootReceiver extends BroadcastReceiver {
    private DBHelper mDbHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        mDbHelper = new DBHelper(context);
        mDbHelper.open();
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Logger.d(this.getClass(),"%d" ,"Re-register alarm after boot!!");
            List<AlarmInfo> alarmList = mDbHelper.selectAll("N");
            for(AlarmInfo alarm : alarmList) {
                Intent myIntent = new Intent(context, AlarmReceiver.class);
                long triggerTime = CommonUtils.setTriggerTime(alarm.getTime().get(Constants.TIME_HOUR),alarm.getTime().get(Constants.TIME_MINUTE));
                intent.putExtra(Constants.ALARM_ID, alarm.getAlarmId());
                if(alarm.getDays() != null ) {
                    AlarmUtils.getInstance().startAlarm(context, intent, triggerTime, 1);
                } else {
                    AlarmUtils.getInstance().startAlarm(context, intent, triggerTime, 0);
                }
            }
            alarmList = mDbHelper.selectAll("Y");
            for(AlarmInfo alarm : alarmList) {
                Intent myIntent = new Intent(context, AlarmReceiver.class);
                long triggerTime = CommonUtils.setTriggerTime(alarm.getTime().get(Constants.TIME_HOUR),alarm.getTime().get(Constants.TIME_MINUTE));
                intent.putExtra(Constants.ALARM_ID, alarm.getAlarmId());
                if(alarm.getDays() != null ) {
                    AlarmUtils.getInstance().startAlarm(context, intent, triggerTime, 1);
                } else {
                    AlarmUtils.getInstance().startAlarm(context, intent, triggerTime, 0);
                }
            }
        }
    }
}