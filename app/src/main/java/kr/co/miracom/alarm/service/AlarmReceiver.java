package kr.co.miracom.alarm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Calendar;

import kr.co.miracom.alarm.util.Logger;

/**
 * @author kws
 * @date 2016-06-02
 * @since 0.1
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String []days = {"일","월","화","수","목","금","토"};
        Bundle extra = intent.getExtras();
        if (extra != null) {
            boolean isOneTime = extra.getBoolean("one_time");
            if (isOneTime) {
                Toast.makeText(context, "One time alram", Toast.LENGTH_SHORT).show();
                //알람 울리는 activity~~~~
            } else {
                Logger.d(this.getClass(), "%s", "day_of_week @@@@ ~~~~~~~~~~~~~");
                boolean[] week = extra.getBooleanArray("day_of_week");
                Calendar cal = Calendar.getInstance();
                if (!week[cal.get(Calendar.DAY_OF_WEEK)]) {
                    Toast.makeText(context, "오늘은  "+days[cal.get(Calendar.DAY_OF_WEEK)-1]+"요일이라 알람패스~~", Toast.LENGTH_SHORT).show();
                    return;
                }
                //알람 울리는 activity~~~~

                Toast.makeText(context, "Daily repeat!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
